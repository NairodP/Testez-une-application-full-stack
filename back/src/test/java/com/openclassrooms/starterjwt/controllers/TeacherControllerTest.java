package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TeacherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TeacherService teacherService;

    @MockBean
    private TeacherMapper teacherMapper;

    private Teacher testTeacher;
    private TeacherDto testTeacherDto;
    private List<Teacher> testTeachers;
    private List<TeacherDto> testTeacherDtos;

    @BeforeEach
    public void setup() {
        LocalDateTime now = LocalDateTime.now();
        
        // Configuration de l'enseignant de test
        testTeacher = new Teacher();
        testTeacher.setId(1L);
        testTeacher.setFirstName("John");
        testTeacher.setLastName("Doe");
        testTeacher.setCreatedAt(now);
        testTeacher.setUpdatedAt(now);

        // Configuration du DTO
        testTeacherDto = new TeacherDto();
        testTeacherDto.setId(1L);
        testTeacherDto.setFirstName("John");
        testTeacherDto.setLastName("Doe");
        testTeacherDto.setCreatedAt(now);
        testTeacherDto.setUpdatedAt(now);

        // Création d'un autre enseignant pour la liste
        Teacher teacher2 = new Teacher();
        teacher2.setId(2L);
        teacher2.setFirstName("Jane");
        teacher2.setLastName("Smith");
        teacher2.setCreatedAt(now);
        teacher2.setUpdatedAt(now);

        TeacherDto teacherDto2 = new TeacherDto();
        teacherDto2.setId(2L);
        teacherDto2.setFirstName("Jane");
        teacherDto2.setLastName("Smith");
        teacherDto2.setCreatedAt(now);
        teacherDto2.setUpdatedAt(now);

        // Création des listes pour findAll
        testTeachers = Arrays.asList(testTeacher, teacher2);
        testTeacherDtos = Arrays.asList(testTeacherDto, teacherDto2);
    }

    @Test
    public void testFindByIdSuccess() throws Exception {
        // Arrange
        when(teacherService.findById(1L)).thenReturn(testTeacher);
        when(teacherMapper.toDto(testTeacher)).thenReturn(testTeacherDto);

        // Act & Assert
        mockMvc.perform(get("/api/teacher/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.lastName", is("Doe")));

        verify(teacherService).findById(1L);
        verify(teacherMapper).toDto(testTeacher);
    }

    @Test
    public void testFindByIdNotFound() throws Exception {
        // Arrange
        when(teacherService.findById(999L)).thenReturn(null);

        // Act & Assert
        mockMvc.perform(get("/api/teacher/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(teacherService).findById(999L);
        verify(teacherMapper, never()).toDto(any(Teacher.class));
    }

    @Test
    public void testFindByIdBadRequest() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/teacher/invalid")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(teacherService, never()).findById(anyLong());
        verify(teacherMapper, never()).toDto(any(Teacher.class));
    }

    @Test
    public void testFindAll() throws Exception {
        // Arrange
        when(teacherService.findAll()).thenReturn(testTeachers);
        when(teacherMapper.toDto(testTeachers)).thenReturn(testTeacherDtos);

        // Act & Assert
        mockMvc.perform(get("/api/teacher")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].firstName", is("John")))
                .andExpect(jsonPath("$[0].lastName", is("Doe")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].firstName", is("Jane")))
                .andExpect(jsonPath("$[1].lastName", is("Smith")));

        verify(teacherService).findAll();
        verify(teacherMapper).toDto(testTeachers);
    }
}