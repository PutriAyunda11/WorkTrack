package com.example.worktrack.service.impl;

import com.example.worktrack.dto.PresenceRequest;
import com.example.worktrack.model.Employee;
import com.example.worktrack.model.Position;
import com.example.worktrack.model.Presence;
import com.example.worktrack.repository.EmployeeRepository;
import com.example.worktrack.repository.PositionRepository;
import com.example.worktrack.repository.PresenceRepository;
import com.example.worktrack.service.FileStorageService;
import com.example.worktrack.service.PresenceService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;


import java.nio.file.FileStore;
import java.time.LocalTime;
import java.util.Date;

@Service
public class PresenceServiceImpl implements PresenceService {
    private final PresenceRepository presenceRepository;
    private final EmployeeRepository employeeRepository;
    private final PositionRepository positionRepository;
    private final FileStorageService fileStorageService;

    PresenceServiceImpl(PresenceRepository presenceRepository, EmployeeRepository employeeRepository,
                        PositionRepository positionRepository, FileStorageService fileStorageService){
        this.presenceRepository=presenceRepository;
        this.employeeRepository=employeeRepository;
        this.positionRepository=positionRepository;
        this.fileStorageService=fileStorageService;
    }

    @Override
    public String checkIn(PresenceRequest req, MultipartFile photo){
        if(req.getName().isBlank()|| req.getPosition().isBlank() || photo == null || photo.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Data cannot be empty");
        }

        Position position = positionRepository.findByPositionName(req.getPosition())
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "position not found"));

        Employee emp = employeeRepository.findByNameAndPosition(req.getName(), position);
        if (emp == null){
            throw  new ResponseStatusException(HttpStatus.NOT_FOUND, "Data employee not found");
        }

        Date dateNow = new Date();
        LocalTime timeNow =LocalTime.now();
        String photoPath = fileStorageService.store(photo);
        Presence presence = new Presence();

        presence.setEmployee(emp);
        presence.setPhoto(photoPath);
        presence.setDatePresence(dateNow);
        presence.setEntryTime(timeNow);
        presenceRepository.save(presence);

        return "success";
    }
}
