/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package patterns.observer;

import models.Notification;
import models.User;
import patterns.singleton.NotificationManager;

/**
 * OBSERVER DESIGN PATTERN - Concrete Observer
 * Represents a user who receives notifications
 */
public class UserObserver implements Observer {
    private User user;
    
    public UserObserver(User user) {
        this.user = user;
    }
    
    @Override
    public void update(Notification notification) {
        // Send notification through the NotificationManager
        NotificationManager.getInstance().sendNotification(user, notification.getMessage(), notification.getType());
    }
    
    public User getUser() {
        return user;
    }
}