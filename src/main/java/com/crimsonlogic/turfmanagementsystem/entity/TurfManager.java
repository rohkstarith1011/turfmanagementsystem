package com.crimsonlogic.turfmanagementsystem.entity;



import com.crimsonlogic.turfmanagementsystem.entity.enums.UserStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "turf_manager")
public class TurfManager extends AbstractUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "turf_manager_id")
    private Long turfManagerId;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    public TurfManager() {
        super();
    }

    public TurfManager(Long turfManagerId,
                       String name,
                       String email,
                       String phone,
                       UserStatus status,
                       User user) {

        super(name, email, phone, status);

        this.turfManagerId = turfManagerId;
        this.user = user;
    }

    public Long getTurfManagerId() {
        return turfManagerId;
    }

    public void setTurfManagerId(Long turfManagerId) {
        this.turfManagerId = turfManagerId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}