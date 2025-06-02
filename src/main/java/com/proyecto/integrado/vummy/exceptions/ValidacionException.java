package com.proyecto.integrado.vummy.exceptions;

import com.proyecto.integrado.vummy.dto.ErrorValidacionDTO;

public class ValidacionException extends RuntimeException {

    private final ErrorValidacionDTO errores;

    public ValidacionException(ErrorValidacionDTO errores) {
        super("Error de validación");
        this.errores = errores;
    }

    public ErrorValidacionDTO getErrores() {
        return errores;
    }
}
