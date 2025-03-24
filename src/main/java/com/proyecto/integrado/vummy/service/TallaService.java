package com.proyecto.integrado.vummy.service;

import com.proyecto.integrado.vummy.entity.Talla;
import com.proyecto.integrado.vummy.repository.TallaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TallaService {

    private final TallaRepository tallaRepository;

    public TallaService(TallaRepository tallaRepository) {
        this.tallaRepository = tallaRepository;
    }

    public List<Talla> obtenerTallasPorTienda(Long tiendaId) {
        return tallaRepository.findByTiendaId(tiendaId);
    }

    public Optional<Talla> obtenerTallaPorId(Long id) {
        return tallaRepository.findById(id);
    }

    public Talla agregarTalla(Talla talla) {
        return tallaRepository.save(talla);
    }

    public boolean eliminarTalla(Long id) {
        if (tallaRepository.existsById(id)) {
            tallaRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
