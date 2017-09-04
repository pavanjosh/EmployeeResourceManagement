package com.cogito.erm.compliance.compliancecheck.controller;

import com.cogito.erm.compliance.compliancecheck.service.SchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by pavankumarjoshi on 6/08/2017.
 */
@RestController
public class JobController {

    @Autowired
    SchedulerService schedulerService;

    @RequestMapping(value = "/runjob",method = RequestMethod.GET)
    public String runJob(){
        schedulerService.scanForDates();
        return "Email will be sent shortly";
    }
}
