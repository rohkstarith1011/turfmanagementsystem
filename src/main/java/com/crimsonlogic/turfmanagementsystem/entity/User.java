package com.crimsonlogic.turfmanagementsystem.entity;





import com.crimsonlogic.turfmanagementsystem.entity.enums.UserStatus;
import com.crimsonlogic.turfmanagementsystem.util.EntityIdGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User extends AbstractUser {

    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(nullable = false)
    private String password;
    
    public User() {
        super();
    }

    public User(String userId, String name, String email,
                String phone, UserStatus status, String password) {

        super(name, email, phone, status);
        this.userId = userId;
        this.password = password;
    }
    @PrePersist
    private void generateUserId() {
        if (userId == null || userId.isBlank()) {
            userId = EntityIdGenerator.generate("USR");
        }
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
   
}
