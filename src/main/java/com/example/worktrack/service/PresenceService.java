package com.example.worktrack.service;

import com.example.worktrack.dto.PresenceRequest;
import org.springframework.web.multipart.MultipartFile;

public interface PresenceService {
    String checkIn(PresenceRequest req, MultipartFile photo);
    String checkOut(PresenceRequest req, MultipartFile photo);
}
