package de.coiaf.tradertool.shared.domain;

import de.coiaf.tradertool.shared.domain.valueobject.BusinessKey;
import de.coiaf.tradertool.shared.infrastructure.jpa.converter.BusinessKeyAttributeConverter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

@MappedSuperclass
public abstract class AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    @Column(name = "modified", nullable = false)
    private LocalDateTime modified;

    @Column(name = "business_key", nullable = false, unique = true, columnDefinition = "BINARY(16)")
    @NotNull
    @Convert(converter = BusinessKeyAttributeConverter.class)
    private BusinessKey businessKey;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getModified() {
        return modified;
    }

    public void setModified(LocalDateTime modified) {
        this.modified = modified;
    }

    public BusinessKey getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(BusinessKey businessKey) {
        this.businessKey = businessKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!AbstractEntity.class.isAssignableFrom(o.getClass())) return false;

        AbstractEntity that = (AbstractEntity) o;
        return this.getBusinessKey() != null && this.getBusinessKey().equals(that.getBusinessKey());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getBusinessKey());
    }
}