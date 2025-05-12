package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class SessionServiceTest {

    @InjectMocks
    private SessionService sessionService;

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private UserRepository userRepository;

    private Session testSession;
    private User testUser;
    private User testUser2;

    @BeforeEach
    public void setup() {
        // Préparation d'une session de test
        testSession = new Session();
        testSession.setId(1L);
        testSession.setName("Yoga matinal");
        testSession.setDescription("Session de yoga pour bien commencer la journée");
        testSession.setUsers(new ArrayList<>());

        // Préparation d'un utilisateur de test
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("user@test.com");
        testUser.setFirstName("John");
        testUser.setLastName("Doe");
        testUser.setPassword("password");
        testUser.setAdmin(false);

        // Second utilisateur pour les tests
        testUser2 = new User();
        testUser2.setId(2L);
        testUser2.setEmail("user2@test.com");
        testUser2.setFirstName("Jane");
        testUser2.setLastName("Smith");
        testUser2.setPassword("password");
        testUser2.setAdmin(false);
    }

    @Test
    public void testCreateSession() {
        // Arrange
        when(sessionRepository.save(testSession)).thenReturn(testSession);

        // Act
        Session result = sessionService.create(testSession);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Yoga matinal", result.getName());
        assertEquals("Session de yoga pour bien commencer la journée", result.getDescription());

        verify(sessionRepository).save(testSession);
    }

    @Test
    public void testDeleteSession() {
        // Arrange
        Long sessionId = 1L;
        doNothing().when(sessionRepository).deleteById(sessionId);

        // Act
        sessionService.delete(sessionId);

        // Assert
        verify(sessionRepository).deleteById(sessionId);
    }

    @Test
    public void testFindAllSessions() {
        // Arrange
        Session session2 = new Session();
        session2.setId(2L);
        session2.setName("Yoga avancé");
        session2.setDescription("Session de yoga avancé");
        List<Session> sessions = Arrays.asList(testSession, session2);

        when(sessionRepository.findAll()).thenReturn(sessions);

        // Act
        List<Session> result = sessionService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals("Yoga matinal", result.get(0).getName());
        assertEquals(2L, result.get(1).getId());
        assertEquals("Yoga avancé", result.get(1).getName());

        verify(sessionRepository).findAll();
    }

    @Test
    public void testGetSessionById() {
        // Arrange
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(testSession));

        // Act
        Session result = sessionService.getById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Yoga matinal", result.getName());

        verify(sessionRepository).findById(1L);
    }

    @Test
    public void testGetSessionByIdReturnsNullWhenNotFound() {
        // Arrange
        when(sessionRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Session result = sessionService.getById(999L);

        // Assert
        assertNull(result);

        verify(sessionRepository).findById(999L);
    }

    @Test
    public void testUpdateSession() {
        // Arrange
        testSession.setName("Yoga du soir");
        testSession.setDescription("Session de yoga pour bien terminer la journée");

        when(sessionRepository.save(testSession)).thenReturn(testSession);

        // Act
        Session result = sessionService.update(1L, testSession);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Yoga du soir", result.getName());
        assertEquals("Session de yoga pour bien terminer la journée", result.getDescription());

        verify(sessionRepository).save(testSession);
    }

    @Test
    public void testParticipateSuccess() {
        // Arrange
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(testSession));
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(sessionRepository.save(testSession)).thenReturn(testSession);

        // Act
        sessionService.participate(1L, 1L);

        // Assert
        verify(sessionRepository).findById(1L);
        verify(userRepository).findById(1L);
        verify(sessionRepository).save(testSession);
        
        // Vérification que l'utilisateur a bien été ajouté à la session
        assertTrue(testSession.getUsers().contains(testUser));
    }

    @Test
    public void testParticipateThrowsNotFoundExceptionWhenSessionNotFound() {
        // Arrange
        when(sessionRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> sessionService.participate(999L, 1L));
        
        verify(sessionRepository).findById(999L);
        verify(userRepository).findById(1L);
        verify(sessionRepository, never()).save(any(Session.class));
    }

    @Test
    public void testParticipateThrowsNotFoundExceptionWhenUserNotFound() {
        // Arrange
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(testSession));
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> sessionService.participate(1L, 999L));
        
        verify(sessionRepository).findById(1L);
        verify(userRepository).findById(999L);
        verify(sessionRepository, never()).save(any(Session.class));
    }

    @Test
    public void testParticipateThrowsBadRequestExceptionWhenUserAlreadyParticipates() {
        // Arrange
        testSession.getUsers().add(testUser);
        
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(testSession));
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // Act & Assert
        assertThrows(BadRequestException.class, () -> sessionService.participate(1L, 1L));
        
        verify(sessionRepository).findById(1L);
        verify(userRepository).findById(1L);
        verify(sessionRepository, never()).save(any(Session.class));
    }

    @Test
    public void testNoLongerParticipateSuccess() {
        // Arrange
        testSession.getUsers().add(testUser);
        
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(testSession));
        when(sessionRepository.save(testSession)).thenReturn(testSession);

        // Act
        sessionService.noLongerParticipate(1L, 1L);

        // Assert
        verify(sessionRepository).findById(1L);
        verify(sessionRepository).save(testSession);
        
        // Vérification que l'utilisateur a bien été retiré de la session
        assertFalse(testSession.getUsers().contains(testUser));
    }

    @Test
    public void testNoLongerParticipateThrowsNotFoundExceptionWhenSessionNotFound() {
        // Arrange
        when(sessionRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> sessionService.noLongerParticipate(999L, 1L));
        
        verify(sessionRepository).findById(999L);
        verify(sessionRepository, never()).save(any(Session.class));
    }

    @Test
    public void testNoLongerParticipateThrowsBadRequestExceptionWhenUserDoesNotParticipate() {
        // Arrange
        // L'utilisateur n'est pas dans la liste des participants
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(testSession));

        // Act & Assert
        assertThrows(BadRequestException.class, () -> sessionService.noLongerParticipate(1L, 1L));
        
        verify(sessionRepository).findById(1L);
        verify(sessionRepository, never()).save(any(Session.class));
    }
}
