
package com.example.onilecms.controller;

import com.example.onilecms.entity.AppUser;
import com.example.onilecms.entity.Course;
import com.example.onilecms.entity.Enrollment;
import com.example.onilecms.entity.Student;
import com.example.onilecms.repository.AppUserRepository;
import com.example.onilecms.repository.CourseRepository;
import com.example.onilecms.repository.StudentRepository;
import com.example.onilecms.service.EnrollmentService;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/student")
public class StudentEnrollmentController {

    private final StudentRepository studentRepository;
    private final AppUserRepository appUserRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentService enrollmentService;

    public StudentEnrollmentController(
            StudentRepository studentRepository,
            AppUserRepository appUserRepository,
            CourseRepository courseRepository,
            EnrollmentService enrollmentService) {
        this.studentRepository = studentRepository;
        this.appUserRepository = appUserRepository;
        this.courseRepository = courseRepository;
        this.enrollmentService = enrollmentService;
    }

    private Student getOrCreateStudent(Authentication authentication) {

        if (authentication == null
                || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getName())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Please log in first.");
        }

        AppUser user = appUserRepository
                .findByEmailIgnoreCase(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User account not found."));

        if (user.getRole() == null
                || !user.getRole().name().equals("STUDENT")) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Only students can enroll in courses.");
        }

        return studentRepository
                .findByEmailIgnoreCase(user.getEmail())
                .orElseGet(() -> {
                    Student student = new Student();
                    student.setName(user.getFullName());
                    student.setEmail(user.getEmail());
                    student.setDepartment("General");
                    student.setMarks(0);
                    return studentRepository.save(student);
                });
    }

    // Opens the enrollment form when the student clicks Enroll Now.
    @GetMapping("/enrollment-form/{courseId}")
    public String showEnrollmentForm(
            @PathVariable Long courseId,
            Authentication authentication,
            Model model) {

        Student student = getOrCreateStudent(authentication);

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Course not found."));

        addFormDetails(model, student, course);

        return "enrollment-form";
    }

    // Saves enrollment only after the student confirms the form.
    @PostMapping("/enroll/{courseId}")
    public String enroll(
            @PathVariable Long courseId,
            @RequestParam(required = false) String contact,
            Authentication authentication,
            Model model) {

        Student student = getOrCreateStudent(authentication);

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Course not found."));

        if (contact != null && !contact.isBlank()) {
            student.setContact(contact.trim());
            studentRepository.save(student);
        }

        if (enrollmentService.isAlreadyEnrolled(
                student.getId(), course.getId())) {

            model.addAttribute(
                    "error",
                    "You are already enrolled in this course.");

        } else {
            Enrollment enrollment = enrollmentService.enroll(
                    student.getId(), course.getId());

            model.addAttribute(
                    "message",
                    "Successfully enrolled in "
                            + course.getTitle() + ".");

            model.addAttribute(
                    "enrollmentId",
                    enrollment.getId());
        }

        addFormDetails(model, student, course);

        return "enrollment-form";
    }

    private void addFormDetails(
            Model model,
            Student student,
            Course course) {

        model.addAttribute("studentName", student.getName());
        model.addAttribute("studentEmail", student.getEmail());
        model.addAttribute("contact", student.getContact());
        model.addAttribute("course", course);
    }
}
