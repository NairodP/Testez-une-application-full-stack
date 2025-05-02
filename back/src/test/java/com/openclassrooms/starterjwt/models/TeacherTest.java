package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class TeacherTest {

    @Test
    public void testConstructorAndGetters() {
        // Arrange
        Long id = 1L;
        String lastName = "Doe";
        String firstName = "John";
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();

        // Act
        Teacher teacher = new Teacher();
        teacher.setId(id);
        teacher.setLastName(lastName);
        teacher.setFirstName(firstName);
        teacher.setCreatedAt(createdAt);
        teacher.setUpdatedAt(updatedAt);

        // Assert
        assertEquals(id, teacher.getId());
        assertEquals(lastName, teacher.getLastName());
        assertEquals(firstName, teacher.getFirstName());
        assertEquals(createdAt, teacher.getCreatedAt());
        assertEquals(updatedAt, teacher.getUpdatedAt());
    }

    @Test
    public void testBuilderPattern() {
        // Arrange & Act
        Teacher teacher = Teacher.builder()
                .id(1L)
                .lastName("Doe")
                .firstName("John")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // Assert
        assertNotNull(teacher);
        assertEquals("Doe", teacher.getLastName());
        assertEquals("John", teacher.getFirstName());
    }

    @Test
    public void testEqualsAndHashCode() {
        // Arrange
        Teacher teacher1 = Teacher.builder().id(1L).lastName("Doe").firstName("John").build();
        Teacher teacher2 = Teacher.builder().id(1L).lastName("Smith").firstName("Jane").build();
        Teacher teacher3 = Teacher.builder().id(2L).lastName("Doe").firstName("John").build();

        // Act & Assert
        assertEquals(teacher1, teacher2, "Teachers with the same id should be equal");
        assertNotEquals(teacher1, teacher3, "Teachers with different ids should not be equal");
        assertEquals(teacher1.hashCode(), teacher2.hashCode(), "Teachers with the same id should have the same hashCode");
        assertNotEquals(teacher1.hashCode(), teacher3.hashCode(), "Teachers with different ids should have different hashCodes");
    }

    @Test
    public void testToString() {
        // Arrange
        Teacher teacher = Teacher.builder()
                .id(1L)
                .lastName("Doe")
                .firstName("John")
                .build();

        // Act
        String toString = teacher.toString();

        // Assert
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("lastName=Doe"));
        assertTrue(toString.contains("firstName=John"));
    }

    @Test
    public void testAllArgsConstructor() {
        // Arrange
        Long id = 1L;
        String lastName = "Doe";
        String firstName = "John";
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();

        // Act
        Teacher teacher = new Teacher(id, lastName, firstName, createdAt, updatedAt);

        // Assert
        assertEquals(id, teacher.getId());
        assertEquals(lastName, teacher.getLastName());
        assertEquals(firstName, teacher.getFirstName());
        assertEquals(createdAt, teacher.getCreatedAt());
        assertEquals(updatedAt, teacher.getUpdatedAt());
    }
}