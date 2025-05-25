package com.proyecto.integrado.vummy.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.proyecto.integrado.vummy.dto.UsuarioDTO;
import com.proyecto.integrado.vummy.entity.Rol;
import com.proyecto.integrado.vummy.entity.Usuario;
import com.proyecto.integrado.vummy.repository.UsuarioRepository;
import com.proyecto.integrado.vummy.security.jwt.JwtService;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public UsuarioDTO convertirAUsuarioDTO(Usuario usuario) {
        return new UsuarioDTO(usuario.getId(), usuario.getNombre(), usuario.getEmail(), usuario.getRol(),
                usuario.getAltura(), usuario.getCuelloManga(), usuario.getPecho(),
                usuario.getCintura(), usuario.getCadera(), usuario.getEntrepierna(), null);
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

    public Optional<Map<String, Object>> obtenerPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .map(usuario -> {
                    Map<String, Object> resultado = new HashMap<>();
                    resultado.put("id", usuario.getId());
                    resultado.put("nombre", usuario.getNombre());
                    resultado.put("email", usuario.getEmail());
                    resultado.put("altura", usuario.getAltura());
                    resultado.put("cuelloManga", usuario.getCuelloManga());
                    resultado.put("pecho", usuario.getPecho());
                    resultado.put("cintura", usuario.getCintura());
                    resultado.put("cadera", usuario.getCadera());
                    resultado.put("entrepierna", usuario.getEntrepierna());
                    return resultado;
                });
    }

    public Optional<Usuario> obtenerUsuarioPorEmailByRepository(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public UsuarioDTO registrarUsuario(Usuario usuario) {
        boolean existeUsuario = usuarioRepository.findByEmail(usuario.getEmail()).isPresent()
                || usuarioRepository.findByNombre(usuario.getNombre()).isPresent();

        if (existeUsuario) {
            throw new IllegalArgumentException("El nombre o correo electrónico ya están en uso.");
        }

        String contraseñaEncriptada = passwordEncoder.encode(usuario.getPassword());
        usuario.setPassword(contraseñaEncriptada);

        validarMedidas(usuario);

        usuario.setRol(Rol.REGISTRADO);

        Usuario nuevoUsuario = usuarioRepository.save(usuario);

        String token = jwtService.generateToken(nuevoUsuario.getEmail());

        UsuarioDTO usuarioDTO = convertirAUsuarioDTO(nuevoUsuario);
        usuarioDTO.setToken(token);

        return usuarioDTO;
    }

    public Optional<UsuarioDTO> iniciarSesion(String email, String password) {
        return usuarioRepository.findByEmail(email)
                .filter(usuario -> passwordEncoder.matches(password, usuario.getPassword()))
                .map(usuario -> {
                    String token = jwtService.generateToken(usuario.getEmail());
                    UsuarioDTO usuarioDTO = convertirAUsuarioDTO(usuario);
                    usuarioDTO.setToken(token);
                    return usuarioDTO;
                });
    }

    public boolean resetearContrasena(String email, String nuevaContrasena) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        if (usuarioOpt.isEmpty()) {
            return false;
        }
        Usuario usuario = usuarioOpt.get();
        String encodedPassword = passwordEncoder.encode(nuevaContrasena);
        usuario.setPassword(encodedPassword);
        usuarioRepository.save(usuario);
        return true;
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

                    if (usuarioActualizado.getPassword() != null && !usuarioActualizado.getPassword().isEmpty()) {
                        String contraseñaEncriptada = passwordEncoder.encode(usuarioActualizado.getPassword());
                        usuario.setPassword(contraseñaEncriptada);
                    }

                    usuario.setAltura(usuarioActualizado.getAltura());
                    usuario.setCuelloManga(usuarioActualizado.getCuelloManga());
                    usuario.setPecho(usuarioActualizado.getPecho());
                    usuario.setCintura(usuarioActualizado.getCintura());
                    usuario.setCadera(usuarioActualizado.getCadera());
                    usuario.setEntrepierna(usuarioActualizado.getEntrepierna());

                    validarMedidas(usuario);
                    Usuario usuarioGuardado = usuarioRepository.save(usuario);

                    String token = jwtService.generateToken(usuarioGuardado.getEmail());

                    UsuarioDTO usuarioDTO = convertirAUsuarioDTO(usuarioGuardado);
                    usuarioDTO.setToken(token);

                    return usuarioDTO;
                });
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
