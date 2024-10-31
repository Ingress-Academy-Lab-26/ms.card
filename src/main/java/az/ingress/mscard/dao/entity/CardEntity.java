package az.ingress.mscard.dao.entity;

import az.ingress.mscard.model.enums.CardBrand;
import az.ingress.mscard.model.enums.CardStatus;
import az.ingress.mscard.model.enums.CardType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cards")
@ToString(of = {"id", "status", "userId"})
@Builder
@FieldDefaults(level = PRIVATE)
public class CardEntity  {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    Long id;

    String pan;

    String cardHolder;

    BigDecimal balance;

    @Enumerated(STRING)
    CardType type;

    @Enumerated(STRING)
    CardBrand brand;

    @CreationTimestamp
    LocalDateTime createdAt;

    @Enumerated(STRING)
    CardStatus status;

    @UpdateTimestamp
    LocalDateTime updatedAt;

    Long userId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CardEntity that = (CardEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
