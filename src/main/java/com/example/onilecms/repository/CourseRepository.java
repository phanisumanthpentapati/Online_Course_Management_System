
package com.example.onilecms.repository;

import com.example.onilecms.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository
        extends JpaRepository<Course, Long> {

    List<Course> findByCategoryIgnoreCase(String category);

    List<Course> findByTitleContainingIgnoreCase(String title);
}
