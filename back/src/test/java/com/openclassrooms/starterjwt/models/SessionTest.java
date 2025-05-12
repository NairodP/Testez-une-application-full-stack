package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SessionTest {

    @Test
    public void testSessionConstructor() {
        // Test du constructeur par défaut
        Session session = new Session();
        assertNotNull(session);
    }

    @Test
    public void testAllArgsConstructor() {
        // Test du constructeur avec tous les arguments
        Long id = 1L;
        String name = "Yoga Session";
        Date date = new Date();
        String description = "A relaxing yoga session";
        Teacher teacher = new Teacher();
        List<User> users = new ArrayList<>();
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();
        
        Session session = new Session(id, name, date, description, teacher, users, createdAt, updatedAt);
        
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
    public void testBuilder() {
        // Test du Builder
        Long id = 1L;
        String name = "Yoga Session";
        Date date = new Date();
        String description = "A relaxing yoga session";
        Teacher teacher = new Teacher();
        List<User> users = new ArrayList<>();
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();
        
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
    public void testSettersAndGetters() {
        Session session = new Session();
        
        Long id = 1L;
        session.setId(id);
        assertEquals(id, session.getId());
        
        String name = "Yoga Session";
        session.setName(name);
        assertEquals(name, session.getName());
        
        Date date = new Date();
        session.setDate(date);
        assertEquals(date, session.getDate());
        
        String description = "A relaxing yoga session";
        session.setDescription(description);
        assertEquals(description, session.getDescription());
        
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        session.setTeacher(teacher);
        assertEquals(teacher, session.getTeacher());
        
        List<User> users = new ArrayList<>();
        User user = new User();
        user.setId(1L);
        users.add(user);
        session.setUsers(users);
        assertEquals(users, session.getUsers());
        
        LocalDateTime createdAt = LocalDateTime.now();
        session.setCreatedAt(createdAt);
        assertEquals(createdAt, session.getCreatedAt());
        
        LocalDateTime updatedAt = LocalDateTime.now();
        session.setUpdatedAt(updatedAt);
        assertEquals(updatedAt, session.getUpdatedAt());
    }
    
    @Test
    public void testEquals() {
        Session session1 = Session.builder().id(1L).build();
        Session session2 = Session.builder().id(1L).build();
        Session session3 = Session.builder().id(2L).build();
        
        // Les sessions avec le même ID devraient être égales
        assertEquals(session1, session2);
        assertEquals(session1.hashCode(), session2.hashCode());
        
        // Les sessions avec des ID différents ne devraient pas être égales
        assertNotEquals(session1, session3);
        assertNotEquals(session1.hashCode(), session3.hashCode());
    }
    
    @Test
    public void testToString() {
        Session session = Session.builder()
                .id(1L)
                .name("Yoga Session")
                .description("A relaxing yoga session")
                .build();
        
        String sessionToString = session.toString();
        
        // Vérifier que la méthode toString contient les informations importantes
        assertTrue(sessionToString.contains("id=1"));
        assertTrue(sessionToString.contains("name=Yoga Session"));
        assertTrue(sessionToString.contains("description=A relaxing yoga session"));
    }
    
    @Test
    public void testChaining() {
        Date date = new Date();
        Teacher teacher = new Teacher();
        List<User> users = new ArrayList<>();
        
        Session session = new Session()
                .setName("Yoga Session")
                .setDate(date)
                .setDescription("A relaxing yoga session")
                .setTeacher(teacher)
                .setUsers(users);
        
        assertEquals("Yoga Session", session.getName());
        assertEquals(date, session.getDate());
        assertEquals("A relaxing yoga session", session.getDescription());
        assertEquals(teacher, session.getTeacher());
        assertEquals(users, session.getUsers());
    }
    
    @Test
    public void testUsersManagement() {
        Session session = new Session();
        List<User> users = new ArrayList<>();
        
        // Ajouter des utilisateurs
        User user1 = new User();
        user1.setId(1L);
        user1.setEmail("user1@example.com");
        
        User user2 = new User();
        user2.setId(2L);
        user2.setEmail("user2@example.com");
        
        users.add(user1);
        users.add(user2);
        
        session.setUsers(users);
        
        // Vérifier que la liste contient bien les deux utilisateurs
        assertEquals(2, session.getUsers().size());
        assertTrue(session.getUsers().contains(user1));
        assertTrue(session.getUsers().contains(user2));
        
        // Vérifier qu'on peut modifier la liste
        users = new ArrayList<>();
        User user3 = new User();
        user3.setId(3L);
        user3.setEmail("user3@example.com");
        users.add(user3);
        
        session.setUsers(users);
        
        assertEquals(1, session.getUsers().size());
        assertTrue(session.getUsers().contains(user3));
        assertFalse(session.getUsers().contains(user1));
    }
}
