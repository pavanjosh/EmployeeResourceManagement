package com.cogito.erm.compliance.compliancecheck.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * Created by pavankumarjoshi on 19/06/2017.
 */
@Configuration
public class MailConfig {

    @Value("${mail.server.port}")
    private int mailServerPort;

    @Value("${mail.server.host}")
    private String mailServerHost;

    @Value("${mail.server.protocol}")
    private String mailServerProtocol;

    @Value("${mail.server.username}")
    private String mailServerUserName;

    @Value("${mail.server.password}")
    private String mailServerPassword;

    @Bean
    public JavaMailSenderImpl javaMailSender(){
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setPort(mailServerPort);
        javaMailSender.setHost(mailServerHost);
        javaMailSender.setProtocol(mailServerProtocol);
        javaMailSender.setUsername(mailServerUserName);
        javaMailSender.setPassword(mailServerPassword);
        return javaMailSender;
    }

}
