package de.coiaf.tradertool.shared.infrastructure.jpa.converter;

import de.coiaf.tradertool.shared.domain.valueobject.BusinessKey;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.UUID;

@Converter(autoApply = true)
public class BusinessKeyAttributeConverter implements AttributeConverter<BusinessKey, byte[]> {
    private static final UUIDAttributeConverter UUID_ATTRIBUTE_CONVERTER = new UUIDAttributeConverter();

    @Override
    public byte[] convertToDatabaseColumn(BusinessKey businessKey) {
        if (businessKey == null) return null;

        return UUID_ATTRIBUTE_CONVERTER.convertToDatabaseColumn(businessKey.getValue());
    }

    @Override
    public BusinessKey convertToEntityAttribute(byte[] bytes) {
        UUID businessKeyValue = UUID_ATTRIBUTE_CONVERTER.convertToEntityAttribute(bytes);

        if (businessKeyValue == null) return null;

        return new BusinessKey(businessKeyValue);
    }
}
