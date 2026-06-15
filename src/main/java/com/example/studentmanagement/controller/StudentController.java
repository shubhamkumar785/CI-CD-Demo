package com.example.studentmanagement.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.studentmanagement.entity.Student;
import com.example.studentmanagement.repository.StudentRepository;

@Controller
public class StudentController {

    private final StudentRepository studentRepository;

    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/students/new")
    public String showAddStudentForm(Model model) {
        model.addAttribute("student", new Student());
        return "add-student";
    }

    @GetMapping("/students")
    public String studentsRootRedirect(Model model) {
        return showTotalStudents(model);
    }

    @PostMapping("/students")
    public String addStudent(@ModelAttribute Student student, Model model, RedirectAttributes redirectAttributes) {
        try {
            if (student == null || student.getName() == null || student.getEmail() == null ||
                    student.getCourse() == null || student.getDateOfJoining() == null) {
                model.addAttribute("student", student);
                model.addAttribute("errorMessage", "All fields are required.");
                return "add-student";
            }

            studentRepository.save(student);
            redirectAttributes.addFlashAttribute("successMessage", "Student saved successfully.");
            return "redirect:/";
        } catch (DataIntegrityViolationException ex) {
            model.addAttribute("student", student);
            model.addAttribute("errorMessage", "Email already exists. Please use a different email.");
            return "add-student";
        } catch (InvalidDataAccessResourceUsageException ex) {
            model.addAttribute("student", student);
            model.addAttribute("errorMessage", "Database schema error. Please restart the application.");
            return "add-student";
        } catch (Exception ex) {
            model.addAttribute("student", student);
            model.addAttribute("errorMessage", "Unable to save student. Please check your input.");
            return "add-student";
        }
    }

    @GetMapping("/students/count")
    public String showTotalStudents(Model model) {
        try {
            model.addAttribute("totalStudents", studentRepository.count());
            model.addAttribute("students", studentRepository.findAll());
        } catch (Exception ex) {
            model.addAttribute("totalStudents", 0L);
            model.addAttribute("students", java.util.Collections.emptyList());
            model.addAttribute("errorMessage", "Unable to load students due to database error.");
        }
        return "total-students";
    }
}
