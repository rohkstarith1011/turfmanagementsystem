package com.crimsonlogic.turfmanagementsystem.service.interfaces;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.CancellationRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.CancellationResponseDTO;

public interface ICancellationService {

    CancellationResponseDTO cancelBooking(
            CancellationRequestDTO requestDTO);
}