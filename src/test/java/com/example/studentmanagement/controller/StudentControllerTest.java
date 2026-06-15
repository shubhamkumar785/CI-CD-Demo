package com.example.studentmanagement.controller;

import com.example.studentmanagement.entity.Student;
import com.example.studentmanagement.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
public class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentRepository studentRepository;

    @Test
    void getAddStudentForm_returnsForm() throws Exception {
        mockMvc.perform(get("/students/new"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Add Student")));
    }

    @Test
    void postStudent_successRedirectsHome() throws Exception {
        Student saved = new Student();
        saved.setId(1L);
        saved.setName("Alice");
        saved.setEmail("alice@example.com");
        saved.setCourse("BSc");
        saved.setDateOfJoining(LocalDate.of(2026,6,15));

        when(studentRepository.save(any(Student.class))).thenReturn(saved);

        mockMvc.perform(post("/students")
                        .param("name", "Alice")
                        .param("email", "alice@example.com")
                        .param("course", "BSc")
                        .param("dateOfJoining", "2026-06-15"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void postStudent_duplicateEmailShowsError() throws Exception {
        doThrow(new DataIntegrityViolationException("duplicate key"))
                .when(studentRepository).save(any(Student.class));

        mockMvc.perform(post("/students")
                        .param("name", "Bob")
                        .param("email", "bob@example.com")
                        .param("course", "BTech")
                        .param("dateOfJoining", "2026-06-15"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Email already exists")));
    }
}
