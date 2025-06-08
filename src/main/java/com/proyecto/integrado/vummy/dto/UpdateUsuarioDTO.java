package com.proyecto.integrado.vummy.dto;

import com.proyecto.integrado.vummy.entity.Rol;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUsuarioDTO {
    private String nombre;
    private String email;
    private Rol rol;
    private Double altura;
    private Double cuelloManga;
    private Double pecho;
    private Double cintura;
    private Double cadera;
    private Double entrepierna;
}
