package az.ingress.mscard.mapper;
import az.ingress.mscard.dao.entity.CardEntity;
import az.ingress.mscard.model.request.CreateCardRequest;
import az.ingress.mscard.model.response.CardResponse;


import java.math.BigDecimal;
import java.time.LocalDateTime;

import static az.ingress.mscard.model.enums.CardStatus.ACTIVE;


public enum CardMapper {
    CARD_MAPPER;

    public CardEntity buildCardEntity(Long userId, CreateCardRequest request){
        return CardEntity.builder()
                .pan(request.getPan())
                .cardHolder(request.getCardHolder())
                .balance(BigDecimal.ZERO)
                .type(request.getType())
                .brand(request.getBrand())
                .createdAt(LocalDateTime.now())
                .status(ACTIVE)
                .updatedAt(LocalDateTime.now())
                .userId(userId)
                .build();
    }

    public CardResponse toCardResponseDto(CardEntity entity){
        return CardResponse.builder()
                .id(entity.getId())
                .pan(entity.getPan())
                .cardHolder(entity.getCardHolder())
                .balance(entity.getBalance())
                .type(entity.getType())
                .brand(entity.getBrand())
                .createdAt(entity.getCreatedAt())
                .status(entity.getStatus())
                .updatedAt(entity.getUpdatedAt())
                .userId(entity.getUserId())
                .build();
    }
}