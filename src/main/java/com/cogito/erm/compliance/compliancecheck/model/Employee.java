package com.cogito.erm.compliance.compliancecheck.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@IdClass(EmployeeKey.class)
@Table(name = "employee")
public class Employee implements Serializable{

  @Id
  private String name;
//  @Id
//  private String lastName;

  private String nswSecurity;
  private String securityExpiryDate;
  private String securityClass;
  private String msicNo;
  private String msicExpiryDate;
  private String firstAidExpiry;
  private String rsa;
  private String rsa_expiry;
  private String paNswInd;
  private String spotlessInd;
  private String trafficControl;
  private String tcExpiry;
  private String phoneNumber;
  private String emailId;
  private String pfso;
  private String welcomeSiteInduction;
  private String location;

  public String getTrafficControl() {
    return trafficControl;
  }

  public void setTrafficControl(String trafficControl) {
    this.trafficControl = trafficControl;
  }

  public String getTcExpiry() {
    return tcExpiry;
  }

  public void setTcExpiry(String tcExpiry) {
    this.tcExpiry = tcExpiry;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }
  //  private String desc;
//
//  public String getDesc() {
//    return desc;
//  }
//
//  public void setDesc(String desc) {
//    this.desc = desc;
//  }

  public String getRsa_expiry() {
    return rsa_expiry;
  }

  public void setRsa_expiry(String rsa_expiry) {
    this.rsa_expiry = rsa_expiry;
  }

  public String getWelcomeSiteInduction() {
    return welcomeSiteInduction;
  }

  public void setWelcomeSiteInduction(String welcomeSiteInduction) {
    this.welcomeSiteInduction = welcomeSiteInduction;
  }

  public String getPfso() {
    return pfso;
  }

  public void setPfso(String pfso) {
    this.pfso = pfso;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getNswSecurity() {
    return nswSecurity;
  }

  public void setNswSecurity(String nswSecurity) {
    this.nswSecurity = nswSecurity;
  }

  public String getSecurityExpiryDate() {
    return securityExpiryDate;
  }

  public void setSecurityExpiryDate(String securityExpiryDate) {
    this.securityExpiryDate = securityExpiryDate;
  }

  public String getSecurityClass() {
    return securityClass;
  }

  public void setSecurityClass(String securityClass) {
    this.securityClass = securityClass;
  }

  public String getMsicNo() {
    return msicNo;
  }

  public void setMsicNo(String msicNo) {
    this.msicNo = msicNo;
  }

  public String getMsicExpiryDate() {
    return msicExpiryDate;
  }

  public void setMsicExpiryDate(String msicExpiryDate) {
    this.msicExpiryDate = msicExpiryDate;
  }

  public String getFirstAidExpiry() {
    return firstAidExpiry;
  }

  public void setFirstAidExpiry(String firstAidExpiry) {
    this.firstAidExpiry = firstAidExpiry;
  }

  public String getRsa() {
    return rsa;
  }

  public void setRsa(String rsa) {
    this.rsa = rsa;
  }

  public String getPaNswInd() {
    return paNswInd;
  }

  public void setPaNswInd(String paNswInd) {
    this.paNswInd = paNswInd;
  }

  public String getSpotlessInd() {
    return spotlessInd;
  }

  public void setSpotlessInd(String spotlessInd) {
    this.spotlessInd = spotlessInd;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getEmailId() {
    return emailId;
  }

  public void setEmailId(String emailId) {
    this.emailId = emailId;
  }

  @Override public String toString() {
    return "Employee{" +
        "name='" + name + '\'' +
        ", nswSecurity='" + nswSecurity + '\'' +
        ", securityExpiryDate='" + securityExpiryDate + '\'' +
        ", employeeClass='" + securityClass + '\'' +
        ", miscNo='" + msicNo + '\'' +
        ", miscExpiryDate='" + msicExpiryDate + '\'' +
        ", firstAidExpiry='" + firstAidExpiry + '\'' +
        ", rsa='" + rsa + '\'' +
        ", paNswInd='" + paNswInd + '\'' +
        ", spotlessInd='" + spotlessInd + '\'' +
        ", phoneNumber='" + phoneNumber + '\'' +
        ", emailId='" + emailId + '\'' +
        '}';
  }
}
