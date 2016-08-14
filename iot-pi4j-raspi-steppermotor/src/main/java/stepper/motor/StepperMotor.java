package stepper.motor;

import java.util.Scanner;

import com.pi4j.component.motor.impl.GpioStepperMotorComponent;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.wiringpi.GpioUtil;

/**
 * Demonstrates how to control a stepper motor
 * using the GPIO pins on the Raspberry Pi.
 *
 */
public class StepperMotor {
	
	private Scanner scanner;
	
	public void controlStepperMotor(){
		System.out.println("Starting GPIO Stepper Motor Example..."); 
		
		//This is required to enable Non Privileged Access to avoid applying sudo to run Pi4j programs
		GpioUtil.enableNonPrivilegedAccess();
				
		// create gpio controller
        final GpioController gpio = GpioFactory.getInstance();
        
        // provision gpio pins #00 to #03 as output pins and ensure in LOW state
        final GpioPinDigitalOutput[] pins = {
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, PinState.LOW),
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, PinState.LOW),
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, PinState.LOW),
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, PinState.LOW)};
        
        // this will ensure that the motor is stopped when the program terminates
        gpio.setShutdownOptions(true, PinState.LOW, pins);
        
        // create motor component
        GpioStepperMotorComponent motor = new GpioStepperMotorComponent(pins);

        // @see http://www.lirtex.com/robotics/stepper-motor-controller-circuit/
        //      for additional details on stepping techniques

        // create byte array to demonstrate a single-step sequencing
        // (This is the most basic method, turning on a single electromagnet every time.
        //  This sequence requires the least amount of energy and generates the smoothest movement.)
        byte[] single_step_sequence = new byte[4];
        single_step_sequence[0] = (byte) 0b0001;
        single_step_sequence[1] = (byte) 0b0010;
        single_step_sequence[2] = (byte) 0b0100;
        single_step_sequence[3] = (byte) 0b1000;
        
        // create byte array to demonstrate a double-step sequencing
        // (In this method two coils are turned on simultaneously.  This method does not generate
        //  a smooth movement as the previous method, and it requires double the current, but as
        //  return it generates double the torque.)
        byte[] double_step_sequence = new byte[4];
        double_step_sequence[0] = (byte) 0b0011;
        double_step_sequence[1] = (byte) 0b0110;
        double_step_sequence[2] = (byte) 0b1100;
        double_step_sequence[3] = (byte) 0b1001;
        
        // create byte array to demonstrate a half-step sequencing
        // (In this method two coils are turned on simultaneously.  This method does not generate
        //  a smooth movement as the previous method, and it requires double the current, but as
        //  return it generates double the torque.)
        byte[] half_step_sequence = new byte[8];
        half_step_sequence[0] = (byte) 0b0001;
        half_step_sequence[1] = (byte) 0b0011;
        half_step_sequence[2] = (byte) 0b0010;
        half_step_sequence[3] = (byte) 0b0110;
        half_step_sequence[4] = (byte) 0b0100;
        half_step_sequence[5] = (byte) 0b1100;
        half_step_sequence[6] = (byte) 0b1000;
        half_step_sequence[7] = (byte) 0b1001;
        
        int oneRevolution = 2038;
        int quarterRevolution = oneRevolution / 4;
        int halfRevolution = oneRevolution / 2;
        int oneDegreeRevolution = oneRevolution / 360;
        
   
        // define stepper parameters before attempting to control motor
        // anything lower than 2 ms does not work for my sample motor using single step sequence
        motor.setStepInterval(2);
        motor.setStepSequence(single_step_sequence);
        //motor.setStepSequence(double_step_sequence);
        //motor.setStepSequence(half_step_sequence);
        //System.out.println("Step Sequence used:"+ motor.getStepSequence());
        
        // There are 32 steps per revolution on my sample motor, and inside is a ~1/64 reduction gear set.
        // Gear reduction is actually: (32/9)/(22/11)x(26/9)x(31/10)=63.683950617
        // This means is that there are really 32*63.683950617 steps per revolution =  2037.88641975 ~ 2038 steps!
        motor.setStepsPerRevolution(oneRevolution);
       
        // test motor control : STEPPING FORWARD for One Revolution
        System.out.println("   Motor is going to FORWARD for 1 Revolution = 2038 steps. Once you are ready, press \"ENTER\" to continue...");
        //Wait for the user input
        scanner = new Scanner(System.in);
	    scanner.nextLine();
        motor.step(oneRevolution);
      
        
        // test motor control : STEPPING REVERSE for 1 Revolution
        System.out.println("   Motor is going to REVERSE for 1 Revolution = 2038 steps. Once you are ready, press \"ENTER\" to continue...");
        scanner = new Scanner(System.in);
	    scanner.nextLine();
        motor.step(-oneRevolution);
        
        // test motor control : ROTATE FORWARD for 2 Revolutions
        System.out.println("   Motor is going to FORWARD for 2 Revolutions = 2 x 2038 = 4076 steps.  Once you are ready, press \"ENTER\" to continue...");
        scanner = new Scanner(System.in);
	    scanner.nextLine();
        motor.rotate(2);
        
        
        // test motor control : ROTATE REVERSE for 2 Revolutions
        System.out.println("   Motor is going to REVERSE for 2 Revolutions = 2 x 2038 = 4076 steps.  Once you are ready, press \"ENTER\" to continue...");
        scanner = new Scanner(System.in);
	    scanner.nextLine();
        motor.rotate(-2);
        
        // test motor control : TIMED FORWARD
        System.out.println("   Motor is going to FORWARD for 5 seconds. Once you are ready, press \"ENTER\" to continue...");
        scanner = new Scanner(System.in);
	    scanner.nextLine();
        motor.forward(5000);
        
        // test motor control : TIMED REVERSE
        System.out.println("   Motor is going to REVERSE for 5 seconds. Once you are ready, press \"ENTER\" to continue...");
        scanner = new Scanner(System.in);
	    scanner.nextLine();
        motor.reverse(5000);
      
        
        // test motor control : STEPPING FORWARD for Half Revolution
        System.out.println("   Motor is going to FORWARD for Half Revolution = 2038/2 = 1019 steps. Once you are ready, press \"ENTER\" to continue...");
        scanner = new Scanner(System.in);
	    scanner.nextLine();
        motor.step(halfRevolution);
        
        // test motor control : STEPPING REVERSE for Half Revolution
        System.out.println("   Motor is going to REVERSE for Half Revolution = 2038/2 = 1019 steps. Once you are ready, press \"ENTER\" to continue...");
        scanner = new Scanner(System.in);
	    scanner.nextLine();
        motor.step(halfRevolution);
       
        // test motor control : STEPPING FORWARD for Quarter Revolution
        System.out.println("   Motor is going to FORWARD for Quarter Revolution = 2038/4 = 509.5 steps. Once you are ready, press \"ENTER\" to continue...");
        scanner = new Scanner(System.in);
	    scanner.nextLine();
        motor.step(quarterRevolution);
        
        
        // test motor control : STEPPING REVERSE for Quarter Revolution
        System.out.println("   Motor is going to REVERSE for Quarter Revolution = 2038/4 = 509.5 steps. Once you are ready, press \"ENTER\" to continue...");
        scanner = new Scanner(System.in);
	    scanner.nextLine();
        motor.step(-quarterRevolution);
                
        
        // test motor control : STEPPING FORWARD for One Degree Revolution
        System.out.println("   Motor is going to FORWARD One Degree Revolution = 2038/360 = 5.66 steps. Once you are ready, press \"ENTER\" to continue...");
        scanner = new Scanner(System.in);
	    scanner.nextLine();
        motor.step(oneDegreeRevolution);
       
        
        // test motor control : STEPPING REVERSE for One Degree Revolution
        System.out.println("   Motor REVERSE One Degree Revolution = 2038/360 = 5.66 steps. Once you are ready, press \"ENTER\" to continue...");
        scanner = new Scanner(System.in);
	    scanner.nextLine();
        motor.step(-oneDegreeRevolution);
     
        // test motor control : ROTATE FORWARD with different timing and sequence
        System.out.println("   Motor FORWARD with DOUBLE STEP SEQUENCE - slower speed and higher torque for 1 Revolution = 2038 steps. Once you are ready, press \"ENTER\" to continue...");
        scanner = new Scanner(System.in);
	    scanner.nextLine();
        motor.setStepSequence(double_step_sequence);
        motor.setStepInterval(10);
        motor.rotate(1);
        
        // test motor control : ROTATE FORWARD with different timing and sequence
        System.out.println("   Motor FORWARD with HALF STEP SEQUENCE - slower speed and higher torque for 1 Revolution = 2038 steps. Once you are ready, press \"ENTER\" to continue...");
        scanner = new Scanner(System.in);
	    scanner.nextLine();
        motor.setStepSequence(half_step_sequence);
        motor.setStepInterval(10);
        motor.rotate(1);
        
        System.out.println("   Motor STOPPED.");
        
        // final stop to ensure no motor activity
        motor.stop();
        
        // stop all GPIO activity/threads by shutting down the GPIO controller
        // (this method will forcefully shutdown all GPIO monitoring threads and scheduled tasks)
        gpio.shutdown();

        System.out.println("Exiting GPIO Stepper Motor Example...");

	}
	
	public static void main(String args[]){
		StepperMotor stepperMotor = new StepperMotor();
		stepperMotor.controlStepperMotor();
		
	}

}
