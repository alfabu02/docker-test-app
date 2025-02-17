package uz.alfa.dockertestapp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@Controller
@RequestMapping
public class WelcomeController {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @GetMapping
    public String welcome() {
        return "welcome";
    }

    @GetMapping("/upload")
    public String upload() {
        return "upload";
    }

    @PostMapping("/upload")
    public String upload(@RequestParam MultipartFile file) throws IOException {
        if (file != null) {
            String originalFilename = file.getOriginalFilename();
            assert originalFilename != null;

            Path uploadPath = Path.of(uploadDir, originalFilename);
            Files.createDirectories(uploadPath.getParent());

            Path uploadedPath = Files.write(uploadPath, file.getBytes(), StandardOpenOption.CREATE);
            System.out.println(uploadedPath);
        }
        return "upload";
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> download(@RequestParam String filename) throws IOException {
        assert filename != null;
        Path path = Path.of(uploadDir, filename);

        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(Files.probeContentType(path)))
                .body(Files.readAllBytes(path));
    }

}
