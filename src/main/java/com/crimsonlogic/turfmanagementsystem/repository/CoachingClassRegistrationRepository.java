package com.crimsonlogic.turfmanagementsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crimsonlogic.turfmanagementsystem.entity.CoachingClassRegistration;

public interface CoachingClassRegistrationRepository
        extends JpaRepository<CoachingClassRegistration, String> {

    List<CoachingClassRegistration> findByCoachingClassCoachingClassId(
            String coachingClassId);

    List<CoachingClassRegistration> findByPlayerPlayerId(
            String playerId);

    boolean existsByCoachingClassCoachingClassIdAndPlayerPlayerId(
            String coachingClassId,
            String playerId);
    
    List<CoachingClassRegistration>
    findByCoachingClassNameContainingIgnoreCase(String name);

    List<CoachingClassRegistration>
    findByPlayerNameContainingIgnoreCase(String name);
}