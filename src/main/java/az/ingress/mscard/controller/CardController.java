package az.ingress.mscard.controller;

import az.ingress.mscard.model.enums.CardStatus;
import az.ingress.mscard.model.request.CreateCardRequest;
import az.ingress.mscard.model.response.CardResponse;
import az.ingress.mscard.service.abstraction.CardService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.Set;

import static az.ingress.mscard.model.constants.HeaderConstants.X_USER_ID;
import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/cards")
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class CardController {
    CardService cardService;

    @PostMapping()
    @ResponseStatus(CREATED)
    public CardResponse createCard(@RequestHeader(name = X_USER_ID) Long userId, @Valid @RequestBody CreateCardRequest request) {
        return cardService.createCard(userId, request);
    }

    @GetMapping("/{id}")
    public CardResponse getCardById(@RequestHeader(name = X_USER_ID) Long userId, @PathVariable Long id) {
        return cardService.getCardById(userId);
    }

    @GetMapping
    public Set<CardResponse> getCardsByUserId(@RequestHeader(name = X_USER_ID) Long userId) {
        return cardService.getCardsByUserId(userId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void deleteCardById(@RequestHeader(name = X_USER_ID) Long userId, @PathVariable Long id) {
        cardService.deleteCardById(userId, id);
    }

    @PatchMapping("/{id}/status")
    @ResponseStatus(NO_CONTENT)
    public void updateCardStatus(@RequestHeader(name = X_USER_ID) Long userId, @PathVariable Long id, @RequestParam CardStatus status) {
        cardService.updateCardStatus(userId, id, status);
    }
}