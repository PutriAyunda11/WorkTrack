package com.example.worktrack.service.impl;

import com.example.worktrack.service.FileStorageService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private final Path fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();

    public FileStorageServiceImpl() {
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    @Override
    public String store(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File cannot be empty");
        }

        try {
            String originalFileName = file.getOriginalFilename();
            String fileName = System.currentTimeMillis() + "_" + (originalFileName != null ? originalFileName : "file");

            Path targetLocation = this.fileStorageLocation.resolve(fileName);

            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;

        } catch (IOException ex) {
            throw new RuntimeException("Failed to store file. " + ex.getMessage(), ex);
        }
    }
}
//@Service
//public class FileStorageServiceImpl implements FileStorageService {
//
//    @Override
//    public String store(MultipartFile file) {
//
//        try {
//
//            String url = "https://catbox.moe/user/api.php";
//
//            RestTemplate restTemplate = new RestTemplate();
//
//            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
//            body.add("reqtype", "fileupload");
//            body.add("fileToUpload", file.getResource());
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
//
//            HttpEntity<MultiValueMap<String, Object>> requestEntity =
//                    new HttpEntity<>(body, headers);
//
//            ResponseEntity<String> response =
//                    restTemplate.postForEntity(url, requestEntity, String.class);
//
//            return response.getBody();
//
//        } catch (Exception e) {
//            throw new RuntimeException("Failed upload image", e);
//        }
//    }
//}