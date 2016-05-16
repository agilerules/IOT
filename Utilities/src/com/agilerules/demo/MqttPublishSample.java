package com.agilerules.demo;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttPublishSample {
	public static void main(String arg[]){
		String topic        = "hello/world";
        String content      = "Message from MqttPublishSample";
        int qos             = 2;
        String broker       = "tcp://192.168.1.2:1883"; //Note: Modify the MQTT server IP in your case
        String clientId     = "JavaSample";
        MemoryPersistence persistence = new MemoryPersistence();
        
        try{
        	//Connect to client
        	MqttClient mqttClient = new MqttClient(broker, clientId, persistence);
        	MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        	mqttConnectOptions.setCleanSession(true);
        	System.out.println("Connecting to broker: "+broker);
        	mqttClient.connect(mqttConnectOptions);
        	System.out.println("Connected");
        	
        	//Publish the message
        	System.out.println("Publishing message: "+content);
        	MqttMessage message = new MqttMessage(content.getBytes());
        	message.setQos(qos);
        	mqttClient.publish(topic, message);
        	System.out.println("Message published");
        	
        	//Disconnect
        	mqttClient.disconnect();
        	System.out.println("Disconnected");
            System.exit(0);
        	 
        }catch(MqttException me) {
            System.out.println("reason "+me.getReasonCode());
            System.out.println("msg "+me.getMessage());
            System.out.println("loc "+me.getLocalizedMessage());
            System.out.println("cause "+me.getCause());
            System.out.println("excep "+me);
            me.printStackTrace();
        }
	}

}
