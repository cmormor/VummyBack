package com.proyecto.integrado.vummy.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.proyecto.integrado.vummy.dto.UsuarioDTO;
import com.proyecto.integrado.vummy.entity.Rol;
import com.proyecto.integrado.vummy.entity.Usuario;
import com.proyecto.integrado.vummy.repository.UsuarioRepository;

@Service
public class UsuarioService {
  
  private final UsuarioRepository usuarioRepository;

  public UsuarioService(UsuarioRepository usuarioRepository){
    this.usuarioRepository = usuarioRepository;
  }

  public UsuarioDTO convertirAUsuarioDTO(Usuario usuario) {
    return new UsuarioDTO(usuario.getId(), usuario.getNombre(), usuario.getEmail(), usuario.getRol());
  }

  public List<UsuarioDTO> obtenerTodos() {
    return usuarioRepository.findAll().stream()
            .map(this::convertirAUsuarioDTO)
            .collect(Collectors.toList());
  }

  public Optional<UsuarioDTO> obtenerPorId(Long id) {
    return usuarioRepository.findById(id)
            .map(this::convertirAUsuarioDTO);
  }

  public Optional<UsuarioDTO> obtenerPorEmail(String email) {
    return usuarioRepository.findByEmail(email)
            .map(this::convertirAUsuarioDTO);
  }

  public UsuarioDTO registrarUsuario(Usuario usuario) {
      usuario.setRol(Rol.REGISTRADO);
      Usuario nuevoUsuario = usuarioRepository.save(usuario);
      return convertirAUsuarioDTO(nuevoUsuario);
  }

  public Optional<UsuarioDTO> iniciarSesion(String email, String password) {
    return usuarioRepository.findByEmail(email)
            .filter(usuario -> usuario.getPassword().equals(password))
            .map(this::convertirAUsuarioDTO);
  }

  public Optional<UsuarioDTO> actualizarRol(Long usuarioId, Rol nuevoRol) {
    return usuarioRepository.findById(usuarioId)
            .map(usuario -> {
                usuario.setRol(nuevoRol);
                return usuarioRepository.save(usuario);
            })
            .map(this::convertirAUsuarioDTO);
  }

  public Optional<UsuarioDTO> actualizarUsuario(Long id, Usuario usuarioActualizado) {
    return usuarioRepository.findById(id)
            .map(usuario -> {
                usuario.setNombre(usuarioActualizado.getNombre());
                usuario.setEmail(usuarioActualizado.getEmail());
                usuario.setPassword(usuarioActualizado.getPassword());
                return usuarioRepository.save(usuario);
            })
            .map(this::convertirAUsuarioDTO);
  }

  public boolean eliminarUsuario(Long id) {
    if (usuarioRepository.existsById(id)) {
        usuarioRepository.deleteById(id);
        return true;
    }
    return false;
  }
}