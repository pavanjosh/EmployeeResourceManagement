package com.cogito.erm.compliance.compliancecheck.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pavankumarjoshi on 6/09/2017.
 */
public class MultiDataHolder {

    private List<Employee> employeeList = new ArrayList<>();

    private List<LocationToMailMapper> locationToMailMapperList = new ArrayList<>();

    public List<Employee> getEmployeeList() {
        return employeeList;
    }

    public void setEmployeeList(List<Employee> employeeList) {
        this.employeeList = employeeList;
    }

    public List<LocationToMailMapper> getLocationToMailMapperList() {
        return locationToMailMapperList;
    }

    public void setLocationToMailMapperList(List<LocationToMailMapper> locationToMailMapperList) {
        this.locationToMailMapperList = locationToMailMapperList;
    }
}
