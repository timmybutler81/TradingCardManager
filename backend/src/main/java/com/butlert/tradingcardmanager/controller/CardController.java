package com.butlert.tradingcardmanager.controller;

import com.butlert.tradingcardmanager.model.Card;
import com.butlert.tradingcardmanager.model.CardDTO;
import com.butlert.tradingcardmanager.service.CardService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Main controller for the Trading Card Manager application.
 * <p>
 * This class acts as the REST API access point used by the GUI frontend to interact
 * with the backend. It includes endpoints for adding, updating, deleting, retrieving,
 * and importing cards, as well as generating statistics and value reports.
 * </p>
 *
 * <p><b>Author:</b> Timothy Butler<br>
 * <b>Course:</b> CEN 3024 - Software Development 1<br>
 * <b>Date:</b> July 18, 2025</p>
 */
@RestController
@RequestMapping("/api/cards")
public class CardController {
    /**
     * The service layer used to handle all business logic for card operations.
     */
    private final CardService cardService;

    /**
     * Constructs a new CardController with the specified CardService.
     *
     * @param cardService the service to handle card-related operations
     */
    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    /**
     * Adds a new card to the system using the data provided in the CardDTO.
     *
     * @param cardDTO the data transfer object containing card information to be added; must be valid
     * @return a ResponseEntity containing the result of the add operation, typically the created Card or an error response
     */
    @PostMapping
    public ResponseEntity<?> addCard(@Valid @RequestBody CardDTO cardDTO) {
        System.out.println("Received DTO: " + cardDTO);
        try {
            Optional<CardDTO> saved = cardService.addCard(cardDTO);

            if (saved.isEmpty()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("error", "Card already exists"));
            }

            URI location = URI.create("/api/cards/" + saved.get().getCardNumber());
            return ResponseEntity.created(location).body(saved.get());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Deletes a card from the system by its card number.
     *
     * @param cardNumber the unique identifier of the card to be deleted
     * @return a ResponseEntity containing a completion message or an error response
     */
    @DeleteMapping("/delete/{cardNumber}")
    public ResponseEntity<?> deleteCard(@PathVariable("cardNumber") int cardNumber) {
        boolean removed = cardService.deleteCard(cardNumber);
        return removed
                ? ResponseEntity.noContent().build()
                : ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Card not found"));
    }

    /**
     * Updates the details of an existing card based on the provided card number and new data.
     *
     * @param cardNumber the unique identifier of the card to be updated
     * @param cardDTO the new data for the card; must be valid
     * @return a ResponseEntity containing the updated card or an error response
     */
    @PutMapping("/put/{cardNumber}")
    public ResponseEntity<?> updateCard(@PathVariable("cardNumber") int cardNumber, @Valid @RequestBody CardDTO cardDTO) {
        System.out.println("Received DTO: " + cardDTO);

        try {
            Optional<Card> updated = cardService.updateCard(cardNumber, cardDTO);
            return updated.isPresent()
                    ? ResponseEntity.ok(updated.get())
                    : ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Card not found"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Retrieves all cards from the system.
     *
     * @return a ResponseEntity containing a list of all stored cards
     */
    @GetMapping
    public ResponseEntity<List<Card>> getAllCards() {
        return ResponseEntity.ok(cardService.getAllCards());
    }

    /**
     * Retrieves a card by its card number.
     *
     * @param cardNumber the unique identifier of the card to retrieve
     * @return a ResponseEntity containing the card if found, or an error message if not
     */
    @GetMapping("/get/{cardNumber}")
    public ResponseEntity<?> getCardById(@PathVariable("cardNumber") int cardNumber) {
        Card card = cardService.findByCardId(cardNumber);
        return card != null
                ? ResponseEntity.ok(card)
                : ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Card not found"));
    }

    /**
     * Calculates and retrieves various statistics about the current card collection.
     *
     * @return a ResponseEntity containing a map of statistical values
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> calculateCollectionStatistics() {
        return ResponseEntity.ok(cardService.calculateCollectionStatistics());
    }

    /**
     * Calculates and retrieves the total value of the card collection.
     *
     * @return a ResponseEntity containing a map with owner and market value data
     */
    @GetMapping("/values")
    public ResponseEntity<Map<String, BigDecimal>> calculateCollectionValue() {
        return ResponseEntity.ok(cardService.calculateCollectionValues());
    }

    /**
     * Imports cards from the provided file and adds them to the system.
     *
     * @param file the uploaded text file containing card data
     * @return a ResponseEntity containing the imported cards or an error message if upload fails
     */
    @PostMapping("/import")
    public ResponseEntity<?> importCardsFromFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "No file uploaded"));
        }

        List<Card> imported = cardService.addAllCardsFromFile(file);
        return ResponseEntity.ok(imported);
    }
}

