package com.example.worktrack.service.impl;

import com.example.worktrack.dto.EmployeeWorkScheEntyResponse;
import com.example.worktrack.dto.EmployeeWorkScheReq;
import com.example.worktrack.dto.EmployeeWorkScheResp;
import com.example.worktrack.dto.UpdateEmployeeWorkScheReq;
import com.example.worktrack.model.Employee;
import com.example.worktrack.model.EmployeeWorkSchedule;
import com.example.worktrack.model.Position;
import com.example.worktrack.model.WorkSchedule;
import com.example.worktrack.repository.EmployeeRepository;
import com.example.worktrack.repository.EmployeeWorkScheduleRepository;
import com.example.worktrack.repository.PositionRepository;
import com.example.worktrack.repository.WorkScheduleRepository;
import com.example.worktrack.service.EmployeeWorkScheduleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class EmployeeWorkScheduleServiceImpl implements EmployeeWorkScheduleService {

    private final EmployeeWorkScheduleRepository employeeWorkScheduleRepository;
    private final PositionRepository positionRepository;
    private final EmployeeRepository employeeRepository;
    private final WorkScheduleRepository workScheduleRepository;

    EmployeeWorkScheduleServiceImpl(EmployeeWorkScheduleRepository employeeWorkScheduleRepository, PositionRepository positionRepository,
                                    EmployeeRepository employeeRepository, WorkScheduleRepository workScheduleRepository){
        this.employeeWorkScheduleRepository=employeeWorkScheduleRepository;
        this.positionRepository=positionRepository;
        this.employeeRepository=employeeRepository;
        this.workScheduleRepository=workScheduleRepository;
    }

    @Override
    public List<EmployeeWorkScheResp> aadEmployeeSchedule(EmployeeWorkScheReq req){
        Position position = positionRepository.findByPositionName(req.getPositionName())
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Position is not available"));

        List<Employee> employeeList = employeeRepository.findByPosition_PositionName(position.getPositionName());
        if(employeeList.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Employee not found");
        }

        List<EmployeeWorkScheResp> response = new ArrayList<>();
        for(var item : req.getEmployeeWorks()){

            Employee employee = employeeList.stream()
                    .filter(emp -> emp.getId() == item.getEmployeeId())
                    .findFirst()
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Employee with ID"+ item.getEmployeeId() + " does not belong to position " + req.getPositionName()));

            boolean cekSchedule = employeeWorkScheduleRepository.findByWorkDateAndEmployee(item.getWorkDate(), employee).isPresent();
            if(cekSchedule){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Schedule for employee " + employee.getName() + " on date " + item.getWorkDate() + " already exists.");
            }


            String day = item.getWorkDate().getDayOfWeek()
                    .getDisplayName(TextStyle.FULL, Locale.ENGLISH);
            WorkSchedule workSchedule = workScheduleRepository.findByNamePositionAndDay(req.getPositionName(), day)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Schedule of this day not found"));
            if(!workSchedule.isWorkingDay() ){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot create schedule, because on "+ day + " the position "+ req.getPositionName() + " is an Off Day.");
            }

            EmployeeWorkSchedule schedule = EmployeeWorkSchedule.builder()
                    .employee(employee)
                    .workDate(item.getWorkDate())
                    .startTime(item.getStartTime())
                    .endTime(item.getEndTime())
                    .scheduleType(item.getScheduleType())
                    .workingDay(workSchedule.isWorkingDay())
                    .build();
            EmployeeWorkSchedule employeeWorkSchedule = employeeWorkScheduleRepository.save(schedule);

            EmployeeWorkScheResp resp = EmployeeWorkScheResp.builder()
                    .positionName(position.getPositionName())
                    .id(employeeWorkSchedule.getId())
                    .employeeName(employee.getName())
                    .workDate(employeeWorkSchedule.getWorkDate())
                    .startTime(employeeWorkSchedule.getStartTime())
                    .endTime(employeeWorkSchedule.getEndTime())
                    .scheduleType(employeeWorkSchedule.getScheduleType())
                    .workingDay(employeeWorkSchedule.isWorkingDay())
                    .build();
            response.add(resp);
        }
        return response;
    }

    @Override
    public EmployeeWorkScheResp updateEmployeeSchedule(UpdateEmployeeWorkScheReq req){
        EmployeeWorkSchedule schedule = employeeWorkScheduleRepository.findById(req.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Employee Work Schedule is not available"));

        Position position = positionRepository.findByPositionName(req.getPositionName())
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Position is not available"));

        List<Employee> employeeList = employeeRepository.findByPosition_PositionName(position.getPositionName());
        if(employeeList.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Employee not found");
        }

        Employee employee = employeeList.stream()
                .filter(emp -> emp.getId() == req.getEmployeeId())
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Employee with ID"+ req.getEmployeeId() + " does not belong to position " + req.getPositionName()));

        if(req.getWorkDate() != null){
                schedule.setWorkDate(req.getWorkDate());
        }

        schedule.setWorkingDay(req.isWorkingDay());
        if(req.isWorkingDay()){
            if(req.getStartTime() != null){
                schedule.setStartTime(req.getStartTime());
            }
            if(req.getEndTime() != null){
                schedule.setEndTime(req.getEndTime());
            }
            if(req.getScheduleType() != null && !req.getScheduleType().isBlank()){
                schedule.setScheduleType(req.getScheduleType());
            }
        }else {
            schedule.setStartTime(null);
            schedule.setEndTime(null);
            schedule.setScheduleType(req.getScheduleType() != null ? req.getScheduleType() : "LIBURR");
        }

        EmployeeWorkSchedule savedSchedule = employeeWorkScheduleRepository.save(schedule);

        EmployeeWorkScheResp response = EmployeeWorkScheResp.builder()
                .positionName(position.getPositionName())
                .id(savedSchedule.getId())
                .employeeName(employee.getName())
                .scheduleType(savedSchedule.getScheduleType())
                .workDate(savedSchedule.getWorkDate())
                .startTime(savedSchedule.getStartTime())
                .endTime(savedSchedule.getEndTime())
                .workingDay(savedSchedule.isWorkingDay())
                .build();

        return response;
    }

    @Override
    public Page<EmployeeWorkScheEntyResponse> getAllEmpWorkSchedule(Pageable pageable){
        Page<EmployeeWorkSchedule> schedules = employeeWorkScheduleRepository.findAll(pageable);

        return schedules.map(tamp -> {
            EmployeeWorkScheEntyResponse response = new EmployeeWorkScheResp();
            response.setId(tamp.getId());
            response.setWorkDate(tamp.getWorkDate());
            response.setScheduleType(tamp.getScheduleType());
            response.setEmployeeName(tamp.getEmployee().getName());
            response.setStartTime(tamp.getStartTime());
            response.setEndTime(tamp.getEndTime());
            response.setWorkingDay(tamp.isWorkingDay());
            return response;
        });
    }
}
