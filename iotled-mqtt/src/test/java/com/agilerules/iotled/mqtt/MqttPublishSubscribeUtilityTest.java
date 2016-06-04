package com.agilerules.iotled.mqtt;

import java.util.Date;

import com.agilerules.iotled.model.LedModel;
import com.google.gson.JsonObject;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class MqttPublishSubscribeUtilityTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public MqttPublishSubscribeUtilityTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( MqttPublishSubscribeUtilityTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
    	MqttPublishSubscribeUtility mqttPublishSubscribeUtility = new MqttPublishSubscribeUtility();
    	LedModel ledModel = new LedModel();
		ledModel.setBlinkStatus("OFF");
		ledModel.setBlinkCounter(1);
		ledModel.setDateTimeStamp(new Date().toString());
    	JsonObject payload = new JsonObject();
        payload.addProperty("blinkStatus", ledModel.getBlinkStatus());
        payload.addProperty("dateTimeStamp",  ledModel.getDateTimeStamp());
        payload.addProperty("blinkCounter",  ledModel.getBlinkCounter());
    	mqttPublishSubscribeUtility.mqttConnectNPublishNSubscribe(payload);
    }
}
