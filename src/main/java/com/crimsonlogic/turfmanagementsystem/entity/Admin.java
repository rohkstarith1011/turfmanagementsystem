package com.crimsonlogic.turfmanagementsystem.entity;


import com.crimsonlogic.turfmanagementsystem.entity.enums.UserStatus;
import com.crimsonlogic.turfmanagementsystem.util.EntityIdGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "admin")
public class Admin extends AbstractUser {

    @Id
    @Column(name = "admin_id")
    private String adminId;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    public Admin() {
        super();
    }

    public Admin(String adminId,
                 String name,
                 String email,
                 String phone,
                 UserStatus status,
                 User user) {

        super(name, email, phone, status);

        this.adminId = adminId;
        this.user = user;
    }
    @PrePersist
    private void generateAdminId() {
        if (adminId == null || adminId.isBlank()) {
            adminId = EntityIdGenerator.generate("ADM");
        }
    }
    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
