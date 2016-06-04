package com.agilerules.iotled.mqtt;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Properties;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import com.google.gson.JsonObject;

/**
 * This is the MQTT Callback class which overrides the MQTT Call back methods 
 *
 */
class SimpleCallback implements MqttCallback {

	//Called when the client lost the connection to the broker
	public void connectionLost(Throwable arg0) {
		System.out.println("Connection lost to the broker tcp://192.168.1.2:1883");
	}
	
    //Called when a new message has arrived
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        System.out.println("-------------------------------------------------");
        System.out.println("| Topic:" + topic);
        System.out.println("| Message: " + new String(message.getPayload()));
        System.out.println("-------------------------------------------------");
    }

    public void deliveryComplete(IMqttDeliveryToken token) {
		System.out.println("Delivery is Complete");
		
	}
	
}

/**
 * This class holds the MQTT methods to Connect, Publish & Subscribe to Broker
 *
 */
public class MqttPublishSubscribeUtility {
	
	private final static String PROPERTIES_FILE_NAME = "/mqtt.properties";
	Properties props = new Properties();
	
	public MqttClient mqttConnect() throws MqttException{
		MemoryPersistence persistence = new MemoryPersistence();
		/**
		  * Load device properties
		  */
		try {
			props.load(MqttPublishSubscribeUtility.class.getResourceAsStream(PROPERTIES_FILE_NAME));
		} catch (IOException e) {
			System.err.println("Not able to read the properties file, exiting..");
			System.exit(-1);
		}
		System.out.println("About to connect to MQTT broker with the following parameters: - BROKER_URL=" + props.getProperty("BROKER_URL")+" CLIENT_ID="+props.getProperty("CLIENT_ID"));
		MqttClient sampleClient = new MqttClient(props.getProperty("BROKER_URL"), props.getProperty("CLIENT_ID"),persistence);
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(true);
        sampleClient.connect(connOpts);
       
        System.out.println("Connected");
		return sampleClient;
	}
	

	
	public void mqttConnectNPublishNSubscribe(JsonObject payload){
	    try {
	    	MqttClient sampleClient = mqttConnect();
	        System.out.println("Publish message="+payload.toString());
	        sampleClient.subscribe(props.getProperty("TOPIC_NAME"), 1);
	        MqttMessage message = new MqttMessage(payload.toString().getBytes(Charset.forName("UTF-8")));
	        if(props.getProperty("QOS")!=null){
	        	message.setQos(Integer.parseInt(props.getProperty("QOS")));
	    	}
	        sampleClient.setCallback(new SimpleCallback());
	        sampleClient.publish(props.getProperty("TOPIC_NAME"), message);
	        System.out.println("Message published");
	        System.out.println("About to disconnect from MQTT Broker..");
	        sampleClient.disconnect();
	        System.out.println("Successfully disconnected from MQTT Broker.");
	    } catch(MqttException me){
	        System.out.println("reason " + me.getReasonCode());
	        System.out.println("msg " + me.getMessage());
	        System.out.println("loc " + me.getLocalizedMessage());
	        System.out.println("cause " + me.getCause());
	        System.out.println("except " + me);
	        me.printStackTrace();
	    }
	}
}