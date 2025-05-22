package com.proyecto.integrado.vummy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.integrado.vummy.entity.PrendaCarrito;
import com.proyecto.integrado.vummy.repository.PrendaCarritoRepository;

import java.util.List;

@Service
public class PrendaCarritoService {

    @Autowired
    private PrendaCarritoRepository repo;

    public void guardarPedidoCarrito(PrendaCarrito pedido) {
        repo.save(pedido);
    }

    public List<PrendaCarrito> obtenerPorCorreo(String correo) {
        return repo.findByCorreo(correo);
    }

    public void vaciarCarrito(String correo) {
        repo.deleteByCorreo(correo);
    }
}
