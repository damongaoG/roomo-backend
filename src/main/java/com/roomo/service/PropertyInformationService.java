package com.roomo.service;

import com.roomo.dto.PropertyInformationRequest;
import com.roomo.dto.PropertyInformationResponse;

public interface PropertyInformationService {

    /**
     * Create a new property information
     *
     * @param request the property information request
     * @return the created property information response
     */
    PropertyInformationResponse createProperty(PropertyInformationRequest request);

    /**
     * Update an existing property information
     *
     * @param id      the property ID
     * @param request the property information request
     * @return the updated property information response
     */
    PropertyInformationResponse updateProperty(Long id, PropertyInformationRequest request);

    /**
     * Get property information by ID
     *
     * @param id the property ID
     * @return the property information response
     */
    PropertyInformationResponse getProperty(Long id);
}
