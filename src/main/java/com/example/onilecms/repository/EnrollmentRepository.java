package com.example.onilecms.repository;

import com.example.onilecms.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnrollmentRepository
        extends JpaRepository<Enrollment, Long> {

    // Get all enrollments for a particular student
    List<Enrollment> findByStudent_Id(Long studentId);

    // Get all enrollments for a particular course
    List<Enrollment> findByCourse_Id(Long courseId);

    // Check whether the student is already enrolled in the course
    boolean existsByStudent_IdAndCourse_Id(
            Long studentId,
            Long courseId
    );
}