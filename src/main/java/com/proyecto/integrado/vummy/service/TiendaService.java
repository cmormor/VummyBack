package com.proyecto.integrado.vummy.service;

import com.proyecto.integrado.vummy.entity.Tienda;
import com.proyecto.integrado.vummy.repository.TiendaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TiendaService {

    private final TiendaRepository tiendaRepository;

    public TiendaService(TiendaRepository tiendaRepository) {
        this.tiendaRepository = tiendaRepository;
    }

    public List<Tienda> listarTiendas() {
        return tiendaRepository.findAll();
    }

    public Optional<Tienda> buscarPorNombre(String nombre) {
        return tiendaRepository.findByNombre(nombre);
    }

    public Tienda agregarTienda(Tienda tienda) {
      return tiendaRepository.save(tienda);
  }

    public boolean eliminarTienda(Long id) {
        if (tiendaRepository.existsById(id)) {
            tiendaRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
