package com.example.worktrack.service;

import com.example.worktrack.dto.AddEmployeeRequest;
import com.example.worktrack.dto.UpdateEmpRequest;
import com.example.worktrack.dto.UpdateEmpResponse;
import com.example.worktrack.model.Employee;

import java.util.List;

public interface EmployeeService {
    List<UpdateEmpResponse> getAllEmployee();
    void addEmployee(AddEmployeeRequest data);
    Employee updateEmployee(UpdateEmpRequest data);
    
}