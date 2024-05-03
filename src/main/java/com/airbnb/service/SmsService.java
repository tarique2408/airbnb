package com.airbnb.service;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.twilio.type.PhoneNumber;

@Service
public class SmsService {

    @Value("${twilio.accountSid}")
    private String ACCOUNT_SID;

    @Value("${twilio.authToken}")
    private String AUTH_TOKEN;

    @Value("${twilio.phoneNumber}")
    private String TWILIO_PHONE_NUMBER;
    public String sendSms(String to, String body) {
        //inti() used to login into twilio account
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        Message message = Message.creator(
                        new PhoneNumber(to),
                        new PhoneNumber(TWILIO_PHONE_NUMBER),
                        body)
                .create();

        return message.getSid();
    }

}
