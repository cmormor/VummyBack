package com.proyecto.integrado.vummy.dto;

import com.proyecto.integrado.vummy.entity.Rol;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {
    private Long id;
    private String nombre;
    private String email;
    private Rol rol;
    private Double altura;
    private Double cuelloManga;
    private Double pecho;
    private Double cintura;
    private Double cadera;
    private Double entrepierna;
    private String token;

    public void setToken(String token) {
      this.token = token;
  }
}
