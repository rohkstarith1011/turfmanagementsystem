package com.crimsonlogic.turfmanagementsystem.service.interfaces;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.AdminRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.AdminResponseDTO;

import java.util.List;

public interface IAdminService {

    AdminResponseDTO createAdmin(AdminRequestDTO requestDTO);

    List<AdminResponseDTO> getAllAdmins();

    AdminResponseDTO getAdminById(String adminId);

    AdminResponseDTO getAdminByUserId(String userId);

    AdminResponseDTO updateAdmin(String adminId, AdminRequestDTO requestDTO);

    void deactivateAdmin(String adminId);
}