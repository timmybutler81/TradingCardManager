package com.butlert.tradingcardmanager.service;

import com.butlert.tradingcardmanager.mapper.CardMapper;
import com.butlert.tradingcardmanager.model.Card;
import com.butlert.tradingcardmanager.model.CardDTO;
import com.butlert.tradingcardmanager.repository.CardRepository;
import com.butlert.tradingcardmanager.utils.CardParser;
import com.butlert.tradingcardmanager.utils.ReadTextFile;
import com.butlert.tradingcardmanager.utils.validation.CardValidator;
import com.butlert.tradingcardmanager.utils.validation.ValidatorResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.servlet.HandlerMapping;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CardServiceImplTest {

    @Mock private CardRepository cardRepository;
    @Mock private CardParser cardParser;
    @Mock private ReadTextFile readTextFile;
    @Mock private CardValidator cardValidator;
    @Mock private HandlerMapping handlerMapping;

    @InjectMocks private CardServiceImpl cardService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cardService = new CardServiceImpl(cardRepository, cardParser, readTextFile, cardValidator, handlerMapping);
    }

    @Test
    void testAddCard_validCard_returnsSavedCardDTO() {
        CardDTO dto = new CardDTO(123, "Name", "Game", "RARE", "10", LocalDate.now().toString(), LocalDate.now().toString(), true);
        Card card = CardMapper.toEntity(dto);

        when(cardValidator.validateCard(card)).thenReturn(ValidatorResult.success());
        when(cardRepository.existsByCardNumber(dto.getCardNumber())).thenReturn(false);
        when(cardRepository.save(any(Card.class))).thenReturn(card);

        Optional<CardDTO> result = cardService.addCard(dto);

        assertTrue(result.isPresent());
        assertEquals(dto.getCardNumber(), result.get().getCardNumber());
        verify(cardRepository).save(any(Card.class));
    }

    @Test
    void testAddCard_duplicateCard_returnsEmptyOptional() {
        CardDTO dto = new CardDTO(123, "Name", "Game", "RARE", "10", LocalDate.now().toString(), LocalDate.now().toString(), true);
        Card card = CardMapper.toEntity(dto);

        when(cardValidator.validateCard(card)).thenReturn(ValidatorResult.success());
        when(cardRepository.existsByCardNumber(dto.getCardNumber())).thenReturn(true);

        Optional<CardDTO> result = cardService.addCard(dto);

        assertTrue(result.isEmpty());
        verify(cardRepository, never()).save(any());
    }

    @Test
    void testAddCard_invalidCard_throwsException() {
        CardDTO dto = new CardDTO(123, "Name", "Game", "RARE", "10", LocalDate.now().toString(), LocalDate.now().toString(), true);
        Card card = CardMapper.toEntity(dto);

        when(cardValidator.validateCard(card)).thenReturn(ValidatorResult.fail("Invalid"));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            cardService.addCard(dto);
        });

        assertTrue(ex.getMessage().contains("Validation failed"));
    }

    @Test
    void testDeleteCard_successful() {
        when(cardRepository.deleteByCardNumber(123)).thenReturn(1);
        boolean result = cardService.deleteCard(123);
        assertTrue(result);
    }

    @Test
    void testDeleteCard_notFound() {
        when(cardRepository.deleteByCardNumber(999)).thenReturn(0);
        boolean result = cardService.deleteCard(999);
        assertFalse(result);
    }

    @Test
    void testGetAllCards_returnsCardList() {
        List<Card> cards = List.of(new Card());
        when(cardRepository.findAll()).thenReturn(cards);
        assertEquals(1, cardService.getAllCards().size());
    }

    @Test
    void testFindByCardId_found() {
        Card card = new Card();
        when(cardRepository.findByCardNumber(101)).thenReturn(Optional.of(card));
        assertNotNull(cardService.findByCardId(101));
    }

    @Test
    void testFindByCardId_notFound() {
        when(cardRepository.findByCardNumber(404)).thenReturn(Optional.empty());
        assertNull(cardService.findByCardId(404));
    }
}
