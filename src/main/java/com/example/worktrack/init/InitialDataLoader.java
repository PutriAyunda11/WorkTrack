package com.example.worktrack.init;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.example.worktrack.enums.*;
import com.example.worktrack.model.*;
import com.example.worktrack.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


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

    @Autowired
    private WorkScheduleRepository workScheduleRepository;

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

        if(workScheduleRepository.findAll().isEmpty()){
            List<Position> position = positionRepository.findAll();
            boolean isWorkingDay = false;
            for(Position post : position){
                WorkSchedule mon = WorkSchedule.builder()
                        .day(DayEnum.MONDAY.getValue())
                        .workingDay(true)
                        .namePosition(post.getPositionName())
                        .build();
                WorkSchedule tues = WorkSchedule.builder()
                        .day(DayEnum.TUESDAY.getValue())
                        .workingDay(true)
                        .namePosition(post.getPositionName())
                        .build();
                WorkSchedule wed = WorkSchedule.builder()
                        .day(DayEnum.WEDNESDAY.getValue())
                        .workingDay(true)
                        .namePosition(post.getPositionName())
                        .build();
                WorkSchedule thurs = WorkSchedule.builder()
                        .day(DayEnum.THURSDAY.getValue())
                        .workingDay(true)
                        .namePosition(post.getPositionName())
                        .build();
                WorkSchedule fri = WorkSchedule.builder()
                        .day(DayEnum.FRIDAY.getValue())
                        .workingDay(true)
                        .namePosition(post.getPositionName())
                        .build();

                if(post.getPositionName().equalsIgnoreCase(PositionEnum.ADMIN_HRD.getValue()) || post.getPositionName().equalsIgnoreCase(PositionEnum.HR_SUPERVISOR.getValue()) ||
                    post.getPositionName().equalsIgnoreCase(PositionEnum.PPIC.getValue()) || post.getPositionName().equalsIgnoreCase(PositionEnum.ADMIN_GUDANG.getValue())){
                    isWorkingDay = false;
                }else {
                    isWorkingDay = true;
                }

                WorkSchedule satur = WorkSchedule.builder()
                        .day(DayEnum.SATURDAY.getValue())
                        .workingDay(isWorkingDay)
                        .namePosition(post.getPositionName())
                        .build();
                WorkSchedule sun = WorkSchedule.builder()
                        .day(DayEnum.SUNDAY.getValue())
                        .workingDay(isWorkingDay)
                        .namePosition(post.getPositionName())
                        .build();
                workScheduleRepository.saveAll(List.of(mon, tues, wed, thurs, fri, satur, sun));
            }
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
