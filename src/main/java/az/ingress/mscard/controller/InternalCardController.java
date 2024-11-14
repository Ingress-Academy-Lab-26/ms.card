package az.ingress.mscard.controller;

import az.ingress.mscard.model.enums.CardStatus;
import az.ingress.mscard.model.response.CardResponse;
import az.ingress.mscard.service.abstraction.CardService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/v1/cards")
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class InternalCardController {
    CardService cardService;

    @GetMapping("/{id}")
    public CardResponse getCardById(@PathVariable Long id){
        return cardService.getCardById(id);
    }

    @GetMapping("/user/{userId}")
    public Set<CardResponse> getAllCardsByUserId(@PathVariable Long userId){
        return cardService.getAllCardsByUserId(userId);
    }

    @PatchMapping("/{id}/status")
    @ResponseStatus(NO_CONTENT)
    public void updateCardStatus(@PathVariable Long id, @RequestParam CardStatus status){
        cardService.updateCardStatus(id, status);
    }

    @DeleteMapping("/user/{userId}")
    @ResponseStatus(NO_CONTENT)
    public void deleteCacheByUserId(@PathVariable Long userId){
        cardService.deleteCacheByUserId(userId);
    }
}
