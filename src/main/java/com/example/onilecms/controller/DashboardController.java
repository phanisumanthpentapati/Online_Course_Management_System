
package com.example.onilecms.controller;

import com.example.onilecms.repository.CourseRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final CourseRepository courseRepository;

    public DashboardController(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @GetMapping("/student/dashboard")
    public String studentDashboard() {
        return "student-dashboard";
    }

    @GetMapping("/instructor/dashboard")
    public String instructorDashboard() {
        return "instructor-dashboard";
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard() {
        return "admin-dashboard";
    }

    @GetMapping("/instructor/course-overview")
    public String courseOverview(Model model) {
        model.addAttribute("courses", courseRepository.findAll());
        return "instructor-course-overview";
    }
}
