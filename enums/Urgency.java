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
public enum Urgency {
    LOW("Low"),
    MEDIUM("Medium"),
    HIGH("High"),
    CRITICAL("Critical");
    
    private final String level;
    
    Urgency(String level) {
        this.level = level;
    }
    
    public String getLevel() {
        return level;
    }
}