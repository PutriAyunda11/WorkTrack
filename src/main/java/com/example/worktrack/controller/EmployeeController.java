package com.example.worktrack.controller;

import com.example.worktrack.dto.UpdateEmpRequest;
import com.example.worktrack.dto.UpdateEmpResponse;
import com.example.worktrack.model.Employee;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.example.worktrack.dto.AddEmployeeRequest;
import com.example.worktrack.dto.GenericResponse;
import com.example.worktrack.service.EmployeeService;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    private final EmployeeService emp;

    EmployeeController(EmployeeService emp){
        this.emp=emp;
    }

    @PostMapping("/add")
    public ResponseEntity<GenericResponse<Object>> add(@Valid @RequestBody AddEmployeeRequest data){
        try {
            emp.addEmployee(data);
            return ResponseEntity.ok().body(GenericResponse.builder()
                    .success(true)
                    .message("Successfully")
                    .data(null)
                    .build());
            
        }catch(ResponseStatusException rse){
            return ResponseEntity.status(rse.getStatusCode()).body(GenericResponse.builder()
                    .success(false)
                    .message(rse.getReason())
                    .data(null)
                    .build());
                    
        }catch (Exception e) {
            return ResponseEntity.internalServerError().body(GenericResponse.builder()
                    .success(false)
                    .message("internal server error")
                    .data(null)
                    .build());

        }
    }

    @PostMapping("/update-employee")
    public ResponseEntity<GenericResponse<Employee>> updateEmp(@RequestBody UpdateEmpRequest data){
        try {
            Employee dataEmp = emp.updateEmployee(data);
            return ResponseEntity.ok().body(GenericResponse.<Employee>builder()
                    .success(true)
                    .message("Successfully")
                    .data(dataEmp)
                    .build());
        }catch (ResponseStatusException re){
            return ResponseEntity.status(re.getStatusCode()).body(GenericResponse.<Employee>builder()
                    .success(false)
                    .message(re.getMessage())
                    .data(null)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(GenericResponse.<Employee>builder()
                    .success(false)
                    .message("internal server error")
                    .data(null)
                    .build());
        }
    }

    @GetMapping("all-employee")
    public ResponseEntity<GenericResponse<List<UpdateEmpResponse>>> getAllEmployee(){
        List<UpdateEmpResponse> data =emp.getAllEmployee();
        return ResponseEntity.ok().body(GenericResponse.<List<UpdateEmpResponse>>builder()
                .success(true)
                .message("successfully")
                .data(data)
                .build());
    }
    
}
