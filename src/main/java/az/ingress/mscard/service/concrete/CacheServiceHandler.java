package az.ingress.mscard.service.concrete;

import az.ingress.mscard.dao.entity.CardEntity;
import az.ingress.mscard.service.abstraction.CacheService;
import az.ingress.mscard.util.CacheUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.TreeSet;

import static az.ingress.mscard.model.constants.CacheConstants.MS_CARD_USER;
import static az.ingress.mscard.model.constants.CacheConstants.SEVEN;
import static java.time.temporal.ChronoUnit.DAYS;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CacheServiceHandler implements CacheService {
    CacheUtil cacheUtil;

    @Async
    @Override
    public void saveCardToCache(CardEntity cardEntity) {
        String cacheKey = MS_CARD_USER + cardEntity.getUserId();
        Set<CardEntity> cards = getFromCache(cacheKey);
        if(cards == null){
            cards = new TreeSet<>();
        }
        cards.add(cardEntity);
        cacheUtil.saveToCache(cacheKey, cards, SEVEN, DAYS);
    }

    @Async
    @Override
    public void saveCardsToCache(Long userId, Set<CardEntity> cards) {
        String cacheKey = MS_CARD_USER + userId;
        cacheUtil.saveToCache(cacheKey, cards, SEVEN, DAYS);
    }

    @Override
    public <T> T getFromCache(String key) {
        return cacheUtil.getBucket(key);
    }

    @Override
    public void deleteCache(String key) {
        cacheUtil.deleteCache(key);
    }
}
