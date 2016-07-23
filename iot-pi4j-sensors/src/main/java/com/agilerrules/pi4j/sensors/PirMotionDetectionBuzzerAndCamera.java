package com.agilerrules.pi4j.sensors;

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

public class PirMotionDetectionBuzzerAndCamera {
	
	/**
	 * Takes two snaps with the file name as "snap_<dateformat_in_yyyyMMddHHmmss>.jpg" with nopreview 
	 * @throws Exception
	 */
	public void takeSnap() throws Exception
	  {
	    Runtime rt = Runtime.getRuntime();
	    System.out.println("Raspberry Camera is going to take 2 snaps, be ready...");
	    System.out.println("Started taking the snaps...");
	    for (int i=1; i<=2; i++)
	    {
	      long before = System.currentTimeMillis();
	      //The below raspistill command will take photos with the image name as "snap_<dateformat_in_yyyyMMddHHmmss>.jpg" with nopreview 
	      String dateTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
	      Process snap = rt.exec("raspistill --timeout 5 --output snap_" + dateTime + ".jpg --nopreview");
	      snap.waitFor(); // Sync
	      long after = System.currentTimeMillis();
	      System.out.println("Snapshot Take #" + i + " is complete in " + Long.toString(after - before) + " ms. You can view the snap snap_" + dateTime + ".jpg in the project root folder i.e inside \\iot-pi4j-sensors ");
	    }
	    System.out.println("Completed taking the snaps..");
	  }
	
	public void detectMotionAndGlowLED(){
		System.out.println("Starting Pi4J Motion Sensor Example"); 
		System.out.println("PIR Motion Sensor is ready and looking for any movement..");

		//This is required to enable Non Privileged Access to avoid applying sudo to run Pi4j programs
		GpioUtil.enableNonPrivilegedAccess();
	
		//Create gpio controller for PIR Motion Sensor listening on the pin GPIO_04        
		final GpioController gpioPIRMotionSensor = GpioFactory.getInstance(); 
		final GpioPinDigitalInput pirMotionsensor = gpioPIRMotionSensor.provisionDigitalInputPin(RaspiPin.GPIO_04, PinPullResistance.PULL_DOWN);          
	
		//Create gpio controller for LED listening on the pin GPIO_01 with default PinState as LOW                    
		final GpioController gpioLED = GpioFactory.getInstance();           
		final GpioPinDigitalOutput led = gpioLED.provisionDigitalOutputPin(RaspiPin.GPIO_01,"LED",PinState.LOW);
		led.low();    
		
		//Create gpio controller for Buzzer listening on the pin GPIO_06 with default PinState as LOW                    
		final GpioController gpioBuzzer = GpioFactory.getInstance();           
		final GpioPinDigitalOutput buzzer = gpioBuzzer.provisionDigitalOutputPin(RaspiPin.GPIO_06,"Buzzer",PinState.LOW);
		buzzer.low();
	
		//Create and register gpio pin listener on PIRMotion Sensor GPIO Input instance            
		pirMotionsensor.addListener(new GpioPinListenerDigital() {
			
			public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
				//if the event state is High then print "Intruder Detected" and turn the LED ON by invoking the high() method
				if(event.getState().isHigh()){  
		            System.out.println("Intruder Detected!, LED is ON and Buzzer is ON"); 
		            led.high();
		            buzzer.high();
		            try {
		            	//Take two snaps from Camera Module
						takeSnap();
					} catch (Exception e) {
						System.out.println("Exception happened while taking the Snap..");
						e.printStackTrace();
					}
		        }   
				//if the event state is Low then print "All is quiet.." and make the LED OFF by invoking the low() method
		        if(event.getState().isLow()){   
		            System.out.println("All is quiet, LED is OFF and Buzzer is OFF");
		            led.low();
		            buzzer.low();
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
		}

	}
	
	public static void main(String args[]){
		PirMotionDetectionBuzzerAndCamera pirMotionDetection = new PirMotionDetectionBuzzerAndCamera();
		pirMotionDetection.detectMotionAndGlowLED();
		
	}

}
