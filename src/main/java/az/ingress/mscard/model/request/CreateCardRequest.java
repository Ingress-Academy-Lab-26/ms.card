package az.ingress.mscard.model.request;

import az.ingress.mscard.model.enums.CardBrand;
import az.ingress.mscard.model.enums.CardType;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import static az.ingress.mscard.exception.FieldExceptionConstants.CARD_PAN_16_DIGITS_ONLY;
import static az.ingress.mscard.exception.FieldExceptionConstants.INVALID_CARD_BRAND;
import static az.ingress.mscard.exception.FieldExceptionConstants.INVALID_CARD_HOLDER;
import static az.ingress.mscard.exception.FieldExceptionConstants.INVALID_CARD_TYPE;
import static lombok.AccessLevel.PRIVATE;

@Data
@ToString(of = {"cardHolder", "type", "brand"})
@Builder
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class CreateCardRequest {

    @NotNull(message = CARD_PAN_16_DIGITS_ONLY)
    @Pattern(regexp = "\\d{16}", message = CARD_PAN_16_DIGITS_ONLY)
    String pan;

    @NotNull(message = INVALID_CARD_HOLDER)
    String cardHolder;

    @NotNull(message = INVALID_CARD_TYPE)
    CardType type;

    @NotNull(message = INVALID_CARD_BRAND)
    CardBrand brand;
}