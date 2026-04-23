/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;
import enums.UserType;
import java.util.Objects;
import java.util.UUID;

public class User {
    private String userId;
    private String username;
    private String password;
    private String email;
    private UserType userType;    
  
    public User(String username, String password, String email, UserType userType) {
        this.userId = UUID.randomUUID().toString().substring(0, 8);
        this.username = username;
        this.password = password;
        this.email = email;
        this.userType = userType;    }    
    // Getters and Setters
    public String getUserId() { return userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public UserType getUserType() { return userType; }
    public void setUserType(UserType userType) { this.userType = userType; }
    
    // Deep cloning using copy constructor
    public User(User other) {
        this.userId = other.userId;
        this.username = other.username;
        this.password = other.password;
        this.email = other.email;
        this.userType = other.userType;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return Objects.equals(userId, user.userId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }
    
    @Override
    public String toString() {
        return String.format("User{id='%s', name='%s', type=%s}", userId, username, userType);
    }
}