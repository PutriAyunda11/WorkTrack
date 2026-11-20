package com.example.worktrack.service;

import com.example.worktrack.dto.AddEmployeeRequest;
import com.example.worktrack.dto.UpdateEmpRequest;
import com.example.worktrack.dto.EmpResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EmployeeService {
    Page<EmpResponse> getAllEmployee(Pageable pageable);
    EmpResponse addEmployee(AddEmployeeRequest data);
    EmpResponse updateEmployee(UpdateEmpRequest data);
    void deletedEmployee(Integer employeeId);
    
}