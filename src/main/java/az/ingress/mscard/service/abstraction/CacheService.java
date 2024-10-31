package az.ingress.mscard.service.abstraction;

import az.ingress.mscard.dao.entity.CardEntity;

import java.util.Set;

public interface CacheService {
    void saveCardToCache(CardEntity cardEntity);

    void saveCardsToCache(Long userId, Set<CardEntity> cards);

    <T> T getFromCache(String key);

    void deleteCache(String key);
}