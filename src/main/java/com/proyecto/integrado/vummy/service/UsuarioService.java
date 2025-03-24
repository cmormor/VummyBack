package com.proyecto.integrado.vummy.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.proyecto.integrado.vummy.entity.Rol;
import com.proyecto.integrado.vummy.entity.Usuario;
import com.proyecto.integrado.vummy.repository.UsuarioRepository;

@Service
public class UsuarioService {
  
  private final UsuarioRepository usuarioRepository;

  public UsuarioService(UsuarioRepository usuarioRepository){
    this.usuarioRepository = usuarioRepository;
  }

  public Optional<Usuario> buscarPorEmail (String email){
    return usuarioRepository.findByEmail(email);
  }

  public Usuario registrarUsuario(Usuario usuario) {
        usuario.setRol(Rol.REGISTRADO);
        return usuarioRepository.save(usuario);
    }

  public Usuario actualizarRol(Long usuarioId, Rol nuevoRol) {
    Optional<Usuario> usuarOptional = usuarioRepository.findById(usuarioId);
    if (usuarOptional.isPresent()) {
      Usuario usuario = usuarOptional.get();
      usuario.setRol(nuevoRol);
      return usuarioRepository.save(usuario);
    }
    return null;
  }

  public boolean eliminarUsuario(Long id) {
    if (usuarioRepository.existsById(id)) {
        usuarioRepository.deleteById(id);
        return true;
    }
    return false;
}

}
