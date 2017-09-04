package com.cogito.erm.compliance.compliancecheck.repo;

import com.cogito.erm.compliance.compliancecheck.model.LocationToMailMapper;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by pavankumarjoshi on 23/08/2017.
 */
public interface LocationToMailMapperRepo extends PagingAndSortingRepository<LocationToMailMapper,String> {

    LocationToMailMapper findByLocationName(String locationName);
}
