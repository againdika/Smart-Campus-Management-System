
package models;

import enums.Equipment;
import java.util.*;

/**
 *
 * @author Aruna Indika
 */
public class Room implements Cloneable {
    private String roomId;
    private String roomName;
    private int capacity;
    private boolean isActive;
    private List<Equipment> equipmentList;
    private String roomType; // Lecture Hall, Lab, Conference Room
    
    public Room(String roomId, String roomName, int capacity, String roomType) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.capacity = capacity;
        this.roomType = roomType;
        this.isActive = true;
        this.equipmentList = new ArrayList<>();
    }
    
    // Getters and Setters
    public String getRoomId() { return roomId; }
    public String getRoomName() { return roomName; }
    public void setRoomName(String roomName) { this.roomName = roomName; }
    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
    public List<Equipment> getEquipmentList() { return equipmentList; }
    public String getRoomType() { return roomType; }
    
    public void addEquipment(Equipment equipment) {
        equipmentList.add(equipment);
    }
    
    public void removeEquipment(Equipment equipment) {
        equipmentList.remove(equipment);
    }
    
    // Shallow clone
    @Override
    public Room clone() {
        try {
            return (Room) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Clone not supported", e);
        }
    }
    
    // Deep clone
    public Room deepClone() {
        Room clonedRoom = new Room(this.roomId, this.roomName, this.capacity, this.roomType);
        clonedRoom.isActive = this.isActive;
        clonedRoom.equipmentList = new ArrayList<>(this.equipmentList);
        return clonedRoom;
    }
    
    @Override
    public String toString() {
        return String.format("Room{id='%s', name='%s', capacity=%d, type=%s, active=%s, equipment=%s}",
                roomId, roomName, capacity, roomType, isActive, equipmentList);
    }
}