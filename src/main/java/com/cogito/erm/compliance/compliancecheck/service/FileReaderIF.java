package com.cogito.erm.compliance.compliancecheck.service;

import com.cogito.erm.compliance.compliancecheck.model.MultiDataHolder;

/**
 * Created by pavankumarjoshi on 25/07/2017.
 */
public interface FileReaderIF {

    public MultiDataHolder read(String path) throws Exception;
}
