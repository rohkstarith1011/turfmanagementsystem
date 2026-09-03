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
@Table(name = "turf_manager")
public class TurfManager extends AbstractUser {

    @Id
    @Column(name = "turf_manager_id")
    private String turfManagerId;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    public TurfManager() {
        super();
    }

    public TurfManager(String turfManagerId,
                       String name,
                       String email,
                       String phone,
                       UserStatus status,
                       User user) {

        super(name, email, phone, status);

        this.turfManagerId = turfManagerId;
        this.user = user;
    }

    public String getTurfManagerId() {
        return turfManagerId;
    }

    public void setTurfManagerId(String turfManagerId) {
        this.turfManagerId = turfManagerId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    @PrePersist
    private void generateTurfManagerId() {
        if (turfManagerId == null || turfManagerId.isBlank()) {
            turfManagerId = EntityIdGenerator.generate("TFM");
        }
    }
}