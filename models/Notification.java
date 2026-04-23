
package models;

import java.time.LocalDateTime;
/**
 *
 * @author Aruna Indika
 * * Notification entity for system messages
 */

public class Notification {
    private String notificationId;
    private User recipient;
    private String message;
    private LocalDateTime timestamp;
    private boolean isRead;
    private String type; // BOOKING, MAINTENANCE, SYSTEM
    
    public Notification(User recipient, String message, String type) {
        this.notificationId = generateNotificationId();
        this.recipient = recipient;
        this.message = message;
        this.type = type;
        this.timestamp = LocalDateTime.now();
        this.isRead = false;
    }
    
    private String generateNotificationId() {
        return "NOT" + System.currentTimeMillis() + (int)(Math.random() * 1000);
    }
    
    // Getters and Setters
    public String getNotificationId() { return notificationId; }
    public User getRecipient() { return recipient; }
    public String getMessage() { return message; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }
    public String getType() { return type; }
    
    @Override
    public String toString() {
        return String.format("[%s] %s: %s", timestamp.toLocalTime(), type, message);
    }
}