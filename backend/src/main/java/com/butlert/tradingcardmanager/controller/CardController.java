/**
 * Timothy Butler
 * CEN 3024 - Software Development 1
 * June 18, 2025
 * CardController.java
 * This class is the main access point for the GUI and uses this exclusively to call the back end part of the
 * application. It has methods for all functionality of the app.
 */
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

@RestController
@RequestMapping("/api/cards")
public class CardController {
    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    /**
     * method: addCard
     * parameters: card
     * return: Card
     * purpose: access point to add card from GUI
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
     * method: deleteCard
     * parameters: card
     * return: completion message or error message
     * purpose: access point to delete card from GUI
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
     * method: updateCard
     * parameters: cardNumber, cardDTO
     * return: Card
     * purpose: updating a specific card
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
     * method: getAllCards
     * parameters: none
     * return: List of Cards
     * purpose: access point to list all cards from GUI
     */
    @GetMapping
    public ResponseEntity<List<Card>> getAllCards() {
        return ResponseEntity.ok(cardService.getAllCards());
    }

    /**
     * method: getCardById
     * parameters: cardId
     * return: Card
     * purpose: access point to look up a card by ID from GUI
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
     * method: calculateCollectionStatistics
     * parameters: none
     * return: Map of Stats and values
     * purpose: access point to retrieve card statistics for the GUI
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> calculateCollectionStatistics() {
        return ResponseEntity.ok(cardService.calculateCollectionStatistics());
    }

    /**
     * method: calculateCollectionValue
     * parameters: none
     * return: Map of owner/market and values
     * purpose: access point to retrieve collection value for the GUI
     */
    @GetMapping("/values")
    public ResponseEntity<Map<String, BigDecimal>> calculateCollectionValue() {
        return ResponseEntity.ok(cardService.calculateCollectionValues());
    }

    /**
     * method: importCardsFromFile
     * parameters: filePath
     * return: completion message or error message
     * purpose: access point to import cards from file for the GUI
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

