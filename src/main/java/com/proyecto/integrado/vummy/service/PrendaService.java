package com.proyecto.integrado.vummy.service;

import com.proyecto.integrado.vummy.entity.Prenda;
import com.proyecto.integrado.vummy.repository.PrendaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrendaService {

    private final PrendaRepository prendaRepository;

    public PrendaService(PrendaRepository prendaRepository) {
        this.prendaRepository = prendaRepository;
    }

    public List<Prenda> obtenerPrendasPorTienda(Long tiendaId) {
        return prendaRepository.findByTiendaId(tiendaId);
    }

    public Prenda guardarPrenda(Prenda prenda) {
        return prendaRepository.save(prenda);
    }

    public boolean eliminarPrenda(Long id) {
        if (prendaRepository.existsById(id)) {
            prendaRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
