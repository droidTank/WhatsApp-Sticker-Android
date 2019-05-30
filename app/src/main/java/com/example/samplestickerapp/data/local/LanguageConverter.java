package com.example.samplestickerapp.data.local;

import androidx.room.TypeConverter;

import java.util.Arrays;
import java.util.List;

public class LanguageConverter {
    @TypeConverter
    public List<String> storedStringToLanguages(String value) {
        List<String> langs = Arrays.asList(value.split("\\s*,\\s*"));
        return (langs);
    }

    @TypeConverter
    public String languagesToStoredString(List<String> cl) {
        String value = "";

        for (String lang :cl)
            value += lang + ",";

        return value;
    }
}
