package com.roomo.repository;

import com.roomo.entity.PropertyInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyInformationRepository extends JpaRepository<PropertyInformation, Long> {
}
