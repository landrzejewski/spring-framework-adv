package pl.training.payments.adapters.output.persistence;

import lombok.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.util.Objects;

@Entity(name = "PAYMENT_PROPERTY")
@EqualsAndHashCode(of = "id")
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
public class PropertyEntity {

    @GeneratedValue
    @Id
    private Long id;
    @NonNull
    @Column(name = "PROPERTY_KEY")
    private String key;
    @NonNull
    @Column(name = "PROPERTY_VALUE")
    private String value;

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        var that = (PropertyEntity) other;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return PropertyEntity.class.hashCode();
    }

}
