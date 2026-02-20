package com.example.worktrack.service.impl;

import java.util.Optional;

import com.example.worktrack.constant.ConstantValue;
import com.example.worktrack.enums.PositionEnum;
import com.example.worktrack.enums.RoleEnum;
import com.example.worktrack.enums.Status;
import com.example.worktrack.service.CostUserDetailsService;
import com.example.worktrack.service.LoginService;
import org.springframework.http.HttpStatus;
// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.worktrack.dto.LoginRequest;
import com.example.worktrack.dto.LoginResponse;
import com.example.worktrack.dto.RegisRequest;
import com.example.worktrack.filter.JwtUtil;
import com.example.worktrack.model.Employee;
import com.example.worktrack.model.Login;
import com.example.worktrack.model.Position;
import com.example.worktrack.model.Role;
import com.example.worktrack.repository.EmployeeRepository;
import com.example.worktrack.repository.LoginRepository;
import com.example.worktrack.repository.RoleRepository;

@Service
public class LoginServiceImpl implements LoginService {
    private final AuthenticationManager authenticationManager;
    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;
    private final LoginRepository loginRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // @Autowired
    LoginServiceImpl(EmployeeRepository employeeRepository, LoginRepository loginRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager, JwtUtil jwtUtil, RoleRepository roleRepository) {
        this.employeeRepository = employeeRepository;
        this.loginRepository = loginRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void registrasi(RegisRequest regis) {

        Optional<Employee> employeeOpt = employeeRepository.findByEmail(regis.getEmail());
        if (employeeOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Employee is not Available");
        }

        if (!regis.getPassword().matches(ConstantValue.PasswordRegex)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Password");
        }

        Employee emp = employeeOpt.get();
        Optional<Login> dataLog = loginRepository.findByEmployeeId(emp.getId());
        if (dataLog.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Employees already have login accounts");
        }

        Position position = emp.getPosition();
        if(!regis.getPosition().equalsIgnoreCase(position.getPositionName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Inappropriate position");
        }
        Role role = new Role();
        if (position.getPositionName().equalsIgnoreCase(PositionEnum.ADMIN_HRD.getValue()) ||
                position.getPositionName().equalsIgnoreCase(PositionEnum.HR_SUPERVISIOR.getValue())) {
            role = roleRepository.findRoleByName(RoleEnum.ADMIN.getValue());
        } else {
            role = roleRepository.findRoleByName(RoleEnum.EMPLOYEE.getValue());
        }

        Login newData = new Login();
        newData.setUsername(regis.getUsename());
        newData.setPassword(passwordEncoder.encode(regis.getPassword()));
        newData.setStatus(Status.ACTIVE.getValue());
        newData.setEmployee(emp);
        newData.setRole(role);
        loginRepository.save(newData);
    }

    @Override
    public LoginResponse login(LoginRequest req) {
        Login login = loginRepository.findByUsername(req.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Data login not found"));

        if (!passwordEncoder.matches(req.getPassword(), login.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "the password you entered is incorrect");
        }

        if (!Status.ACTIVE.getValue().equalsIgnoreCase(login.getStatus())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Status Akun  Inactive");
        }

        String role = login.getRole().getName();
        String name;

        if (login.getEmployee() != null) {
            name = login.getEmployee().getName();
        } else {
            name = login.getUsername();
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword()));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "authenticatin is failed : " + e.getMessage());
        }

        CostUserDetailsService costUserDetailsService = new CostUserDetailsService(login);
        String token = jwtUtil.generateToken(costUserDetailsService);

        return new LoginResponse(token, role, name);
    }
}
