package com.crimsonlogic.turfmanagementsystem.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.TurfOwnerDashboardResponseDTO;
import com.crimsonlogic.turfmanagementsystem.service.interfaces.ITurfOwnerDashboardService;

@RestController
@RequestMapping("/api/turf-owner-dashboard")
public class TurfOwnerDashboardController {

    private final ITurfOwnerDashboardService dashboardService;

    public TurfOwnerDashboardController(
            ITurfOwnerDashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/{ownerId}")
    public ResponseEntity<TurfOwnerDashboardResponseDTO> getDashboard(
            @PathVariable String ownerId) {

        return ResponseEntity.ok(
                dashboardService.getDashboard(ownerId));
    }
}