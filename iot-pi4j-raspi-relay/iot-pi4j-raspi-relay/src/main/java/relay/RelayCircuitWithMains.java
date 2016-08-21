package relay;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.wiringpi.GpioUtil;

/**
 * Demonstrates how to control a Relay Circuit
 * using the GPIO pins on the Raspberry Pi.
 *
 */
public class RelayCircuitWithMains {
	
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
		System.out.println("LED's on Relay IN1 will turn ON..");
		GpioPinDigitalOutput relayLED1 = gpioRelayLED1.provisionDigitalOutputPin(RaspiPin.GPIO_01,"RelayLED1",PinState.HIGH); //OFF
		relayLED1.low(); //ON
		
		introduceDelay(10000);
		System.out.println("LED's on Relay IN1 will turn OFF..");
		relayLED1.high(); //OFF
	
        System.out.println("Exiting Relay Circuit With Mains Example...");

	}
	
	/**
	 * Introduce Delay with parameter in milliseconds
	 * @param n Delay parameter in milliseconds
	 */
	public void introduceDelay(int n){
		try {
			System.out.println("Wait for "+ (n/1000) +" seconds..");
			Thread.sleep(n);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]){
		RelayCircuitWithMains stepperMotor = new RelayCircuitWithMains();
		stepperMotor.controlRelayCircuit();
		
	}

}
