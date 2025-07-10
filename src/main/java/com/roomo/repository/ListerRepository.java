package com.roomo.repository;

import com.roomo.entity.Lister;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ListerRepository extends JpaRepository<Lister, Long> {

    /**
     * Find a lister by user ID
     *
     * @param userId the user ID from JWT
     * @return Optional containing the lister if found
     */
    Optional<Lister> findByUserId(String userId);

    /**
     * Check if a lister exists for a given user ID
     *
     * @param userId the user ID from JWT
     * @return true if exists, false otherwise
     */
    boolean existsByUserId(String userId);
}
