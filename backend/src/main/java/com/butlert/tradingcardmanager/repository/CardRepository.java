/**
 * Timothy Butler
 * CEN 3024 - Software Development 1
 * June 18, 2025
 * CardRepository.java
 * This interface is used to define the functionality the responsibility must implement. It is used so
 * repository implementations can be swapped out easily (i.e. the database part of this project)
 */
package com.butlert.tradingcardmanager.repository;

import com.butlert.tradingcardmanager.model.Card;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    Optional<Card> findByCardNumber(int cardNumber);
    boolean existsByCardNumber(int cardNumber);

    @Transactional
    @Modifying
    int deleteByCardNumber(int cardNumber);
}