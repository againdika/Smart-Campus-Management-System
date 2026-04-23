
package models;

import enums.MaintenanceStatus;
import enums.Urgency;
import java.time.LocalDateTime;

/**
 *
 * @author Aruna Indika
 */
public class MaintenanceRequest {
    private String requestId;
    private Room room;
    private User reportedBy;
    private String description;
    private Urgency urgency;
    private MaintenanceStatus status;
    private User assignedTo;
    private LocalDateTime reportedDate;
    private LocalDateTime completedDate;
    
    public MaintenanceRequest(Room room, User reportedBy, String description, Urgency urgency) {
        this.requestId = generateRequestId();
        this.room = room;
        this.reportedBy = reportedBy;
        this.description = description;
        this.urgency = urgency;
        this.status = MaintenanceStatus.PENDING;
        this.reportedDate = LocalDateTime.now();
    }
    
    private String generateRequestId() {
        return "MR" + System.currentTimeMillis() + (int)(Math.random() * 1000);
    }
    
    // Getters and Setters
    public String getRequestId() { return requestId; }
    public Room getRoom() { return room; }
    public User getReportedBy() { return reportedBy; }
    public String getDescription() { return description; }
    public Urgency getUrgency() { return urgency; }
    public MaintenanceStatus getStatus() { return status; }
    public void setStatus(MaintenanceStatus status) { this.status = status; }
    public User getAssignedTo() { return assignedTo; }
    public void setAssignedTo(User assignedTo) { this.assignedTo = assignedTo; }
    public LocalDateTime getReportedDate() { return reportedDate; }
    public LocalDateTime getCompletedDate() { return completedDate; }
    public void setCompletedDate(LocalDateTime completedDate) { this.completedDate = completedDate; }
    
    @Override
    public String toString() {
        return String.format("MaintenanceRequest{id='%s', room='%s', urgency=%s, status=%s}",
                requestId, room.getRoomName(), urgency.getLevel(), status.getDisplayName());
    }
}