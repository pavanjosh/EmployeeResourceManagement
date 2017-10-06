package com.cogito.erm.compliance.compliancecheck.service;

import com.cogito.erm.compliance.compliancecheck.model.Employee;
import com.cogito.erm.compliance.compliancecheck.model.LocationToMailMapper;
import com.cogito.erm.compliance.compliancecheck.repo.EmployeeRepo;
import com.cogito.erm.compliance.compliancecheck.repo.LocationToMailMapperRepo;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class SchedulerService {

    private final String SECURITY_LICENSE_EXPIRY = "SecurityExpiry";
    private final String MSIC_EXPIRY = "MsicExpiry";
    private final String FIRSTAID_EXPIRY = "firstAidExpiry";
    private final String PAIND_EXPIRY = "paExpiry";
    private final String SPOTLESS_EXPIRY = "SpotlessExpiry";


    @Autowired
    private CogitoEmailServiceIF collabEmailService;

    private static final Logger log = LoggerFactory.getLogger(SchedulerService.class);

    @Value("${task.config.security.license.threshold}")
    private int securityExpiryDateThreshold;

    @Value("${task.config.msic.license.threshold}")
    private int msicExpiryDateThreshold;

    @Value("${task.config.firstaid.license.threshold}")
    private int firstAidExpiryDateThreshold;

    @Value("${task.config.portauthority.license.threshold}")
    private int paExpiryDateThreshold;

    @Value("${task.config.spotless.license.threshold}")
    private int spotlessExpiryDateThreshold;

    @Autowired
    private EmployeeRepo employeeRepo;

    @Autowired
    private LocationToMailMapperRepo locationToMailMapperRepo;

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    //@Scheduled(cron = "0 0 12 * * ?")
    @Scheduled(cron = "0 0 0 ? * 2/3")
    public void scanForDates() {

        log.info("In Send an email for expiry..." );
        Map<String, List<String>> employeeMissingDocumentsMap = new TreeMap<>();
        Map<String, List<String>> employeeLicenseExpiredMessageMap = new TreeMap<>();
        Map<String, List<String>> employeeLicenseAboutToExpireMessageMap = new TreeMap<>();

        Map<String,Map<String,List<String>>> multiValueMap = new LinkedHashMap<>();

        Iterable<LocationToMailMapper> all = locationToMailMapperRepo.findAll();
        for(LocationToMailMapper locationToMailMapper : all){

            employeeMissingDocumentsMap.clear();
            employeeLicenseExpiredMessageMap.clear();
            employeeLicenseAboutToExpireMessageMap.clear();

            List<Employee> allByLocation = employeeRepo.findAllByLocation(locationToMailMapper.getLocationName());

            for (Employee employee : allByLocation) {

                List<String> employeeLicenseMissingDetailsList = new ArrayList<>();
                List<String> employeeLicenseExpiredList = new ArrayList<>();
                List<String> employeeLicenseAboutToExpireList = new ArrayList<>();

                handleSecurity(employee, employeeLicenseMissingDetailsList,employeeLicenseExpiredList,employeeLicenseAboutToExpireList);
                handleMsic(employee, employeeLicenseMissingDetailsList,employeeLicenseExpiredList,employeeLicenseAboutToExpireList);
                handlefirstAid(employee, employeeLicenseMissingDetailsList,employeeLicenseExpiredList,employeeLicenseAboutToExpireList);
                handlePA(employee, employeeLicenseMissingDetailsList,employeeLicenseExpiredList,employeeLicenseAboutToExpireList);
                handleSpotless(employee, employeeLicenseMissingDetailsList,employeeLicenseExpiredList,employeeLicenseAboutToExpireList);

                employeeMissingDocumentsMap.put(employee.getFirstName()+","+employee.getLastName(),employeeLicenseMissingDetailsList);
                employeeLicenseExpiredMessageMap.put(employee.getFirstName()+","+employee.getLastName(),employeeLicenseExpiredList);
                employeeLicenseAboutToExpireMessageMap.put(employee.getFirstName()+","+employee.getLastName(),employeeLicenseAboutToExpireList);

            }
            multiValueMap.put("MISSING Documents" , employeeMissingDocumentsMap);
            multiValueMap.put("ALREADY EXPIRED Documents" , employeeLicenseExpiredMessageMap);
            multiValueMap.put("ABOUT TO EXPIRE Documents" , employeeLicenseAboutToExpireMessageMap);

            collabEmailService.sendEmail(multiValueMap,
                    locationToMailMapper.getLocationName(),
                    locationToMailMapper.getEmailAddress());

        }


    }

    private void handleSecurity(Employee employee,List<String> employeeMissingDetailsList,List<String> employeeLicenseExpiredList,
                                        List<String> employeeLicenseAboutToExpireList){
        String securityExpiry = employee.getSecurityExpiryDate();
        if(StringUtils.isEmpty(securityExpiry) || StringUtils.isEmpty(employee.getNswSecurity())){
            employeeMissingDetailsList.add("No Document for NSW Security License " );
        } else if("NO EXPIRY".equalsIgnoreCase(securityExpiry)) {
            log.info("security expiry for " + employee.getFirstName() + " " + employee.getLastName() + " is empty or NO expiry ");
        }
        else{

            // Take current Date
            // first check if expiry date is less than today
            // Then already expired
            // Else
            // if expiry < today+treshold then aboutToExpire
            LocalDate securityExpiryDate = LocalDate.parse(securityExpiry, formatter);

            DateTime expiryDateTime = new DateTime(securityExpiryDate.getYear(),securityExpiryDate.getMonthValue(),
                    securityExpiryDate.getDayOfMonth(),0,0);
            if(!expiryDateTime.isAfterNow()){
                employeeLicenseExpiredList.add("Security License already Expired with date " + employee.getSecurityExpiryDate());
            }
            else if(!expiryDateTime.isAfter(new DateTime().plusWeeks(securityExpiryDateThreshold))){
                employeeLicenseAboutToExpireList.add("Security License is about to expire with date " + employee.getSecurityExpiryDate());
            }
        }
    }

    private void handleMsic(Employee employee,List<String> employeeMissingDetailsList,List<String> employeeLicenseExpiredList,
    List<String> employeeLicenseAboutToExpireList){
        String msicExpiry = employee.getMsicExpiryDate();
        if(StringUtils.isEmpty(msicExpiry) || StringUtils.isEmpty(employee.getMsicExpiryDate())){
            employeeMissingDetailsList.add("No Document for MSIC "  );
        } else if(StringUtils.isEmpty(msicExpiry) || "NO EXPIRY".equalsIgnoreCase(msicExpiry)) {
            log.info("MSIC expiry for " + employee.getFirstName() + " " + employee.getLastName() + " is empty or NO expiry ");
        }
        else{
            LocalDate miscExpiryDate = LocalDate.parse(msicExpiry, formatter);

            DateTime expiryDateTime = new DateTime(miscExpiryDate.getYear(),miscExpiryDate.getMonthValue(),
                    miscExpiryDate.getDayOfMonth(),0,0);
            if(!expiryDateTime.isAfterNow()){
                employeeLicenseExpiredList.add("MSIC already Expired with date " + msicExpiry);
            }
            else if(!expiryDateTime.isAfter(new DateTime().plusWeeks(msicExpiryDateThreshold))){
                employeeLicenseAboutToExpireList.add("MSIC is about to expire with date " + msicExpiry);
            }
        }

    }

    private void handlefirstAid(Employee employee,List<String> employeeMissingDetailsList,List<String> employeeLicenseExpiredList,
                                        List<String> employeeLicenseAboutToExpireList){
        String firstAidExpiry = employee.getFirstAidExpiry();
        if(StringUtils.isEmpty(firstAidExpiry) || StringUtils.isEmpty(employee.getFirstAidExpiry())){
            employeeMissingDetailsList.add("No Document for First Aid "   );
        } else if(StringUtils.isEmpty(firstAidExpiry) || "NO EXPIRY".equalsIgnoreCase(firstAidExpiry)) {
            log.info("first aid expiry for " + employee.getFirstName() + " " + employee.getLastName() + " is empty or NO expiry ");
        }
        else{
            LocalDate firstAidExpiryDate = LocalDate.parse(firstAidExpiry, formatter);

            DateTime expiryDateTime = new DateTime(firstAidExpiryDate.getYear(),firstAidExpiryDate.getMonthValue(),
                    firstAidExpiryDate.getDayOfMonth(),0,0);
            if(!expiryDateTime.isAfterNow()){
                employeeLicenseExpiredList.add("First Aid already Expired with date " + firstAidExpiry);
            }
            else if(!expiryDateTime.isAfter(new DateTime().plusWeeks(firstAidExpiryDateThreshold))){
                employeeLicenseAboutToExpireList.add("First Aid is about to expire with date " + firstAidExpiry);
            }
        }
    }

    private void handlePA(Employee employee,List<String> employeeMissingDetailsList,List<String> employeeLicenseExpiredList,
                          List<String> employeeLicenseAboutToExpireList){
        String paNswInd = employee.getPaNswInd();
        if(StringUtils.isEmpty(paNswInd)){
            employeeMissingDetailsList.add("No Document for PA Ind ");
        } else if("NO EXPIRY".equalsIgnoreCase(paNswInd)) {
            log.info("port authority induction expiry for " + employee.getFirstName() + " " + employee.getLastName() + " is empty or NO expiry ");
        }
        else{
            LocalDate portAuthorityExpiryDate = LocalDate.parse(paNswInd, formatter);

            DateTime expiryDateTime = new DateTime(portAuthorityExpiryDate.getYear(),portAuthorityExpiryDate.getMonthValue(),
                    portAuthorityExpiryDate.getDayOfMonth(),0,0);
            if(!expiryDateTime.isAfterNow()){
                employeeLicenseExpiredList.add("Port Authority already Expired with date " + paNswInd);
            }
            else if(!expiryDateTime.isAfter(new DateTime().plusWeeks(paExpiryDateThreshold))){
                employeeLicenseAboutToExpireList.add("Port Authority is about to expire with date " + paNswInd);
            }
        }
    }

    private void handleSpotless(Employee employee,List<String> employeeMissingDetailsList,List<String> employeeLicenseExpiredList,
                                        List<String> employeeLicenseAboutToExpireList){
        String spotlessInd = employee.getSpotlessInd();
        if(StringUtils.isEmpty(spotlessInd)){
            employeeMissingDetailsList.add("No Document for Spotless Induction ");
        } else if("NO EXPIRY".equalsIgnoreCase(spotlessInd)
                || "NOEXPIRY".equalsIgnoreCase(spotlessInd)) {
            log.info("spotless induction expiry for " + employee.getFirstName() + " " + employee.getLastName() + " is NO expiry ");
        }
        else{
            LocalDate spotlessExpiryDate = LocalDate.parse(spotlessInd, formatter);

            DateTime expiryDateTime = new DateTime(spotlessExpiryDate.getYear(),spotlessExpiryDate.getMonthValue(),
                    spotlessExpiryDate.getDayOfMonth(),0,0);
            if(!expiryDateTime.isAfterNow()){
                employeeLicenseExpiredList.add("SPOTLESS already Expired with date " + spotlessInd);
            }
            else if(!expiryDateTime.isAfter(new DateTime().plusWeeks(spotlessExpiryDateThreshold))){
                employeeLicenseAboutToExpireList.add("SPOTLESS is about to expire with date " + spotlessInd);
            }
        }

    }


}
