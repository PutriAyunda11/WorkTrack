package com.example.worktrack.controller;

import com.example.worktrack.dto.GenericResponse;
import com.example.worktrack.dto.PresenceRequest;
import com.example.worktrack.service.PresenceService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/presence")
public class PresenceController {

    private final PresenceService presenceService;

    PresenceController(PresenceService presenceService){
        this.presenceService=presenceService;
    }

    @PostMapping(value = "/check-in", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<GenericResponse<String>> checkIn(
            @RequestParam String name,
            @RequestParam String position,
            @RequestPart MultipartFile photo
    ) {
        PresenceRequest req = PresenceRequest.builder()
                .name(name)
                .position(position)
                .build();
        String data = presenceService.checkIn(req, photo);
        return ResponseEntity.ok().body(
                GenericResponse.<String>builder()
                        .success(true)
                        .message("successfully")
                        .data(data)
                        .build()
        );
    }

    @PostMapping(value = "/check-out", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<GenericResponse<String>> checkOut(
            @RequestParam String name,
            @RequestParam String position,
            @RequestPart MultipartFile photo
    ) {
        PresenceRequest req = PresenceRequest.builder()
                .name(name)
                .position(position)
                .build();

        String data = presenceService.checkOut(req, photo);

        return ResponseEntity.ok().body(
                GenericResponse.<String>builder()
                        .success(true)
                        .message("successfully checked out")
                        .data(data)
                        .build()
        );
    }

}
