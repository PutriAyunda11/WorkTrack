package com.example.worktrack.service.impl;

import com.example.worktrack.dto.PresenceRequest;
import com.example.worktrack.enums.Status;
import com.example.worktrack.model.*;
import com.example.worktrack.repository.*;
import com.example.worktrack.service.FileStorageService;
import com.example.worktrack.service.PresenceService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

@Service
public class PresenceServiceImpl implements PresenceService {
    private final PresenceRepository presenceRepository;
    private final EmployeeRepository employeeRepository;
    private final PositionRepository positionRepository;
    private final FileStorageService fileStorageService;
    private final EmployeeWorkScheduleRepository employeeWorkScheduleRepository;
    private final OvertimeRepository overtimeRepository;

    PresenceServiceImpl(PresenceRepository presenceRepository, EmployeeRepository employeeRepository,
                        PositionRepository positionRepository, FileStorageService fileStorageService,
                        EmployeeWorkScheduleRepository employeeWorkScheduleRepository, OvertimeRepository overtimeRepository){
        this.presenceRepository=presenceRepository;
        this.employeeRepository=employeeRepository;
        this.positionRepository=positionRepository;
        this.fileStorageService=fileStorageService;
        this.employeeWorkScheduleRepository = employeeWorkScheduleRepository;
        this.overtimeRepository = overtimeRepository;
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

        LocalDate dateNow = LocalDate.now();
        LocalTime timeNow =LocalTime.now();
        String photoPath = fileStorageService.store(photo);

        EmployeeWorkSchedule schedule = employeeWorkScheduleRepository.findByWorkDateAndEmployee(dateNow, emp)
                .orElse(null);
        if (schedule!= null && schedule.isWorkingDay()){
            Presence presence = new Presence();
            presence.setEmployee(emp);
            presence.setPhotoIn(photoPath);
            presence.setDatePresence(dateNow);
            presence.setEntryTime(timeNow);

            LocalTime tolerance = schedule.getStartTime().plusMinutes(5);
            if(timeNow.isBefore(tolerance)){
                presence.setStatus(Status.ON_TIME.getValue());
            } else {
                presence.setStatus(Status.LATE.getValue());
            }

            presenceRepository.save(presence);
        }else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Today is not a working day");
        }

        return "success";
    }

    @Override
    public String checkOut(PresenceRequest req, MultipartFile photo) {
        if (req.getName().isBlank() || req.getPosition().isBlank() || photo == null || photo.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Data cannot be empty");
        }

        Position position = positionRepository.findByPositionName(req.getPosition())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Position not found"));

        Employee emp = employeeRepository.findByNameAndPosition(req.getName(), position);
        if (emp == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found");
        }

        Presence presence = presenceRepository.findByEmployeeAndExitTimeIsNull(emp)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "You have not checked in today or already checked out"));

        String photoOutPath = fileStorageService.store(photo);

        LocalTime timeNow = LocalTime.now();
        LocalDateTime dateTimeIn = LocalDateTime.of(presence.getDatePresence(), presence.getEntryTime());
        LocalDate dateOut = presence.getDatePresence();
        if(timeNow.isBefore(presence.getEntryTime())){
            dateOut = dateOut.plusDays(1);
        }
        LocalDateTime dateTimeOut = LocalDateTime.of(dateOut, timeNow);
        long totalMinutes = ChronoUnit.MINUTES.between(dateTimeIn, dateTimeOut);

        Overtime overtime = overtimeRepository.findByOvertimeDateAndEmployee(presence.getDatePresence(), emp);
        if(overtime != null && overtime.getStatus().equalsIgnoreCase(Status.APPROVED.getValue()) ){
            presence.setNoted(Status.OVERTIME.getValue());
        }

        EmployeeWorkSchedule schedule = employeeWorkScheduleRepository.findByWorkDateAndEmployee(presence.getDatePresence(), emp)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,"Schedule for today not found"));

        if(timeNow.isBefore(schedule.getEndTime())){
            presence.setNoted(Status.EARLY_LEAVE.getValue());
        }

        presence.setPhotoOut(photoOutPath);
        presence.setExitTime(timeNow);
        presence.setTotalWorkTime(totalMinutes);

        presenceRepository.save(presence);
        return "Successfully checked out";
    }
}
