package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SessionTest {

    @Test
    public void testConstructorAndGetters() {
        // Arrange
        Long id = 1L;
        String name = "Yoga Session";
        Date date = new Date();
        String description = "A yoga session for beginners";
        Teacher teacher = new Teacher();
        List<User> users = new ArrayList<>();
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();

        // Act
        Session session = new Session();
        session.setId(id);
        session.setName(name);
        session.setDate(date);
        session.setDescription(description);
        session.setTeacher(teacher);
        session.setUsers(users);
        session.setCreatedAt(createdAt);
        session.setUpdatedAt(updatedAt);

        // Assert
        assertEquals(id, session.getId());
        assertEquals(name, session.getName());
        assertEquals(date, session.getDate());
        assertEquals(description, session.getDescription());
        assertEquals(teacher, session.getTeacher());
        assertEquals(users, session.getUsers());
        assertEquals(createdAt, session.getCreatedAt());
        assertEquals(updatedAt, session.getUpdatedAt());
    }

    @Test
    public void testBuilderPattern() {
        // Arrange
        Long id = 1L;
        String name = "Yoga Session";
        Date date = new Date();
        String description = "A yoga session for beginners";
        Teacher teacher = new Teacher();
        List<User> users = new ArrayList<>();
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();

        // Act
        Session session = Session.builder()
                .id(id)
                .name(name)
                .date(date)
                .description(description)
                .teacher(teacher)
                .users(users)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

        // Assert
        assertEquals(id, session.getId());
        assertEquals(name, session.getName());
        assertEquals(date, session.getDate());
        assertEquals(description, session.getDescription());
        assertEquals(teacher, session.getTeacher());
        assertEquals(users, session.getUsers());
        assertEquals(createdAt, session.getCreatedAt());
        assertEquals(updatedAt, session.getUpdatedAt());
    }

    @Test
    public void testEqualsAndHashCode() {
        // Arrange
        Date date = new Date();
        Teacher teacher = new Teacher();
        List<User> users = new ArrayList<>();
        
        Session session1 = Session.builder().id(1L).name("Session 1").date(date).description("Description 1").teacher(teacher).users(users).build();
        Session session2 = Session.builder().id(1L).name("Different Name").date(new Date(date.getTime() + 1000)).description("Different Description").teacher(new Teacher()).users(new ArrayList<>()).build();
        Session session3 = Session.builder().id(2L).name("Session 1").date(date).description("Description 1").teacher(teacher).users(users).build();

        // Act & Assert
        assertEquals(session1, session2, "Sessions with the same id should be equal");
        assertNotEquals(session1, session3, "Sessions with different ids should not be equal");
        assertEquals(session1.hashCode(), session2.hashCode(), "Sessions with the same id should have the same hashCode");
        assertNotEquals(session1.hashCode(), session3.hashCode(), "Sessions with different ids should have different hashCodes");
    }

    @Test
    public void testToString() {
        // Arrange
        Session session = Session.builder()
                .id(1L)
                .name("Yoga Session")
                .date(new Date())
                .description("A yoga session for beginners")
                .build();

        // Act
        String toString = session.toString();

        // Assert
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("name=Yoga Session"));
        assertTrue(toString.contains("description=A yoga session for beginners"));
    }

    @Test
    public void testAllArgsConstructor() {
        // Arrange
        Long id = 1L;
        String name = "Yoga Session";
        Date date = new Date();
        String description = "A yoga session for beginners";
        Teacher teacher = new Teacher();
        List<User> users = new ArrayList<>();
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();

        // Act
        Session session = new Session(id, name, date, description, teacher, users, createdAt, updatedAt);

        // Assert
        assertEquals(id, session.getId());
        assertEquals(name, session.getName());
        assertEquals(date, session.getDate());
        assertEquals(description, session.getDescription());
        assertEquals(teacher, session.getTeacher());
        assertEquals(users, session.getUsers());
        assertEquals(createdAt, session.getCreatedAt());
        assertEquals(updatedAt, session.getUpdatedAt());
    }
    
    @Test
    public void testChainedSetters() {
        // Arrange
        Long id = 1L;
        String name = "Yoga Session";
        Date date = new Date();
        String description = "A yoga session for beginners";
        Teacher teacher = new Teacher();
        List<User> users = new ArrayList<>();
        
        // Act
        Session session = new Session()
            .setId(id)
            .setName(name)
            .setDate(date)
            .setDescription(description)
            .setTeacher(teacher)
            .setUsers(users);
        
        // Assert
        assertEquals(id, session.getId());
        assertEquals(name, session.getName());
        assertEquals(date, session.getDate());
        assertEquals(description, session.getDescription());
        assertEquals(teacher, session.getTeacher());
        assertEquals(users, session.getUsers());
    }
}