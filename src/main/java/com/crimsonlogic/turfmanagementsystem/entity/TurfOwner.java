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
@Table(name = "turf_owner")
public class TurfOwner extends AbstractUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "turf_owner_id")
    private Long turfOwnerId;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    public TurfOwner() {
        super();
    }

    public TurfOwner(Long turfOwnerId,
                     String name,
                     String email,
                     String phone,
                     UserStatus status,
                     User user) {

        super(name, email, phone, status);

        this.turfOwnerId = turfOwnerId;
        this.user = user;
    }

    public Long getTurfOwnerId() {
        return turfOwnerId;
    }

    public void setTurfOwnerId(Long turfOwnerId) {
        this.turfOwnerId = turfOwnerId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
