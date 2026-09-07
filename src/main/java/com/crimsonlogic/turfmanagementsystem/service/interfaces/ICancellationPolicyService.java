package com.crimsonlogic.turfmanagementsystem.service.interfaces;

import java.util.List;

import com.crimsonlogic.turfmanagementsystem.dto.requestdtos.CancellationPolicyRequestDTO;
import com.crimsonlogic.turfmanagementsystem.dto.responsedtos.CancellationPolicyResponseDTO;

public interface ICancellationPolicyService {

    CancellationPolicyResponseDTO createPolicy(
            CancellationPolicyRequestDTO requestDTO);

    CancellationPolicyResponseDTO updatePolicy(
            String policyId,
            CancellationPolicyRequestDTO requestDTO);

    CancellationPolicyResponseDTO getPolicyById(
            String policyId);

    CancellationPolicyResponseDTO getPolicyByFacility(
            String facilityId);

    List<CancellationPolicyResponseDTO> getAllPolicies();

    void deactivatePolicy(String policyId);
}