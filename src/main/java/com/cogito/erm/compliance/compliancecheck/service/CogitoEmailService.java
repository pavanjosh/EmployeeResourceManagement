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
import org.springframework.util.MultiValueMap;

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

    @Value("#{'${mail.bccemail}'.split(',')}")
    private List<String> bccEmailList;

    @Autowired
    private JavaMailSenderImpl javaMailSender;

    @Override
    public void sendEmail(Map<String, Map<String,List<String>>> multiValueMap,
                          String locationName, String emailAddresses) {
        Log.debug("in sendEmail method of Email Service");

        SimpleMailMessage passwordResetEmail = new SimpleMailMessage();
        passwordResetEmail.setFrom(fromEmail);
        if(bccEmailList!=null){
            String[] bccEmailStrings = bccEmailList.toArray(new String[0]);
            passwordResetEmail.setBcc(bccEmailStrings);
        }

        if (emailAddresses!=null){
            String[] emailAddressesArray = emailAddresses.split(",");
            passwordResetEmail.setTo(emailAddressesArray);
        }
        else{
            Log.error("There is no to Email address for location {}",locationName);
        }

        passwordResetEmail.setSubject(documentsExpirySubject  + " For Location " + locationName);

        String messageText = getMessagetext(multiValueMap);
        if(messageText != null){
            passwordResetEmail.setText(documentsExpiryContent + "\n\n" + messageText +"\n\n" +
                    "\n\n"+ mailSignature);

            Log.info("The SimpleMailMessage to be sent is {}", passwordResetEmail);
            javaMailSender.send(passwordResetEmail);
            Log.debug("The mail is sent successfully");
        }
        else{
            Log.debug("Email was not sent because there was no content");
        }
    }

    private String getMessagetext(Map<String,Map<String,List<String>>> multiValueMap){

        StringBuilder stringBuilder = new StringBuilder();
        boolean valuePresent = false;
        if(multiValueMap == null || multiValueMap.size() == 0)
            return null;

        Set<String> categories = multiValueMap.keySet();

        for(String category : categories) {

            Map<String, List<String>> employeeNameListMap = multiValueMap.get(category);
            if (employeeNameListMap != null && employeeNameListMap.size() > 0) {
                stringBuilder.append(category + "\r\n");
                stringBuilder.append("------------------------------------------------------\r\n");
                Set<String> keySet = employeeNameListMap.keySet();
                for (String key : keySet) {
                    List<String> employeeDetailsList = employeeNameListMap.get(key);
                    if (!CollectionUtils.isEmpty(employeeDetailsList)) {
                        for (String message : employeeDetailsList) {
                            stringBuilder.append(key + ":" + message + " \r\n");
                            stringBuilder.append("\r\n");
                            valuePresent = true;
                        }
                    }
                }
                stringBuilder.append("\r\n");
            }

        }
        if(valuePresent)
            return stringBuilder.toString();
        else
            return null;
    }
}
