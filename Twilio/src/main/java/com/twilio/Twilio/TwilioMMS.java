package com.twilio.Twilio;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.MessageFactory;
import com.twilio.sdk.resource.instance.Message;

public class TwilioMMS {
	// Find your Account Sid and Token at twilio.com/user/account
	public static final String ACCOUNT_SID = "<Your_Twilo_Account_SID_Here>";
	public static final String AUTH_TOKEN = "Your_Auth_Token_Here";
	
	
	public void sendMMS(String mediaURL){
		TwilioRestClient client = new TwilioRestClient(ACCOUNT_SID, AUTH_TOKEN);

        // Build the parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("To", "<Mobile_No_for_which_you_want_to_send_SMS>"));
        params.add(new BasicNameValuePair("From", "<Your_Twilio_Registered_Number>"));
        params.add(new BasicNameValuePair("Body", "This is test message and check the below URL:"));
        params.add(new BasicNameValuePair("MediaUrl", mediaURL));
        
        MessageFactory messageFactory = client.getAccount().getMessageFactory();
        Message message;
		try {
			System.out.println("About to send MMS using Twilio to the mobile number ..");
			message = messageFactory.create(params);
			System.out.println("MMS is successfully send using Twilio and the SID of the message is: "+message.getSid());
		} catch (TwilioRestException e) {
			System.out.println("Error while sending the SMS from Twilio..");
			e.printStackTrace();
		}
	}

	public static void main(String args[]){
		TwilioMMS twilioMMS = new TwilioMMS();
		String mediaURL = "http://farm2.static.flickr.com/1075/1404618563_3ed9a44a3a.jpg";
		twilioMMS.sendMMS(mediaURL);
		
	}
}
