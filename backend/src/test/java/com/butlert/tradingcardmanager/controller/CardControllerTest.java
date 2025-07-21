package com.butlert.tradingcardmanager.controller;

import com.butlert.tradingcardmanager.controller.CardController;
import com.butlert.tradingcardmanager.model.Card;
import com.butlert.tradingcardmanager.model.CardDTO;
import com.butlert.tradingcardmanager.service.CardService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CardController.class)
class CardControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockBean private CardService cardService;

    private CardDTO validDTO;
    private Card validCard;

    @BeforeEach
    void setUp() {
        validDTO = new CardDTO(123, "Test Card", "Magic", "RARE", "5", LocalDate.now().toString(), LocalDate.now().toString(), true);
        validCard = new Card();
        validCard.setCardNumber(123);
        validCard.setCardName("Test Card");
        validCard.setCardGame("Magic");
        validCard.setPurchasePrice(BigDecimal.valueOf(5));
        validCard.setFoiled(true);
        validCard.setDatePurchased(LocalDate.now());
        validCard.setDateSetPublished(LocalDate.now());
    }

    @Test
    void testAddCard_success() throws Exception {
        when(cardService.addCard(any(CardDTO.class))).thenReturn(Optional.of(validDTO));

        mockMvc.perform(post("/api/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.cardNumber").value(123));
    }

    @Test
    void testAddCard_duplicateCard_conflict() throws Exception {
        when(cardService.addCard(any(CardDTO.class))).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validDTO)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Card already exists"));
    }

    @Test
    void testAddCard_invalidCard_badRequest() throws Exception {
        when(cardService.addCard(any(CardDTO.class)))
                .thenThrow(new IllegalArgumentException("Validation failed"));

        mockMvc.perform(post("/api/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation failed"));
    }

    @Test
    void testDeleteCard_success() throws Exception {
        when(cardService.deleteCard(123)).thenReturn(true);

        mockMvc.perform(delete("/api/cards/delete/123"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteCard_notFound() throws Exception {
        when(cardService.deleteCard(999)).thenReturn(false);

        mockMvc.perform(delete("/api/cards/delete/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Card not found"));
    }

    @Test
    void testUpdateCard_success() throws Exception {
        when(cardService.updateCard(eq(123), any(CardDTO.class)))
                .thenReturn(Optional.of(validCard));

        mockMvc.perform(put("/api/cards/put/123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cardNumber").value(123));
    }

    @Test
    void testUpdateCard_notFound() throws Exception {
        when(cardService.updateCard(eq(123), any(CardDTO.class)))
                .thenReturn(Optional.empty());

        mockMvc.perform(put("/api/cards/put/123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Card not found"));
    }

    @Test
    void testGetAllCards_returnsCards() throws Exception {
        when(cardService.getAllCards()).thenReturn(List.of(validCard));

        mockMvc.perform(get("/api/cards"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void testGetCardById_found() throws Exception {
        when(cardService.findByCardId(123)).thenReturn(validCard);

        mockMvc.perform(get("/api/cards/get/123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cardNumber").value(123));
    }

    @Test
    void testGetCardById_notFound() throws Exception {
        when(cardService.findByCardId(404)).thenReturn(null);

        mockMvc.perform(get("/api/cards/get/404"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Card not found"));
    }

    @Test
    void testCalculateCollectionStatistics() throws Exception {
        Map<String, Object> stats = Map.of("totalCards", 1, "totalFoiled", 1, "percentFoiled", 100.0);
        when(cardService.calculateCollectionStatistics()).thenReturn(stats);

        mockMvc.perform(get("/api/cards/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalCards").value(1));
    }

    @Test
    void testCalculateCollectionValues() throws Exception {
        Map<String, BigDecimal> values = Map.of("marketValue", BigDecimal.TEN, "ownerValue", BigDecimal.ONE);
        when(cardService.calculateCollectionValues()).thenReturn(values);

        mockMvc.perform(get("/api/cards/values"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.marketValue").value(10));
    }

    @Test
    void testImportCardsFromFile_success() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "cards.txt", "text/plain", "dummy".getBytes());
        when(cardService.addAllCardsFromFile(any())).thenReturn(List.of(validCard));

        mockMvc.perform(multipart("/api/cards/import").file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void testImportCardsFromFile_emptyFile() throws Exception {
        MockMultipartFile emptyFile = new MockMultipartFile("file", "", "text/plain", new byte[0]);

        mockMvc.perform(multipart("/api/cards/import").file(emptyFile))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("No file uploaded"));
    }
}
