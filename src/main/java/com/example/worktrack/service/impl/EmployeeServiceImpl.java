package com.example.worktrack.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.worktrack.constant.ConstantValue;
import com.example.worktrack.dto.UpdateEmpRequest;
import com.example.worktrack.dto.UpdateEmpResponse;
import com.example.worktrack.enums.DayEnum;
import com.example.worktrack.enums.PositionEnum;
import com.example.worktrack.enums.Status;
import com.example.worktrack.model.WorkSchedule;
import com.example.worktrack.repository.WorkScheduleRepository;
import com.example.worktrack.service.EmployeeService;
import org.springframework.http.HttpStatus;
// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.worktrack.dto.AddEmployeeRequest;
import com.example.worktrack.model.Employee;
import com.example.worktrack.model.Position;
import com.example.worktrack.repository.EmployeeRepository;
import com.example.worktrack.repository.PositionRepository;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    
    private final EmployeeRepository employeeRepository;
    private final PositionRepository positionRepository;
    private final WorkScheduleRepository workScheduleRepository;

    // @Autowired
    EmployeeServiceImpl(EmployeeRepository employeeRepository, PositionRepository positionRepository, WorkScheduleRepository workScheduleRepository){
        this.employeeRepository=employeeRepository;
        this.positionRepository=positionRepository;
        this.workScheduleRepository=workScheduleRepository;
    }

    @Override
    public void addEmployee(AddEmployeeRequest data){

        String email = data.getEmail();

        if( data.getName().isBlank() || data.getAddress().isBlank()
            || data.getEmail().isBlank() || data.getNoTelp().isBlank()
            || data.getPosition().isBlank()){
            throw new ResponseStatusException( HttpStatus.BAD_REQUEST,"Data cannot be empty");
        }

        if(!email.matches(ConstantValue.EmailRegex)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid email");
        }
        employeeRepository.findByEmail(email)
                            .ifPresent(
                                e ->{
                                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Duplicate Email");
                            });
        
        if(!data.getNoTelp().matches(ConstantValue.PhoneRegex)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid no Telp");
        }

        System.out.println("POSITION : "+data.getPosition());
        Position position = positionRepository.findByPositionName(data.getPosition())
                            .orElseThrow(
                                ()-> new ResponseStatusException( HttpStatus.NOT_FOUND,"Data position not found")
                            );

        boolean isWorkingDay = false;
        if(data.getPosition() != null && !data.getPosition().isBlank()){
            if(data.getPosition().equalsIgnoreCase(PositionEnum.ADMIN_HRD.getValue()) || data.getPosition().equalsIgnoreCase(PositionEnum.HR_SUPERVISOR.getValue())
                || data.getPosition().equalsIgnoreCase(PositionEnum.PPIC.getValue()) || data.getPosition().equalsIgnoreCase(PositionEnum.ADMIN_GUDANG.getValue())){
                isWorkingDay = false;
            }else {
                isWorkingDay = true;
            }
        }

        WorkSchedule mon = WorkSchedule.builder()
                .day(DayEnum.MONDAY.getValue())
                .workingDay(true)
                .namePosition(data.getPosition())
                .build();
        WorkSchedule tues = WorkSchedule.builder()
                .day(DayEnum.TUESDAY.getValue())
                .workingDay(true)
                .namePosition(data.getPosition())
                .build();
        WorkSchedule wed = WorkSchedule.builder()
                .day(DayEnum.WEDNESDAY.getValue())
                .workingDay(true)
                .namePosition(data.getPosition())
                .build();
        WorkSchedule thurs = WorkSchedule.builder()
                .day(DayEnum.THURSDAY.getValue())
                .workingDay(true)
                .namePosition(data.getPosition())
                .build();
        WorkSchedule fri = WorkSchedule.builder()
                .day(DayEnum.FRIDAY.getValue())
                .workingDay(true)
                .namePosition(data.getPosition())
                .build();
        WorkSchedule satur = WorkSchedule.builder()
                .day(DayEnum.SATURDAY.getValue())
                .workingDay(isWorkingDay)
                .namePosition(data.getPosition())
                .build();
        WorkSchedule sun = WorkSchedule.builder()
                .day(DayEnum.SUNDAY.getValue())
                .workingDay(isWorkingDay)
                .namePosition(data.getPosition())
                .build();
        workScheduleRepository.saveAll(List.of(mon, tues, wed, thurs, fri, satur, sun));

        Employee emp = new Employee();
        emp.setName(data.getName());
        emp.setEmail(email);
        emp.setNoTelp(data.getNoTelp());
        emp.setAddress(data.getAddress());
        emp.setStatus(Status.ACTIVE.getValue());
        emp.setJoiningDate(LocalDate.now());
        emp.setMonthlySalary(data.getMonthlySalary());
        emp.setPosition(position);
        employeeRepository.save(emp);
    }

    @Override
    public Employee updateEmployee(UpdateEmpRequest data){
        Employee emp = employeeRepository.findById(data.getId())
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Employee is not Available"));

        if(data.getName() != null && !data.getName().isBlank()){
            if(emp.getName() == null || emp.getName().isBlank()|| !emp.getName().equalsIgnoreCase(data.getName())){
                emp.setName(data.getName());
            }
        }


        if(data.getEmail()!= null && !data.getEmail().isBlank()){
            if(data.getEmail().matches(ConstantValue.EmailRegex)){
                if(emp.getEmail() == null || !emp.getEmail().equalsIgnoreCase(data.getEmail())){
                    emp.setEmail(data.getEmail());
                }
            }else if(!data.getEmail().matches(ConstantValue.EmailRegex)){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Input Email Invalid");
            }

        }

        if(data.getAddress() != null  && !data.getAddress().isBlank()){
            if(emp.getAddress() == null || !emp.getAddress().equalsIgnoreCase(data.getAddress())){
                emp.setAddress(data.getAddress());
            }
        }

        if(data.getNoTelp() != null  && !data.getNoTelp().isBlank()){
            if(data.getNoTelp().matches(ConstantValue.PhoneRegex)){
                if(emp.getNoTelp() == null || !emp.getNoTelp().equalsIgnoreCase(data.getNoTelp())){
                    emp.setNoTelp(data.getNoTelp());
                }
            }else if(!data.getNoTelp().matches(ConstantValue.PhoneRegex)){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid No Telp");
            }
        }

        if(data.getPosition() != null && !data.getPosition().isBlank()){
            if(emp.getPosition() == null || !emp.getPosition().getPositionName().equalsIgnoreCase(data.getPosition())){
                Position position = positionRepository.findByPositionName(data.getPosition())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Position not found"));
                emp.setPosition(position);
            }
        }

        if(data.getMonthlySalary() != null  && data.getMonthlySalary() > 0){
            if(emp.getMonthlySalary() == null || emp.getMonthlySalary() == 0){
                emp.setMonthlySalary(data.getMonthlySalary());
            }
        }

        emp.setUpdateAt(LocalDateTime.now());
        employeeRepository.save(emp);

        Employee get = employeeRepository.findById(emp.getId())
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "DATA NOT FOUND"));

        return get;
    }

    @Override
    public List<UpdateEmpResponse> getAllEmployee(){
        List<Employee> emp =employeeRepository.findAll();
        List<UpdateEmpResponse> data = new ArrayList<>();
        for(Employee tamp : emp){
            UpdateEmpResponse dataTamp= new UpdateEmpResponse();
            dataTamp.setId(tamp.getId());
            dataTamp.setName(tamp.getName());
            dataTamp.setEmail(tamp.getEmail());
            dataTamp.setAddress(tamp.getAddress());
            dataTamp.setNoTelp(tamp.getNoTelp());
            dataTamp.setStatus(tamp.getStatus());
            dataTamp.setJoiningDate(tamp.getJoiningDate());
            dataTamp.setPosition(tamp.getPosition().getPositionName());
            data.add(dataTamp);
        }
        return data;
    }
}
