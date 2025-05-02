package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TeacherServiceTest {

    @InjectMocks
    private TeacherService teacherService;

    @Mock
    private TeacherRepository teacherRepository;

    private Teacher teacher1;
    private Teacher teacher2;

    @BeforeEach
    public void setUp() {
        teacher1 = new Teacher();
        teacher1.setId(1L);
        teacher1.setFirstName("Margot");
        teacher1.setLastName("DELAHAYE");
        teacher1.setCreatedAt(LocalDateTime.now());
        teacher1.setUpdatedAt(LocalDateTime.now());

        teacher2 = new Teacher();
        teacher2.setId(2L);
        teacher2.setFirstName("Hélène");
        teacher2.setLastName("THIERCELIN");
        teacher2.setCreatedAt(LocalDateTime.now());
        teacher2.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    public void testFindAll() {
        // Arrange
        List<Teacher> expectedTeachers = Arrays.asList(teacher1, teacher2);
        when(teacherRepository.findAll()).thenReturn(expectedTeachers);

        // Act
        List<Teacher> actualTeachers = teacherService.findAll();

        // Assert
        assertEquals(2, actualTeachers.size());
        assertEquals("Margot", actualTeachers.get(0).getFirstName());
        assertEquals("Hélène", actualTeachers.get(1).getFirstName());
        verify(teacherRepository, times(1)).findAll();
    }

    @Test
    public void testFindById_ExistingTeacher() {
        // Arrange
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher1));

        // Act
        Teacher foundTeacher = teacherService.findById(1L);

        // Assert
        assertNotNull(foundTeacher);
        assertEquals(1L, foundTeacher.getId());
        assertEquals("Margot", foundTeacher.getFirstName());
        verify(teacherRepository, times(1)).findById(1L);
    }

    @Test
    public void testFindById_NonExistingTeacher() {
        // Arrange
        when(teacherRepository.findById(3L)).thenReturn(Optional.empty());

        // Act
        Teacher foundTeacher = teacherService.findById(3L);

        // Assert
        assertNull(foundTeacher);
        verify(teacherRepository, times(1)).findById(3L);
    }
}