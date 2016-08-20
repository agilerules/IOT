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
public class RelayCircuit {
	
	/**
	 * This method will control the Relay Circuit by turning them ON and OFF
	 * 
	 */
	public void controlRelayCircuit(){
		System.out.println("Starting Relay Circuit Example..."); 
		
		//This is required to enable Non Privileged Access to avoid applying sudo to run Pi4j programs
		GpioUtil.enableNonPrivilegedAccess();
		
		//Create gpio controller for LED listening on the pin GPIO_01 with default PinState as LOW                    
		final GpioController gpioRelayLED1 = GpioFactory.getInstance();
		
		//LOW-->ON,HIGH-->OFF
		System.out.println("All LED's on Relay will turn ON..");
		GpioPinDigitalOutput relayLED1 = gpioRelayLED1.provisionDigitalOutputPin(RaspiPin.GPIO_00,"RelayLED1",PinState.HIGH); //OFF
		relayLED1.low(); //ON
	
		final GpioController gpioRelayLED2 = GpioFactory.getInstance();           
		final GpioPinDigitalOutput relayLED2 = gpioRelayLED2.provisionDigitalOutputPin(RaspiPin.GPIO_01,"RelayLED2",PinState.HIGH); //OFF
		relayLED2.low();  //ON
		
		final GpioController gpioRelayLED3 = GpioFactory.getInstance();           
		final GpioPinDigitalOutput relayLED3 = gpioRelayLED3.provisionDigitalOutputPin(RaspiPin.GPIO_02,"RelayLED3",PinState.HIGH); //OFF
		relayLED3.low(); //ON
		
		final GpioController gpioRelayLED4 = GpioFactory.getInstance();           
		final GpioPinDigitalOutput relayLED4 = gpioRelayLED4.provisionDigitalOutputPin(RaspiPin.GPIO_03,"RelayLED4",PinState.HIGH); //OFF
		relayLED4.low(); //ON
		
		introduceDelay(2000);
		
		System.out.println("All LED's on Relay will turn OFF..");
		relayLED1.high(); //OFF
		introduceDelay(1000);
		relayLED2.high(); //OFF
		introduceDelay(1000);
		relayLED3.high(); //OFF
		introduceDelay(1000);
		relayLED4.high(); //OFF
		
		System.out.println("All LED's on Relay will turn ON again..");
		introduceDelay(1000);
		relayLED4.low(); //ON
		introduceDelay(1000);
		relayLED3.low(); //ON
		introduceDelay(1000);
		relayLED2.low(); //ON
		introduceDelay(1000);
		relayLED1.low(); //ON
		introduceDelay(1000);
		
		System.out.println("All LED's on Relay will turn OFF..");
		relayLED1.high(); //ON
		introduceDelay(1000);
		relayLED2.high(); //ON
		introduceDelay(1000);
		relayLED3.high(); //ON
		introduceDelay(1000);
		relayLED4.high(); //ON
		
        System.out.println("Exiting Relay Circuit Example...");

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
		RelayCircuit stepperMotor = new RelayCircuit();
		stepperMotor.controlRelayCircuit();
		
	}

}
