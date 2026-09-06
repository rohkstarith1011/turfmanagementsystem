package com.crimsonlogic.turfmanagementsystem.service.impl;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.AdminRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.AdminResponseDTO;
import com.crimsonlogic.turfmanagementsystem.entity.Admin;
import com.crimsonlogic.turfmanagementsystem.entity.Role;
import com.crimsonlogic.turfmanagementsystem.entity.User;
import com.crimsonlogic.turfmanagementsystem.entity.UserRole;
import com.crimsonlogic.turfmanagementsystem.entity.enums.UserStatus;
import com.crimsonlogic.turfmanagementsystem.repository.AdminRepository;
import com.crimsonlogic.turfmanagementsystem.repository.UserRepository;
import com.crimsonlogic.turfmanagementsystem.repository.UserRoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceImplTest {

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserRoleRepository userRoleRepository;

    @Mock
    private User user;

    @Mock
    private UserRole userRole;

    @Mock
    private Role role;

    @Mock
    private Admin admin;

    @InjectMocks
    private AdminServiceImpl adminService;

    private AdminRequestDTO requestDTO;

    @BeforeEach
    void setUp() {

        requestDTO = new AdminRequestDTO();

        requestDTO.setUserId("USR123456");
        requestDTO.setName("Admin");
        requestDTO.setEmail("admin@turf.com");
        requestDTO.setPhone("9847521365");
    }

    // ============================================================
    // CREATE ADMIN
    // ============================================================

    @Test
    void createAdminSuccessfully() {

        when(userRepository.findById("USR123456"))
                .thenReturn(Optional.of(user));

        when(user.getUserId())
                .thenReturn("USR123456");

        when(user.getStatus())
                .thenReturn(UserStatus.ACTIVE);

        when(user.getName())
                .thenReturn("Admin");

        when(user.getEmail())
                .thenReturn("admin@turf.com");

        when(user.getPhone())
                .thenReturn("9847521365");

        when(userRoleRepository.findByUserUserId("USR123456"))
                .thenReturn(List.of(userRole));

        when(userRole.getRole())
                .thenReturn(role);

        when(role.getRoleName())
                .thenReturn("ADMIN");

        Admin savedAdmin = new Admin();

        savedAdmin.setAdminId("ADM123456");
        savedAdmin.setUser(user);
        savedAdmin.setName("Admin");
        savedAdmin.setEmail("admin@turf.com");
        savedAdmin.setPhone("9847521365");
        savedAdmin.setStatus(UserStatus.ACTIVE);

        when(adminRepository.save(any(Admin.class)))
                .thenReturn(savedAdmin);

        AdminResponseDTO response =
                adminService.createAdmin(requestDTO);

        assertNotNull(response);
        assertEquals("ADM123456", response.getAdminId());
        assertEquals("USR123456", response.getUserId());
        assertEquals("Admin", response.getName());
        assertEquals("admin@turf.com", response.getEmail());
        assertEquals("9847521365", response.getPhone());
        assertEquals(UserStatus.ACTIVE, response.getStatus());

        verify(adminRepository, times(1))
                .save(any(Admin.class));
    }

    @Test
    void createAdmin_UserNotFound() {

        when(userRepository.findById("USR123456"))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> adminService.createAdmin(requestDTO)
        );

        assertEquals(
                "User not found",
                exception.getMessage()
        );

        verify(adminRepository, never())
                .save(any(Admin.class));
    }

    @Test
    void createAdmin_UserInactive() {

        when(userRepository.findById("USR123456"))
                .thenReturn(Optional.of(user));

        when(user.getStatus())
                .thenReturn(UserStatus.INACTIVE);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> adminService.createAdmin(requestDTO)
        );

        assertEquals(
                "Cannot create Admin profile for an inactive user",
                exception.getMessage()
        );

        verify(adminRepository, never())
                .save(any(Admin.class));
    }

    @Test
    void createAdmin_UserDoesNotHaveAdminRole() {

        when(userRepository.findById("USR123456"))
                .thenReturn(Optional.of(user));

        when(user.getUserId())
                .thenReturn("USR123456");

        when(user.getStatus())
                .thenReturn(UserStatus.ACTIVE);

        when(userRoleRepository.findByUserUserId("USR123456"))
                .thenReturn(List.of(userRole));

        when(userRole.getRole())
                .thenReturn(role);

        when(role.getRoleName())
                .thenReturn("PLAYER");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> adminService.createAdmin(requestDTO)
        );

        assertEquals(
                "User does not have ADMIN role",
                exception.getMessage()
        );

        verify(adminRepository, never())
                .save(any(Admin.class));
    }

    @Test
    void createAdmin_NameDoesNotMatchUser() {

        when(userRepository.findById("USR123456"))
                .thenReturn(Optional.of(user));

        when(user.getUserId())
                .thenReturn("USR123456");

        when(user.getStatus())
                .thenReturn(UserStatus.ACTIVE);

        when(user.getName())
                .thenReturn("Original Admin");

        when(userRoleRepository.findByUserUserId("USR123456"))
                .thenReturn(List.of(userRole));

        when(userRole.getRole())
                .thenReturn(role);

        when(role.getRoleName())
                .thenReturn("ADMIN");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> adminService.createAdmin(requestDTO)
        );

        assertEquals(
                "Admin name must match the User name",
                exception.getMessage()
        );

        verify(adminRepository, never())
                .save(any(Admin.class));
    }

    @Test
    void createAdmin_EmailDoesNotMatchUser() {

        when(userRepository.findById("USR123456"))
                .thenReturn(Optional.of(user));

        when(user.getUserId())
                .thenReturn("USR123456");

        when(user.getStatus())
                .thenReturn(UserStatus.ACTIVE);

        when(user.getName())
                .thenReturn("Admin");

        when(user.getEmail())
                .thenReturn("admin@turf.com");

        when(userRoleRepository.findByUserUserId("USR123456"))
                .thenReturn(List.of(userRole));

        when(userRole.getRole())
                .thenReturn(role);

        when(role.getRoleName())
                .thenReturn("ADMIN");

        requestDTO.setEmail("wrong@gmail.com");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> adminService.createAdmin(requestDTO)
        );

        assertEquals(
                "Admin email must match the User email",
                exception.getMessage()
        );

        verify(adminRepository, never())
                .save(any(Admin.class));
    }

    @Test
    void createAdmin_PhoneDoesNotMatchUser() {

        when(userRepository.findById("USR123456"))
                .thenReturn(Optional.of(user));

        when(user.getUserId())
                .thenReturn("USR123456");

        when(user.getStatus())
                .thenReturn(UserStatus.ACTIVE);

        when(user.getName())
                .thenReturn("Admin");

        when(user.getEmail())
                .thenReturn("admin@turf.com");

        when(user.getPhone())
                .thenReturn("9847521365");

        when(userRoleRepository.findByUserUserId("USR123456"))
                .thenReturn(List.of(userRole));

        when(userRole.getRole())
                .thenReturn(role);

        when(role.getRoleName())
                .thenReturn("ADMIN");

        requestDTO.setPhone("9876543210");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> adminService.createAdmin(requestDTO)
        );

        assertEquals(
                "Admin phone must match the User phone",
                exception.getMessage()
        );

        verify(adminRepository, never())
                .save(any(Admin.class));
    }

    @Test
    void createAdmin_AdminProfileAlreadyExists() {

        when(userRepository.findById("USR123456"))
                .thenReturn(Optional.of(user));

        when(user.getStatus())
                .thenReturn(UserStatus.ACTIVE);

        when(user.getUserId())
                .thenReturn("USR123456");

        when(userRoleRepository.findByUserUserId("USR123456"))
                .thenReturn(List.of(userRole));

        when(userRole.getRole())
                .thenReturn(role);

        when(role.getRoleName())
                .thenReturn("ADMIN");

        when(adminRepository.existsByUserUserId("USR123456"))
                .thenReturn(true);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> adminService.createAdmin(requestDTO)
        );

        assertEquals(
                "Admin profile already exists for this user",
                exception.getMessage()
        );

        verify(adminRepository, never())
                .save(any(Admin.class));
    }

    // ============================================================
    // GET ALL ADMINS
    // ============================================================

    @Test
    void getAllAdmins() {

        Admin admin1 = new Admin();

        admin1.setAdminId("ADM123456");
        admin1.setUser(user);
        admin1.setName("Admin");
        admin1.setEmail("admin@turf.com");
        admin1.setPhone("9847521365");
        admin1.setStatus(UserStatus.ACTIVE);

        when(adminRepository.findAll())
                .thenReturn(List.of(admin1));

        when(user.getUserId())
                .thenReturn("USR123456");

        List<AdminResponseDTO> response =
                adminService.getAllAdmins();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("ADM123456",
                response.get(0).getAdminId());
        assertEquals("USR123456",
                response.get(0).getUserId());

        verify(adminRepository, times(1))
                .findAll();
    }

    // ============================================================
    // GET ADMIN BY ADMIN ID
    // ============================================================

    @Test
    void getAdminByAdminId() {

        when(adminRepository.findById("ADM123456"))
                .thenReturn(Optional.of(admin));

        when(admin.getAdminId())
                .thenReturn("ADM123456");

        when(admin.getUser())
                .thenReturn(user);

        when(user.getUserId())
                .thenReturn("USR123456");

        when(admin.getName())
                .thenReturn("Admin");

        when(admin.getEmail())
                .thenReturn("admin@turf.com");

        when(admin.getPhone())
                .thenReturn("9847521365");

        when(admin.getStatus())
                .thenReturn(UserStatus.ACTIVE);

        AdminResponseDTO response =
                adminService.getAdminById("ADM123456");

        assertNotNull(response);
        assertEquals("ADM123456",
                response.getAdminId());
        assertEquals("USR123456",
                response.getUserId());
        assertEquals("Admin",
                response.getName());
        assertEquals("admin@turf.com",
                response.getEmail());
        assertEquals("9847521365",
                response.getPhone());
        assertEquals(UserStatus.ACTIVE,
                response.getStatus());
    }

    @Test
    void getAdminByAdminId_AdminNotFound() {

        when(adminRepository.findById("ADM123456"))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> adminService.getAdminById("ADM123456")
        );

        assertEquals(
                "Admin not found",
                exception.getMessage()
        );
    }

    // ============================================================
    // GET ADMIN BY USER ID
    // ============================================================

    @Test
    void getAdminByUserId() {

        when(adminRepository.findByUserUserId("USR123456"))
                .thenReturn(Optional.of(admin));

        when(admin.getAdminId())
                .thenReturn("ADM123456");

        when(admin.getUser())
                .thenReturn(user);

        when(user.getUserId())
                .thenReturn("USR123456");

        when(admin.getName())
                .thenReturn("Admin");

        when(admin.getEmail())
                .thenReturn("admin@turf.com");

        when(admin.getPhone())
                .thenReturn("9847521365");

        when(admin.getStatus())
                .thenReturn(UserStatus.ACTIVE);

        AdminResponseDTO response =
                adminService.getAdminByUserId("USR123456");

        assertNotNull(response);
        assertEquals("ADM123456",
                response.getAdminId());
        assertEquals("USR123456",
                response.getUserId());
    }

    @Test
    void getAdminByUserId_AdminProfileNotFound() {

        when(adminRepository.findByUserUserId("USR123456"))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> adminService.getAdminByUserId("USR123456")
        );

        assertEquals(
                "Admin profile not found for this user",
                exception.getMessage()
        );
    }

    // ============================================================
    // UPDATE ADMIN
    // ============================================================

    @Test
    void updateAdminSuccessfully() {
        AdminRequestDTO requestDTO = new AdminRequestDTO();
        requestDTO.setUserId("USR001");
        requestDTO.setName("Updated Admin");
        requestDTO.setEmail("updatedadmin@gmail.com");
        requestDTO.setPhone("9999999999");

        User user = mock(User.class);

        when(user.getUserId()).thenReturn("USR001");
        when(user.getStatus()).thenReturn(UserStatus.ACTIVE);
        when(user.getName()).thenReturn("Updated Admin");
        when(user.getEmail()).thenReturn("updatedadmin@gmail.com");
        when(user.getPhone()).thenReturn("9999999999");

        Admin admin = new Admin();
        admin.setAdminId("ADM001");
        admin.setUser(user);
        admin.setName("Old Admin");
        admin.setEmail("oldadmin@gmail.com");
        admin.setPhone("8888888888");
        admin.setStatus(UserStatus.ACTIVE);

        when(adminRepository.findById("ADM001"))
                .thenReturn(Optional.of(admin));

        when(userRepository.findById("USR001"))
                .thenReturn(Optional.of(user));

        when(adminRepository.save(admin))
                .thenReturn(admin);

        AdminResponseDTO result =
                adminService.updateAdmin("ADM001", requestDTO);

        assertNotNull(result);

        assertEquals("ADM001", result.getAdminId());
        assertEquals("USR001", result.getUserId());
        assertEquals("Updated Admin", result.getName());
        assertEquals("updatedadmin@gmail.com", result.getEmail());
        assertEquals("9999999999", result.getPhone());
        assertEquals(UserStatus.ACTIVE, result.getStatus());

        assertEquals("Updated Admin", admin.getName());
        assertEquals("updatedadmin@gmail.com", admin.getEmail());
        assertEquals("9999999999", admin.getPhone());

        verify(adminRepository).findById("ADM001");
        verify(userRepository).findById("USR001");
        verify(adminRepository).save(admin);
    }

    @Test
    void updateAdmin_AdminNotFound() {

        when(adminRepository.findById("ADM123456"))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> adminService.updateAdmin(
                        "ADM123456",
                        requestDTO
                )
        );

        assertEquals(
                "Admin not found",
                exception.getMessage()
        );

        verify(adminRepository, never())
                .save(any(Admin.class));

        verify(userRepository, never())
                .save(any(User.class));
    }

    @Test
    void updateAdmin_UserNotFound() {

        when(adminRepository.findById("ADM123456"))
                .thenReturn(Optional.of(admin));

        when(userRepository.findById("USR123456"))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> adminService.updateAdmin(
                        "ADM123456",
                        requestDTO
                )
        );

        assertEquals(
                "User not found",
                exception.getMessage()
        );

        verify(adminRepository, never())
                .save(any(Admin.class));

        verify(userRepository, never())
                .save(any(User.class));
    }

    @Test
    void updateAdmin_CannotReassignToAnotherUser() {

        when(adminRepository.findById("ADM123456"))
                .thenReturn(Optional.of(admin));

        when(admin.getUser())
                .thenReturn(user);

        when(user.getUserId())
                .thenReturn("USR123456");

        User anotherUser = mock(User.class);

        when(userRepository.findById("USR999999"))
                .thenReturn(Optional.of(anotherUser));

        requestDTO.setUserId("USR999999");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> adminService.updateAdmin(
                        "ADM123456",
                        requestDTO
                )
        );

        assertEquals(
                "Admin profile cannot be reassigned to another user",
                exception.getMessage()
        );

        verify(adminRepository, never())
                .save(any(Admin.class));

        verify(userRepository, never())
                .save(any(User.class));
    }

    @Test
    void updateAdmin_InactiveUserRejected() {

        when(adminRepository.findById("ADM123456"))
                .thenReturn(Optional.of(admin));

        when(admin.getUser())
                .thenReturn(user);

        when(user.getUserId())
                .thenReturn("USR123456");

        when(userRepository.findById("USR123456"))
                .thenReturn(Optional.of(user));

        when(user.getStatus())
                .thenReturn(UserStatus.INACTIVE);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> adminService.updateAdmin(
                        "ADM123456",
                        requestDTO
                )
        );

        assertEquals(
                "Cannot update an inactive user's Admin profile",
                exception.getMessage()
        );

        verify(adminRepository, never())
                .save(any(Admin.class));

        verify(userRepository, never())
                .save(any(User.class));
    }

    @Test
    void updateAdmin_BasicInformationUpdatedSuccessfully() {
        AdminRequestDTO requestDTO = new AdminRequestDTO();
        requestDTO.setUserId("USR001");
        requestDTO.setName("New Admin Name");
        requestDTO.setEmail("newadmin@gmail.com");
        requestDTO.setPhone("7777777777");

        User user = mock(User.class);

        when(user.getUserId()).thenReturn("USR001");
        when(user.getStatus()).thenReturn(UserStatus.ACTIVE);
        when(user.getName()).thenReturn("New Admin Name");
        when(user.getEmail()).thenReturn("newadmin@gmail.com");
        when(user.getPhone()).thenReturn("7777777777");

        Admin admin = new Admin();
        admin.setAdminId("ADM001");
        admin.setUser(user);
        admin.setName("Old Admin Name");
        admin.setEmail("oldadmin@gmail.com");
        admin.setPhone("6666666666");
        admin.setStatus(UserStatus.ACTIVE);

        when(adminRepository.findById("ADM001"))
                .thenReturn(Optional.of(admin));

        when(userRepository.findById("USR001"))
                .thenReturn(Optional.of(user));

        when(adminRepository.save(admin))
                .thenReturn(admin);

        AdminResponseDTO result =
                adminService.updateAdmin("ADM001", requestDTO);

        assertNotNull(result);

        // Admin must now contain the User's current information
        assertEquals("New Admin Name", admin.getName());
        assertEquals("newadmin@gmail.com", admin.getEmail());
        assertEquals("7777777777", admin.getPhone());

        // Verify response
        assertEquals("ADM001", result.getAdminId());
        assertEquals("USR001", result.getUserId());
        assertEquals("New Admin Name", result.getName());
        assertEquals("newadmin@gmail.com", result.getEmail());
        assertEquals("7777777777", result.getPhone());

        verify(adminRepository).findById("ADM001");
        verify(userRepository).findById("USR001");
        verify(adminRepository).save(admin);
    }

    // ============================================================
    // DEACTIVATE ADMIN
    // ============================================================

    @Test
    void deactivateAdminSuccessfully() {

        when(adminRepository.findById("ADM123456"))
                .thenReturn(Optional.of(admin));

        when(adminRepository.save(admin))
                .thenReturn(admin);

        adminService.deactivateAdmin("ADM123456");

        verify(admin)
                .setStatus(UserStatus.INACTIVE);

        verify(adminRepository, times(1))
                .save(admin);
    }

    @Test
    void deactivateAdmin_AdminNotFound() {

        when(adminRepository.findById("ADM123456"))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> adminService.deactivateAdmin("ADM123456")
        );

        assertEquals(
                "Admin not found",
                exception.getMessage()
        );

        verify(adminRepository, never())
                .save(any(Admin.class));
    }
}