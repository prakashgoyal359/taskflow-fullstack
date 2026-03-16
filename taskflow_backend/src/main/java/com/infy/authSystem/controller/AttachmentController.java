package com.infy.authSystem.controller;

import com.infy.authSystem.entity.TaskAttachment;
import com.infy.authSystem.repository.TaskAttachmentRepository;
import com.infy.authSystem.service.AttachmentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class AttachmentController {

    @Autowired
    private AttachmentService service;

	@Autowired
    private TaskAttachmentRepository repository;

    @PostMapping("/tasks/{taskId}/attachments")
    public ResponseEntity<?> uploadFile(
            @PathVariable Long taskId,
            @RequestParam("file") MultipartFile file
    ) throws IOException {

        TaskAttachment saved = service.uploadFile(taskId, 1L, file);

        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/tasks/{taskId}/attachments")
    public List<TaskAttachment> list(@PathVariable Long taskId) {
        return repository.findByTaskId(taskId);
    }

    @GetMapping("/attachments/{id}/download")
    public ResponseEntity<byte[]> download(@PathVariable Long id) {

        TaskAttachment file = repository.findById(id).orElseThrow();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + file.getOriginalName() + "\"")
                .contentType(MediaType.parseMediaType(file.getMimeType()))
                .body(file.getFileData());
    }

    @DeleteMapping("/attachments/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {

        repository.deleteById(id);

        return ResponseEntity.ok().build();
    }
}
