package relay;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.wiringpi.GpioUtil;

import util.TwilioMMS;

/**
 * Demonstrates how to control a Relay Circuit
 * using the GPIO pins on the Raspberry Pi.
 *
 */
public class RelayWithMainsNMotionSensorNBuzzerNCam {
	
	
	/**
	 * This method will control the Relay Circuit by turning them ON and OFF
	 * 
	 */
	public void controlRelayCircuit(){
		System.out.println("Starting Relay Circuit With Mains Example..."); 
		
		//This is required to enable Non Privileged Access to avoid applying sudo to run Pi4j programs
		GpioUtil.enableNonPrivilegedAccess();
		
		//Create gpio controller for LED listening on the pin GPIO_01 with default PinState as LOW                    
		final GpioController gpioRelayLED1 = GpioFactory.getInstance();
		
		//LOW-->ON,HIGH-->OFF
		System.out.println("LED's on Relay IN1 will be OFF first..");
		final GpioPinDigitalOutput relayLED1 = gpioRelayLED1.provisionDigitalOutputPin(RaspiPin.GPIO_01,"RelayLED1",PinState.HIGH); //OFF
		
		System.out.println("PIR Motion Sensor is looking for any movement!!!..");
		//Create gpio controller for PIR Motion Sensor listening on the pin GPIO_04        
		final GpioController gpioPIRMotionSensor = GpioFactory.getInstance(); 
		final GpioPinDigitalInput pirMotionsensor = gpioPIRMotionSensor.provisionDigitalInputPin(RaspiPin.GPIO_04, PinPullResistance.PULL_DOWN);
		
		//Create gpio controller for Buzzer listening on the pin GPIO_05 with default PinState as LOW                    
		final GpioController gpioBuzzer = GpioFactory.getInstance();           
		final GpioPinDigitalOutput buzzer = gpioBuzzer.provisionDigitalOutputPin(RaspiPin.GPIO_05,"Buzzer",PinState.LOW);
		buzzer.low();
		
		//Create and register gpio pin listener on PIRMotion Sensor GPIO Input instance            
		pirMotionsensor.addListener(new GpioPinListenerDigital() {
			
			public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
				//if the event state is High then print "Intruder Detected" and turn the Relay and Light ON by invoking the low() method
				if(event.getState().isHigh()){  
		            System.out.println("Intruder Detected!, Relay is ON, Light is ON and Buzzer is ON");
		            buzzer.high(); //Buzzer to be made High
		            relayLED1.low(); //ON
		            try {
		            	//Take two snaps from Camera Module
						takeSnap();
					} catch (Exception e) {
						System.out.println("Exception happened while taking the Snap..");
						e.printStackTrace();
					}
		        }   
				//if the event state is Low then print "All is quiet.." and make the Relay and Light OFF by invoking the high() method
		        if(event.getState().isLow()){   
		            System.out.println("All is quiet, Relay is OFF, Light is OFF and Buzzer is OFF");
		            buzzer.low(); //Buzzer to be made Low
		            relayLED1.high(); //OFF
		        } 
			}
		});
		
		try {           
		    // keep program running until user aborts 
		    for (;;) {      
		        //Thread.sleep(500);  
		    }       
		}           
		catch (final Exception e) {         
		    System.out.println(e.getMessage());     
		}finally{
			System.out.println("LED's on Relay IN1 will turn OFF..");
			relayLED1.high(); //OFF
		}
	
		System.out.println("LED's on Relay IN1 will turn OFF..");
		relayLED1.high(); //OFF
        System.out.println("Exiting Relay Circuit With Mains Example...");

	}
	
	/**
	 * Takes two snaps with the file name as "snap_<dateformat_in_yyyyMMddHHmmss>.jpg" with nopreview 
	 * @throws Exception
	 */
	public void takeSnap() throws Exception
	  {
	    Runtime rt = Runtime.getRuntime();
	    TwilioMMS twilioMMS = new TwilioMMS();
	    System.out.println("Raspberry Camera is going to take 2 snaps...");
	    System.out.println("Started taking the snaps...");
	    for (int i=1; i<=2; i++)
	    {
	      long before = System.currentTimeMillis();
	      //The below raspistill command will take photos with the image name as "snap_<dateformat_in_yyyyMMddHHmmss>.jpg" with nopreview 
	      String dateTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
	      String fileName="snap_" + dateTime + ".jpg";
	      Process snap = rt.exec("raspistill --width 400 --height 300 --timeout 1 --output " + fileName + " --nopreview");
	      snap.waitFor(); // Sync
	      long after = System.currentTimeMillis();
	      System.out.println("Snapshot Take #" + i + " is complete in " + Long.toString(after - before) + " ms. You can view the snap snap_" + dateTime + ".jpg in the project root folder i.e inside \\iot-pi4j-raspi-relay ");
	      File snapFile = new File(fileName);
	      //Move the file to Apache root folder path /var/www/html/images/
	      if(snapFile.renameTo(new File("/var/www/html/images/" + snapFile.getName()))){
    		System.out.println("File "+snapFile.getName()+" is moved successfully to /var/www/html/images/");
    	   }else{
    		System.out.println("File "+snapFile.getName()+" is failed to move!");
    	   }
	      //Send MMS message using Twilio to the recipient mobile number
	      twilioMMS.sendMMS(fileName);
	    }
	    
	    System.out.println("Completed taking the snaps..");
	  }
	
	public static void main(String args[]){
		RelayWithMainsNMotionSensorNBuzzerNCam stepperMotor = new RelayWithMainsNMotionSensorNBuzzerNCam();
		stepperMotor.controlRelayCircuit();
		
	}

}
