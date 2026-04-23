package test;

/**
 *
 * @author Aruna Indika
 */
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import models.*;
import enums.*;
import patterns.facade.CampusManagementFacade;
import patterns.singleton.NotificationManager;
import java.time.LocalDateTime;

public class SCMSTest {

    private CampusManagementFacade facade;

    @Before
    public void setUp() {
        facade = new CampusManagementFacade();
    }

    // ─── TEST 1: Valid Admin Login ──────────────
    // Scenario: Admin logs in with correct credentials
    // Expected: Returns User object with ADMINISTRATOR type
    @Test
    public void testValidAdminLogin() throws Exception {
        User user = facade.login("admin", "admin123");
        assertNotNull(user);
        assertEquals(UserType.ADMINISTRATOR, user.getUserType());
    }

    // ─── TEST 2: Valid Staff Login ──────────────
    // Scenario: Staff logs in with correct credentials
    // Expected: Returns User object with STAFF type
    @Test
    public void testValidStaffLogin() throws Exception {
        User user = facade.login("staff1", "staff123");
        assertNotNull(user);
        assertEquals(UserType.STAFF, user.getUserType());
    }

    // ─── TEST 3: Invalid Login ──────────────────
    // Scenario: User enters wrong password
    // Expected: Exception thrown — "Invalid credentials"
    @Test(expected = Exception.class)
    public void testInvalidLogin() throws Exception {
        facade.login("admin", "wrongpass");
    }

    // ─── TEST 4: Valid Room Booking by Admin ────
    // Scenario: Admin books a valid room with future time
    // Expected: Booking confirmed successfully
    @Test
    public void testValidRoomBookingByAdmin() throws Exception {
        User user = facade.login("admin", "admin123");
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = start.plusHours(2);
        Booking booking = facade.bookRoom(user, "R001", start, end, "Lecture");
        assertNotNull(booking);
        assertEquals(BookingStatus.CONFIRMED, booking.getStatus());
    }

    // ─── TEST 5: Valid Room Booking by Student ──
    // Scenario: Student books a valid room
    // Expected: Booking confirmed successfully
    @Test
    public void testValidRoomBookingByStudent() throws Exception {
        User user = facade.login("student1", "student123");
        LocalDateTime start = LocalDateTime.now().plusDays(2);
        LocalDateTime end = start.plusHours(1);
        Booking booking = facade.bookRoom(user, "R002", start, end, "Study");
        assertNotNull(booking);
        assertEquals(BookingStatus.CONFIRMED, booking.getStatus());
    }

    // ─── TEST 6: Double Booking Conflict ────────
    // Scenario: Same room booked twice for overlapping time
    // Expected: Exception — "Room is already booked for the requested time slot"
    @Test(expected = Exception.class)
    public void testDoubleBookingConflict() throws Exception {
        User user = facade.login("admin", "admin123");
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = start.plusHours(2);
        facade.bookRoom(user, "R001", start, end, "First Booking");
        facade.bookRoom(user, "R001", start, end, "Second Booking");
    }

    // ─── TEST 7: Booking Invalid Room ID ────────
    // Scenario: User tries to book a room that does not exist
    // Expected: Exception — "Room not found"
    @Test(expected = Exception.class)
    public void testBookingInvalidRoomId() throws Exception {
        User user = facade.login("admin", "admin123");
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = start.plusHours(2);
        facade.bookRoom(user, "INVALID999", start, end, "Test");
    }

    // ─── TEST 8: INTENTIONALLY FAILING TEST ─────
    // Scenario: Booking attempted with a past date
    // Expected: IllegalArgumentException
    // Reason: Past date validation not yet implemented
    // in BookingRequestBuilder — this test is expected to FAIL
    @Test(expected = IllegalArgumentException.class)
    public void testBookingPastDate() throws Exception {
        User user = facade.login("staff1", "staff123");
        LocalDateTime pastTime = LocalDateTime.now().minusDays(1);
        LocalDateTime endTime = pastTime.plusHours(1);
        facade.bookRoom(user, "R001", pastTime, endTime, "Test");
    }

    // ─── TEST 9: Cancel Valid Booking ───────────
    // Scenario: User cancels their own existing booking
    // Expected: Booking status changes to CANCELLED
    @Test
    public void testCancelBooking() throws Exception {
        User user = facade.login("admin", "admin123");
        LocalDateTime start = LocalDateTime.now().plusDays(2);
        LocalDateTime end = start.plusHours(1);
        Booking booking = facade.bookRoom(user, "R001", start, end, "Test");
        facade.cancelBooking(user, booking.getBookingId());
        assertEquals(BookingStatus.CANCELLED,
            facade.getUserBookings(user).get(0).getStatus());
    }

