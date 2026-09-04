package com.crimsonlogic.turfmanagementsystem.service.interfaces;



import java.util.List;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.UserRoleRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.UserRoleResponseDTO;
import com.crimsonlogic.turfmanagementsystem.entity.UserRole;

public interface IUserRoleService {

    UserRoleResponseDTO assignRole(UserRoleRequestDTO requestDTO);

    List<UserRoleResponseDTO> getAllUserRoles();

    UserRoleResponseDTO getUserRoleById(String userRoleId);

    List<UserRoleResponseDTO> getRolesByUserId(String userId);

    void removeRole(String userRoleId);
    
    List<UserRoleResponseDTO> getRolesByUserName(String name);
}