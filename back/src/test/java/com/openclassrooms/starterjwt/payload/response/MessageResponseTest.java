package com.openclassrooms.starterjwt.payload.response;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MessageResponseTest {

    @Test
    public void testConstructor() {
        // Arrange
        String message = "Test message";
        
        // Act
        MessageResponse response = new MessageResponse(message);
        
        // Assert
        assertEquals(message, response.getMessage());
    }
    
    @Test
    public void testSetAndGetMessage() {
        // Arrange
        MessageResponse response = new MessageResponse("Initial message");
        String newMessage = "New message";
        
        // Act
        response.setMessage(newMessage);
        
        // Assert
        assertEquals(newMessage, response.getMessage());
    }
}