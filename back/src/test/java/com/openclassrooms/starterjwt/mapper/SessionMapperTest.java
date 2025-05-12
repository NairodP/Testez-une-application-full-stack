package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = {"spring.profiles.active=test"})
public class SessionMapperTest {

    @Autowired
    private SessionMapper sessionMapper;

    private Session session;
    private SessionDto sessionDto;
    private Teacher teacher;
    private List<User> users;

    @BeforeEach
    public void setup() {
        // Création d'un enseignant
        teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("John");
        teacher.setLastName("Doe");

        // Création d'utilisateurs
        User user1 = new User();
        user1.setId(1L);
        user1.setEmail("user1@test.com");
        user1.setFirstName("User");
        user1.setLastName("One");
        user1.setPassword("password");
        user1.setAdmin(false);

        User user2 = new User();
        user2.setId(2L);
        user2.setEmail("user2@test.com");
        user2.setFirstName("User");
        user2.setLastName("Two");
        user2.setPassword("password");
        user2.setAdmin(false);

        users = Arrays.asList(user1, user2);

        // Création d'une session
        session = new Session();
        session.setId(1L);
        session.setName("Yoga matinal");
        session.setDescription("Session de yoga pour bien commencer la journée");
        session.setDate(new java.util.Date());
        session.setTeacher(teacher);
        session.setUsers(new ArrayList<>(users));

        // Création d'un DTO de session
        sessionDto = new SessionDto();
        sessionDto.setId(1L);
        sessionDto.setName("Yoga matinal");
        sessionDto.setDescription("Session de yoga pour bien commencer la journée");
        sessionDto.setDate(new java.util.Date());
        sessionDto.setTeacher_id(1L);
        sessionDto.setUsers(Arrays.asList(1L, 2L));
    }

    @Test
    public void testToDto() {
        // Act
        SessionDto result = sessionMapper.toDto(session);

        // Assert
        assertNotNull(result);
        assertEquals(session.getId(), result.getId());
        assertEquals(session.getName(), result.getName());
        assertEquals(session.getDescription(), result.getDescription());
        assertEquals(session.getTeacher().getId(), result.getTeacher_id());
        
        // Vérification des IDs d'utilisateurs
        assertEquals(2, result.getUsers().size());
        assertTrue(result.getUsers().contains(1L));
        assertTrue(result.getUsers().contains(2L));
    }

    @Test
    public void testToDtoWithNullValues() {
        // Arrange
        Session sessionWithNulls = new Session();
        sessionWithNulls.setId(1L);
        sessionWithNulls.setName("Test Session");
        // Pas de description, teacher ou users

        // Act
        SessionDto result = sessionMapper.toDto(sessionWithNulls);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Session", result.getName());
        assertNull(result.getDescription());
        assertNull(result.getTeacher_id());
        assertTrue(result.getUsers().isEmpty());
    }

    @Test
    public void testToDtoList() {
        // Arrange
        Session session2 = new Session();
        session2.setId(2L);
        session2.setName("Yoga avancé");
        session2.setDescription("Session de yoga pour les expérimentés");
        session2.setDate(new java.util.Date());
        session2.setTeacher(teacher);
        session2.setUsers(new ArrayList<>());

        List<Session> sessions = Arrays.asList(session, session2);

        // Act
        List<SessionDto> result = sessionMapper.toDto(sessions);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        
        assertEquals(1L, result.get(0).getId());
        assertEquals("Yoga matinal", result.get(0).getName());
        assertEquals(2, result.get(0).getUsers().size());
        
        assertEquals(2L, result.get(1).getId());
        assertEquals("Yoga avancé", result.get(1).getName());
        assertTrue(result.get(1).getUsers().isEmpty());
    }
}
