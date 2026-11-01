
package com.example.onilecms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

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
}
