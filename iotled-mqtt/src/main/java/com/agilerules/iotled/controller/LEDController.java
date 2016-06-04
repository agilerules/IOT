package com.agilerules.iotled.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.agilerules.iotled.model.LedModel;
import com.agilerules.iotled.mqtt.MqttPublishSubscribeUtility;
import com.agilerules.iotled.repositories.LedDAORepository;
import com.google.gson.JsonObject;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

/**
 * This is Controller class which defines the URI mappings like / and /light and /display
 *
 */
@RestController
public class LEDController {
	
	@Autowired
	private LedDAORepository ledDAORepository;
	
	public static GpioPinDigitalOutput pin;
	public int blinkCounter = 0;
	
	@RequestMapping("/")
	public String greeting(){
		return "Hello World!!";
	}
	
	@RequestMapping("/light")
	public String light(){
		LedModel ledModel = new LedModel();
		ledModel.setBlinkStatus("OFF");
		MqttPublishSubscribeUtility mqttPublishSubscribeUtility = new MqttPublishSubscribeUtility();
		if(pin==null){
			System.out.println("Entering for the first time..");
			GpioController gpio = GpioFactory.getInstance();
			pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01,"My LED",PinState.LOW);
			ledModel.setBlinkStatus("OFF");
		}
		pin.toggle();
		if( pin.getState().isHigh()){
			System.out.println("Pin state is HIGH!!!");
			ledModel.setBlinkStatus("ON");
		}
		blinkCounter++;
		ledModel.setBlinkCounter(blinkCounter);
		ledModel.setDateTimeStamp(new Date().toString());
		
		JsonObject payload = new JsonObject();
		payload.addProperty("Id", ledModel.getId());
        payload.addProperty("blinkStatus", ledModel.getBlinkStatus());
        payload.addProperty("dateTimeStamp",  ledModel.getDateTimeStamp());
        payload.addProperty("blinkCounter",  ledModel.getBlinkCounter());
		
        ledModel.setJsonPayload(payload.toString());
		//Connect to MQTT Broker, Publish the message to topic 
		mqttPublishSubscribeUtility.mqttConnectNPublishNSubscribe(payload);
		
		//Persist the LedModel to MySQL Database
		System.out.println("About to persist the records to MySQL Database..");
		ledDAORepository.save(ledModel);
		System.out.println("Persisting the records to MySQL Database is complete.");
		return "Light status is: "+ledModel.getBlinkStatus()+ " as of the Time:"+ledModel.getDateTimeStamp()+" and the blink counter is:"+ledModel.getBlinkCounter();
	}
	
	/**
	   * /display  --> Display the top 10 records
	   * 
	   * @return A StringBuffer concatenated by all the top 10 records
	   */
	  @RequestMapping("/display")
	  public StringBuffer display() {
		  StringBuffer strBuffer = new StringBuffer();
		  List<LedModel> ledModels = ledDAORepository.findTop10ByOrderByIdDesc();
		  for (LedModel ledModel : ledModels){
			  strBuffer.append("<html><body>");
			  strBuffer.append("Id="+ledModel.getId()+" |BlinkStatus="+ledModel.getBlinkStatus()+" |DateTimeStamp="+ledModel.getDateTimeStamp()+" |Json Payload="+ledModel.getJsonPayload());
			  strBuffer.append(System.getProperty("line.separator"));
			  strBuffer.append("<br>");
			  strBuffer.append("</body></html>");
		  }
	    return strBuffer;
	  }

}
