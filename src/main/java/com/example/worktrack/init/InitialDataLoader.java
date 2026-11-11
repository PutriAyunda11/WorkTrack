package com.example.worktrack.init;

import java.util.Arrays;
import java.util.List;

import com.example.worktrack.enums.CutiEnum;
import com.example.worktrack.enums.PositionEnum;
import com.example.worktrack.enums.RoleEnum;
import com.example.worktrack.enums.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.worktrack.model.Cuti;
import com.example.worktrack.model.Login;
import com.example.worktrack.model.Position;
import com.example.worktrack.model.Role;
import com.example.worktrack.repository.CutiRepository;
import com.example.worktrack.repository.LoginRepository;
import com.example.worktrack.repository.PositionRepository;
import com.example.worktrack.repository.RoleRepository;


@Component
@Transactional
public class InitialDataLoader implements ApplicationRunner{
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CutiRepository cutiRepository;

    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private LoginRepository loginRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception{
        if(roleRepository.findAll().isEmpty()){
            Role admin = new Role();
            admin.setName(RoleEnum.ADMIN.getValue());
            Role employee = new Role();
            employee.setName(RoleEnum.EMPLOYEE.getValue());
            roleRepository.saveAll(List.of(admin, employee)); 

        }

        if(cutiRepository.findAll().isEmpty()){
            Cuti tahunan = Cuti.builder()
                    .tipeCuti(CutiEnum.CUTI_TAHUNAN.getValue())
                    .totalCuti(12)
                    .build();
            Cuti sakit = Cuti.builder()
                    .tipeCuti(CutiEnum.CUTI_SAKIT.getValue())
                    .build();
            Cuti melahirkan = Cuti.builder()
                    .tipeCuti(CutiEnum.CUTI_MELAHIRKAN.getValue())
                    .totalCuti(180)
                    .build();
            Cuti menikah = Cuti.builder()
                    .tipeCuti(CutiEnum.CUTI_MENIKAH.getValue())
                    .totalCuti(4)
                    .build();
            Cuti keluarga = Cuti.builder()
                    .tipeCuti(CutiEnum.CUTI_KEPENTINGAN_KELUARGA.getValue())
                    .build();

            Cuti ibadah = Cuti.builder()
                    .tipeCuti(CutiEnum.CUTI_UMROH.getValue())
                    .totalCuti(45)
                    .build();

            cutiRepository.saveAll(List.of(tahunan, sakit, melahirkan,menikah, keluarga, ibadah));
        }

        if(positionRepository.findAll().isEmpty()){
            List<Position> positions = Arrays.stream(PositionEnum.values())
                    .map(enumPost ->{
                        Position pos = new Position();
                        pos.setPositionName(enumPost.getValue());
                        return pos;
                    })
                    .toList();
            positionRepository.saveAll(positions);
        }

        if(loginRepository.findAll().isEmpty()){
                Role cari = roleRepository.findRoleByName(RoleEnum.ADMIN.getValue());
            Login login = Login.builder()
                    .username(RoleEnum.ADMIN.name())
                    .password(passwordEncoder.encode("ADMIN123"))
                    .status(Status.ACTIVE.name())
                    .role(cari)
                    .build();
            loginRepository.save(login);
        }



    }
    
}
