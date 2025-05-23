package com.proyecto.integrado.vummy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.integrado.vummy.entity.PrendaCarrito;
import com.proyecto.integrado.vummy.repository.PrendaCarritoRepository;

import java.util.List;

@Service
public class PrendaCarritoService {

    @Autowired
    private PrendaCarritoRepository prendaCarritoRepository;

    public void guardarPedidoCarrito(PrendaCarrito pedido) {
        prendaCarritoRepository.save(pedido);
    }

    public List<PrendaCarrito> obtenerPorCorreo(String correo) {
        return prendaCarritoRepository.findByCorreoOrderByIdAsc(correo);
    }

    public PrendaCarrito obtenerPorId(Long id) {
    return prendaCarritoRepository.findById(id).orElse(null);
    }

    public void vaciarCarritoPorCorreo(String correo) {
        prendaCarritoRepository.deleteByCorreo(correo);
    }
 
    public void eliminarPorId(Long id) {
    prendaCarritoRepository.deleteById(id);
    }

}
