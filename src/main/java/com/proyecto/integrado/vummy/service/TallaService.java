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

    public List<Talla> obtenerTodas() {
        return tallaRepository.findAll();
    }

    public Optional<Talla> obtenerPorId(Long id) {
        return tallaRepository.findById(id);
    }

    public List<Talla> obtenerPorTienda(Long tiendaId) {
        return tallaRepository.findByTiendaId(tiendaId);
    }

    public Talla agregarTalla(Talla talla) {
        return tallaRepository.save(talla);
    }

    public Optional<Talla> actualizarTalla(Long id, Talla tallaActualizada) {
        Optional<Talla> tallaOptional = tallaRepository.findById(id);
        if (tallaOptional.isPresent()) {
            Talla talla = tallaOptional.get();
            talla.setNombre(tallaActualizada.getNombre());
            talla.setAltura(tallaActualizada.getAltura());
            talla.setPecho(tallaActualizada.getPecho());
            talla.setCintura(tallaActualizada.getCintura());
            talla.setCadera(tallaActualizada.getCadera());
            talla.setEntrepierna(tallaActualizada.getEntrepierna());
            return Optional.of(tallaRepository.save(talla));
        }
        return Optional.empty();
    }

    public boolean eliminarTalla(Long id) {
        if (tallaRepository.existsById(id)) {
            tallaRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
