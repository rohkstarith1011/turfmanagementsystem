package com.crimsonlogic.turfmanagementsystem.service.interfaces;

import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.TurfOwnerDashboardResponseDTO;

public interface ITurfOwnerDashboardService {

    TurfOwnerDashboardResponseDTO getDashboard(String ownerId);
}