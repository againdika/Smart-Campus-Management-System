/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package patterns.factory;

import models.Room;
import enums.Equipment;

//FACTORY DESIGN PATTERN - Factory class
 
public class RoomFactory {
    
    public static Room createLectureHall(String roomId, String roomName, int capacity) {
        Room room = new Room(roomId, roomName, capacity, "Lecture Hall");
        room.addEquipment(Equipment.PROJECTOR);
        room.addEquipment(Equipment.WHITEBOARD);
        room.addEquipment(Equipment.SPEAKERS);
        return room;
    }    
    public static Room createComputerLab(String roomId, String roomName, int capacity) {
        Room room = new Room(roomId, roomName, capacity, "Computer Lab");
        room.addEquipment(Equipment.COMPUTER);
        room.addEquipment(Equipment.PROJECTOR);
        room.addEquipment(Equipment.AC);
        return room;
    }    
    public static Room createConferenceRoom(String roomId, String roomName, int capacity) {
        Room room = new Room(roomId, roomName, capacity, "Conference Room");
        room.addEquipment(Equipment.SMART_BOARD);
        room.addEquipment(Equipment.SPEAKERS);
        room.addEquipment(Equipment.AC);
        return room;
    }
    
    public static Room createSmartClassroom(String roomId, String roomName, int capacity) {
        Room room = new Room(roomId, roomName, capacity, "Smart Classroom");
        room.addEquipment(Equipment.SMART_BOARD);
        room.addEquipment(Equipment.PROJECTOR);
        room.addEquipment(Equipment.COMPUTER);
        room.addEquipment(Equipment.SPEAKERS);
        room.addEquipment(Equipment.AC);
        return room;
    }
}