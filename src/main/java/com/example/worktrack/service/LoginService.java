package com.example.worktrack.service;

import com.example.worktrack.dto.LoginRequest;
import com.example.worktrack.dto.LoginResponse;
import com.example.worktrack.dto.RegisRequest;

public interface LoginService {
    LoginResponse login(LoginRequest loginReq);
    void registrasi(RegisRequest regis);
}
