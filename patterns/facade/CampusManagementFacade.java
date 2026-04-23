/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package patterns.facade;

import models.*;
import enums.*;
import patterns.singleton.NotificationManager;
import patterns.observer.Observer;
import patterns.observer.Subject;
import patterns.observer.UserObserver;
import patterns.factory.RoomFactory;
import patterns.builder.BookingRequestBuilder;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

//FACADE DESIGN PATTERN

public class CampusManagementFacade implements Subject {
    // Data storage using Java Generics
    private Map<String, User> users;
    private Map<String, Room> rooms;
    private List<Booking> bookings;
    private List<MaintenanceRequest> maintenanceRequests;
    private Map<String, UserObserver> observers;
    
    // Singleton NotificationManager injected
    private NotificationManager notificationManager;
    
    public CampusManagementFacade() {
        this.users = new HashMap<>();
        this.rooms = new HashMap<>();
        this.bookings = new ArrayList<>();
        this.maintenanceRequests = new ArrayList<>();
        this.observers = new HashMap<>();
        this.notificationManager = NotificationManager.getInstance();
        initializeData();
    }
    

    private void initializeData() {
        // Create admin
        User admin = new User("admin", "admin123", "admin@campus.edu", UserType.ADMINISTRATOR);
        users.put(admin.getUserId(), admin);
        
        // Create staff
        User staff = new User("staff1", "staff123", "staff@campus.edu", UserType.STAFF);
        users.put(staff.getUserId(), staff);
        
        // Create student
        User student = new User("student1", "student123", "student@campus.edu", UserType.STUDENT);
        users.put(student.getUserId(), student);
        
        // Create rooms using Factory pattern
        Room lectureHall = RoomFactory.createLectureHall("R001", "Main Lecture Hall", 200);
        Room computerLab = RoomFactory.createComputerLab("R002", "Computer Lab A", 50);
        Room conferenceRoom = RoomFactory.createConferenceRoom("R003", "Conference Room", 30);
        Room smartClassroom = RoomFactory.createSmartClassroom("R004", "Smart Classroom", 60);
        
        rooms.put(lectureHall.getRoomId(), lectureHall);
        rooms.put(computerLab.getRoomId(), computerLab);
        rooms.put(conferenceRoom.getRoomId(), conferenceRoom);
        rooms.put(smartClassroom.getRoomId(), smartClassroom);
        
        // Register observers for users
        for (User user : users.values()) {
            UserObserver observer = new UserObserver(user);
            observers.put(user.getUserId(), observer);
        }
    }
    

    @Override
    public void attach(Observer observer) {
        if (observer instanceof UserObserver) {
            UserObserver uo = (UserObserver) observer;
            observers.put(uo.getUser().getUserId(), uo);
        }
    }

    @Override
    public void detach(Observer observer) {
        if (observer instanceof UserObserver) {
            UserObserver uo = (UserObserver) observer;
            observers.remove(uo.getUser().getUserId());
        }
    }

    @Override
    public void notifyObservers(models.Notification notification) {
        observers.values().forEach(o -> o.update(notification));
    }
    // ─────────────────────────────────────────────────────────────────────────

