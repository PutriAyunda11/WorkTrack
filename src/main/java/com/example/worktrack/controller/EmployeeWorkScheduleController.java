package com.example.worktrack.controller;

import com.example.worktrack.dto.*;
import com.example.worktrack.service.EmployeeWorkScheduleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/employee-work-schedule")
public class EmployeeWorkScheduleController {

    private final EmployeeWorkScheduleService employeeWorkScheduleService;
    EmployeeWorkScheduleController(EmployeeWorkScheduleService employeeWorkScheduleService){
        this.employeeWorkScheduleService=employeeWorkScheduleService;
    }

    @PostMapping("/add-schedule")
    public ResponseEntity<GenericResponse<List<EmployeeWorkScheResp>>> addEmployeeSchedule(@RequestBody EmployeeWorkScheReq req){
        try {
            List<EmployeeWorkScheResp> result = employeeWorkScheduleService.aadEmployeeSchedule(req);
            return ResponseEntity.ok().body( GenericResponse.<List<EmployeeWorkScheResp>>builder()
                    .success(true)
                    .message("Successfully")
                    .data(result)
                    .build());
        }catch(ResponseStatusException rse){
            return ResponseEntity.status(rse.getStatusCode()).body(GenericResponse.<List<EmployeeWorkScheResp>>builder()
                    .success(false)
                    .message(rse.getReason())
                    .data(null)
                    .build());

        }catch (Exception e) {
            return ResponseEntity.internalServerError().body(GenericResponse.<List<EmployeeWorkScheResp>>builder()
                    .success(false)
                    .message("internal server error")
                    .data(null)
                    .build());

        }
    }

    @PostMapping("/update-schedule")
    public ResponseEntity<GenericResponse<EmployeeWorkScheResp>> updateEmployeeSchedule(@RequestBody UpdateEmployeeWorkScheReq req){
        try {
            EmployeeWorkScheResp result = employeeWorkScheduleService.updateEmployeeSchedule(req);
            return ResponseEntity.ok().body( GenericResponse.<EmployeeWorkScheResp>builder()
                    .success(true)
                    .message("Successfully")
                    .data(result)
                    .build());
        }catch(ResponseStatusException rse){
            return ResponseEntity.status(rse.getStatusCode()).body(GenericResponse.<EmployeeWorkScheResp>builder()
                    .success(false)
                    .message(rse.getReason())
                    .data(null)
                    .build());

        }catch (Exception e) {
            return ResponseEntity.internalServerError().body(GenericResponse.<EmployeeWorkScheResp>builder()
                    .success(false)
                    .message("internal server error")
                    .data(null)
                    .build());

        }
    }

    @GetMapping("/all-schedule")
    public ResponseEntity<GenericResponse<Page<EmployeeWorkScheEntyResponse>>> getAllEmployeeSchedule(Pageable pageable){
        Page<EmployeeWorkScheEntyResponse> result = employeeWorkScheduleService.getAllEmpWorkSchedule(pageable);

        return ResponseEntity.ok().body(GenericResponse.<Page<EmployeeWorkScheEntyResponse>>builder()
                        .success(true)
                        .message("Successfully")
                        .data(result)
                .build());
    }
}
