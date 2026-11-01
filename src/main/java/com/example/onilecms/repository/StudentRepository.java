
package com.example.onilecms.repository;

import com.example.onilecms.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    // Existing methods required by StudentService
    boolean existsByEmail(String email);

    Optional<Student> findByEmail(String email);

    // Methods required for email-based student lookup
    Optional<Student> findByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCase(String email);
}
