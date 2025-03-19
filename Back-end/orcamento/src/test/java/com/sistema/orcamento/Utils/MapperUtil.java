package com.sistema.orcamento.Utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MapperUtil {
    
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T deserialize(Class<T> type,String json) throws JsonProcessingException {
        return objectMapper.readValue(json, type);
    }
    
}
