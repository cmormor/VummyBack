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

  public UsuarioService(UsuarioRepository usuarioRepository) {
    this.usuarioRepository = usuarioRepository;
  }

  public UsuarioDTO convertirAUsuarioDTO(Usuario usuario) {
    return new UsuarioDTO(usuario.getId(), usuario.getNombre(), usuario.getEmail(), usuario.getRol(), 
                           usuario.getAltura(), usuario.getCuelloManga(), usuario.getPecho(),
                           usuario.getCintura(), usuario.getCadera(), usuario.getEntrepierna());
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
    boolean existeUsuario = usuarioRepository.findByEmail(usuario.getEmail()).isPresent()
                         || usuarioRepository.findByNombre(usuario.getNombre()).isPresent();

    if (existeUsuario) {
        throw new IllegalArgumentException("El nombre o correo electrónico ya están en uso.");
    }

    validarMedidas(usuario);
    usuario.setRol(Rol.REGISTRADO);
    Usuario nuevoUsuario = usuarioRepository.save(usuario);
    return convertirAUsuarioDTO(nuevoUsuario);
}


  public Optional<UsuarioDTO> iniciarSesion(String email, String password) {
    return usuarioRepository.findByEmail(email)
            .filter(usuario -> usuario.getPassword().equals(password))
            .map(usuario -> {
                usuario.setRol(Rol.REGISTRADO);
                usuarioRepository.save(usuario);
                return convertirAUsuarioDTO(usuario);
            });
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
                usuario.setAltura(usuarioActualizado.getAltura());
                usuario.setCuelloManga(usuarioActualizado.getCuelloManga());
                usuario.setPecho(usuarioActualizado.getPecho());
                usuario.setCintura(usuarioActualizado.getCintura());
                usuario.setCadera(usuarioActualizado.getCadera());
                usuario.setEntrepierna(usuarioActualizado.getEntrepierna());
                validarMedidas(usuario);
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

  private void validarMedidas(Usuario usuario) {
    if (usuario.getAltura() != null && usuario.getAltura() < 100) {
      throw new IllegalArgumentException("La altura debe ser al menos 100 cm");
    }
    if (usuario.getCuelloManga() != null && usuario.getCuelloManga() < 25) {
      throw new IllegalArgumentException("El cuello/manga debe ser al menos 25 cm");
    }
    if (usuario.getPecho() != null && usuario.getPecho() < 60) {
      throw new IllegalArgumentException("El pecho debe ser al menos 60 cm");
    }
    if (usuario.getCintura() != null && usuario.getCintura() < 50) {
      throw new IllegalArgumentException("La cintura debe ser al menos 50 cm");
    }
    if (usuario.getCadera() != null && usuario.getCadera() < 50) {
      throw new IllegalArgumentException("La cadera debe ser al menos 50 cm");
    }
    if (usuario.getEntrepierna() != null && usuario.getEntrepierna() < 50) {
      throw new IllegalArgumentException("La entrepierna debe ser al menos 50 cm");
    }
  }
}
