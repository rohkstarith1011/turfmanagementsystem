package com.crimsonlogic.turfmanagementsystem.entity;

import com.crimsonlogic.turfmanagementsystem.util.EntityIdGenerator;

import jakarta.persistence.*;

@Entity
@Table(
    name = "booking_players",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"booking_id", "player_id"})
    }
)
public class BookingPlayer {
 
    @Id
    @Column(name = "booking_player_id", nullable = false, unique = true)
    private String bookingPlayerId;
 
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;
 
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;
 
    @Column(name = "player_name", nullable = false)
    private String playerName;
 
    @Column(name = "status", nullable = false)
    private String status;
 
    public BookingPlayer() {
    }
 
    public BookingPlayer(String bookingPlayerId, Booking booking, Player player,
                          String playerName, String status) {
        this.bookingPlayerId = bookingPlayerId;
        this.booking = booking;
        this.player = player;
        this.playerName = playerName;
        this.status = status;
    }
 
    public String getBookingPlayerId() {
        return bookingPlayerId;
    }
 
    public void setBookingPlayerId(String bookingPlayerId) {
        this.bookingPlayerId = bookingPlayerId;
    }
 
    public Booking getBooking() {
        return booking;
    }
 
    public void setBooking(Booking booking) {
        this.booking = booking;
    }
 
    public Player getPlayer() {
        return player;
    }
 
    public void setPlayer(Player player) {
        this.player = player;
    }
 
    public String getPlayerName() {
        return playerName;
    }
 
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
 
    public String getStatus() {
        return status;
    }
 
    public void setStatus(String status) {
        this.status = status;
    }
    @PrePersist
    private void generateBookingPlayerId() {
        if (bookingPlayerId == null || bookingPlayerId.isBlank()) {
            bookingPlayerId = EntityIdGenerator.generate("BKP");
        }
    }
}
