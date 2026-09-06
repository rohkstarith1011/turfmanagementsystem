package com.crimsonlogic.turfmanagementsystem.service.impl;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.AdminRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.AdminResponseDTO;
import com.crimsonlogic.turfmanagementsystem.entity.Admin;
import com.crimsonlogic.turfmanagementsystem.entity.User;
import com.crimsonlogic.turfmanagementsystem.entity.UserRole;
import com.crimsonlogic.turfmanagementsystem.entity.enums.UserStatus;
import com.crimsonlogic.turfmanagementsystem.repository.AdminRepository;
import com.crimsonlogic.turfmanagementsystem.repository.UserRepository;
import com.crimsonlogic.turfmanagementsystem.repository.UserRoleRepository;
import com.crimsonlogic.turfmanagementsystem.service.interfaces.IAdminService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminServiceImpl implements IAdminService {

    private final AdminRepository adminRepository;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    public AdminServiceImpl(AdminRepository adminRepository,
                            UserRepository userRepository,
                            UserRoleRepository userRoleRepository) {
        this.adminRepository = adminRepository;
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public AdminResponseDTO createAdmin(AdminRequestDTO requestDTO) {

        User user = userRepository.findById(requestDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new IllegalArgumentException(
                    "Cannot create Admin profile for an inactive user");
        }

        if (!hasAdminRole(user.getUserId())) {
            throw new IllegalArgumentException(
                    "User does not have ADMIN role");
        }

        if (adminRepository.existsByUserUserId(user.getUserId())) {
            throw new IllegalArgumentException(
                    "Admin profile already exists for this user");
        }

        validateUserDetails(requestDTO, user);

        Admin admin = new Admin();
        admin.setName(user.getName());
        admin.setEmail(user.getEmail());
        admin.setPhone(user.getPhone());
        admin.setStatus(user.getStatus());
        admin.setUser(user);

        Admin savedAdmin = adminRepository.save(admin);

        return mapToResponseDTO(savedAdmin);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdminResponseDTO> getAllAdmins() {
        return adminRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AdminResponseDTO getAdminById(String adminId) {

        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new IllegalArgumentException("Admin not found"));

        return mapToResponseDTO(admin);
    }

    @Override
    @Transactional(readOnly = true)
    public AdminResponseDTO getAdminByUserId(String userId) {

        Admin admin = adminRepository.findByUserUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Admin profile not found for this user"));

        return mapToResponseDTO(admin);
    }

    @Override
    public AdminResponseDTO updateAdmin(String adminId, AdminRequestDTO requestDTO) {

        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new IllegalArgumentException("Admin not found"));

        User user = userRepository.findById(requestDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!admin.getUser().getUserId().equals(user.getUserId())) {
            throw new IllegalArgumentException("Admin profile cannot be reassigned to another user");
        }

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new IllegalArgumentException("Cannot update an inactive user's Admin profile");
        }

       

        user.setName(requestDTO.getName());
        user.setEmail(requestDTO.getEmail());
        user.setPhone(requestDTO.getPhone());

        userRepository.save(user);

        admin.setName(user.getName());
        admin.setEmail(user.getEmail());
        admin.setPhone(user.getPhone());

        Admin updatedAdmin = adminRepository.save(admin);
        return mapToResponseDTO(updatedAdmin);
    }

    @Override
    public void deactivateAdmin(String adminId) {

        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new IllegalArgumentException("Admin not found"));

        admin.setStatus(UserStatus.INACTIVE);

        adminRepository.save(admin);
    }

    private boolean hasAdminRole(String userId) {
        return userRoleRepository.findByUserUserId(userId)
                .stream()
                .map(UserRole::getRole)
                .anyMatch(role -> "ADMIN".equalsIgnoreCase(role.getRoleName()));
    }

    private AdminResponseDTO mapToResponseDTO(Admin admin) {

        AdminResponseDTO responseDTO = new AdminResponseDTO();

        responseDTO.setAdminId(admin.getAdminId());
        responseDTO.setUserId(admin.getUser().getUserId());
        responseDTO.setName(admin.getName());
        responseDTO.setEmail(admin.getEmail());
        responseDTO.setPhone(admin.getPhone());
        responseDTO.setStatus(admin.getStatus());

        return responseDTO;
    }
    private void validateUserDetails(AdminRequestDTO requestDTO, User user) {

        if (!user.getName().equals(requestDTO.getName())) {
            throw new IllegalArgumentException(
                    "Admin name must match the User name");
        }

        if (!user.getEmail().equalsIgnoreCase(requestDTO.getEmail())) {
            throw new IllegalArgumentException(
                    "Admin email must match the User email");
        }

        if (!user.getPhone().equals(requestDTO.getPhone())) {
            throw new IllegalArgumentException(
                    "Admin phone must match the User phone");
        }
    }
}