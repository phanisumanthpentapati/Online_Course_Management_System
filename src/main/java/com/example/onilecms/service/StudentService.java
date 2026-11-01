
package com.example.onilecms.service;

import com.example.onilecms.entity.Student;
import com.example.onilecms.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StudentService {

    private final StudentRepository repository;

    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Student register(Student student) {
        if (repository.existsByEmail(student.getEmail())) {
            throw new IllegalArgumentException(
                    "Email is already registered");
        }
        return repository.save(student);
    }

    public List<Student> getAllStudents() {
        return repository.findAll();
    }

    public Student getStudentById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Student not found: " + id));
    }

    @Transactional
    public Student updateStudent(Long id, Student updated) {
        Student student = getStudentById(id);

        repository.findByEmail(updated.getEmail())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new IllegalArgumentException(
                            "Email is already registered");
                });

        student.setName(updated.getName());
        student.setEmail(updated.getEmail());
        student.setContact(updated.getContact());

        return repository.save(student);
    }

    @Transactional
    public void deleteStudent(Long id) {
        Student student = getStudentById(id);
        repository.delete(student);
    }
}
