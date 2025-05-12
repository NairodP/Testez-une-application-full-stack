package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

public class TeacherTest {

    @Test
    public void testTeacherConstructor() {
        // Test du constructeur par défaut
        Teacher teacher = new Teacher();
        assertNotNull(teacher);
    }

    @Test
    public void testAllArgsConstructor() {
        // Test du constructeur avec tous les arguments
        Long id = 1L;
        String lastName = "Smith";
        String firstName = "John";
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();
        
        Teacher teacher = new Teacher(id, lastName, firstName, createdAt, updatedAt);
        
        assertEquals(id, teacher.getId());
        assertEquals(lastName, teacher.getLastName());
        assertEquals(firstName, teacher.getFirstName());
        assertEquals(createdAt, teacher.getCreatedAt());
        assertEquals(updatedAt, teacher.getUpdatedAt());
    }

    @Test
    public void testBuilder() {
        // Test du Builder
        Long id = 1L;
        String lastName = "Smith";
        String firstName = "John";
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();
        
        Teacher teacher = Teacher.builder()
                .id(id)
                .lastName(lastName)
                .firstName(firstName)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
        
        assertEquals(id, teacher.getId());
        assertEquals(lastName, teacher.getLastName());
        assertEquals(firstName, teacher.getFirstName());
        assertEquals(createdAt, teacher.getCreatedAt());
        assertEquals(updatedAt, teacher.getUpdatedAt());
    }
    
    @Test
    public void testSettersAndGetters() {
        Teacher teacher = new Teacher();
        
        Long id = 1L;
        teacher.setId(id);
        assertEquals(id, teacher.getId());
        
        String lastName = "Smith";
        teacher.setLastName(lastName);
        assertEquals(lastName, teacher.getLastName());
        
        String firstName = "John";
        teacher.setFirstName(firstName);
        assertEquals(firstName, teacher.getFirstName());
        
        LocalDateTime createdAt = LocalDateTime.now();
        teacher.setCreatedAt(createdAt);
        assertEquals(createdAt, teacher.getCreatedAt());
        
        LocalDateTime updatedAt = LocalDateTime.now();
        teacher.setUpdatedAt(updatedAt);
        assertEquals(updatedAt, teacher.getUpdatedAt());
    }
    
    @Test
    public void testEquals() {
        Teacher teacher1 = Teacher.builder().id(1L).build();
        Teacher teacher2 = Teacher.builder().id(1L).build();
        Teacher teacher3 = Teacher.builder().id(2L).build();
        
        // Les professeurs avec le même ID devraient être égaux
        assertEquals(teacher1, teacher2);
        assertEquals(teacher1.hashCode(), teacher2.hashCode());
        
        // Les professeurs avec des ID différents ne devraient pas être égaux
        assertNotEquals(teacher1, teacher3);
        assertNotEquals(teacher1.hashCode(), teacher3.hashCode());
    }
    
    @Test
    public void testToString() {
        Teacher teacher = Teacher.builder()
                .id(1L)
                .lastName("Smith")
                .firstName("John")
                .build();
        
        String teacherToString = teacher.toString();
        
        // Vérifier que la méthode toString contient les informations importantes
        assertTrue(teacherToString.contains("id=1"));
        assertTrue(teacherToString.contains("lastName=Smith"));
        assertTrue(teacherToString.contains("firstName=John"));
    }
    
    @Test
    public void testChaining() {
        Teacher teacher = new Teacher()
                .setLastName("Smith")
                .setFirstName("John");
        
        assertEquals("Smith", teacher.getLastName());
        assertEquals("John", teacher.getFirstName());
    }
}
