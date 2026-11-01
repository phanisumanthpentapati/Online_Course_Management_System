package com.example.onilecms.controller;

import com.example.onilecms.entity.Enrollment;
import com.example.onilecms.entity.EnrollmentStatus;
import com.example.onilecms.service.EnrollmentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    // Enroll a student in a course
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Enrollment enroll(@RequestBody EnrollmentRequest request) {
        if (request.studentId() == null || request.courseId() == null) {
            throw new org.springframework.web.server.ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "studentId and courseId are required"
            );
        }

        return enrollmentService.enroll(
                request.studentId(),
                request.courseId()
        );
    }

    // Get all enrollments
    @GetMapping
    public List<Enrollment> getAllEnrollments() {
        return enrollmentService.getAllEnrollments();
    }

    // Get one enrollment
    @GetMapping("/{id}")
    public Enrollment getEnrollment(@PathVariable Long id) {
        return enrollmentService.getEnrollmentById(id);
    }

    // Get enrollments for one student
    @GetMapping("/student/{studentId}")
    public List<Enrollment> getByStudent(
            @PathVariable Long studentId) {
        return enrollmentService.getByStudent(studentId);
    }

    // Get students enrolled in one course
    @GetMapping("/course/{courseId}")
    public List<Enrollment> getByCourse(
            @PathVariable Long courseId) {
        return enrollmentService.getByCourse(courseId);
    }

    // Update enrollment status
    @PatchMapping("/{id}/status")
    public Enrollment updateStatus(
            @PathVariable Long id,
            @RequestBody StatusRequest request) {
        return enrollmentService.updateStatus(id, request.status());
    }

    // Delete an enrollment
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEnrollment(@PathVariable Long id) {
        enrollmentService.deleteEnrollment(id);
    }

    public record EnrollmentRequest(Long studentId, Long courseId) {
    }

    public record StatusRequest(EnrollmentStatus status) {
    }
}