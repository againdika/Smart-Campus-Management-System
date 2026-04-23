
import models.*;
import enums.*;
import patterns.facade.CampusManagementFacade;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class MainApplication {
    private static CampusManagementFacade facade;
    private static Scanner scanner;
    private static User currentUser;
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    
    public static void main(String[] args) {
        System.out.println("=== SMART CAMPUS MANAGEMENT SYSTEM ===");
        System.out.println("Initializing system...\n");
        
        // Dependency injection example
        facade = new CampusManagementFacade();
        scanner = new Scanner(System.in);
        
        try {
            while (true) {
                if (currentUser == null) {
                    showLoginMenu();
                } else {
                    showMainMenu();
                }
            }
        } catch (Exception e) {
            System.err.println("System error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void showLoginMenu() {
        System.out.println("\n=== LOGIN ===");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        
        try {
            currentUser = facade.login(username, password);
            System.out.println("\nWelcome, " + currentUser.getUsername() + "! (" + currentUser.getUserType() + ")");
            
            // Show unread notifications count
            int unreadCount = facade.getUserNotifications(currentUser).size();
            if (unreadCount > 0) {
                System.out.println("You have " + unreadCount + " unread notifications.");
            }
        } catch (Exception e) {
            System.out.println("Login failed: " + e.getMessage());
        }
    }
    
    private static void showMainMenu() {
        System.out.println("\n=== MAIN MENU ===");
        System.out.println("1. View Available Rooms");
        
         if (currentUser.getUserType() == UserType.STAFF ||
                currentUser.getUserType() == UserType.ADMINISTRATOR) {
            System.out.println("2. Book a Room");
        }
         else
         {
         System.out.println("2. Request a Room");
         }
        
        System.out.println("3. View My Bookings");
        System.out.println("4. Cancel Booking");

        if (currentUser.getUserType() == UserType.STAFF ||
                currentUser.getUserType() == UserType.ADMINISTRATOR) {
            System.out.println("5. Report Maintenance Issue");
        }
        
        if (currentUser.getUserType() == UserType.ADMINISTRATOR) {
            System.out.println("6. Admin: Manage Rooms");
            System.out.println("7. Admin: View All Maintenance Requests");
            System.out.println("8. Admin: Assign Maintenance Task");
            System.out.println("9. Admin: Update Maintenance Status");
            System.out.println("10. Admin: View Analytics Dashboard");
        } else if (currentUser.getUserType() == UserType.STAFF) {
            System.out.println("6. View My Maintenance Requests");
        }
        
         if (currentUser.getUserType() == UserType.STAFF ||
                currentUser.getUserType() == UserType.ADMINISTRATOR) {
             System.out.println("11. View My Notifications");
        }
         else
         {
         System.out.println("11. View Announcements");
         }
         
       
        
        System.out.println("0. Logout");
        System.out.print("Choose option: ");
        
        try {
            int choice = Integer.parseInt(scanner.nextLine());
            handleMenuChoice(choice);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private static void handleMenuChoice(int choice) throws Exception {
        switch (choice) {
            case 1:
                viewAvailableRooms();
                break;
            case 2:
                bookRoom();
                break;
            case 3:
                viewMyBookings();
                break;
            case 4:
                cancelBooking();
                break;
            case 5:

                if (currentUser.getUserType() == UserType.STAFF ||
                        currentUser.getUserType() == UserType.ADMINISTRATOR) {
                    reportMaintenance();
                } else {
                    System.out.println("Unauthorized: Only Staff and Administrators can report maintenance issues.");
                }
                break;
            case 6:
                if (currentUser.getUserType() == UserType.ADMINISTRATOR) {
                    adminManageRooms();
                } else if (currentUser.getUserType() == UserType.STAFF) {
                    viewAllMaintenanceRequests();
                }
                break;
            case 7:
                if (currentUser.getUserType() == UserType.ADMINISTRATOR) {
                    viewAllMaintenanceRequests();
                }
                break;
            case 8:
                if (currentUser.getUserType() == UserType.ADMINISTRATOR) {
                    assignMaintenanceTask();
                }
                break;
            case 9:
                if (currentUser.getUserType() == UserType.ADMINISTRATOR) {
                    updateMaintenanceStatus();
                }
                break;
            case 10:
                if (currentUser.getUserType() == UserType.ADMINISTRATOR) {
                    viewAnalytics();
                }
                break;
            case 11:
                viewNotifications();
                break;
            case 0:
                System.out.println("Logging out...");
                currentUser = null;
                break;
            default:
                System.out.println("Invalid option");
        }
    }
    
    private static void viewAvailableRooms() {
        System.out.println("\n=== AVAILABLE ROOMS ===");
        System.out.print("Enter date and time (yyyy-MM-dd HH:mm): ");
        String dateTimeStr = scanner.nextLine();
        
        try {
            LocalDateTime startTime = LocalDateTime.parse(dateTimeStr, formatter);
            LocalDateTime endTime = startTime.plusHours(1);
            
            List<Room> availableRooms = facade.getAvailableRooms(startTime, endTime);
            
            if (availableRooms.isEmpty()) {
                System.out.println("No rooms available at this time.");
            } else {
                System.out.println("\nAvailable Rooms:");
                for (Room room : availableRooms) {
                    System.out.printf("- %s (%s) Capacity: %d, Equipment: %s%n",
                        room.getRoomName(), room.getRoomType(), room.getCapacity(), 
                        room.getEquipmentList());
                }
            }
        } catch (Exception e) {
            System.out.println("Invalid date format. Please use: yyyy-MM-dd HH:mm");
        }
    }
    
    private static void bookRoom() {
        System.out.println("\n=== BOOK A ROOM ===");
        
        System.out.print("Enter Room ID: ");
        String roomId = scanner.nextLine();
        
        System.out.print("Start Date and Time (yyyy-MM-dd HH:mm): ");
        String startStr = scanner.nextLine();
        
        System.out.print("End Date and Time (yyyy-MM-dd HH:mm): ");
        String endStr = scanner.nextLine();
        
        System.out.print("Purpose: ");
        String purpose = scanner.nextLine();
        
        try {
            LocalDateTime startTime = LocalDateTime.parse(startStr, formatter);
            LocalDateTime endTime = LocalDateTime.parse(endStr, formatter);
            
            Booking booking = facade.bookRoom(currentUser, roomId, startTime, endTime, purpose);
            System.out.println("Booking successful! Booking ID: " + booking.getBookingId());
        } catch (Exception e) {
            System.out.println("Booking failed: " + e.getMessage());
        }
    }
    
    private static void viewMyBookings() {
        System.out.println("\n=== MY BOOKINGS ===");
        List<Booking> bookings = facade.getUserBookings(currentUser);
        
        if (bookings.isEmpty()) {
            System.out.println("No bookings found.");
        } else {
            for (Booking booking : bookings) {
                System.out.println(booking);
            }
        }
    }
    
    private static void cancelBooking() {
        System.out.println("\n=== CANCEL BOOKING ===");
        System.out.print("Enter Booking ID: ");
        String bookingId = scanner.nextLine();
        
        try {
            facade.cancelBooking(currentUser, bookingId);
            System.out.println("Booking cancelled successfully.");
        } catch (Exception e) {
            System.out.println("Cancellation failed: " + e.getMessage());
        }
    }
    
    private static void reportMaintenance() {
        System.out.println("\n=== REPORT MAINTENANCE ISSUE ===");
        System.out.print("Room ID: ");
        String roomId = scanner.nextLine();
        
        System.out.print("Description of issue: ");
        String description = scanner.nextLine();
        
        System.out.println("Urgency Level:");
        System.out.println("1. LOW");
        System.out.println("2. MEDIUM");
        System.out.println("3. HIGH");
        System.out.println("4. CRITICAL");
        System.out.print("Choose urgency (1-4): ");
        
        int urgencyChoice = Integer.parseInt(scanner.nextLine());
        Urgency urgency = Urgency.values()[urgencyChoice - 1];
        
        try {
            MaintenanceRequest request = facade.reportMaintenance(currentUser, roomId, description, urgency);
            System.out.println("Maintenance request submitted. Request ID: " + request.getRequestId());
        } catch (Exception e) {
            System.out.println("Failed to submit request: " + e.getMessage());
        }
    }
    
    private static void adminManageRooms() {
        System.out.println("\n=== ADMIN: MANAGE ROOMS ===");
        System.out.println("1. Add New Room");
        System.out.println("2. Update Room");
        System.out.println("3. Deactivate Room");
        System.out.println("4. View All Rooms");
        System.out.print("Choose option: ");
        
        int choice = Integer.parseInt(scanner.nextLine());
        
        try {
            switch (choice) {
                case 1:
                    addNewRoom();
                    break;
                case 2:
                    updateRoom();
                    break;
                case 3:
                    deactivateRoom();
                    break;
                case 4:
                    viewAllRooms();
                    break;
                default:
                    System.out.println("Invalid option");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private static void addNewRoom() throws Exception {
        System.out.print("Room ID: ");
        String id = scanner.nextLine();
        System.out.print("Room Name: ");
        String name = scanner.nextLine();
        System.out.print("Capacity: ");
        int capacity = Integer.parseInt(scanner.nextLine());
        System.out.print("Room Type (Lecture Hall/Computer Lab/Conference Room/Smart Classroom): ");
        String type = scanner.nextLine();
        
        Room room = null;
        switch (type.toLowerCase()) {
            case "lecture hall":
                room = patterns.factory.RoomFactory.createLectureHall(id, name, capacity);
                break;
            case "computer lab":
                room = patterns.factory.RoomFactory.createComputerLab(id, name, capacity);
                break;
            case "conference room":
                room = patterns.factory.RoomFactory.createConferenceRoom(id, name, capacity);
                break;
            case "smart classroom":
                room = patterns.factory.RoomFactory.createSmartClassroom(id, name, capacity);
                break;
            default:
                room = new Room(id, name, capacity, type);
        }
        
        facade.addRoom(room);
        System.out.println("Room added successfully!");
    }
    
    private static void updateRoom() throws Exception {
        System.out.print("Room ID to update: ");
        String id = scanner.nextLine();
        System.out.print("New Name: ");
        String name = scanner.nextLine();
        System.out.print("New Capacity: ");
        int capacity = Integer.parseInt(scanner.nextLine());
        
        Room room = facade.getAllRooms().stream()
            .filter(r -> r.getRoomId().equals(id))
            .findFirst()
            .orElse(null);
        
        if (room != null) {
            room.setRoomName(name);
            room.setCapacity(capacity);
            facade.updateRoom(room);
            System.out.println("Room updated successfully!");
        } else {
            System.out.println("Room not found");
        }
    }
    
    private static void deactivateRoom() throws Exception {
        System.out.print("Room ID to deactivate: ");
        String id = scanner.nextLine();
        facade.deactivateRoom(id);
        System.out.println("Room deactivated successfully!");
    }
    
    private static void viewAllRooms() {
        System.out.println("\n=== ALL ROOMS ===");
        for (Room room : facade.getAllRooms()) {
            System.out.println(room);
        }
    }
    
    private static void viewAllMaintenanceRequests() {
        System.out.println("\n=== ALL MAINTENANCE REQUESTS ===");
        List<MaintenanceRequest> requests = facade.getAllMaintenanceRequests();
        
        if (requests.isEmpty()) {
            System.out.println("No maintenance requests.");
        } else {
            for (MaintenanceRequest request : requests) {
                System.out.println(request);
                System.out.println("  Description: " + request.getDescription());
                System.out.println("  Reported by: " + request.getReportedBy().getUsername());
                System.out.println("  Assigned to: " + (request.getAssignedTo() != null ? 
                    request.getAssignedTo().getUsername() : "Not assigned"));
                System.out.println();
            }
        }
    }
    
    private static void assignMaintenanceTask() throws Exception {
        System.out.println("\n=== ASSIGN MAINTENANCE TASK ===");
        viewAllMaintenanceRequests();

        System.out.print("Enter Request ID to assign: ");
        String requestId = scanner.nextLine();

        System.out.print("Enter Staff Username to assign to: ");
        String staffUsername = scanner.nextLine();

        User staff = facade.getAllUsers().stream()
            .filter(u -> u.getUsername().equals(staffUsername) &&
                         u.getUserType() == UserType.STAFF)
            .findFirst()
            .orElseThrow(() -> new Exception("Staff user '" + staffUsername + "' not found in the system."));

        facade.assignMaintenance(currentUser, requestId, staff);
        System.out.println("Maintenance task assigned to " + staff.getUsername() + " successfully!");
    }
    
    private static void updateMaintenanceStatus() throws Exception {
        System.out.println("\n=== UPDATE MAINTENANCE STATUS ===");
        viewAllMaintenanceRequests();
        
        System.out.print("Enter Request ID: ");
        String requestId = scanner.nextLine();
        
        System.out.println("New Status:");
        System.out.println("1. PENDING");
        System.out.println("2. ASSIGNED");
        System.out.println("3. IN_PROGRESS");
        System.out.println("4. COMPLETED");
        System.out.print("Choose status (1-4): ");
        
        int statusChoice = Integer.parseInt(scanner.nextLine());
        MaintenanceStatus status = MaintenanceStatus.values()[statusChoice - 1];
        
        facade.updateMaintenanceStatus(currentUser, requestId, status);
        System.out.println("Status updated successfully!");
    }
    
    private static void viewAnalytics() {
        System.out.println("\n=== ADMIN ANALYTICS DASHBOARD ===");
        Map<String, Object> analytics = facade.getAnalytics();

        System.out.println("\n--- System Overview ---");
        System.out.println("Total Rooms: " + analytics.get("totalRooms"));
        System.out.println("Total Bookings: " + analytics.get("totalBookings"));
        System.out.println("Active Maintenance Requests: " + analytics.get("activeMaintenance"));

        System.out.println("\n--- Most Booked Rooms ---");
        @SuppressWarnings("unchecked")
        Map<String, Long> mostBooked = (Map<String, Long>) analytics.get("mostBookedRooms");
        if (mostBooked.isEmpty()) {
            System.out.println("No booking data available");
        } else {
            mostBooked.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(5)
                .forEach(entry -> System.out.println(entry.getKey() + ": " + entry.getValue() + " bookings"));
        }

        System.out.println("\n--- Frequent Maintenance Issues ---");
        @SuppressWarnings("unchecked")
        Map<String, Long> frequentIssues = (Map<String, Long>) analytics.get("frequentMaintenanceIssues");
        if (frequentIssues.isEmpty()) {
            System.out.println("No maintenance data available");
        } else {
            frequentIssues.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(5)
                .forEach(entry -> System.out.println(entry.getKey() + ": " + entry.getValue() + " reports"));
        }
    }
    
    private static void viewNotifications() {
        System.out.println("\n=== MY NOTIFICATIONS ===");
        List<Notification> notifications = facade.getUserNotifications(currentUser);
        
        if (notifications.isEmpty()) {
            System.out.println("No notifications.");
        } else {
            for (Notification notification : notifications) {
                System.out.println(notification);
            }
        }
    }
}