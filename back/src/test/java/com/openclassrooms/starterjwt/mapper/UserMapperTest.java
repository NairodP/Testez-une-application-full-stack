package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserMapperTest {

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Test
    public void shouldMapUserToUserDto() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        User user = User.builder()
                .id(1L)
                .email("test@test.com")
                .firstName("John")
                .lastName("Doe")
                .password("password123")
                .admin(true)
                .createdAt(now)
                .updatedAt(now)
                .build();

        // Act
        UserDto userDto = userMapper.toDto(user);

        // Assert
        assertNotNull(userDto);
        assertEquals(user.getId(), userDto.getId());
        assertEquals(user.getEmail(), userDto.getEmail());
        assertEquals(user.getFirstName(), userDto.getFirstName());
        assertEquals(user.getLastName(), userDto.getLastName());
        assertEquals(user.getPassword(), userDto.getPassword());
        assertEquals(user.isAdmin(), userDto.isAdmin());
        assertEquals(user.getCreatedAt(), userDto.getCreatedAt());
        assertEquals(user.getUpdatedAt(), userDto.getUpdatedAt());
    }

    @Test
    public void shouldMapUserDtoToUser() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setEmail("test@test.com");
        userDto.setFirstName("John");
        userDto.setLastName("Doe");
        userDto.setPassword("password123");
        userDto.setAdmin(true);
        userDto.setCreatedAt(now);
        userDto.setUpdatedAt(now);

        // Act
        User user = userMapper.toEntity(userDto);

        // Assert
        assertNotNull(user);
        assertEquals(userDto.getId(), user.getId());
        assertEquals(userDto.getEmail(), user.getEmail());
        assertEquals(userDto.getFirstName(), user.getFirstName());
        assertEquals(userDto.getLastName(), user.getLastName());
        assertEquals(userDto.getPassword(), user.getPassword());
        assertEquals(userDto.isAdmin(), user.isAdmin());
        assertEquals(userDto.getCreatedAt(), user.getCreatedAt());
        assertEquals(userDto.getUpdatedAt(), user.getUpdatedAt());
    }

    @Test
    public void shouldMapUserListToUserDtoList() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        List<User> userList = new ArrayList<>();
        
        User user1 = User.builder()
                .id(1L)
                .email("user1@test.com")
                .firstName("John")
                .lastName("Doe")
                .password("password1")
                .admin(true)
                .createdAt(now)
                .updatedAt(now)
                .build();
                
        User user2 = User.builder()
                .id(2L)
                .email("user2@test.com")
                .firstName("Jane")
                .lastName("Smith")
                .password("password2")
                .admin(false)
                .createdAt(now)
                .updatedAt(now)
                .build();
                
        userList.add(user1);
        userList.add(user2);

        // Act
        List<UserDto> userDtoList = userMapper.toDto(userList);

        // Assert
        assertNotNull(userDtoList);
        assertEquals(2, userDtoList.size());
        
        assertEquals(user1.getId(), userDtoList.get(0).getId());
        assertEquals(user1.getEmail(), userDtoList.get(0).getEmail());
        assertEquals(user1.getFirstName(), userDtoList.get(0).getFirstName());
        assertEquals(user1.getLastName(), userDtoList.get(0).getLastName());
        assertEquals(user1.isAdmin(), userDtoList.get(0).isAdmin());
        
        assertEquals(user2.getId(), userDtoList.get(1).getId());
        assertEquals(user2.getEmail(), userDtoList.get(1).getEmail());
        assertEquals(user2.getFirstName(), userDtoList.get(1).getFirstName());
        assertEquals(user2.getLastName(), userDtoList.get(1).getLastName());
        assertEquals(user2.isAdmin(), userDtoList.get(1).isAdmin());
    }

    @Test
    public void shouldMapUserDtoListToUserList() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        List<UserDto> userDtoList = new ArrayList<>();
        
        UserDto userDto1 = new UserDto();
        userDto1.setId(1L);
        userDto1.setEmail("user1@test.com");
        userDto1.setFirstName("John");
        userDto1.setLastName("Doe");
        userDto1.setPassword("password1");
        userDto1.setAdmin(true);
        userDto1.setCreatedAt(now);
        userDto1.setUpdatedAt(now);
        
        UserDto userDto2 = new UserDto();
        userDto2.setId(2L);
        userDto2.setEmail("user2@test.com");
        userDto2.setFirstName("Jane");
        userDto2.setLastName("Smith");
        userDto2.setPassword("password2");
        userDto2.setAdmin(false);
        userDto2.setCreatedAt(now);
        userDto2.setUpdatedAt(now);
        
        userDtoList.add(userDto1);
        userDtoList.add(userDto2);

        // Act
        List<User> userList = userMapper.toEntity(userDtoList);

        // Assert
        assertNotNull(userList);
        assertEquals(2, userList.size());
        
        assertEquals(userDto1.getId(), userList.get(0).getId());
        assertEquals(userDto1.getEmail(), userList.get(0).getEmail());
        assertEquals(userDto1.getFirstName(), userList.get(0).getFirstName());
        assertEquals(userDto1.getLastName(), userList.get(0).getLastName());
        assertEquals(userDto1.isAdmin(), userList.get(0).isAdmin());
        
        assertEquals(userDto2.getId(), userList.get(1).getId());
        assertEquals(userDto2.getEmail(), userList.get(1).getEmail());
        assertEquals(userDto2.getFirstName(), userList.get(1).getFirstName());
        assertEquals(userDto2.getLastName(), userList.get(1).getLastName());
        assertEquals(userDto2.isAdmin(), userList.get(1).isAdmin());
    }
}