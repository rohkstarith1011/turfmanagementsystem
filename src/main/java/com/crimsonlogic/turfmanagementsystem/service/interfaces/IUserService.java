package com.crimsonlogic.turfmanagementsystem.service.interfaces;



import java.util.List;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.UserRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.UserResponseDTO;

public interface IUserService {

    UserResponseDTO createUser(UserRequestDTO requestDTO);

    List<UserResponseDTO> getAllUsers();

    UserResponseDTO getUserById(String userId);

    UserResponseDTO updateUser(String userId, UserRequestDTO requestDTO);

    void deactivateUser(String userId);
}