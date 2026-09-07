package com.crimsonlogic.turfmanagementsystem.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.crimsonlogic.turfmanagementsystem.entity.enums.BookingStatus;
import com.crimsonlogic.turfmanagementsystem.util.EntityIdGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
 
@Entity
@Table(name = "booking")
public class Booking {
 
    @Id
    @Column(name = "booking_id")
    private String bookingId;
 
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;
 
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "slot_id", nullable = false)
    private Slot slot;
 
    @Column(nullable = false)
    private LocalDate bookingDate;
 
    @Column(nullable = false)
    private LocalTime startTime;
 
    @Column(nullable = false)
    private LocalTime endTime;
 
    @Column(nullable = false)
    private Integer numberOfPlayers;
 
    @Column(nullable = false)
    private Double price;
 
    @Column(nullable = false)
    private Double discount;
 
    @Column(nullable = false)
    private Double tax;
 
    @Column(nullable = false)
    private Double totalAmount;
 
    @OneToOne(mappedBy = "booking", fetch = FetchType.LAZY)
    private Payment payment;
    
    @Column(name = "need_coach", nullable = false)
    private Boolean needCoach;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status;
 
    @Column(nullable = false)
    private LocalDateTime createdAt;
 
    @Column(nullable = false)
    private LocalDateTime updatedAt;
 
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coach_id")
    private Coach coach;
    
    public Booking() {
    }
 
//    public Booking(String bookingId, Player player, Slot slot,
//                   LocalDate bookingDate, LocalTime startTime,
//                   LocalTime endTime, Integer numberOfPlayers,
//                   Double price, Double discount, Double tax,
//                   Double totalAmount, Payment payment,
//                   BookingStatus status, LocalDateTime createdAt,
//                   LocalDateTime updatedAt) {
// 
//        this.bookingId = bookingId;
//        this.player = player;
//        this.slot = slot;
//        this.bookingDate = bookingDate;
//        this.startTime = startTime;
//        this.endTime = endTime;
//        this.numberOfPlayers = numberOfPlayers;
//        this.price = price;
//        this.discount = discount;
//        this.tax = tax;
//        this.totalAmount = totalAmount;
//        this.payment = payment;
//        this.status = status;
//        this.createdAt = createdAt;
//        this.updatedAt = updatedAt;
//    }
 
    
    public String getBookingId() {
        return bookingId;
    }
 
    public Booking(String bookingId, Player player, Slot slot, LocalDate bookingDate, LocalTime startTime,
		LocalTime endTime, Integer numberOfPlayers, Double price, Double discount, Double tax, Double totalAmount,
		Payment payment, Boolean needCoach, BookingStatus status, LocalDateTime createdAt, LocalDateTime updatedAt,
		Coach coach) {
	super();
	this.bookingId = bookingId;
	this.player = player;
	this.slot = slot;
	this.bookingDate = bookingDate;
	this.startTime = startTime;
	this.endTime = endTime;
	this.numberOfPlayers = numberOfPlayers;
	this.price = price;
	this.discount = discount;
	this.tax = tax;
	this.totalAmount = totalAmount;
	this.payment = payment;
	this.needCoach = needCoach;
	this.status = status;
	this.createdAt = createdAt;
	this.updatedAt = updatedAt;
	this.coach = coach;
}

	public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }
 
    public Player getPlayer() {
        return player;
    }
 
    public void setPlayer(Player player) {
        this.player = player;
    }
 
    public Slot getSlot() {
        return slot;
    }
 
    public void setSlot(Slot slot) {
        this.slot = slot;
    }
 
    public LocalDate getBookingDate() {
        return bookingDate;
    }
 
    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }
 
    public LocalTime getStartTime() {
        return startTime;
    }
 
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }
 
    public LocalTime getEndTime() {
        return endTime;
    }
 
    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }
 
    public Integer getNumberOfPlayers() {
        return numberOfPlayers;
    }
 
    public void setNumberOfPlayers(Integer numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }
 
    public Double getPrice() {
        return price;
    }
 
    public void setPrice(Double price) {
        this.price = price;
    }
 
    public Double getDiscount() {
        return discount;
    }
 
    public void setDiscount(Double discount) {
        this.discount = discount;
    }
 
    public Double getTax() {
        return tax;
    }
 
    public void setTax(Double tax) {
        this.tax = tax;
    }
 
    public Double getTotalAmount() {
        return totalAmount;
    }
 
    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }
 
    public Payment getPayment() {
        return payment;
    }
 
    public void setPayment(Payment payment) {
        this.payment = payment;
    }
 
    public BookingStatus getStatus() {
        return status;
    }
 
    public void setStatus(BookingStatus status) {
        this.status = status;
    }
 
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
 
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
 
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
 
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    
    public Boolean getNeedCoach() {
		return needCoach;
	}

	public void setNeedCoach(Boolean needCoach) {
		this.needCoach = needCoach;
	}

	public Coach getCoach() {
		return coach;
	}

	public void setCoach(Coach coach) {
		this.coach = coach;
	}

	@PrePersist
    private void generateBookingId() {
        if (bookingId == null || bookingId.isBlank()) {
            bookingId = EntityIdGenerator.generate("BKI");
        }
    }
}
