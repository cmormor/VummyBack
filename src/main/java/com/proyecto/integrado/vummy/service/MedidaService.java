package com.proyecto.integrado.vummy.service;

import com.proyecto.integrado.vummy.entity.Medidas;
import com.proyecto.integrado.vummy.repository.MedidasRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MedidaService {

    private final MedidasRepository medidasRepository;

    public MedidaService(MedidasRepository medidasRepository) {
        this.medidasRepository = medidasRepository;
    }

    public Optional<Medidas> obtenerPorId(Long id) {
        return medidasRepository.findById(id);
    }

    public Medidas actualizarMedidas(Medidas medidas) {
        return medidasRepository.save(medidas);
    }

    public boolean eliminarMedidas(Long id) {
        if (medidasRepository.existsById(id)) {
            medidasRepository.deleteById(id);
            return true;
        }
        return false;
    }
}