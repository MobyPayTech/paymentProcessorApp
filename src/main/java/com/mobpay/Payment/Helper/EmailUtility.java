package com.mobpay.Payment.Helper;

import net.sargue.mailgun.Configuration;
import net.sargue.mailgun.Mail;

public class EmailUtility {

    public static void sentEmail(){
        Configuration configuration = new Configuration()
                .domain("mg2.airapay.my")
                .apiKey("b61574e1f0e8b8d81e767fdd1c9481af-9776af14-e1d40911")
                .from("Mobypay Error Events", "noreply@airapay.my");

        Mail.using(configuration)
                .to("selva@mobypay.my")
                .subject("Error Events")
                .text("This is a test")
                .build()
                .send();
    }
}
