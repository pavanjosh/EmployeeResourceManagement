package com.cogito.erm.compliance.compliancecheck.service;

import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.Map;

/**
 * Created by pavankumarjoshi on 19/06/2017.
 */

public interface CogitoEmailServiceIF {

     public void sendEmail(Map<String,List<String>> employeeMessageMap,String locationName,String emailAddresses);

}
