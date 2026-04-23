/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package enums;
/**
 *
 * @author Aruna Indika
 */
public enum Equipment {
    PROJECTOR("Projector"),
    WHITEBOARD("Whiteboard"),
    SMART_BOARD("Smart Board"),
    COMPUTER("Computer"),
    AC("Air Conditioner"),
    SPEAKERS("Speakers");
    
    private final String name;
    
    Equipment(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
}