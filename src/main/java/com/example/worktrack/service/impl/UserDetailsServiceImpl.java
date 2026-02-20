package com.example.worktrack.service.impl;


import com.example.worktrack.service.CostUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.worktrack.model.Login;
import com.example.worktrack.repository.LoginRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final LoginRepository loginRepository;

    public UserDetailsServiceImpl(LoginRepository loginRepository){
        this.loginRepository=loginRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        Login login = loginRepository.findByUsername(username)
                        .orElseThrow(()->new UsernameNotFoundException("Username not found"));
        System.out.println("Username di database ditemukan : "+login.getUsername()+" dengan role : "+login.getRole().getName());

        return new CostUserDetailsService(login);
    }
}
