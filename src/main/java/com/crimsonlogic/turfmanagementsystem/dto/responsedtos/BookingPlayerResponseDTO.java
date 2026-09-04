package com.crimsonlogic.turfmanagementsystem.dto.responsedtos;



public class BookingPlayerResponseDTO {

    private String bookingPlayerId;

    private String bookingId;

    private String playerId;

    private String playerName;

    private String status;

    // Getters and Setters

    public String getBookingPlayerId() {
        return bookingPlayerId;
    }

    public void setBookingPlayerId(String bookingPlayerId) {
        this.bookingPlayerId = bookingPlayerId;
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
