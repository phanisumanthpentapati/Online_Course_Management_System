package com.example.onilecms.service;

import com.example.onilecms.entity.Course;
import com.example.onilecms.entity.Enrollment;
import com.example.onilecms.entity.EnrollmentStatus;
import com.example.onilecms.entity.Student;
import com.example.onilecms.repository.CourseRepository;
import com.example.onilecms.repository.EnrollmentRepository;
import com.example.onilecms.repository.StudentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public EnrollmentService(
            EnrollmentRepository enrollmentRepository,
            StudentRepository studentRepository,
            CourseRepository courseRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    @Transactional
    public Enrollment enroll(Long studentId, Long courseId) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Student not found: " + studentId
                ));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Course not found: " + courseId
                ));

        if (enrollmentRepository
                .existsByStudent_IdAndCourse_Id(studentId, courseId)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Student is already enrolled in this course"
            );
        }

        Enrollment enrollment = new Enrollment(student, course);

        return enrollmentRepository.save(enrollment);
    }

    @Transactional(readOnly = true)
    public List<Enrollment> getAllEnrollments() {
        return enrollmentRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Enrollment getEnrollmentById(Long id) {
        return enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Enrollment not found: " + id
                ));
    }

    @Transactional(readOnly = true)
    public List<Enrollment> getByStudent(Long studentId) {

        if (!studentRepository.existsById(studentId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Student not found: " + studentId
            );
        }

        return enrollmentRepository.findByStudent_Id(studentId);
    }

    @Transactional(readOnly = true)
    public List<Enrollment> getByCourse(Long courseId) {

        if (!courseRepository.existsById(courseId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Course not found: " + courseId
            );
        }

        return enrollmentRepository.findByCourse_Id(courseId);
    }

    @Transactional
    public Enrollment updateStatus(
            Long id,
            EnrollmentStatus status) {

        if (status == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Status is required"
            );
        }

        Enrollment enrollment = getEnrollmentById(id);
        enrollment.setStatus(status);

        return enrollmentRepository.save(enrollment);
    }

    @Transactional
    public void deleteEnrollment(Long id) {

        Enrollment enrollment = getEnrollmentById(id);
        enrollmentRepository.delete(enrollment);
    }
}