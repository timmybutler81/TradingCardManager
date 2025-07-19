package com.butlert.tradingcardmanager.repository;

import com.butlert.tradingcardmanager.model.Card;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for performing CRUD and custom operations on {@link Card} entities.
 * <p>
 * Extends {@link JpaRepository} to provide standard JPA functionality, and includes
 * custom methods for querying and deleting cards based on card number.
 * This abstraction makes the persistence layer easy to swap or mock for testing.
 * </p>
 *
 * <p><b>Author:</b> Timothy Butler<br>
 * <b>Course:</b> CEN 3024 - Software Development 1<br>
 * <b>Date:</b> June 18, 2025</p>
 */
@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    /**
     * Finds a card by its unique card number.
     *
     * @param cardNumber the card number to search for
     * @return an {@link Optional} containing the matching {@link Card}, or empty if not found
     */
    Optional<Card> findByCardNumber(int cardNumber);

    /**
     * Checks if a card exists with the specified card number.
     *
     * @param cardNumber the card number to check
     * @return true if a card with that number exists, false otherwise
     */
    boolean existsByCardNumber(int cardNumber);

    /**
     * Deletes a card by its unique card number.
     *
     * @param cardNumber the card number of the card to delete
     * @return the number of records deleted (should be 0 or 1)
     */
    @Transactional
    @Modifying
    int deleteByCardNumber(int cardNumber);
}