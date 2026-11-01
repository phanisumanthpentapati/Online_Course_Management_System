
package com.example.onilecms.service;

import com.example.onilecms.entity.Course;
import com.example.onilecms.repository.CourseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CourseService {

    private final CourseRepository repository;

    public CourseService(CourseRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Course createCourse(Course course) {
        return repository.save(course);
    }

    public List<Course> getAllCourses() {
        return repository.findAll();
    }

    public Course getCourseById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Course not found: " + id));
    }

    public List<Course> getByCategory(String category) {
        return repository.findByCategoryIgnoreCase(category);
    }

    public List<Course> searchByTitle(String title) {
        return repository.findByTitleContainingIgnoreCase(title);
    }

    @Transactional
    public Course updateCourse(Long id, Course updated) {
        Course course = getCourseById(id);

        course.setTitle(updated.getTitle());
        course.setDescription(updated.getDescription());
        course.setCategory(updated.getCategory());
        course.setInstructor(updated.getInstructor());
        course.setContentUrl(updated.getContentUrl());

        return repository.save(course);
    }

    @Transactional
    public void deleteCourse(Long id) {
        Course course = getCourseById(id);
        repository.delete(course);
    }
}
