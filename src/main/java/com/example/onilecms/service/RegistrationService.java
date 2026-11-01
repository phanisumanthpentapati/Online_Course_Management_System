
package com.example.onilecms.service;

import com.example.onilecms.entity.AppUser;
import com.example.onilecms.entity.Student;
import com.example.onilecms.entity.UserRole;
import com.example.onilecms.repository.AppUserRepository;
import com.example.onilecms.repository.StudentRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegistrationService {

    private final AppUserRepository userRepository;
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationService(
            AppUserRepository userRepository,
            StudentRepository studentRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void register(
            String fullName,
            String email,
            String rawPassword,
            UserRole role) {

        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException("Full name is required.");
        }

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email is required.");
        }

        if (rawPassword == null || rawPassword.length() < 8) {
            throw new IllegalArgumentException(
                    "Password must contain at least 8 characters.");
        }

        if (role == null || role == UserRole.ADMIN) {
            throw new IllegalArgumentException("Invalid registration role.");
        }

        String normalizedEmail =
                email.trim().toLowerCase(java.util.Locale.ROOT);

        if (userRepository.existsByEmailIgnoreCase(normalizedEmail)) {
            throw new IllegalArgumentException(
                    "An account with this email already exists.");
        }

        AppUser user = new AppUser(
                fullName.trim(),
                normalizedEmail,
                passwordEncoder.encode(rawPassword),
                role
        );

        userRepository.save(user);

        if (role == UserRole.STUDENT) {
            Student student = new Student();
            student.setName(fullName.trim());
            student.setEmail(normalizedEmail);
            studentRepository.save(student);
        }
    }
}
