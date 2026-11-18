package com.example.worktrack.service;

import com.example.worktrack.dto.EmployeeWorkScheEntyResponse;
import com.example.worktrack.dto.EmployeeWorkScheReq;
import com.example.worktrack.dto.EmployeeWorkScheResp;
import com.example.worktrack.dto.UpdateEmployeeWorkScheReq;
import com.example.worktrack.model.EmployeeWorkSchedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EmployeeWorkScheduleService {
    List<EmployeeWorkScheResp> aadEmployeeSchedule(EmployeeWorkScheReq req);
    EmployeeWorkScheResp updateEmployeeSchedule(UpdateEmployeeWorkScheReq req);
    Page<EmployeeWorkScheEntyResponse> getAllEmpWorkSchedule(Pageable pageable);

}
