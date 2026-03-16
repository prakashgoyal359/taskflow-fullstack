package com.infy.authSystem.service;


import com.infy.authSystem.entity.TaskAttachment;
import com.infy.authSystem.repository.TaskAttachmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class AttachmentService {

    @Autowired
    private TaskAttachmentRepository repository;

    public TaskAttachment uploadFile(Long taskId, Long userId, MultipartFile file) throws IOException {

        if (repository.countByTaskId(taskId) >= 5) {
            throw new RuntimeException("Maximum 5 files allowed");
        }

        TaskAttachment attachment = new TaskAttachment();

        attachment.setTaskId(taskId);
        attachment.setUploaderId(userId);
        attachment.setOriginalName(file.getOriginalFilename());
        attachment.setMimeType(file.getContentType());
        attachment.setFileSizeBytes(file.getSize());
        attachment.setFileData(file.getBytes());

        return repository.save(attachment);
    }
}