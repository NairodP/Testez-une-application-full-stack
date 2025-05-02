package com.openclassrooms.starterjwt.integration;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class TeacherIntegrationTest {

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private TeacherRepository teacherRepository;
    
    @Autowired
    private SessionRepository sessionRepository;

    private Teacher teacher1;
    private Teacher teacher2;

    @BeforeEach
    public void setUp() {
        // Supprimer d'abord les sessions pour éviter les violations de contraintes de clé étrangère
        sessionRepository.deleteAll();
        teacherRepository.deleteAll();

        // Création des enseignants
        teacher1 = new Teacher();
        teacher1.setFirstName("Margot");
        teacher1.setLastName("DELAHAYE");
        teacher1.setCreatedAt(LocalDateTime.now());
        teacher1.setUpdatedAt(LocalDateTime.now());
        teacherRepository.save(teacher1);

        teacher2 = new Teacher();
        teacher2.setFirstName("Hélène");
        teacher2.setLastName("THIERCELIN");
        teacher2.setCreatedAt(LocalDateTime.now());
        teacher2.setUpdatedAt(LocalDateTime.now());
        teacherRepository.save(teacher2);
    }

    @Test
    public void testFindAll() {
        // Act
        List<Teacher> teachers = teacherService.findAll();

        // Assert
        assertEquals(2, teachers.size());
        assertTrue(teachers.stream().anyMatch(t -> t.getFirstName().equals("Margot")));
        assertTrue(teachers.stream().anyMatch(t -> t.getFirstName().equals("Hélène")));
    }

    @Test
    public void testFindById_ExistingTeacher() {
        // Arrange
        Long id = teacher1.getId();

        // Act
        Teacher foundTeacher = teacherService.findById(id);

        // Assert
        assertNotNull(foundTeacher);
        assertEquals(id, foundTeacher.getId());
        assertEquals("Margot", foundTeacher.getFirstName());
    }

    @Test
    public void testFindById_NonExistingTeacher() {
        // Act
        Teacher foundTeacher = teacherService.findById(999L);

        // Assert
        assertNull(foundTeacher);
    }
}