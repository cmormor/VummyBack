package com.proyecto.integrado.vummy.service;

import com.proyecto.integrado.vummy.entity.Medidas;
import com.proyecto.integrado.vummy.repository.MedidasRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MedidasService {

    private final MedidasRepository medidasRepository;

    public MedidasService(MedidasRepository medidasRepository) {
        this.medidasRepository = medidasRepository;
    }

    public Optional<Medidas> obtenerPorId(Long id) {
        return medidasRepository.findById(id);
    }

    public Optional<Medidas> obtenerPorUsuario(Long usuarioId) {
        return medidasRepository.findByUsuarioId(usuarioId);
    }

    public Medidas guardarMedidas(Medidas medidas) {
        return medidasRepository.save(medidas);
    }

    public Optional<Medidas> actualizarMedidas(Long id, Medidas medidasActualizadas) {
        Optional<Medidas> medidasOptional = medidasRepository.findById(id);
        if (medidasOptional.isPresent()) {
            Medidas medidas = medidasOptional.get();
            medidas.setAltura(medidasActualizadas.getAltura());
            medidas.setCuelloManga(medidasActualizadas.getCuelloManga());
            medidas.setPecho(medidasActualizadas.getPecho());
            medidas.setCintura(medidasActualizadas.getCintura());
            medidas.setCadera(medidasActualizadas.getCadera());
            medidas.setEntrepierna(medidasActualizadas.getEntrepierna());
            return Optional.of(medidasRepository.save(medidas));
        }
        return Optional.empty();
    }
}