    // ─── TEST 10: Cancel Non-Existent Booking ───
    // Scenario: User tries to cancel a booking that does not exist
    // Expected: Exception — "Booking not found"
    @Test(expected = Exception.class)
    public void testCancelInvalidBooking() throws Exception {
        User user = facade.login("admin", "admin123");
        facade.cancelBooking(user, "INVALID_BOOKING_ID");
    }

    // ─── TEST 11: Report Maintenance — Valid ────
    // Scenario: Staff reports a maintenance issue for a valid room
    // Expected: MaintenanceRequest created with PENDING status
    @Test
    public void testReportMaintenance() throws Exception {
        User user = facade.login("staff1", "staff123");
        MaintenanceRequest request = facade.reportMaintenance(
            user, "R001", "Projector not working", Urgency.HIGH);
        assertNotNull(request);
        assertEquals(MaintenanceStatus.PENDING, request.getStatus());
    }

    // ─── TEST 12: Report Maintenance — Unauthorized
    // Scenario: Student tries to report a maintenance issue
    // Expected: Exception — "Unauthorized: Only Staff and Admins
    // can report maintenance issues"
    @Test(expected = Exception.class)
    public void testMaintenanceUnauthorizedByStudent() throws Exception {
        User student = facade.login("student1", "student123");
        facade.reportMaintenance(
            student, "R001", "Broken chair", Urgency.LOW);
    }

    // ─── TEST 13: Assign Maintenance Task ───────
    // Scenario: Admin assigns a maintenance request to a staff member
    // Expected: Request status changes to ASSIGNED
    @Test
    public void testAssignMaintenanceTask() throws Exception {
        User admin = facade.login("admin", "admin123");
        User staff = facade.login("staff1", "staff123");
        MaintenanceRequest request = facade.reportMaintenance(
            admin, "R001", "AC not working", Urgency.HIGH);
        facade.assignMaintenance(admin, request.getRequestId(), staff);
        assertEquals(MaintenanceStatus.ASSIGNED, request.getStatus());
    }

    // ─── TEST 14: Assign Maintenance — Unauthorized
    // Scenario: Staff tries to assign a maintenance task
    // Expected: Exception — "Unauthorized: Only admins can
    // assign maintenance tasks"
    @Test(expected = Exception.class)
    public void testAssignMaintenanceUnauthorized() throws Exception {
        User admin = facade.login("admin", "admin123");
        User staff = facade.login("staff1", "staff123");
        MaintenanceRequest request = facade.reportMaintenance(
            admin, "R001", "AC not working", Urgency.HIGH);
        facade.assignMaintenance(staff, request.getRequestId(), staff);
    }

    // ─── TEST 15: Update Maintenance Status ─────
    // Scenario: Admin updates maintenance request to COMPLETED
    // Expected: Request status changes to COMPLETED
    @Test
    public void testUpdateMaintenanceStatus() throws Exception {
        User admin = facade.login("admin", "admin123");
        MaintenanceRequest request = facade.reportMaintenance(
            admin, "R001", "Light broken", Urgency.LOW);
        facade.updateMaintenanceStatus(
            admin, request.getRequestId(), MaintenanceStatus.COMPLETED);
        assertEquals(MaintenanceStatus.COMPLETED, request.getStatus());
    }

    // ─── TEST 16: Notification on Booking ───────
    // Scenario: Notification sent when booking is confirmed
    // Expected: User notifications list is not empty
    @Test
    public void testNotificationOnBooking() throws Exception {
        User user = facade.login("admin", "admin123");
        LocalDateTime start = LocalDateTime.now().plusDays(3);
        LocalDateTime end = start.plusHours(1);
        facade.bookRoom(user, "R001", start, end, "Test");
        assertFalse(facade.getUserNotifications(user).isEmpty());
    }

    // ─── TEST 17: Notification on Cancellation ──
    // Scenario: Notification sent when booking is cancelled
    // Expected: User has at least 2 notifications
    // (one for booking, one for cancellation)
    @Test
    public void testNotificationOnCancellation() throws Exception {
        User user = facade.login("admin", "admin123");
        LocalDateTime start = LocalDateTime.now().plusDays(4);
        LocalDateTime end = start.plusHours(1);
        Booking booking = facade.bookRoom(user, "R001", start, end, "Test");
        facade.cancelBooking(user, booking.getBookingId());
        assertTrue(facade.getUserNotifications(user).size() >= 2);
    }

    // ─── TEST 18: Singleton NotificationManager ─
    // Scenario: Two calls to getInstance() return same object
    // Expected: Both instances are identical — same object in memory
    @Test
    public void testSingletonNotificationManager() {
        NotificationManager instance1 = NotificationManager.getInstance();
        NotificationManager instance2 = NotificationManager.getInstance();
        assertSame(instance1, instance2);
    }
}