package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SessionMapperTest {

    @Mock
    private TeacherService teacherService;

    @Mock
    private UserService userService;

    @Spy
    @InjectMocks
    private SessionMapperImpl sessionMapper;

    private Teacher teacher;
    private User user1;
    private User user2;
    private Date sessionDate;
    private LocalDateTime now;
    
    @BeforeEach
    public void setup() {
        now = LocalDateTime.now();
        sessionDate = new Date();
        
        teacher = Teacher.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .createdAt(now)
                .updatedAt(now)
                .build();
        
        user1 = User.builder()
                .id(1L)
                .email("user1@test.com")
                .firstName("User")
                .lastName("One")
                .password("password1")
                .admin(false)
                .build();
                
        user2 = User.builder()
                .id(2L)
                .email("user2@test.com")
                .firstName("User")
                .lastName("Two")
                .password("password2")
                .admin(false)
                .build();
        
        // Configurer les mocks pour teacherService et userService
        when(teacherService.findById(1L)).thenReturn(teacher);
        when(userService.findById(1L)).thenReturn(user1);
        when(userService.findById(2L)).thenReturn(user2);
    }

    @Test
    public void shouldMapSessionToSessionDto() {
        // Arrange
        List<User> userList = Arrays.asList(user1, user2);
        
        Session session = Session.builder()
                .id(1L)
                .name("Yoga Session")
                .date(sessionDate)
                .description("Yoga session description")
                .teacher(teacher)
                .users(userList)
                .createdAt(now)
                .updatedAt(now)
                .build();

        // Act
        SessionDto sessionDto = sessionMapper.toDto(session);

        // Assert
        assertNotNull(sessionDto);
        assertEquals(session.getId(), sessionDto.getId());
        assertEquals(session.getName(), sessionDto.getName());
        assertEquals(session.getDate(), sessionDto.getDate());
        assertEquals(session.getDescription(), sessionDto.getDescription());
        assertEquals(teacher.getId(), sessionDto.getTeacher_id());
        assertEquals(session.getCreatedAt(), sessionDto.getCreatedAt());
        assertEquals(session.getUpdatedAt(), sessionDto.getUpdatedAt());
        
        assertNotNull(sessionDto.getUsers());
        assertEquals(2, sessionDto.getUsers().size());
        assertTrue(sessionDto.getUsers().contains(1L));
        assertTrue(sessionDto.getUsers().contains(2L));
    }

    @Test
    public void shouldMapSessionDtoToSession() {
        // Arrange
        List<Long> userIds = Arrays.asList(1L, 2L);
        
        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(1L);
        sessionDto.setName("Yoga Session");
        sessionDto.setDate(sessionDate);
        sessionDto.setDescription("Yoga session description");
        sessionDto.setTeacher_id(1L);
        sessionDto.setUsers(userIds);
        sessionDto.setCreatedAt(now);
        sessionDto.setUpdatedAt(now);

        // Act
        Session session = sessionMapper.toEntity(sessionDto);

        // Assert
        assertNotNull(session);
        assertEquals(sessionDto.getId(), session.getId());
        assertEquals(sessionDto.getName(), session.getName());
        assertEquals(sessionDto.getDate(), session.getDate());
        assertEquals(sessionDto.getDescription(), session.getDescription());
        assertEquals(sessionDto.getCreatedAt(), session.getCreatedAt());
        assertEquals(sessionDto.getUpdatedAt(), session.getUpdatedAt());
        
        assertNotNull(session.getTeacher());
        assertEquals(teacher.getId(), session.getTeacher().getId());
        assertEquals(teacher.getFirstName(), session.getTeacher().getFirstName());
        assertEquals(teacher.getLastName(), session.getTeacher().getLastName());
        
        assertNotNull(session.getUsers());
        assertEquals(2, session.getUsers().size());
        assertTrue(session.getUsers().stream().anyMatch(user -> user.getId().equals(1L)));
        assertTrue(session.getUsers().stream().anyMatch(user -> user.getId().equals(2L)));
    }

    @Test
    public void shouldHandleNullTeacherIdAndEmptyUsersList() {
        // Arrange
        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(1L);
        sessionDto.setName("Yoga Session");
        sessionDto.setDate(sessionDate);
        sessionDto.setDescription("Yoga session description");
        sessionDto.setTeacher_id(null);
        sessionDto.setUsers(null);
        sessionDto.setCreatedAt(now);
        sessionDto.setUpdatedAt(now);

        // Act
        Session session = sessionMapper.toEntity(sessionDto);

        // Assert
        assertNotNull(session);
        assertEquals(sessionDto.getId(), session.getId());
        assertEquals(sessionDto.getName(), session.getName());
        assertEquals(sessionDto.getDate(), session.getDate());
        assertEquals(sessionDto.getDescription(), session.getDescription());
        assertNull(session.getTeacher());
        assertNotNull(session.getUsers());
        assertTrue(session.getUsers().isEmpty());
    }

    @Test
    public void shouldHandleSessionWithNullTeacherAndEmptyUsers() {
        // Arrange
        Session session = Session.builder()
                .id(1L)
                .name("Yoga Session")
                .date(sessionDate)
                .description("Yoga session description")
                .teacher(null)
                .users(new ArrayList<>())
                .createdAt(now)
                .updatedAt(now)
                .build();

        // Act
        SessionDto sessionDto = sessionMapper.toDto(session);

        // Assert
        assertNotNull(sessionDto);
        assertEquals(session.getId(), sessionDto.getId());
        assertEquals(session.getName(), sessionDto.getName());
        assertEquals(session.getDate(), sessionDto.getDate());
        assertEquals(session.getDescription(), sessionDto.getDescription());
        assertNull(sessionDto.getTeacher_id());
        assertNotNull(sessionDto.getUsers());
        assertTrue(sessionDto.getUsers().isEmpty());
    }

    @Test
    public void shouldMapSessionListToSessionDtoList() {
        // Arrange
        List<User> userList = Arrays.asList(user1, user2);
        
        Session session1 = Session.builder()
                .id(1L)
                .name("Yoga Session 1")
                .date(sessionDate)
                .description("Yoga session 1 description")
                .teacher(teacher)
                .users(userList)
                .build();
        
        Session session2 = Session.builder()
                .id(2L)
                .name("Yoga Session 2")
                .date(sessionDate)
                .description("Yoga session 2 description")
                .teacher(teacher)
                .users(userList)
                .build();
        
        List<Session> sessions = Arrays.asList(session1, session2);

        // Act
        List<SessionDto> sessionDtos = sessionMapper.toDto(sessions);

        // Assert
        assertNotNull(sessionDtos);
        assertEquals(2, sessionDtos.size());
        
        assertEquals(session1.getId(), sessionDtos.get(0).getId());
        assertEquals(session1.getName(), sessionDtos.get(0).getName());
        assertEquals(session1.getDescription(), sessionDtos.get(0).getDescription());
        
        assertEquals(session2.getId(), sessionDtos.get(1).getId());
        assertEquals(session2.getName(), sessionDtos.get(1).getName());
        assertEquals(session2.getDescription(), sessionDtos.get(1).getDescription());
    }

    @Test
    public void shouldMapSessionDtoListToSessionList() {
        // Arrange
        List<Long> userIds = Arrays.asList(1L, 2L);
        
        SessionDto sessionDto1 = new SessionDto();
        sessionDto1.setId(1L);
        sessionDto1.setName("Yoga Session 1");
        sessionDto1.setDate(sessionDate);
        sessionDto1.setDescription("Yoga session 1 description");
        sessionDto1.setTeacher_id(1L);
        sessionDto1.setUsers(userIds);
        
        SessionDto sessionDto2 = new SessionDto();
        sessionDto2.setId(2L);
        sessionDto2.setName("Yoga Session 2");
        sessionDto2.setDate(sessionDate);
        sessionDto2.setDescription("Yoga session 2 description");
        sessionDto2.setTeacher_id(1L);
        sessionDto2.setUsers(userIds);
        
        List<SessionDto> sessionDtos = Arrays.asList(sessionDto1, sessionDto2);

        // Act
        List<Session> sessions = sessionMapper.toEntity(sessionDtos);

        // Assert
        assertNotNull(sessions);
        assertEquals(2, sessions.size());
        
        assertEquals(sessionDto1.getId(), sessions.get(0).getId());
        assertEquals(sessionDto1.getName(), sessions.get(0).getName());
        assertEquals(sessionDto1.getDescription(), sessions.get(0).getDescription());
        
        assertEquals(sessionDto2.getId(), sessions.get(1).getId());
        assertEquals(sessionDto2.getName(), sessions.get(1).getName());
        assertEquals(sessionDto2.getDescription(), sessions.get(1).getDescription());
    }
}