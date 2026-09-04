package com.crimsonlogic.turfmanagementsystem.dto.requestdtos;



import jakarta.validation.constraints.NotBlank;

public class BookingPlayerRequestDTO {

    @NotBlank(message = "Booking ID is required")
    private String bookingId;

    @NotBlank(message = "Player ID is required")
    private String playerId;

    @NotBlank(message = "Player name is required")
    private String playerName;

    // Getters and Setters

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}