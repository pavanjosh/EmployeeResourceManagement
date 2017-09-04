package com.cogito.erm.compliance.compliancecheck.model;

import org.hibernate.annotations.Table;
import org.hibernate.tuple.entity.EntityMetamodel;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by pavankumarjoshi on 23/08/2017.
 */

@Entity
public class LocationToMailMapper implements Serializable {

    private static final long serialVersionUID = -6847077919236772990L;

    @Id
    String locationName;

    String emailAddress;

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}
