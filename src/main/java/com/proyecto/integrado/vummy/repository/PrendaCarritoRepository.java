package com.proyecto.integrado.vummy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyecto.integrado.vummy.entity.PrendaCarrito;

public interface PrendaCarritoRepository extends JpaRepository<PrendaCarrito, Long> {
    List<PrendaCarrito> findByCorreo(String correo);
    void deleteByCorreo(String correo);
}
