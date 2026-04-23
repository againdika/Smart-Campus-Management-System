
package models;
/**
 *
 * @author Aruna Indika
 */
import enums.BookingStatus;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Booking entity for room reservations
 */
public class Booking {
    private String bookingId;
    private Room room;
    private User user;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BookingStatus status;
    private String purpose;
    private boolean isRecurring;
    private String recurringPattern; 
    
    public Booking(Room room, User user, LocalDateTime startTime, LocalDateTime endTime, String purpose) {
        this.bookingId = generateBookingId();
        this.room = room;
        this.user = user;
        this.startTime = startTime;
        this.endTime = endTime;
        this.purpose = purpose;
        this.status = BookingStatus.PENDING;
        this.isRecurring = false;
    }
    
    private String generateBookingId() {
        return "BK" + System.currentTimeMillis() + (int)(Math.random() * 1000);
    }
    
    // Getters and Setters
    public String getBookingId() { return bookingId; }
    public Room getRoom() { return room; }
    public User getUser() { return user; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }
    public String getPurpose() { return purpose; }
    public boolean isRecurring() { return isRecurring; }
    public void setRecurring(boolean recurring, String pattern) { 
        isRecurring = recurring; 
        recurringPattern = pattern;
    }
    
    public boolean conflictsWith(Booking other) {
        // Fix #5: Use strict comparison so back-to-back bookings (e.g. 10:00-11:00 and
        // 11:00-12:00) are NOT treated as conflicts. A booking conflicts only when the
        // intervals genuinely overlap (one starts strictly before the other ends).
        return this.room.getRoomId().equals(other.room.getRoomId()) &&
               this.startTime.isBefore(other.endTime) &&
               other.startTime.isBefore(this.endTime);
    }
    
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return String.format("Booking{id='%s', room='%s', user='%s', time=%s to %s, status=%s}",
                bookingId, room.getRoomName(), user.getUsername(),
                startTime.format(formatter), endTime.format(formatter), status);
    }
}