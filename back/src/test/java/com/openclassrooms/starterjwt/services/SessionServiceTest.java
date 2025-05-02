package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SessionServiceTest {

    @InjectMocks
    private SessionService sessionService;

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private UserRepository userRepository;

    private Session session1;
    private Session session2;
    private User user;
    private Teacher teacher;

    @BeforeEach
    public void setUp() {
        // Configuration du user
        user = new User();
        user.setId(1L);
        user.setEmail("user@test.com");
        user.setLastName("User");
        user.setFirstName("Test");
        user.setPassword("password");
        user.setAdmin(false);
        
        // Configuration du teacher
        teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("Teacher");
        teacher.setLastName("Test");
        
        // Configuration de la session 1
        session1 = new Session();
        session1.setId(1L);
        session1.setName("Session Yoga");
        session1.setDate(new Date()); // Utiliser Date au lieu de LocalDateTime
        session1.setDescription("Description de la session yoga");
        session1.setTeacher(teacher); // Utiliser un objet Teacher au lieu de setTeacher_id
        session1.setUsers(new ArrayList<>());
        session1.setCreatedAt(LocalDateTime.now());
        session1.setUpdatedAt(LocalDateTime.now());

        // Configuration de la session 2
        session2 = new Session();
        session2.setId(2L);
        session2.setName("Session Méditation");
        session2.setDate(new Date()); // Utiliser Date au lieu de LocalDateTime
        session2.setDescription("Description de la session méditation");
        session2.setTeacher(teacher); // Utiliser un objet Teacher au lieu de setTeacher_id
        session2.setUsers(new ArrayList<>());
        session2.setCreatedAt(LocalDateTime.now());
        session2.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    public void testCreate() {
        // Arrange
        when(sessionRepository.save(any(Session.class))).thenReturn(session1);

        // Act
        Session createdSession = sessionService.create(session1);

        // Assert
        assertNotNull(createdSession);
        assertEquals("Session Yoga", createdSession.getName());
        verify(sessionRepository, times(1)).save(session1);
    }

    @Test
    public void testDelete() {
        // Act
        sessionService.delete(1L);

        // Assert
        verify(sessionRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testFindAll() {
        // Arrange
        List<Session> expectedSessions = Arrays.asList(session1, session2);
        when(sessionRepository.findAll()).thenReturn(expectedSessions);

        // Act
        List<Session> actualSessions = sessionService.findAll();

        // Assert
        assertEquals(2, actualSessions.size());
        assertEquals("Session Yoga", actualSessions.get(0).getName());
        assertEquals("Session Méditation", actualSessions.get(1).getName());
        verify(sessionRepository, times(1)).findAll();
    }

    @Test
    public void testGetById_ExistingSession() {
        // Arrange
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session1));

        // Act
        Session foundSession = sessionService.getById(1L);

        // Assert
        assertNotNull(foundSession);
        assertEquals(1L, foundSession.getId());
        assertEquals("Session Yoga", foundSession.getName());
        verify(sessionRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetById_NonExistingSession() {
        // Arrange
        when(sessionRepository.findById(3L)).thenReturn(Optional.empty());

        // Act
        Session foundSession = sessionService.getById(3L);

        // Assert
        assertNull(foundSession);
        verify(sessionRepository, times(1)).findById(3L);
    }

    @Test
    public void testUpdate() {
        // Arrange
        Session updatedSession = session1;
        updatedSession.setName("Session Yoga Updated");
        when(sessionRepository.save(any(Session.class))).thenReturn(updatedSession);

        // Act
        Session result = sessionService.update(1L, updatedSession);

        // Assert
        assertNotNull(result);
        assertEquals("Session Yoga Updated", result.getName());
        assertEquals(1L, result.getId());
        verify(sessionRepository, times(1)).save(updatedSession);
    }

    @Test
    public void testParticipate() {
        // Arrange
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session1));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        
        // Act
        sessionService.participate(1L, 1L);
        
        // Assert
        verify(sessionRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(1L);
        verify(sessionRepository, times(1)).save(session1);
        assertTrue(session1.getUsers().contains(user));
    }

    @Test
    public void testParticipate_SessionNotFound() {
        // Arrange
        when(sessionRepository.findById(3L)).thenReturn(Optional.empty());
        // Puisque le code appelle userRepository.findById même si la session n'est pas trouvée
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        
        // Act & Assert
        assertThrows(NotFoundException.class, () -> sessionService.participate(3L, 1L));
        verify(sessionRepository, times(1)).findById(3L);
        // Ne vérifions pas si userRepository.findById est appelé ou non, car cela dépend de l'implémentation
    }

    @Test
    public void testParticipate_UserNotFound() {
        // Arrange
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session1));
        when(userRepository.findById(3L)).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(NotFoundException.class, () -> sessionService.participate(1L, 3L));
        verify(sessionRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(3L);
    }
    
    @Test
    public void testParticipate_UserAlreadyParticipates() {
        // Arrange
        session1.getUsers().add(user); // Ajout préalable de l'utilisateur
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session1));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        
        // Act & Assert
        assertThrows(BadRequestException.class, () -> sessionService.participate(1L, 1L));
        verify(sessionRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    public void testNoLongerParticipate() {
        // Arrange
        session1.getUsers().add(user); // Ajouter l'utilisateur à la session
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session1));
        
        // Act
        sessionService.noLongerParticipate(1L, 1L);
        
        // Assert
        verify(sessionRepository, times(1)).findById(1L);
        // Remarque: Dans l'implémentation réelle, userRepository.findById n'est pas appelé
        verify(sessionRepository, times(1)).save(session1);
        assertFalse(session1.getUsers().contains(user));
    }

    @Test
    public void testNoLongerParticipate_SessionNotFound() {
        // Arrange
        when(sessionRepository.findById(3L)).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(NotFoundException.class, () -> sessionService.noLongerParticipate(3L, 1L));
        verify(sessionRepository, times(1)).findById(3L);
    }

    @Test
    public void testNoLongerParticipate_UserNotParticipating() {
        // Arrange - l'utilisateur ne participe pas à la session
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session1));
        
        // Act & Assert
        assertThrows(BadRequestException.class, () -> sessionService.noLongerParticipate(1L, 1L));
        verify(sessionRepository, times(1)).findById(1L);
    }
}