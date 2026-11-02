package com.example.worktrack.init;

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
            Position op = new Position();
            op.setPositionName(PositionEnum.OPERATOR_PRODUKSI.getValue());
            op.setTimeWork(8);
            op.setHourlySalary(25000);

            Position hp = new Position();
            hp.setPositionName(PositionEnum.HELPER_PRODUKSI.getValue());
            hp.setTimeWork(8);
            hp.setHourlySalary(20000);

            Position tm = new Position();
            tm.setPositionName(PositionEnum.TEKNISI_MESIN.getValue());
            tm.setTimeWork(9);
            tm.setHourlySalary(28000);

            Position qc = new Position();
            qc.setPositionName(PositionEnum.QUALITY_CONTROL.getValue());
            qc.setTimeWork(9);
            qc.setHourlySalary(26500);

            Position sg = new Position();
            sg.setPositionName(PositionEnum.STAF_GUDANG.getValue());
            sg.setTimeWork(9);
            sg.setHourlySalary(25000);

            Position ag = new Position();
            ag.setPositionName(PositionEnum.ADMIN_GUDANG.getValue());
            ag.setTimeWork(9);
            ag.setHourlySalary(27000);

            Position cs = new Position();
            cs.setPositionName(PositionEnum.CLEANING_SERVICE.getValue());
            cs.setTimeWork(9);
            cs.setHourlySalary(18000);

            Position sec = new Position();
            sec.setPositionName(PositionEnum.SECURITY.getValue());
            sec.setTimeWork(12);
            sec.setHourlySalary(21000);

            Position hrd = new Position();
            hrd.setPositionName(PositionEnum.ADMIN_HRD.getValue());
            hrd.setTimeWork(9);
            hrd.setHourlySalary(31000);

            Position hrs = new Position();
            hrs.setPositionName(PositionEnum.HR_SUPERVISIOR.getValue());
            hrs.setTimeWork(9);
            hrs.setHourlySalary(43000);

            positionRepository.saveAll(List.of(op,hp,tm,qc,sg,ag,cs,sec,hrd,hrs));
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
