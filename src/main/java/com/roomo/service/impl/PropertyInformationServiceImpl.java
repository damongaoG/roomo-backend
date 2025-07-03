package com.roomo.service.impl;

import com.roomo.dto.PropertyInformationRequest;
import com.roomo.dto.PropertyInformationResponse;
import com.roomo.entity.PropertyInformation;
import com.roomo.exception.ResourceNotFoundException;
import com.roomo.repository.PropertyInformationRepository;
import com.roomo.service.PropertyInformationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PropertyInformationServiceImpl implements PropertyInformationService {

    private final PropertyInformationRepository propertyRepository;

    @Override
    public PropertyInformationResponse createProperty(PropertyInformationRequest request) {
        PropertyInformation property = mapToEntity(request);
        PropertyInformation savedProperty = propertyRepository.save(property);
        return mapToResponse(savedProperty);
    }

    @Override
    public PropertyInformationResponse updateProperty(Long id, PropertyInformationRequest request) {
        PropertyInformation property = propertyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Property not found with id: " + id));

        updateEntityFromRequest(property, request);
        PropertyInformation updatedProperty = propertyRepository.save(property);
        return mapToResponse(updatedProperty);
    }

    @Override
    @Transactional(readOnly = true)
    public PropertyInformationResponse getProperty(Long id) {
        PropertyInformation property = propertyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Property not found with id: " + id));
        return mapToResponse(property);
    }

    private PropertyInformation mapToEntity(PropertyInformationRequest request) {
        PropertyInformation property = new PropertyInformation();
        copyProperties(request, property);
        return property;
    }

    private void updateEntityFromRequest(PropertyInformation property, PropertyInformationRequest request) {
        copyProperties(request, property);
    }

    private void copyProperties(PropertyInformationRequest source, PropertyInformation target) {
        // Use Spring BeanUtils to automatically copy all matching properties
        BeanUtils.copyProperties(source, target);
    }

    private PropertyInformationResponse mapToResponse(PropertyInformation property) {
        PropertyInformationResponse response = new PropertyInformationResponse();

        // Use Spring BeanUtils to automatically copy all matching properties
        BeanUtils.copyProperties(property, response);

        // Set system-generated fields that are not present in the request
        response.setId(property.getId());
        response.setCreatedAt(property.getCreatedAt());
        response.setUpdatedAt(property.getUpdatedAt());

        return response;
    }
}
