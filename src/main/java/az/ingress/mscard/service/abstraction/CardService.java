package az.ingress.mscard.service.abstraction;

import az.ingress.mscard.model.enums.CardStatus;
import az.ingress.mscard.model.request.CreateCardRequest;
import az.ingress.mscard.model.response.CardResponse;

import java.util.Set;

public interface CardService {
    CardResponse createCard(Long userId, CreateCardRequest request);

    CardResponse getCardById(Long id);

   // CardResponse getCardById(Long userId, Long id);

    Set<CardResponse> getCardsByUserId(Long userId);

    Set<CardResponse> getAllCardsByUserId(Long userId);

    void deleteCardById(Long userId, Long id);

    void updateCardStatus(Long id, CardStatus status);

    void updateCardStatus(Long userId, Long id, CardStatus status);

    void deleteCacheByUserId(Long userId);
}
