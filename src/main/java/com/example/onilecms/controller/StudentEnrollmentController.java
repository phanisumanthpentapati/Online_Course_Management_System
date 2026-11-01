
package com.example.onilecms.controller;

import com.example.onilecms.entity.Enrollment;
import com.example.onilecms.entity.Student;
import com.example.onilecms.repository.StudentRepository;
import com.example.onilecms.service.EnrollmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/student")
public class StudentEnrollmentController {

    private final StudentRepository studentRepository;
    private final EnrollmentService enrollmentService;

    public StudentEnrollmentController(
            StudentRepository studentRepository,
            EnrollmentService enrollmentService) {
        this.studentRepository = studentRepository;
        this.enrollmentService = enrollmentService;
    }

    @PostMapping("/enroll/{courseId}")
    public ResponseEntity<Map<String, Object>> enroll(
            @PathVariable Long courseId,
            Authentication authentication) {

        Student student = studentRepository
                .findByEmailIgnoreCase(authentication.getName())
                .orElseThrow(() -> new org.springframework.web.server
                        .ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No student record exists for this account."));

        Enrollment enrollment =
                enrollmentService.enroll(student.getId(), courseId);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "Successfully enrolled in the course.");
        response.put("enrollmentId", enrollment.getId());
        response.put("courseId", courseId);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
