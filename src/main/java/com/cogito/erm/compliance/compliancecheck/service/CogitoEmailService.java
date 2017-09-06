package com.cogito.erm.compliance.compliancecheck.service;

import com.cogito.erm.compliance.compliancecheck.model.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by pavankumarjoshi on 19/06/2017.
 */

@Service
public class CogitoEmailService implements CogitoEmailServiceIF {

    private static final Logger Log = LoggerFactory.getLogger(CogitoEmailService.class);

    @Value("${reset.password.from.email}")
    private String fromEmail;

    @Value("${mail.server.documents.expiry.subject}")
    private String documentsExpirySubject;

    @Value("${mail.server.documents.expiry.content}")
    private String documentsExpiryContent;

    @Value("${mail.server.email.signature}")
    private String mailSignature;

    @Value("#{'${mail.ccemail}'.split(',')}")
    private List<String> bccEmailList;

    @Autowired
    private JavaMailSenderImpl javaMailSender;

    @Override
    public void sendEmail(Map<String,List<String>> employeesMessageMap, String locationName,String emailAddressList) {
        Log.debug("in sendEmail method of Email Service");

        SimpleMailMessage passwordResetEmail = new SimpleMailMessage();
        passwordResetEmail.setFrom(fromEmail);
        if(bccEmailList!=null){
            String[] bccEmailStrings = bccEmailList.toArray(new String[0]);
            passwordResetEmail.setBcc(bccEmailStrings);
        }

        if (emailAddressList!=null){
            String[] emailAddresses = emailAddressList.split(",");
            passwordResetEmail.setTo(emailAddresses);
        }
        else{
            Log.error("There is no to Email address for location {}",locationName);
        }


        passwordResetEmail.setSubject(documentsExpirySubject  + " For Location " + locationName);

        String messageText = getMessagetext(employeesMessageMap);
        passwordResetEmail.setText(documentsExpiryContent + "\n\n" + messageText +"\n\n" +
                "\n\n"+ mailSignature);

        Log.info("The SimpleMailMessage to be sent is {}", passwordResetEmail);
        javaMailSender.send(passwordResetEmail);
        Log.debug("The mail is sent successfully");
    }

    private String getMessagetext(Map<String,List<String>> employeeMessageMap){

        StringBuilder stringBuilder = new StringBuilder();
        Set<String> keySet = employeeMessageMap.keySet();
        for(String key : keySet){
            List<String> employeeDetailsList = employeeMessageMap.get(key);
            if(!CollectionUtils.isEmpty(employeeDetailsList)){
                for(String message: employeeDetailsList){
                    stringBuilder.append(key + ":"+message + " \r\n");
                }
            }
            stringBuilder.append("\r\n");
        }
        return stringBuilder.toString();
    }
}
