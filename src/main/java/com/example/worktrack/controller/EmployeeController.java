package com.example.worktrack.controller;

import com.example.worktrack.dto.UpdateEmpRequest;
import com.example.worktrack.dto.EmpResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public ResponseEntity<GenericResponse<EmpResponse>> add(@Valid @RequestBody AddEmployeeRequest data){
        try {
            EmpResponse result = emp.addEmployee(data);
            return ResponseEntity.ok().body(GenericResponse.<EmpResponse>builder()
                    .success(true)
                    .message("Successfully")
                    .data(result)
                    .build());
            
        }catch(ResponseStatusException rse){
            return ResponseEntity.status(rse.getStatusCode()).body(GenericResponse.<EmpResponse>builder()
                    .success(false)
                    .message(rse.getReason())
                    .data(null)
                    .build());
                    
        }catch (Exception e) {
            return ResponseEntity.internalServerError().body(GenericResponse.<EmpResponse>builder()
                    .success(false)
                    .message("internal server error")
                    .data(null)
                    .build());

        }
    }

    @PostMapping("/update-employee")
    public ResponseEntity<GenericResponse<EmpResponse>> updateEmp(@RequestBody UpdateEmpRequest data){
        try {
            EmpResponse dataEmp = emp.updateEmployee(data);
            return ResponseEntity.ok().body(GenericResponse.<EmpResponse>builder()
                    .success(true)
                    .message("Successfully")
                    .data(dataEmp)
                    .build());
        }catch (ResponseStatusException re){
            return ResponseEntity.status(re.getStatusCode()).body(GenericResponse.<EmpResponse>builder()
                    .success(false)
                    .message(re.getReason())
                    .data(null)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(GenericResponse.<EmpResponse>builder()
                    .success(false)
                    .message("internal server error")
                    .data(null)
                    .build());
        }
    }

    @GetMapping("/all-employee")
    public ResponseEntity<GenericResponse<Page<EmpResponse>>> getAllEmployee( Pageable pageable){
        Page<EmpResponse> data =emp.getAllEmployee(pageable);
        return ResponseEntity.ok().body(GenericResponse.<Page<EmpResponse>>builder()
                .success(true)
                .message("successfully")
                .data(data)
                .build());
    }

    @PostMapping("/soft-delete")
    public ResponseEntity<GenericResponse<Void>> softDeleted(@RequestParam Integer employeeId){

        emp.deletedEmployee(employeeId);
        return ResponseEntity.ok().body(GenericResponse.<Void>builder()
                .success(true)
                .message("Successfully")
                .data(null)
                .build());
    }
    
}
