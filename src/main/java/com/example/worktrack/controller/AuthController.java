package com.example.worktrack.controller;

import com.example.worktrack.dto.GenericResponse;
import com.example.worktrack.dto.LoginRequest;
import com.example.worktrack.dto.LoginResponse;
import com.example.worktrack.dto.RegisRequest;

// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.worktrack.service.LoginService;

@RestController
@RequestMapping("/authentication")
public class AuthController {
    private final LoginService loginService;

    // @Autowired
    public AuthController(LoginService loginService){
        this.loginService=loginService;
    }

    @PostMapping("/login")
    public ResponseEntity<GenericResponse<LoginResponse>> login(@RequestBody LoginRequest loginReq){
        LoginResponse resp =loginService.login(loginReq);
        return ResponseEntity.ok().body(GenericResponse.<LoginResponse>builder()
        .success(true)
        .message("successfully")
        .data(resp)
        .build()
        );
    }

    @PostMapping("/registrasi")
    public ResponseEntity<GenericResponse<Object>> registrasi(@RequestBody RegisRequest req){
        loginService.registrasi(req);
        return ResponseEntity.ok().body(GenericResponse.builder()
        .success(true)
        .data(null)
        .message("successfully")
        .build()
        );
    }


}
