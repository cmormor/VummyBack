package com.proyecto.integrado.vummy.service;

import com.proyecto.integrado.vummy.dto.PrendaDTO;
import com.proyecto.integrado.vummy.entity.Prenda;
import com.proyecto.integrado.vummy.entity.Talla;
import com.proyecto.integrado.vummy.entity.Tienda;
import com.proyecto.integrado.vummy.repository.PrendaRepository;
import com.proyecto.integrado.vummy.repository.TallaRepository;
import com.proyecto.integrado.vummy.repository.TiendaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrendaService {

    private final PrendaRepository prendaRepository;
    private final TiendaRepository tiendaRepository;
    private final TallaRepository tallaRepository;

    public List<PrendaDTO> obtenerTodas() {
        return prendaRepository.findAll().stream()
                .map(prenda -> new PrendaDTO(
                        prenda.getId(),
                        prenda.getNombre(),
                        prenda.getPrecio(),
                        prenda.getTalla().getId(),
                        prenda.getTienda().getId(),
                        prenda.getTalla().getNombre().name(),
                        prenda.getTienda().getNombre()
                ))
                .collect(Collectors.toList());
    }

    public Optional<PrendaDTO> obtenerPorId(Long id) {
        return prendaRepository.findById(id).map(prenda -> new PrendaDTO(
                prenda.getId(),
                prenda.getNombre(),
                prenda.getPrecio(),
                prenda.getTalla().getId(),
                prenda.getTienda().getId(),
                prenda.getTalla().getNombre().name(),
                prenda.getTienda().getNombre()
        ));
    }

    public PrendaDTO guardarPrenda(Prenda prenda) {
        Prenda prendaGuardada = prendaRepository.save(prenda);
        return new PrendaDTO(
                prendaGuardada.getId(),
                prendaGuardada.getNombre(),
                prendaGuardada.getPrecio(),
                prendaGuardada.getTalla().getId(),
                prendaGuardada.getTienda().getId(),
                prendaGuardada.getTalla().getNombre().name(),
                prendaGuardada.getTienda().getNombre()
        );
    }

    public Optional<PrendaDTO> actualizarPrenda(Long id, Prenda prenda) {
        if (prendaRepository.existsById(id)) {
            prenda.setId(id);
            Prenda prendaActualizada = prendaRepository.save(prenda);
            return Optional.of(new PrendaDTO(
                    prendaActualizada.getId(),
                    prendaActualizada.getNombre(),
                    prendaActualizada.getPrecio(),
                    prendaActualizada.getTalla().getId(),
                    prendaActualizada.getTienda().getId(),
                    prendaActualizada.getTalla().getNombre().name(),
                    prendaActualizada.getTienda().getNombre()
            ));
        }
        return Optional.empty();
    }

    public Optional<Tienda> obtenerTiendaPorId(Long tiendaId) {
        return tiendaRepository.findById(tiendaId);
    }

    public Optional<Talla> obtenerTallaPorId(Long tallaId) {
        return tallaRepository.findById(tallaId);
    }

    public List<PrendaDTO> obtenerPorTienda(Long tiendaId) {
        return prendaRepository.findAll().stream()
                .filter(prenda -> prenda.getTienda().getId().equals(tiendaId))
                .map(prenda -> new PrendaDTO(
                        prenda.getId(),
                        prenda.getNombre(),
                        prenda.getPrecio(),
                        prenda.getTalla().getId(),
                        prenda.getTienda().getId(),
                        prenda.getTalla().getNombre().name(),
                        prenda.getTienda().getNombre()
                ))
                .collect(Collectors.toList());
    }

    

    public boolean eliminarPrenda(Long id) {
      if (prendaRepository.existsById(id)) {
          prendaRepository.deleteById(id);
          return true;
      }
      return false;
  }
}
