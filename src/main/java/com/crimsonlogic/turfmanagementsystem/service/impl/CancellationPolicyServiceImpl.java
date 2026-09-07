package com.crimsonlogic.turfmanagementsystem.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.CancellationPolicyRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.CancellationPolicyResponseDTO;
import com.crimsonlogic.turfmanagementsystem.entity.CancellationPolicy;
import com.crimsonlogic.turfmanagementsystem.entity.Facility;
import com.crimsonlogic.turfmanagementsystem.entity.User;
import com.crimsonlogic.turfmanagementsystem.repository.CancellationPolicyRepository;
import com.crimsonlogic.turfmanagementsystem.repository.FacilityRepository;
import com.crimsonlogic.turfmanagementsystem.repository.UserRepository;
import com.crimsonlogic.turfmanagementsystem.service.interfaces.ICancellationPolicyService;

@Service
public class CancellationPolicyServiceImpl
        implements ICancellationPolicyService {

    private final CancellationPolicyRepository policyRepository;
    private final FacilityRepository facilityRepository;
    private final UserRepository userRepository;

    public CancellationPolicyServiceImpl(
            CancellationPolicyRepository policyRepository,
            FacilityRepository facilityRepository,
            UserRepository userRepository) {

        this.policyRepository = policyRepository;
        this.facilityRepository = facilityRepository;
        this.userRepository = userRepository;
    }

    // ============================================================
    // CREATE POLICY
    // ============================================================

    @Override
    @Transactional
    public CancellationPolicyResponseDTO createPolicy(
            CancellationPolicyRequestDTO requestDTO) {

        Facility facility = facilityRepository
                .findById(requestDTO.getFacilityId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Facility not found"));

        User configuringUser = userRepository
                .findById(requestDTO.getConfiguredByUserId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Configuring user not found"));

        validateConfigurationUser(configuringUser);

        if (policyRepository
                .existsByFacilityFacilityId(
                        requestDTO.getFacilityId())) {

            throw new IllegalArgumentException(
                    "Cancellation policy already exists for this facility");
        }

        validatePolicyPercentages(requestDTO);
        validatePolicyHours(requestDTO);

        CancellationPolicy policy =
                new CancellationPolicy();

        policy.setFacility(facility);
        policy.setFullRefundHours(
                requestDTO.getFullRefundHours());
        policy.setPartialRefundHours(
                requestDTO.getPartialRefundHours());
        policy.setLimitedRefundHours(
                requestDTO.getLimitedRefundHours());

        policy.setFullRefundPercentage(
                requestDTO.getFullRefundPercentage());
        policy.setPartialRefundPercentage(
                requestDTO.getPartialRefundPercentage());
        policy.setLimitedRefundPercentage(
                requestDTO.getLimitedRefundPercentage());
        policy.setMinimumRefundPercentage(
                requestDTO.getMinimumRefundPercentage());

        policy.setStatus("ACTIVE");

        return mapToResponseDTO(
                policyRepository.save(policy));
    }

    // ============================================================
    // UPDATE POLICY
    // ============================================================

    @Override
    @Transactional
    public CancellationPolicyResponseDTO updatePolicy(
            String policyId,
            CancellationPolicyRequestDTO requestDTO) {

        CancellationPolicy policy =
                policyRepository.findById(policyId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Cancellation policy not found"));

        Facility facility =
                facilityRepository
                        .findById(requestDTO.getFacilityId())
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Facility not found"));

        User configuringUser =
                userRepository
                        .findById(
                                requestDTO.getConfiguredByUserId())
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Configuring user not found"));

        validateConfigurationUser(configuringUser);

        if (!policy.getFacility()
                .getFacilityId()
                .equals(facility.getFacilityId())) {

            throw new IllegalArgumentException(
                    "Policy facility cannot be changed");
        }

        validatePolicyPercentages(requestDTO);
        validatePolicyHours(requestDTO);

        policy.setFullRefundHours(
                requestDTO.getFullRefundHours());
        policy.setPartialRefundHours(
                requestDTO.getPartialRefundHours());
        policy.setLimitedRefundHours(
                requestDTO.getLimitedRefundHours());

        policy.setFullRefundPercentage(
                requestDTO.getFullRefundPercentage());
        policy.setPartialRefundPercentage(
                requestDTO.getPartialRefundPercentage());
        policy.setLimitedRefundPercentage(
                requestDTO.getLimitedRefundPercentage());
        policy.setMinimumRefundPercentage(
                requestDTO.getMinimumRefundPercentage());

        return mapToResponseDTO(
                policyRepository.save(policy));
    }

    // ============================================================
    // GET BY ID
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public CancellationPolicyResponseDTO getPolicyById(
            String policyId) {

        CancellationPolicy policy =
                policyRepository.findById(policyId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Cancellation policy not found"));

        return mapToResponseDTO(policy);
    }

    // ============================================================
    // GET BY FACILITY
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public CancellationPolicyResponseDTO getPolicyByFacility(
            String facilityId) {

        if (!facilityRepository.existsById(facilityId)) {

            throw new IllegalArgumentException(
                    "Facility not found");
        }

        CancellationPolicy policy =
                policyRepository
                        .findByFacilityFacilityId(facilityId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Cancellation policy not found for this facility"));

        return mapToResponseDTO(policy);
    }

    // ============================================================
    // GET ALL
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public List<CancellationPolicyResponseDTO> getAllPolicies() {

        return policyRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    // ============================================================
    // DEACTIVATE
    // ============================================================

    @Override
    @Transactional
    public void deactivatePolicy(String policyId) {

        CancellationPolicy policy =
                policyRepository.findById(policyId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Cancellation policy not found"));

        if ("INACTIVE".equalsIgnoreCase(
                policy.getStatus())) {

            throw new IllegalArgumentException(
                    "Cancellation policy is already INACTIVE");
        }

        policy.setStatus("INACTIVE");

        policyRepository.save(policy);
    }

    // ============================================================
    // VALIDATE CONFIGURING USER
    // ============================================================

    private void validateConfigurationUser(User user) {

        if (user.getStatus() == null ||
                !"ACTIVE".equalsIgnoreCase(
                        user.getStatus().name())) {

            throw new IllegalArgumentException(
                    "Configuring user must be ACTIVE");
        }

        /*
         * OWNER / ADMIN role validation will be enforced
         * through the existing role model.
         *
         * We will wire the role repository here after
         * confirming the exact existing role structure.
         */
    }

    // ============================================================
    // VALIDATE PERCENTAGES
    // ============================================================

    private void validatePolicyPercentages(
            CancellationPolicyRequestDTO requestDTO) {

        validatePercentage(
                requestDTO.getFullRefundPercentage(),
                "Full refund percentage");

        validatePercentage(
                requestDTO.getPartialRefundPercentage(),
                "Partial refund percentage");

        validatePercentage(
                requestDTO.getLimitedRefundPercentage(),
                "Limited refund percentage");

        validatePercentage(
                requestDTO.getMinimumRefundPercentage(),
                "Minimum refund percentage");
    }

    private void validatePercentage(
            Double percentage,
            String fieldName) {

        if (percentage < 0 || percentage > 100) {

            throw new IllegalArgumentException(
                    fieldName +
                    " must be between 0 and 100");
        }
    }

    // ============================================================
    // VALIDATE HOURS
    // ============================================================

    private void validatePolicyHours(
            CancellationPolicyRequestDTO requestDTO) {

        if (requestDTO.getFullRefundHours()
                <= requestDTO.getPartialRefundHours()) {

            throw new IllegalArgumentException(
                    "Full refund hours must be greater than partial refund hours");
        }

        if (requestDTO.getPartialRefundHours()
                <= requestDTO.getLimitedRefundHours()) {

            throw new IllegalArgumentException(
                    "Partial refund hours must be greater than limited refund hours");
        }
    }

    // ============================================================
    // MAP ENTITY → RESPONSE DTO
    // ============================================================

    private CancellationPolicyResponseDTO mapToResponseDTO(
            CancellationPolicy policy) {

        CancellationPolicyResponseDTO response =
                new CancellationPolicyResponseDTO();

        response.setPolicyId(
                policy.getPolicyId());

        response.setFacilityId(
                policy.getFacility()
                        .getFacilityId());

        response.setFullRefundHours(
                policy.getFullRefundHours());

        response.setPartialRefundHours(
                policy.getPartialRefundHours());

        response.setLimitedRefundHours(
                policy.getLimitedRefundHours());

        response.setFullRefundPercentage(
                policy.getFullRefundPercentage());

        response.setPartialRefundPercentage(
                policy.getPartialRefundPercentage());

        response.setLimitedRefundPercentage(
                policy.getLimitedRefundPercentage());

        response.setMinimumRefundPercentage(
                policy.getMinimumRefundPercentage());

        response.setStatus(
                policy.getStatus());

        return response;
    }
}