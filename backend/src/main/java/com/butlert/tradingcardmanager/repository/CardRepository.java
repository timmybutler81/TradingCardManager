/**
 * Timothy Butler
 * CEN 3024 - Software Development 1
 * June 18, 2025
 * CardRepository.java
 * This interface defines the data access operations for the Card entity. By using this interface, the persistence
 * layer can be easily swapped or mocked for testing.
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