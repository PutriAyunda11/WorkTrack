package com.example.worktrack.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.worktrack.constant.ConstantValue;
import com.example.worktrack.dto.UpdateEmpRequest;
import com.example.worktrack.dto.EmpResponse;
import com.example.worktrack.enums.Status;
import com.example.worktrack.model.EmployeeWorkSchedule;
import com.example.worktrack.repository.EmployeeWorkScheduleRepository;
import com.example.worktrack.service.EmployeeService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    private final EmployeeWorkScheduleRepository employeeWorkScheduleRepository;

    // @Autowired
    EmployeeServiceImpl(EmployeeRepository employeeRepository, PositionRepository positionRepository,
                        EmployeeWorkScheduleRepository employeeWorkScheduleRepository){
        this.employeeRepository=employeeRepository;
        this.positionRepository=positionRepository;
        this.employeeWorkScheduleRepository=employeeWorkScheduleRepository;
    }

    @Override
    public EmpResponse addEmployee(AddEmployeeRequest data){

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


        Employee emp = new Employee();
        LocalDate join = LocalDate.now();
        if(data.getEmploymentStatus().equalsIgnoreCase(Status.PKWT.getValue()) && (data.getContractPeriod() == null || data.getContractPeriod() == 0)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Contract period is required and must be greater than 0 for PKWT employees.");
        } else if (data.getEmploymentStatus().equalsIgnoreCase(Status.PKWT.getValue()) && (data.getContractPeriod() != null || data.getContractPeriod() != 0)) {
            LocalDate termination = join.plusMonths(data.getContractPeriod());

            emp.setEmploymentStatus(Status.PKWT.getValue());
            emp.setTerminationDate(termination);

        } else if (data.getEmploymentStatus().equalsIgnoreCase(Status.PKWTT.getValue())) {
            emp.setTerminationDate(null);
            emp.setEmploymentStatus(Status.PKWTT.getValue());
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid employment status. Allowed values are PKWT or PKWTT.");
        }
        emp.setName(data.getName());
        emp.setEmail(email);
        emp.setNoTelp(data.getNoTelp());
        emp.setAddress(data.getAddress());
        emp.setStatus(Status.ACTIVE.getValue());
        emp.setJoiningDate(join);
        emp.setMonthlySalary(data.getMonthlySalary());
        emp.setPosition(position);

        Employee savedEmp = employeeRepository.save(emp);

        EmpResponse response = EmpResponse.builder()
                .id(savedEmp.getId())
                .name(savedEmp.getName())
                .address(savedEmp.getAddress())
                .email(savedEmp.getEmail())
                .noTelp(savedEmp.getNoTelp())
                .joiningDate(savedEmp.getJoiningDate())
                .EmploymentStatus(savedEmp.getEmploymentStatus())
                .terminatorDate(savedEmp.getTerminationDate())
                .status(savedEmp.getStatus())
                .monthlySalary(savedEmp.getMonthlySalary())
                .position(savedEmp.getPosition().getPositionName())
                .build();

        return response;
    }

    @Override
    public EmpResponse updateEmployee(UpdateEmpRequest data){
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
                emp.setMonthlySalary(data.getMonthlySalary());
        }

        String status = data.getEmploymentStatus();
        Integer contractPeriod = data.getContractPeriod();
        if (status != null && !status.isBlank()) {
            if (Status.PKWT.getValue().equalsIgnoreCase(status)) {
                if (contractPeriod == null || contractPeriod <= 0) {
                    throw new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "Contract period is required and must be greater than 0 for PKWT employees."
                    );
                }
                LocalDate baseDate = emp.getJoiningDate() != null ? emp.getJoiningDate() : LocalDate.now();
                emp.setEmploymentStatus(Status.PKWT.getValue());
                emp.setTerminationDate(baseDate.plusMonths(contractPeriod));

            } else if (Status.PKWTT.getValue().equalsIgnoreCase(status)) {
                emp.setEmploymentStatus(Status.PKWTT.getValue());
                emp.setTerminationDate(null);

            } else {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Invalid employment status. Allowed values are PKWT or PKWTT."
                );
            }
        } else if (contractPeriod != null) {
            if (contractPeriod <= 0) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Contract period must be greater than 0."
                );
            }
            if (!Status.PKWT.getValue().equalsIgnoreCase(emp.getEmploymentStatus())) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Contract period can only be updated for PKWT employees."
                );
            }
            LocalDate baseDate = emp.getJoiningDate() != null ? emp.getJoiningDate() : LocalDate.now();
            emp.setTerminationDate(baseDate.plusMonths(contractPeriod));
        }


        emp.setUpdateAt(LocalDateTime.now());
        Employee savedEmp = employeeRepository.save(emp);

        EmpResponse response = EmpResponse.builder()
                .id(savedEmp.getId())
                .name(savedEmp.getName())
                .address(savedEmp.getAddress())
                .noTelp(savedEmp.getNoTelp())
                .email(savedEmp.getEmail())
                .status(savedEmp.getStatus())
                .monthlySalary(savedEmp.getMonthlySalary())
                .joiningDate(savedEmp.getJoiningDate())
                .terminatorDate(savedEmp.getTerminationDate())
                .EmploymentStatus(savedEmp.getEmploymentStatus())
                .position(savedEmp.getPosition() != null ? savedEmp.getPosition().getPositionName() : null)
                .build();

        return response;
    }

    @Override
    public Page<EmpResponse> getAllEmployee(Pageable pageable){
        Page<Employee> emp = employeeRepository.findAllByStatus(Status.ACTIVE.getValue(), pageable);

        return emp.map(tamp -> {
            EmpResponse dataTamp= new EmpResponse();
            dataTamp.setId(tamp.getId());
            dataTamp.setName(tamp.getName());
            dataTamp.setEmail(tamp.getEmail());
            dataTamp.setAddress(tamp.getAddress());
            dataTamp.setNoTelp(tamp.getNoTelp());
            dataTamp.setStatus(tamp.getStatus());
            dataTamp.setMonthlySalary(tamp.getMonthlySalary());
            dataTamp.setJoiningDate(tamp.getJoiningDate());
            dataTamp.setPosition(tamp.getPosition().getPositionName());
            return dataTamp;
        });
    }

    @Override
    @Transactional
    public void deletedEmployee(Integer employeeId){
        if(employeeId == null){
            throw new IllegalArgumentException("Employee ID must not be NULL");
        }

        Employee employee = employeeRepository.findByIdAndStatus(employeeId, Status.ACTIVE.getValue())
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id = " + employeeId));

        boolean isPkwt = Status.PKWT.getValue().equalsIgnoreCase(employee.getEmploymentStatus());
        boolean isPkwtt = Status.PKWTT.getValue().equalsIgnoreCase(employee.getEmploymentStatus());

        if(isPkwt){
            LocalDate termination = employee.getTerminationDate();
            if(termination != null && !termination.isBefore(LocalDate.now())){
                throw new IllegalArgumentException("Employee still has remaining contract time!");
            }
        } else if (!isPkwtt) {
            throw new IllegalArgumentException("Unsupported employment status: " + employee.getEmploymentStatus());
        }

        List<EmployeeWorkSchedule> schedule = employeeWorkScheduleRepository.findByEmployee_Id(employee.getId());
        if(schedule != null && !schedule.isEmpty()){
            for (EmployeeWorkSchedule sc : schedule){
                sc.setWorkingDay(false);
            }
            employeeWorkScheduleRepository.saveAll(schedule);
        }

        employee.setStatus(Status.INACTIVE.getValue());
        employeeRepository.save(employee);
    }
}
