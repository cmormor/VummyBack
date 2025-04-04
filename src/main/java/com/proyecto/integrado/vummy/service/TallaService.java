package com.proyecto.integrado.vummy.service;

import com.proyecto.integrado.vummy.entity.Talla;
import com.proyecto.integrado.vummy.repository.TallaRepository;
import com.proyecto.integrado.vummy.dto.TallaDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TallaService {

    private final TallaRepository tallaRepository;

    public TallaService(TallaRepository tallaRepository) {
        this.tallaRepository = tallaRepository;
    }

    public List<TallaDTO> obtenerTodas() {
        return tallaRepository.findAll().stream()
                .map(TallaDTO::new)
                .collect(Collectors.toList());
    }

    public Optional<TallaDTO> obtenerPorId(Long id) {
        return tallaRepository.findById(id)
                .map(TallaDTO::new);
    }

    public List<TallaDTO> obtenerPorTienda(Long tiendaId) {
        return tallaRepository.findByTiendaId(tiendaId).stream()
                .map(TallaDTO::new)
                .collect(Collectors.toList());
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
