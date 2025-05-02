package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TeacherMapperTest {

    private final TeacherMapper teacherMapper = Mappers.getMapper(TeacherMapper.class);

    @Test
    public void shouldMapTeacherToTeacherDto() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        Teacher teacher = Teacher.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .createdAt(now)
                .updatedAt(now)
                .build();

        // Act
        TeacherDto teacherDto = teacherMapper.toDto(teacher);

        // Assert
        assertNotNull(teacherDto);
        assertEquals(teacher.getId(), teacherDto.getId());
        assertEquals(teacher.getFirstName(), teacherDto.getFirstName());
        assertEquals(teacher.getLastName(), teacherDto.getLastName());
        assertEquals(teacher.getCreatedAt(), teacherDto.getCreatedAt());
        assertEquals(teacher.getUpdatedAt(), teacherDto.getUpdatedAt());
    }

    @Test
    public void shouldMapTeacherDtoToTeacher() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        TeacherDto teacherDto = new TeacherDto();
        teacherDto.setId(1L);
        teacherDto.setFirstName("John");
        teacherDto.setLastName("Doe");
        teacherDto.setCreatedAt(now);
        teacherDto.setUpdatedAt(now);

        // Act
        Teacher teacher = teacherMapper.toEntity(teacherDto);

        // Assert
        assertNotNull(teacher);
        assertEquals(teacherDto.getId(), teacher.getId());
        assertEquals(teacherDto.getFirstName(), teacher.getFirstName());
        assertEquals(teacherDto.getLastName(), teacher.getLastName());
        assertEquals(teacherDto.getCreatedAt(), teacher.getCreatedAt());
        assertEquals(teacherDto.getUpdatedAt(), teacher.getUpdatedAt());
    }

    @Test
    public void shouldMapTeacherListToTeacherDtoList() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        List<Teacher> teacherList = new ArrayList<>();
        
        Teacher teacher1 = Teacher.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .createdAt(now)
                .updatedAt(now)
                .build();
                
        Teacher teacher2 = Teacher.builder()
                .id(2L)
                .firstName("Jane")
                .lastName("Smith")
                .createdAt(now)
                .updatedAt(now)
                .build();
                
        teacherList.add(teacher1);
        teacherList.add(teacher2);

        // Act
        List<TeacherDto> teacherDtoList = teacherMapper.toDto(teacherList);

        // Assert
        assertNotNull(teacherDtoList);
        assertEquals(2, teacherDtoList.size());
        
        assertEquals(teacher1.getId(), teacherDtoList.get(0).getId());
        assertEquals(teacher1.getFirstName(), teacherDtoList.get(0).getFirstName());
        assertEquals(teacher1.getLastName(), teacherDtoList.get(0).getLastName());
        assertEquals(teacher1.getCreatedAt(), teacherDtoList.get(0).getCreatedAt());
        assertEquals(teacher1.getUpdatedAt(), teacherDtoList.get(0).getUpdatedAt());
        
        assertEquals(teacher2.getId(), teacherDtoList.get(1).getId());
        assertEquals(teacher2.getFirstName(), teacherDtoList.get(1).getFirstName());
        assertEquals(teacher2.getLastName(), teacherDtoList.get(1).getLastName());
        assertEquals(teacher2.getCreatedAt(), teacherDtoList.get(1).getCreatedAt());
        assertEquals(teacher2.getUpdatedAt(), teacherDtoList.get(1).getUpdatedAt());
    }

    @Test
    public void shouldMapTeacherDtoListToTeacherList() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        List<TeacherDto> teacherDtoList = new ArrayList<>();
        
        TeacherDto teacherDto1 = new TeacherDto();
        teacherDto1.setId(1L);
        teacherDto1.setFirstName("John");
        teacherDto1.setLastName("Doe");
        teacherDto1.setCreatedAt(now);
        teacherDto1.setUpdatedAt(now);
        
        TeacherDto teacherDto2 = new TeacherDto();
        teacherDto2.setId(2L);
        teacherDto2.setFirstName("Jane");
        teacherDto2.setLastName("Smith");
        teacherDto2.setCreatedAt(now);
        teacherDto2.setUpdatedAt(now);
        
        teacherDtoList.add(teacherDto1);
        teacherDtoList.add(teacherDto2);

        // Act
        List<Teacher> teacherList = teacherMapper.toEntity(teacherDtoList);

        // Assert
        assertNotNull(teacherList);
        assertEquals(2, teacherList.size());
        
        assertEquals(teacherDto1.getId(), teacherList.get(0).getId());
        assertEquals(teacherDto1.getFirstName(), teacherList.get(0).getFirstName());
        assertEquals(teacherDto1.getLastName(), teacherList.get(0).getLastName());
        assertEquals(teacherDto1.getCreatedAt(), teacherList.get(0).getCreatedAt());
        assertEquals(teacherDto1.getUpdatedAt(), teacherList.get(0).getUpdatedAt());
        
        assertEquals(teacherDto2.getId(), teacherList.get(1).getId());
        assertEquals(teacherDto2.getFirstName(), teacherList.get(1).getFirstName());
        assertEquals(teacherDto2.getLastName(), teacherList.get(1).getLastName());
        assertEquals(teacherDto2.getCreatedAt(), teacherList.get(1).getCreatedAt());
        assertEquals(teacherDto2.getUpdatedAt(), teacherList.get(1).getUpdatedAt());
    }
}