    // User authentication
    public User login(String username, String password) throws Exception {
        return users.values().stream()
            .filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password))
            .findFirst()
            .orElseThrow(() -> new Exception("Invalid credentials"));
    }
    

    public List<Room> getAllRooms() {
        return new ArrayList<>(rooms.values());
    }

    
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }
    
    public List<Room> getAvailableRooms(LocalDateTime startTime, LocalDateTime endTime) {
        return rooms.values().stream()
            .filter(Room::isActive)
            .filter(room -> isRoomAvailable(room, startTime, endTime))
            .collect(Collectors.toList());
    }
    
    private boolean isRoomAvailable(Room room, LocalDateTime startTime, LocalDateTime endTime) {
            // Two intervals [a,b) and [c,d) overlap only when a < d AND c < b.
        return bookings.stream()
            .filter(b -> b.getStatus() == BookingStatus.CONFIRMED)
            .noneMatch(b -> b.getRoom().getRoomId().equals(room.getRoomId()) &&
                           b.getStartTime().isBefore(endTime) &&
                           startTime.isBefore(b.getEndTime()));
    }
    
    public void addRoom(Room room) throws Exception {
        if (rooms.containsKey(room.getRoomId())) {
            throw new Exception("Room ID already exists");
        }
        rooms.put(room.getRoomId(), room);
        notificationManager.sendNotification(getAdminUser(), "New room added: " + room.getRoomName(), "SYSTEM");
    }
    
    public void updateRoom(Room updatedRoom) throws Exception {
        if (!rooms.containsKey(updatedRoom.getRoomId())) {
            throw new Exception("Room not found");
        }
        rooms.put(updatedRoom.getRoomId(), updatedRoom);
    }
    
    public void deactivateRoom(String roomId) throws Exception {
        Room room = rooms.get(roomId);
        if (room == null) throw new Exception("Room not found");
        room.setActive(false);
    }
    
    // Booking Methods
    public Booking bookRoom(User user, String roomId, LocalDateTime startTime, 
                           LocalDateTime endTime, String purpose) throws Exception {
        // Validate user authorization
        if (user.getUserType() == UserType.ADMINISTRATOR || 
            user.getUserType() == UserType.STAFF ||
            user.getUserType() == UserType.STUDENT) {
            
            Room room = rooms.get(roomId);
            if (room == null) throw new Exception("Room not found");
            if (!room.isActive()) throw new Exception("Room is deactivated");
            
            // Check for double booking
            if (!isRoomAvailable(room, startTime, endTime)) {
                throw new Exception("Room is already booked for the requested time slot");
            }
            
            Booking booking = new BookingRequestBuilder()
                .setRoom(room)
                .setUser(user)
                .setStartTime(startTime)
                .setEndTime(endTime)
                .setPurpose(purpose)
                .build();
            
            booking.setStatus(BookingStatus.CONFIRMED);
            bookings.add(booking);
            
            // Send notifications using Observer pattern 
            String message = String.format("Booking confirmed: %s on %s from %s to %s",
                room.getRoomName(), startTime.toLocalDate(), startTime.toLocalTime(), endTime.toLocalTime());
            notifyUser(user, message, "BOOKING");
            
            return booking;
        }
        throw new Exception("Unauthorized: Only Staff, Students, and Admins can book rooms");
    }
    
    public List<Booking> getUserBookings(User user) {
        return bookings.stream()
            .filter(b -> b.getUser().getUserId().equals(user.getUserId()))
            .collect(Collectors.toList());
    }
    
    public void cancelBooking(User user, String bookingId) throws Exception {
        Booking booking = bookings.stream()
            .filter(b -> b.getBookingId().equals(bookingId))
            .findFirst()
            .orElseThrow(() -> new Exception("Booking not found"));
        
        if (!booking.getUser().getUserId().equals(user.getUserId()) && 
            user.getUserType() != UserType.ADMINISTRATOR) {
            throw new Exception("Unauthorized: Cannot cancel another user's booking");
        }
        
        booking.setStatus(BookingStatus.CANCELLED);
        String message = "Your booking for " + booking.getRoom().getRoomName() + " has been cancelled";
        notifyUser(user, message, "BOOKING"); 
    }
    
    // Maintenance Methods
    public MaintenanceRequest reportMaintenance(User user, String roomId, 
                                                 String description, Urgency urgency) throws Exception {
        if (user.getUserType() != UserType.STAFF && user.getUserType() != UserType.ADMINISTRATOR) {
            throw new Exception("Unauthorized: Only Staff and Admins can report maintenance issues");
        }
        
        Room room = rooms.get(roomId);
        if (room == null) throw new Exception("Room not found");
        
        MaintenanceRequest request = new MaintenanceRequest(room, user, description, urgency);
        maintenanceRequests.add(request);
        
        String message = String.format("Maintenance request submitted for %s: %s (Urgency: %s)",
            room.getRoomName(), description, urgency.getLevel());
        notifyUser(user, message, "MAINTENANCE"); 
        
        // Notify admin
        notificationManager.sendNotification(getAdminUser(), 
            "New maintenance request: " + request.getRequestId(), "MAINTENANCE");
        
        return request;
    }
    
    public List<MaintenanceRequest> getAllMaintenanceRequests() {
        return new ArrayList<>(maintenanceRequests);
    }
    
    public void assignMaintenance(User admin, String requestId, User staff) throws Exception {
        if (admin.getUserType() != UserType.ADMINISTRATOR) {
            throw new Exception("Unauthorized: Only admins can assign maintenance tasks");
        }
        
        MaintenanceRequest request = maintenanceRequests.stream()
            .filter(r -> r.getRequestId().equals(requestId))
            .findFirst()
            .orElseThrow(() -> new Exception("Maintenance request not found"));
        
        request.setStatus(MaintenanceStatus.ASSIGNED);
        request.setAssignedTo(staff);
        
        String message = "Maintenance request " + requestId + " assigned to " + staff.getUsername();
        notifyUser(admin, message, "MAINTENANCE"); 

        notificationManager.sendNotification(staff,
            "You have been assigned maintenance request: " + request.getDescription(), "MAINTENANCE");
    }
    
    public void updateMaintenanceStatus(User admin, String requestId, MaintenanceStatus status) throws Exception {
        if (admin.getUserType() != UserType.ADMINISTRATOR) {
            throw new Exception("Unauthorized: Only admins can update maintenance status");
        }
        
        MaintenanceRequest request = maintenanceRequests.stream()
            .filter(r -> r.getRequestId().equals(requestId))
            .findFirst()
            .orElseThrow(() -> new Exception("Maintenance request not found"));
        
        request.setStatus(status);
        if (status == MaintenanceStatus.COMPLETED) {
            request.setCompletedDate(java.time.LocalDateTime.now());
        }
        
        String message = "Maintenance request " + requestId + " status updated to: " + status.getDisplayName();
        notifyUser(request.getReportedBy(), message, "MAINTENANCE"); 
    }
    
    // Analytics for Admin
    public Map<String, Object> getAnalytics() {
        Map<String, Object> analytics = new HashMap<>();
        
        // Most booked rooms
        Map<String, Long> roomBookingCount = bookings.stream()
            .filter(b -> b.getStatus() == BookingStatus.CONFIRMED)
            .collect(Collectors.groupingBy(b -> b.getRoom().getRoomName(), Collectors.counting()));
        
        analytics.put("mostBookedRooms", roomBookingCount);
        
        // Frequent maintenance types
        Map<String, Long> maintenanceTypes = maintenanceRequests.stream()
            .collect(Collectors.groupingBy(m -> m.getDescription().substring(0, Math.min(20, m.getDescription().length())), 
                                          Collectors.counting()));
        analytics.put("frequentMaintenanceIssues", maintenanceTypes);
        
        analytics.put("totalBookings", bookings.size());
        analytics.put("activeMaintenance", maintenanceRequests.stream()
            .filter(m -> m.getStatus() != MaintenanceStatus.COMPLETED).count());
        analytics.put("totalRooms", rooms.size());
        
        return analytics;
    }
    
    private User getAdminUser() {
        return users.values().stream()
            .filter(u -> u.getUserType() == UserType.ADMINISTRATOR)
            .findFirst()
            .orElse(null);
    }

   
    private void notifyUser(User user, String message, String type) {
        Notification notification = new Notification(user, message, type);
        UserObserver observer = observers.get(user.getUserId());
        if (observer != null) {
            observer.update(notification);
        } else {
        
            notificationManager.sendNotification(user, message, type);
        }
    }

    public List<Notification> getUserNotifications(User user) {
        return notificationManager.getNotifications(user);
    }
}