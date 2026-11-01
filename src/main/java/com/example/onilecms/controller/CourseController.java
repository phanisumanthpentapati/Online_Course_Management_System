
package com.example.onilecms.controller;

import com.example.onilecms.entity.Course;
import com.example.onilecms.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService service;

    public CourseController(CourseService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Course createCourse(@Valid @RequestBody Course course) {
        return service.createCourse(course);
    }

    @GetMapping
    public List<Course> getAllCourses() {
        return service.getAllCourses();
    }

    @GetMapping("/{id}")
    public Course getCourse(@PathVariable Long id) {
        return service.getCourseById(id);
    }

    @GetMapping("/category/{category}")
    public List<Course> getByCategory(
            @PathVariable String category) {
        return service.getByCategory(category);
    }

    @GetMapping("/search")
    public List<Course> searchByTitle(@RequestParam String title) {
        return service.searchByTitle(title);
    }

    @PutMapping("/{id}")
    public Course updateCourse(
            @PathVariable Long id,
            @Valid @RequestBody Course course) {
        return service.updateCourse(id, course);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCourse(@PathVariable Long id) {
        service.deleteCourse(id);
    }
}
