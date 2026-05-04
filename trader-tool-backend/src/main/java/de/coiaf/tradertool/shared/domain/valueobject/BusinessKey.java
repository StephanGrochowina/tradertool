package de.coiaf.tradertool.shared.domain.valueobject;

import java.util.Objects;
import java.util.UUID;

/**
 * the business key of a domain object
 */
public class BusinessKey {
    private final UUID value;

    public UUID getValue() {
        return this.value;
    }

    public BusinessKey(UUID value) {
        this.value = Objects.requireNonNull(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BusinessKey)) return false;
        BusinessKey that = (BusinessKey) o;
        return Objects.equals(this.getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getValue());
    }
}
