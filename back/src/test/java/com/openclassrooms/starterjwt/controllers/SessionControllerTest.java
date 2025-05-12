package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SessionControllerTest {

    @InjectMocks
    private SessionController sessionController;

    @Mock
    private SessionService sessionService;

    @Mock
    private SessionMapper sessionMapper;

    private Session session1;
    private Session session2;
    private SessionDto sessionDto1;
    private SessionDto sessionDto2;
    private List<Session> sessionList;
    private List<SessionDto> sessionDtoList;

    @BeforeEach
    public void setup() {
        // Configuration des sessions de test
        session1 = new Session();
        session1.setId(1L);
        session1.setName("Yoga matinal");
        session1.setDescription("Session de yoga pour bien commencer la journée");
        session1.setDate(new java.util.Date());
        session1.setUsers(new ArrayList<>());

        session2 = new Session();
        session2.setId(2L);
        session2.setName("Yoga avancé");
        session2.setDescription("Session de yoga pour les expérimentés");
        session2.setDate(new java.util.Date());
        session2.setUsers(new ArrayList<>());

        sessionList = Arrays.asList(session1, session2);

        // Configuration des DTOs de session
        sessionDto1 = new SessionDto();
        sessionDto1.setId(1L);
        sessionDto1.setName("Yoga matinal");
        sessionDto1.setDescription("Session de yoga pour bien commencer la journée");
        sessionDto1.setDate(session1.getDate());

        sessionDto2 = new SessionDto();
        sessionDto2.setId(2L);
        sessionDto2.setName("Yoga avancé");
        sessionDto2.setDescription("Session de yoga pour les expérimentés");
        sessionDto2.setDate(session2.getDate());

        sessionDtoList = Arrays.asList(sessionDto1, sessionDto2);
    }

    @Test
    public void testFindByIdReturnsSession() {
        // Arrange
        when(sessionService.getById(1L)).thenReturn(session1);
        when(sessionMapper.toDto(session1)).thenReturn(sessionDto1);

        // Act
        ResponseEntity<?> response = sessionController.findById("1");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sessionDto1, response.getBody());
        verify(sessionService).getById(1L);
        verify(sessionMapper).toDto(session1);
    }

    @Test
    public void testFindByIdReturnsNotFoundWhenSessionNotFound() {
        // Arrange
        when(sessionService.getById(999L)).thenReturn(null);

        // Act
        ResponseEntity<?> response = sessionController.findById("999");

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(sessionService).getById(999L);
        verify(sessionMapper, never()).toDto(any(Session.class));
    }

    @Test
    public void testFindByIdReturnsBadRequestWithInvalidId() {
        // Act
        ResponseEntity<?> response = sessionController.findById("invalid");

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(sessionService, never()).getById(anyLong());
    }

    @Test
    public void testFindAllReturnsAllSessions() {
        // Arrange
        when(sessionService.findAll()).thenReturn(sessionList);
        when(sessionMapper.toDto(sessionList)).thenReturn(sessionDtoList);

        // Act
        ResponseEntity<?> response = sessionController.findAll();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sessionDtoList, response.getBody());
        verify(sessionService).findAll();
        verify(sessionMapper).toDto(sessionList);
    }

    @Test
    public void testCreateSessionSuccess() {
        // Arrange
        when(sessionMapper.toEntity(sessionDto1)).thenReturn(session1);
        when(sessionService.create(session1)).thenReturn(session1);
        when(sessionMapper.toDto(session1)).thenReturn(sessionDto1);

        // Act
        ResponseEntity<?> response = sessionController.create(sessionDto1);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sessionDto1, response.getBody());
        verify(sessionMapper).toEntity(sessionDto1);
        verify(sessionService).create(session1);
        verify(sessionMapper).toDto(session1);
    }

    @Test
    public void testUpdateSessionSuccess() {
        // Arrange
        when(sessionMapper.toEntity(sessionDto1)).thenReturn(session1);
        when(sessionService.update(1L, session1)).thenReturn(session1);
        when(sessionMapper.toDto(session1)).thenReturn(sessionDto1);

        // Act
        ResponseEntity<?> response = sessionController.update("1", sessionDto1);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sessionDto1, response.getBody());
        verify(sessionMapper).toEntity(sessionDto1);
        verify(sessionService).update(1L, session1);
        verify(sessionMapper).toDto(session1);
    }

    @Test
    public void testUpdateSessionReturnsBadRequestWithInvalidId() {
        // Act
        ResponseEntity<?> response = sessionController.update("invalid", sessionDto1);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(sessionMapper, never()).toEntity(any(SessionDto.class));
        verify(sessionService, never()).update(anyLong(), any(Session.class));
    }

    @Test
    public void testDeleteSessionSuccess() {
        // Arrange
        when(sessionService.getById(1L)).thenReturn(session1);
        doNothing().when(sessionService).delete(1L);

        // Act
        ResponseEntity<?> response = sessionController.save("1");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(sessionService).getById(1L);
        verify(sessionService).delete(1L);
    }

    @Test
    public void testDeleteSessionReturnsNotFoundWhenSessionNotFound() {
        // Arrange
        when(sessionService.getById(999L)).thenReturn(null);

        // Act
        ResponseEntity<?> response = sessionController.save("999");

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(sessionService).getById(999L);
        verify(sessionService, never()).delete(anyLong());
    }

    @Test
    public void testDeleteSessionReturnsBadRequestWithInvalidId() {
        // Act
        ResponseEntity<?> response = sessionController.save("invalid");

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(sessionService, never()).getById(anyLong());
        verify(sessionService, never()).delete(anyLong());
    }

    @Test
    public void testParticipateSuccess() {
        // Arrange
        doNothing().when(sessionService).participate(1L, 1L);

        // Act
        ResponseEntity<?> response = sessionController.participate("1", "1");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(sessionService).participate(1L, 1L);
    }

    @Test
    public void testParticipateBadRequestWithInvalidId() {
        // Act
        ResponseEntity<?> response = sessionController.participate("invalid", "1");

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(sessionService, never()).participate(anyLong(), anyLong());
    }

    @Test
    public void testNoLongerParticipateSuccess() {
        // Arrange
        doNothing().when(sessionService).noLongerParticipate(1L, 1L);

        // Act
        ResponseEntity<?> response = sessionController.noLongerParticipate("1", "1");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(sessionService).noLongerParticipate(1L, 1L);
    }

    @Test
    public void testNoLongerParticipateBadRequestWithInvalidId() {
        // Act
        ResponseEntity<?> response = sessionController.noLongerParticipate("invalid", "1");

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(sessionService, never()).noLongerParticipate(anyLong(), anyLong());
    }
}
