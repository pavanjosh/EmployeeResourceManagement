package com.cogito.erm.compliance.compliancecheck.service;

import com.cogito.erm.compliance.compliancecheck.model.Employee;
import com.cogito.erm.compliance.compliancecheck.model.LocationToMailMapper;
import com.cogito.erm.compliance.compliancecheck.model.MultiDataHolder;
import com.cogito.erm.compliance.compliancecheck.repo.EmployeeRepo;
import com.cogito.erm.compliance.compliancecheck.repo.LocationToMailMapperRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by pavankumarjoshi on 6/09/2017.
 */
@Service
public class DBService implements DBServiceIF{

    @Autowired
    private EmployeeRepo employeeRepo;

    @Autowired
    private LocationToMailMapperRepo locationToMailMapperRepo;

    @Override
    public void update(MultiDataHolder multiDataHolder) {


        // then compare those names who are not in the current List
        // then save the current list
        // delete from the db who are missing in the list
        // save location to mail mapper
        Set<String> stringSet = new HashSet<>();


        // First get all the current employees names.
        Iterable<Employee> dbEmployees = employeeRepo.findAll();

        List<Employee> employeeList = multiDataHolder.getEmployeeList();
        List<LocationToMailMapper> locationToMailMapperList = multiDataHolder.getLocationToMailMapperList();

        for (Employee employee :employeeList){
            stringSet.add(employee.getFirstName()+employee.getLastName());
        }

        for (Employee employee:dbEmployees){
            if(! stringSet.contains(employee.getFirstName()+employee.getLastName())){
                employeeRepo.delete(employee);
            }
        }

        employeeRepo.save(employeeList);
        locationToMailMapperRepo.save(locationToMailMapperList);


    }
}
