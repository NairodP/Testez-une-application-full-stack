package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TeacherControllerTest {

    @InjectMocks
    private TeacherController teacherController;

    @Mock
    private TeacherService teacherService;

    @Mock
    private TeacherMapper teacherMapper;

    private Teacher teacher1;
    private Teacher teacher2;
    private TeacherDto teacherDto1;
    private TeacherDto teacherDto2;
    private List<Teacher> teacherList;
    private List<TeacherDto> teacherDtoList;

    @BeforeEach
    public void setup() {
        // Configuration des enseignants de test
        teacher1 = new Teacher();
        teacher1.setId(1L);
        teacher1.setFirstName("John");
        teacher1.setLastName("Doe");

        teacher2 = new Teacher();
        teacher2.setId(2L);
        teacher2.setFirstName("Jane");
        teacher2.setLastName("Smith");

        teacherList = Arrays.asList(teacher1, teacher2);

        // Configuration des DTOs d'enseignant
        teacherDto1 = new TeacherDto();
        teacherDto1.setId(1L);
        teacherDto1.setFirstName("John");
        teacherDto1.setLastName("Doe");

        teacherDto2 = new TeacherDto();
        teacherDto2.setId(2L);
        teacherDto2.setFirstName("Jane");
        teacherDto2.setLastName("Smith");

        teacherDtoList = Arrays.asList(teacherDto1, teacherDto2);
    }

    @Test
    public void testFindByIdReturnsTeacher() {
        // Arrange
        when(teacherService.findById(1L)).thenReturn(teacher1);
        when(teacherMapper.toDto(teacher1)).thenReturn(teacherDto1);

        // Act
        ResponseEntity<?> response = teacherController.findById("1");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(teacherDto1, response.getBody());
        verify(teacherService).findById(1L);
        verify(teacherMapper).toDto(teacher1);
    }

    @Test
    public void testFindByIdReturnsNotFoundWhenTeacherNotFound() {
        // Arrange
        when(teacherService.findById(999L)).thenReturn(null);

        // Act
        ResponseEntity<?> response = teacherController.findById("999");

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(teacherService).findById(999L);
        verify(teacherMapper, never()).toDto(any(Teacher.class));
    }

    @Test
    public void testFindByIdReturnsBadRequestWithInvalidId() {
        // Act
        ResponseEntity<?> response = teacherController.findById("invalid");

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(teacherService, never()).findById(anyLong());
    }

    @Test
    public void testFindAllReturnsAllTeachers() {
        // Arrange
        when(teacherService.findAll()).thenReturn(teacherList);
        when(teacherMapper.toDto(teacherList)).thenReturn(teacherDtoList);

        // Act
        ResponseEntity<?> response = teacherController.findAll();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(teacherDtoList, response.getBody());
        verify(teacherService).findAll();
        verify(teacherMapper).toDto(teacherList);
    }

    @Test
    public void testFindAllReturnsEmptyListWhenNoTeachers() {
        // Arrange
        when(teacherService.findAll()).thenReturn(List.of());
        when(teacherMapper.toDto(List.of())).thenReturn(List.of());

        // Act
        ResponseEntity<?> response = teacherController.findAll();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(List.of(), response.getBody());
        verify(teacherService).findAll();
        verify(teacherMapper).toDto(List.of());
    }
}
