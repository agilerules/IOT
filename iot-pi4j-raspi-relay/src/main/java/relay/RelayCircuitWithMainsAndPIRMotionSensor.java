package relay;

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

/**
 * Demonstrates how to control a Relay Circuit
 * using the GPIO pins on the Raspberry Pi.
 *
 */
public class RelayCircuitWithMainsAndPIRMotionSensor {
	
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
		
		//Create and register gpio pin listener on PIRMotion Sensor GPIO Input instance            
		pirMotionsensor.addListener(new GpioPinListenerDigital() {
			
			public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
				//if the event state is High then print "Intruder Detected" and turn the Relay and Light ON by invoking the low() method
				if(event.getState().isHigh()){  
		            System.out.println("Intruder Detected!, Relay is ON and Light is ON"); 
		            relayLED1.low(); //ON
		        }   
				//if the event state is Low then print "All is quiet.." and make the Relay and Light OFF by invoking the high() method
		        if(event.getState().isLow()){   
		            System.out.println("All is quiet, Relay is OFF and Light is OFF");
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
	
	public static void main(String args[]){
		RelayCircuitWithMainsAndPIRMotionSensor stepperMotor = new RelayCircuitWithMainsAndPIRMotionSensor();
		stepperMotor.controlRelayCircuit();
		
	}

}
