package com.proyecto.integrado.vummy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResetearContrasena {
    private String email;
    private String newPassword;
}
