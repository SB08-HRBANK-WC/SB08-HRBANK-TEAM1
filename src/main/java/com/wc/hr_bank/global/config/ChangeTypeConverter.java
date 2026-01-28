package com.wc.hr_bank.global.config;

import com.wc.hr_bank.entity.ChangeType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ChangeTypeConverter implements Converter<String, ChangeType>
{
    @Override
    public ChangeType convert(String source) {
        if (source.isBlank() || "ALL".equalsIgnoreCase(source)) {
            return null;
        }
        try {
            return ChangeType.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}