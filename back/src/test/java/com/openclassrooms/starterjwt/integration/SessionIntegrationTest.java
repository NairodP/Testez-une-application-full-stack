package com.openclassrooms.starterjwt.integration;

import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class SessionIntegrationTest {

    @Autowired
    private SessionService sessionService;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private TeacherRepository teacherRepository;

    private Session session1;
    private Session session2;
    private User user1;
    private User user2;
    private Teacher teacher;

    @BeforeEach
    public void setUp() {
        sessionRepository.deleteAll();
        userRepository.deleteAll();
        teacherRepository.deleteAll();

        // Création d'un enseignant
        teacher = new Teacher();
        teacher.setFirstName("John");
        teacher.setLastName("Doe");
        teacher.setCreatedAt(LocalDateTime.now());
        teacher.setUpdatedAt(LocalDateTime.now());
        teacherRepository.save(teacher);

        // Création des utilisateurs
        user1 = new User();
        user1.setEmail("user1@test.com");
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setPassword("password");
        userRepository.save(user1);

        user2 = new User();
        user2.setEmail("user2@test.com");
        user2.setFirstName("Jane");
        user2.setLastName("Smith");
        user2.setPassword("password");
        userRepository.save(user2);

        // Création des sessions
        session1 = new Session();
        session1.setName("Session 1");
        session1.setDescription("Description 1");
        session1.setTeacher(teacher);
        session1.setUsers(new ArrayList<>());
        session1.setDate(new Date());
        session1.setCreatedAt(LocalDateTime.now());
        session1.setUpdatedAt(LocalDateTime.now());
        sessionRepository.save(session1);

        session2 = new Session();
        session2.setName("Session 2");
        session2.setDescription("Description 2");
        session2.setTeacher(teacher);
        session2.setUsers(new ArrayList<>(Arrays.asList(user1)));
        session2.setDate(new Date());
        session2.setCreatedAt(LocalDateTime.now());
        session2.setUpdatedAt(LocalDateTime.now());
        sessionRepository.save(session2);
    }

    @Test
    public void testCreate() {
        // Arrange
        Session newSession = new Session();
        newSession.setName("New Session");
        newSession.setDescription("New Description");
        newSession.setTeacher(teacher);
        newSession.setUsers(new ArrayList<>());
        newSession.setDate(new Date());

        // Act
        Session createdSession = sessionService.create(newSession);

        // Assert
        assertNotNull(createdSession);
        assertNotNull(createdSession.getId());
        assertEquals("New Session", createdSession.getName());

        // Verify in database
        Session fromDb = sessionRepository.findById(createdSession.getId()).orElse(null);
        assertNotNull(fromDb);
        assertEquals("New Session", fromDb.getName());
    }

    @Test
    public void testFindAll() {
        // Act
        List<Session> sessions = sessionService.findAll();

        // Assert
        assertEquals(2, sessions.size());
        assertTrue(sessions.stream().anyMatch(s -> s.getName().equals("Session 1")));
        assertTrue(sessions.stream().anyMatch(s -> s.getName().equals("Session 2")));
    }

    @Test
    public void testGetById() {
        // Arrange
        Long id = sessionRepository.findAll().get(0).getId();

        // Act
        Session foundSession = sessionService.getById(id);

        // Assert
        assertNotNull(foundSession);
        assertEquals(id, foundSession.getId());
    }

    @Test
    public void testUpdate() {
        // Arrange
        Session sessionToUpdate = sessionRepository.findAll().get(0);
        sessionToUpdate.setName("Updated Session");
        sessionToUpdate.setDescription("Updated Description");

        // Act
        Session updatedSession = sessionService.update(sessionToUpdate.getId(), sessionToUpdate);

        // Assert
        assertEquals("Updated Session", updatedSession.getName());
        assertEquals("Updated Description", updatedSession.getDescription());

        // Verify in database
        Session fromDb = sessionRepository.findById(sessionToUpdate.getId()).orElse(null);
        assertNotNull(fromDb);
        assertEquals("Updated Session", fromDb.getName());
    }

    @Test
    public void testDelete() {
        // Arrange
        Long id = sessionRepository.findAll().get(0).getId();

        // Act
        sessionService.delete(id);

        // Assert
        assertFalse(sessionRepository.existsById(id));
    }

    @Test
    public void testParticipate() {
        // Arrange
        Long sessionId = session1.getId();
        Long userId = user2.getId();

        // Act
        sessionService.participate(sessionId, userId);

        // Assert
        Session updatedSession = sessionRepository.findById(sessionId).orElse(null);
        assertNotNull(updatedSession);
        assertTrue(updatedSession.getUsers().stream().anyMatch(u -> u.getId().equals(userId)));
    }

    @Test
    public void testNoLongerParticipate() {
        // Arrange
        Long sessionId = session2.getId();
        Long userId = user1.getId();

        // Act
        sessionService.noLongerParticipate(sessionId, userId);

        // Assert
        Session updatedSession = sessionRepository.findById(sessionId).orElse(null);
        assertNotNull(updatedSession);
        assertFalse(updatedSession.getUsers().stream().anyMatch(u -> u.getId().equals(userId)));
    }
}