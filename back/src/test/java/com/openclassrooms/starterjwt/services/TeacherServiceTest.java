package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TeacherServiceTest {

    @InjectMocks
    private TeacherService teacherService;

    @Mock
    private TeacherRepository teacherRepository;

    private Teacher teacher1;
    private Teacher teacher2;

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
    }

    @Test
    public void testFindAll() {
        // Arrange
        when(teacherRepository.findAll()).thenReturn(Arrays.asList(teacher1, teacher2));

        // Act
        List<Teacher> result = teacherService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals("Doe", result.get(0).getLastName());
        assertEquals(2L, result.get(1).getId());
        assertEquals("Jane", result.get(1).getFirstName());
        assertEquals("Smith", result.get(1).getLastName());

        verify(teacherRepository).findAll();
    }

    @Test
    public void testFindAllReturnsEmptyList() {
        // Arrange
        when(teacherRepository.findAll()).thenReturn(List.of());

        // Act
        List<Teacher> result = teacherService.findAll();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(teacherRepository).findAll();
    }

    @Test
    public void testFindByIdReturnsTeacher() {
        // Arrange
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher1));

        // Act
        Teacher result = teacherService.findById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());

        verify(teacherRepository).findById(1L);
    }

    @Test
    public void testFindByIdReturnsNullWhenNotFound() {
        // Arrange
        when(teacherRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Teacher result = teacherService.findById(999L);

        // Assert
        assertNull(result);
        verify(teacherRepository).findById(999L);
    }
}
