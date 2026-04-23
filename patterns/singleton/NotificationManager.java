// patterns/singleton/NotificationManager.java
package patterns.singleton;

import models.Notification;
import models.User;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SINGLETON DESIGN PATTERN
 * Ensures only one instance of NotificationManager exists throughout the application
 * Manages all system notifications
 */
public class NotificationManager {
   
    private static NotificationManager instance;    

    private Map<String, List<Notification>> userNotifications;
    
    private NotificationManager() {
        userNotifications = new ConcurrentHashMap<>();
        System.out.println("NotificationManager initialized (Singleton)");
    }
   
    public static synchronized NotificationManager getInstance() {
        if (instance == null) {
            instance = new NotificationManager();
        }
        return instance;
    }
    
    // Send notification to a user
    public void sendNotification(User user, String message, String type) {
        Notification notification = new Notification(user, message, type);
        userNotifications.computeIfAbsent(user.getUserId(), k -> new ArrayList<>()).add(notification);
        System.out.println("Notification sent to " + user.getUsername() + ": " + message);
    }
    

    public List<Notification> getNotifications(User user) {
        return userNotifications.getOrDefault(user.getUserId(), new ArrayList<>());
    }
    
    // Get unread notifications count
    public int getUnreadCount(User user) {
        List<Notification> notifications = userNotifications.get(user.getUserId());
        if (notifications == null) return 0;
        return (int) notifications.stream().filter(n -> !n.isRead()).count();
    }
    
    // Mark notification as read
    public void markAsRead(User user, String notificationId) {
        List<Notification> notifications = userNotifications.get(user.getUserId());
        if (notifications != null) {
            notifications.stream()
                .filter(n -> n.getNotificationId().equals(notificationId))
                .findFirst()
                .ifPresent(n -> n.setRead(true));
        }
    }
}