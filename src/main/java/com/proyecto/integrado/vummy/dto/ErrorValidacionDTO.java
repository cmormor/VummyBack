package com.proyecto.integrado.vummy.dto;

import java.util.HashMap;
import java.util.Map;

public class ErrorValidacionDTO {

    private Map<String, String> errores = new HashMap<>();

    public void agregarError(String campo, String mensaje) {
        errores.put(campo, mensaje);
    }

    public Map<String, String> getErrores() {
        return errores;
    }

    public boolean tieneErrores() {
        return !errores.isEmpty();
    }
}
