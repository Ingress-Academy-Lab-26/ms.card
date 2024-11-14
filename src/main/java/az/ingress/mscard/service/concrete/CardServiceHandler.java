package az.ingress.mscard.service.concrete;

import az.ingress.mscard.aspect.annotation.LogDto;
import az.ingress.mscard.aspect.annotation.LogExecutionTime;
import az.ingress.mscard.dao.entity.CardEntity;
import az.ingress.mscard.dao.repository.CardRepository;
import az.ingress.mscard.exception.NotFoundException;
import az.ingress.mscard.model.enums.CardStatus;
import az.ingress.mscard.model.request.CreateCardRequest;
import az.ingress.mscard.model.response.CardResponse;
import az.ingress.mscard.service.abstraction.CacheService;
import az.ingress.mscard.service.abstraction.CardService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static az.ingress.mscard.exception.ExceptionConstants.CARD_NOT_FOUND_CODE;
import static az.ingress.mscard.exception.ExceptionConstants.CARD_NOT_FOUND_MESSAGE;
import static az.ingress.mscard.mapper.CardMapper.CARD_MAPPER;
import static az.ingress.mscard.model.constants.CacheConstants.MS_CARD_USER;
import static az.ingress.mscard.model.enums.CardStatus.ACTIVE;
import static az.ingress.mscard.model.enums.CardStatus.DELETED;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CardServiceHandler implements CardService {
    CardRepository cardRepository;
    CacheService cacheService;

    @Transactional
    @LogDto
    @Override
    public CardResponse createCard(Long userId, CreateCardRequest request){
        var cardEntity = CARD_MAPPER.buildCardEntity(userId, request);
        cardEntity = cardRepository.save(cardEntity);
        cacheService.saveCardToCache(cardEntity);
        return CARD_MAPPER.toCardResponseDto(cardEntity);
    }

    @LogDto
    @Override
    public CardResponse getCardById(Long id){
        var cardEntity = fetchCardIfExists(id);
        return CARD_MAPPER.toCardResponseDto(cardEntity);
    }
//
//    @LogDto
//    @Override
//    public CardResponse getCardById(Long userId, Long id){
//        Set<CardEntity> cards = cacheService.getFromCache(MS_CARD_USER + userId);
//        Optional<CardEntity> optCardEntity = Optional.empty();
//        if(cards != null){
//            optCardEntity = cards.stream()
//                    .filter(card -> Objects.equals(card.getId(), id))
//                    .findFirst();
//        }
//
//        if(optCardEntity.isPresent()){
//            if(!Objects.equals(optCardEntity.get().getUserId(), userId)){
//                throw new ChangeSetPersister.NotFoundException(CARD_NOT_FOUND_CODE, String.format(CARD_NOT_FOUND_MESSAGE, id));
//            }
//            return CARD_MAPPER.toCardResponseDto(optCardEntity.get());
//        } else {
//            CardEntity cardEntity = fetchActiveCardIfExists(id);
//            cacheService.saveCardToCache(cardEntity);
//            return CARD_MAPPER.toCardResponseDto(cardEntity);
//        }
//    }

    @LogDto
    @LogExecutionTime
    @Override
    public Set<CardResponse> getCardsByUserId(Long userId){
        Set<CardEntity> cards = cacheService.getFromCache(MS_CARD_USER + userId);
        if(cards != null){
            return cards.stream()
                    .map(CARD_MAPPER::toCardResponseDto)
                    .collect(Collectors.toSet());
        }

        cards = cardRepository.findAllByUserIdAndStatus(userId, ACTIVE);
        cacheService.saveCardsToCache(userId, cards);

        return cards.stream()
                .map(CARD_MAPPER::toCardResponseDto)
                .collect(Collectors.toSet());
    }

    @LogDto
    @LogExecutionTime
    @Override
    public Set<CardResponse> getAllCardsByUserId(Long userId){
        var cards = cardRepository.findByUserId(userId);
        return cards.stream()
                .map(CARD_MAPPER::toCardResponseDto)
                .collect(Collectors.toSet());
    }

    @Transactional
    @LogDto
    @Override
    public void deleteCardById(Long userId, Long id){
        var cardEntity = fetchActiveCardIfExists(id);
        if(!Objects.equals(cardEntity.getUserId(), userId)){
            throw new NotFoundException(CARD_NOT_FOUND_CODE, String.format(CARD_NOT_FOUND_MESSAGE, id));
        }
        updateStatus(cardEntity, DELETED);
    }

    @Transactional
    @LogDto
    @Override
    public void updateCardStatus(Long id, CardStatus status){
        var cardEntity = fetchCardIfExists(id);
        updateStatus(cardEntity, status);
    }

    @Transactional
    @LogDto
    @Override
    public void updateCardStatus(Long userId, Long id, CardStatus status){
        var cardEntity = fetchCardIfExists(id);
        if(!Objects.equals(cardEntity.getUserId(), userId)){
            throw new NotFoundException(CARD_NOT_FOUND_CODE, String.format(CARD_NOT_FOUND_MESSAGE, id));
        }
        updateStatus(cardEntity, status);
    }

    @Override
    public void deleteCacheByUserId(Long userId){
        cacheService.deleteCache(MS_CARD_USER + userId);
    }

    private void updateStatus(CardEntity cardEntity, CardStatus status){
        cardEntity.setStatus(status);
        cardRepository.save(cardEntity);

        Set<CardEntity> cards = cacheService.getFromCache(MS_CARD_USER + cardEntity.getUserId());
        if(cards != null){
            boolean hasRemoved = cards.removeIf(card -> Objects.equals(card.getId(), cardEntity.getId()));
            if(hasRemoved) {
                cacheService.saveCardsToCache(cardEntity.getUserId(), cards);
            }
        }
    }

    private CardEntity fetchActiveCardIfExists(Long id){
        return cardRepository.findByIdAndStatus(id, ACTIVE)
                .orElseThrow(() -> new NotFoundException(CARD_NOT_FOUND_CODE, String.format(CARD_NOT_FOUND_MESSAGE, id)));
    }

    private CardEntity fetchCardIfExists(Long id){
        return cardRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(CARD_NOT_FOUND_CODE, String.format(CARD_NOT_FOUND_MESSAGE, id)));
    }
}
