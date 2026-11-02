
package com.example.onilecms.controller;

import com.example.onilecms.entity.Course;
import com.example.onilecms.repository.CourseRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/instructor")
public class InstructorController {

    private final CourseRepository courseRepository;

    public InstructorController(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @GetMapping("/courses")
    public String courses(Model model) {
        model.addAttribute("courses", courseRepository.findAll());
        return "instructor-courses";
    }

    @GetMapping("/students")
    public String students() {
        return "instructor-students";
    }
}
