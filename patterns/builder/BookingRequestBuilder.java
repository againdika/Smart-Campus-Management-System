/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package patterns.builder;

import models.Booking;
import models.Room;
import models.User;
import java.time.LocalDateTime;

/**
 * BUILDER DESIGN PATTERN
 * construct Booking objects with optional parameters
 */
public class BookingRequestBuilder {
    private Room room;
    private User user;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String purpose;
    private boolean isRecurring;
    private String recurringPattern;
    
    public BookingRequestBuilder setRoom(Room room) {
        this.room = room;
        return this;
    }
    
    public BookingRequestBuilder setUser(User user) {
        this.user = user;
        return this;
    }
    
    public BookingRequestBuilder setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
        return this;
    }
    
    public BookingRequestBuilder setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
        return this;
    }
    
    public BookingRequestBuilder setPurpose(String purpose) {
        this.purpose = purpose;
        return this;
    }
    
    public BookingRequestBuilder setRecurring(boolean recurring, String pattern) {
        this.isRecurring = recurring;
        this.recurringPattern = pattern;
        return this;
    }
    
    public Booking build() {
        if (room == null || user == null || startTime == null || endTime == null) {
            throw new IllegalArgumentException("Room, User, StartTime and EndTime are required");
        }
        if (startTime.isAfter(endTime)) {
            throw new IllegalArgumentException("Start time must be before end time");
        }
        
        Booking booking = new Booking(room, user, startTime, endTime, purpose);
        if (isRecurring) {
            booking.setRecurring(true, recurringPattern);
        }
        return booking;
    }
}