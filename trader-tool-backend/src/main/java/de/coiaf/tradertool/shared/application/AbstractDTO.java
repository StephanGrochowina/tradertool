package de.coiaf.tradertool.shared.application;

import de.coiaf.tradertool.shared.domain.valueobject.BusinessKey;

import java.util.Objects;

public abstract class AbstractDTO {

    private Long id;
    private BusinessKey businessKey;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        if (!AbstractDTO.class.isAssignableFrom(o.getClass())) return false;
        if (this.getBusinessKey() == null) return false;

        AbstractDTO that = (AbstractDTO) o;
        return Objects.equals(this.getBusinessKey() , that.getBusinessKey() );
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getBusinessKey() );
    }
}
