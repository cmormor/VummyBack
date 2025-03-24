package com.proyecto.integrado.vummy.service;

import com.proyecto.integrado.vummy.entity.Prenda;
import com.proyecto.integrado.vummy.repository.PrendaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PrendaService {

    private final PrendaRepository prendaRepository;

    public PrendaService(PrendaRepository prendaRepository) {
        this.prendaRepository = prendaRepository;
    }

    public List<Prenda> obtenerTodas() {
        return prendaRepository.findAll();
    }

    public Optional<Prenda> obtenerPorId(Long id) {
        return prendaRepository.findById(id);
    }

    public List<Prenda> obtenerPorTienda(Long tiendaId) {
        return prendaRepository.findByTiendaId(tiendaId);
    }

    public Prenda guardarPrenda(Prenda prenda) {
        return prendaRepository.save(prenda);
    }

    public Optional<Prenda> actualizarPrenda(Long id, Prenda prendaActualizada) {
        Optional<Prenda> prendaOptional = prendaRepository.findById(id);
        if (prendaOptional.isPresent()) {
            Prenda prenda = prendaOptional.get();
            prenda.setNombre(prendaActualizada.getNombre());
            prenda.setPrecio(prendaActualizada.getPrecio());
            prenda.setTalla(prendaActualizada.getTalla());
            prenda.setTienda(prendaActualizada.getTienda());
            return Optional.of(prendaRepository.save(prenda));
        }
        return Optional.empty();
    }

    public boolean eliminarPrenda(Long id) {
        if (prendaRepository.existsById(id)) {
            prendaRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
