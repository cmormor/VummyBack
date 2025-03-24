package com.proyecto.integrado.vummy.service;

import java.util.List;
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

  public List<Usuario> obtenerTodos() {
    return usuarioRepository.findAll();
  }

  public Optional<Usuario> obtenerPorId(Long id) {
    return usuarioRepository.findById(id);
  }

  public Optional<Usuario> obtenerPorEmail (String email){
    return usuarioRepository.findByEmail(email);
  }

  public Usuario registrarUsuario(Usuario usuario) {
      usuario.setRol(Rol.REGISTRADO);
      return usuarioRepository.save(usuario);
  }

  public Usuario iniciarSesion(String email, String password) {
    Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(email);
    if (usuarioOptional.isPresent()) {
      Usuario usuario = usuarioOptional.get();
      if (usuario.getPassword().equals(password)) {
        return usuario;
      }
    }
    return null;
  }  

  public Usuario actualizarRol(Long usuarioId, Rol nuevoRol) {
    Optional<Usuario> usuarioOptional = usuarioRepository.findById(usuarioId);
    if (usuarioOptional.isPresent()) {
      Usuario usuario = usuarioOptional.get();
      usuario.setRol(nuevoRol);
      return usuarioRepository.save(usuario);
    }
    return null;
  }

  public Optional<Usuario> actualizarUsuario(Long id, Usuario usuarioActualizado) {
    Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);
    if (usuarioOptional.isPresent()) {
      Usuario usuario = usuarioOptional.get();
      
      usuario.setNombre(usuarioActualizado.getNombre());
      usuario.setEmail(usuarioActualizado.getEmail());
      usuario.setPassword(usuarioActualizado.getPassword());

      return Optional.of(usuarioRepository.save(usuario));
    }
    return Optional.empty();
  }

  public boolean eliminarUsuario(Long id) {
    if (usuarioRepository.existsById(id)) {
        usuarioRepository.deleteById(id);
        return true;
    }
    return false;
  }
}
