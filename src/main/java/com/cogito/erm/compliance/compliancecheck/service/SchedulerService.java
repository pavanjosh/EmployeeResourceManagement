package com.cogito.erm.compliance.compliancecheck.service;

import com.cogito.erm.compliance.compliancecheck.model.Employee;
import com.cogito.erm.compliance.compliancecheck.model.LocationToMailMapper;
import com.cogito.erm.compliance.compliancecheck.repo.EmployeeRepo;
import com.cogito.erm.compliance.compliancecheck.repo.LocationToMailMapperRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
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


        boolean isMapEmpty = true;
        log.info("In Send an email for expiry..." );
        Map<String, List<String>> employeeMessageMap = new TreeMap<>();
        Iterable<LocationToMailMapper> all = locationToMailMapperRepo.findAll();
        for(LocationToMailMapper locationToMailMapper : all){

            employeeMessageMap.clear();
            isMapEmpty = true;
            List<Employee> allByLocation = employeeRepo.findAllByLocation(locationToMailMapper.getLocationName());
            for (Employee employee : allByLocation) {

                List<String> employeeDetailsList = new ArrayList<>();

                employeeDetailsList = handleSecurity(employee, employeeDetailsList);
                employeeDetailsList = handleMsic(employee,employeeDetailsList);
                employeeDetailsList = handlefirstAid(employee,employeeDetailsList);
                employeeDetailsList = handlePA(employee,employeeDetailsList);
                employeeDetailsList = handleSpotless(employee,employeeDetailsList);

                if(employeeDetailsList.size() >0){
                    isMapEmpty = false;
                    employeeMessageMap.put(employee.getFirstName()+","+employee.getLastName(),employeeDetailsList);
                }
            }
            if(!isMapEmpty){
                collabEmailService.sendEmail(employeeMessageMap,locationToMailMapper.getLocationName(),locationToMailMapper.getEmailAddress());
            }
        }


    }

    private List<String> handleSecurity(Employee employee,List<String> employeeDetailsList){
        String securityExpiry = employee.getSecurityExpiryDate();
        if(StringUtils.isEmpty(securityExpiry) || StringUtils.isEmpty(employee.getNswSecurity())){
            employeeDetailsList.add("No Document for NSW Security License for " + employee.getFirstName() + " " + employee.getLastName() + " " + " From location " + employee.getLocation() + " " );
        } else if("NO EXPIRY".equalsIgnoreCase(securityExpiry)) {
            log.info("security expiry for " + employee.getFirstName() + " " + employee.getLastName() + " is empty or NO expiry ");
        }
        else{
            LocalDate today = LocalDate.now();
            LocalDate securityExpiryDate = LocalDate.parse(securityExpiry, formatter).minusWeeks(securityExpiryDateThreshold);
            if (today.compareTo(securityExpiryDate) > 0) {
                log.info("Send an email for security expiry..." + employee.getFirstName() + " " + employee.getLastName());
                employeeDetailsList.add(getExpiryMessageForEmployee(employee,SECURITY_LICENSE_EXPIRY));
            }
        }
        return employeeDetailsList;
    }

    private List<String> handleMsic(Employee employee,List<String> employeeDetailsList){
        String msicExpiry = employee.getMsicExpiryDate();
        if(StringUtils.isEmpty(msicExpiry) || StringUtils.isEmpty(employee.getMsicExpiryDate())){
            employeeDetailsList.add("No Document for MSIC for " + employee.getFirstName() + " " + employee.getLastName() + " " + " From location " + employee.getLocation() + " " );
        } else if(StringUtils.isEmpty(msicExpiry) || "NO EXPIRY".equalsIgnoreCase(msicExpiry)) {
            log.info("MSIC expiry for " + employee.getFirstName() + " " + employee.getLastName() + " is empty or NO expiry ");
        }
        else{
            LocalDate today = LocalDate.now();
            LocalDate msicExpiryDate = LocalDate.parse(msicExpiry, formatter).minusWeeks(msicExpiryDateThreshold);
            if (today.compareTo(msicExpiryDate) > 0) {
                log.info("Send an email for MSIC expiry..." + employee.getFirstName() + " " + employee.getLastName());
                employeeDetailsList.add(getExpiryMessageForEmployee(employee,MSIC_EXPIRY));
            }
        }
        return employeeDetailsList;
    }

    private List<String> handlefirstAid(Employee employee,List<String> employeeDetailsList){
        String firstAidExpiry = employee.getFirstAidExpiry();
        if(StringUtils.isEmpty(firstAidExpiry) || StringUtils.isEmpty(employee.getFirstAidExpiry())){
            employeeDetailsList.add("No Document for First Aid for " + employee.getFirstName() + " " + employee.getLastName() + " " + " From location " + employee.getLocation() + " "  );
        } else if(StringUtils.isEmpty(firstAidExpiry) || "NO EXPIRY".equalsIgnoreCase(firstAidExpiry)) {
            log.info("first aid expiry for " + employee.getFirstName() + " " + employee.getLastName() + " is empty or NO expiry ");
        }
        else{
            LocalDate today = LocalDate.now();
            LocalDate firstAidExpiryDate = LocalDate.parse(firstAidExpiry, formatter).minusWeeks(firstAidExpiryDateThreshold);
            if (today.compareTo(firstAidExpiryDate) > 0) {
                log.info("Send an email for first Aid expiry..." + employee.getFirstName() + " " + employee.getLastName());
                employeeDetailsList.add(getExpiryMessageForEmployee(employee,FIRSTAID_EXPIRY));
            }
        }
        return employeeDetailsList;
    }

    private List<String> handlePA(Employee employee,List<String> employeeDetailsList){
        String paNswInd = employee.getPaNswInd();
        if(StringUtils.isEmpty(paNswInd)){
            employeeDetailsList.add("No Document for PA Ind for " + employee.getFirstName() + " " + employee.getLastName() + " " + " From location " + employee.getLocation() + " " );
        } else if("NO EXPIRY".equalsIgnoreCase(paNswInd)) {
            log.info("port authority induction expiry for " + employee.getFirstName() + " " + employee.getLastName() + " is empty or NO expiry ");
        }
        else{
            LocalDate today = LocalDate.now();
            LocalDate paNswIndExpiryDate = LocalDate.parse(paNswInd, formatter).minusWeeks(paExpiryDateThreshold);
            if (today.compareTo(paNswIndExpiryDate) > 0) {
                log.info("Send an email for PA induction expiry..." + employee.getFirstName() + " " + employee.getLastName());
                employeeDetailsList.add(getExpiryMessageForEmployee(employee,PAIND_EXPIRY));
            }
        }
        return employeeDetailsList;
    }

    private List<String> handleSpotless(Employee employee,List<String> employeeDetailsList){
        String spotlessInd = employee.getSpotlessInd();
        if(StringUtils.isEmpty(spotlessInd)){
            employeeDetailsList.add("No Document for Spotless Induction for " + employee.getFirstName() + " " + employee.getLastName() + " " + " From location " + employee.getLocation() + " ");
        } else if("NO EXPIRY".equalsIgnoreCase(spotlessInd)
                || "NOEXPIRY".equalsIgnoreCase(spotlessInd)) {
            log.info("spotless induction expiry for " + employee.getFirstName() + " " + employee.getLastName() + " is NO expiry ");
        }
        else{
            LocalDate today = LocalDate.now();
            LocalDate spotlessIndExpiryDate = LocalDate.parse(spotlessInd, formatter).minusWeeks(spotlessExpiryDateThreshold);
            if (today.compareTo(spotlessIndExpiryDate) > 0) {
                log.info("Send an email for spotless expiry..." + employee.getFirstName() + " " + employee.getLastName());
                employeeDetailsList.add(getExpiryMessageForEmployee(employee,SPOTLESS_EXPIRY));
            }
        }
        return employeeDetailsList;
    }
    private String getExpiryMessageForEmployee(Employee employee, String expiryCategory){
        if(SECURITY_LICENSE_EXPIRY.equalsIgnoreCase(expiryCategory)) {
            return "Security License About To Expire Or Expired From location " + employee.getLocation() + " " + employee.getSecurityExpiryDate() ;
        }
        if(MSIC_EXPIRY.equalsIgnoreCase(expiryCategory)) {
            return "MSIC License About To Expire Or Expired For From location " + employee.getLocation() + " " + employee.getMsicExpiryDate() ;
        }
        if(FIRSTAID_EXPIRY.equalsIgnoreCase(expiryCategory)) {
            return "First Aid About To Expire Or Expired From location " + employee.getLocation() + " " +  employee.getFirstAidExpiry() ;
        }
        if(PAIND_EXPIRY.equalsIgnoreCase(expiryCategory)) {
            return "Port Authority Induction About To Expire Or Expired From location " + employee.getLocation() + " " +  employee.getPaNswInd() ;
        }
        if(SPOTLESS_EXPIRY.equalsIgnoreCase(expiryCategory)) {
            return "Spotless About To Expire Or Expired From location " + employee.getLocation() + " " +  employee.getSpotlessInd() ;
        }
        return null;
    }

}
