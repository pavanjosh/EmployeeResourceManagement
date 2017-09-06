package com.cogito.erm.compliance.compliancecheck.facade;

import com.cogito.erm.compliance.compliancecheck.model.MultiDataHolder;
import com.cogito.erm.compliance.compliancecheck.service.DBServiceIF;
import com.cogito.erm.compliance.compliancecheck.service.ExcelFileReader;
import com.cogito.erm.compliance.compliancecheck.service.FileReaderIF;
import com.cogito.erm.compliance.compliancecheck.service.SchedulerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * Created by pavankumarjoshi on 6/09/2017.
 */
@Service
public class WorkFlowFacade implements WorkFlowFacadeIF{


    private static final Logger Log = LoggerFactory.getLogger(WorkFlowFacade.class);

    @Autowired
    private FileReaderIF fileReader;

    @Autowired
    private DBServiceIF dbService;

    @Autowired
    SchedulerService schedulerService;

    @Override
    public void executeWorkFlow(String path) throws Exception {

        try {
            MultiDataHolder multiDataHolder = fileReader.read(path);
            dbService.update(multiDataHolder);
            schedulerService.scanForDates();

        }
        catch (Exception ex){
            ex.printStackTrace();
            Log.error(ex.getMessage());
            throw ex;
        }

    }
}
