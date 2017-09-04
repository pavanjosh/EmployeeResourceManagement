package com.cogito.erm.compliance.compliancecheck.repo;

import com.cogito.erm.compliance.compliancecheck.model.Employee;
import com.cogito.erm.compliance.compliancecheck.model.EmployeeKey;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface EmployeeRepo extends CrudRepository<Employee, String> {
    List<Employee> findAllByLocation(String location);
}
