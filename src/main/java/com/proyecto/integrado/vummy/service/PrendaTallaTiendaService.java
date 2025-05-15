package com.proyecto.integrado.vummy.service;

import com.proyecto.integrado.vummy.dto.PrendaTallaTiendaDTO;
import com.proyecto.integrado.vummy.entity.Prenda;
import com.proyecto.integrado.vummy.entity.PrendaTallaTienda;
import com.proyecto.integrado.vummy.entity.Tienda;
import com.proyecto.integrado.vummy.repository.PrendaTallaTiendaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrendaTallaTiendaService {

    private final PrendaTallaTiendaRepository prendaTallaTiendaRepository;

    public void asociarTallas(Long prendaId, List<PrendaTallaTienda> tallas) {
        Long tiendaId = obtenerTiendaIdPorPrenda(prendaId);

        tallas.forEach(talla -> {
            Prenda prenda = new Prenda();
            prenda.setId(prendaId);
            talla.setPrenda(prenda);

            Tienda tienda = new Tienda();
            tienda.setId(tiendaId);
            talla.setTienda(tienda);

            prendaTallaTiendaRepository.save(talla);
        });
    }

    private Long obtenerTiendaIdPorPrenda(Long prendaId) {
        return prendaTallaTiendaRepository.findTiendaIdByPrendaId(prendaId);
    }

    public List<PrendaTallaTiendaDTO> obtenerTallasDisponibles(Long prendaId, Long tiendaId) {
        List<PrendaTallaTienda> tallas = prendaTallaTiendaRepository.findByPrendaIdAndTiendaId(prendaId, tiendaId);
        return tallas.stream()
                .map(talla -> new PrendaTallaTiendaDTO(
                        talla.getTalla().getId(),
                        talla.getTalla().getNombre().name(),
                        talla.getTienda().getId(),
                        talla.getTienda().getNombre(),
                        talla.getCantidad()
                ))
                .collect(Collectors.toList());
    }

    public List<PrendaTallaTiendaDTO> obtenerTallasDisponibles(Long prendaId) {
        Long tiendaId = obtenerTiendaIdPorPrenda(prendaId);

        List<PrendaTallaTienda> tallas = prendaTallaTiendaRepository.findByPrendaIdAndTiendaId(prendaId, tiendaId);

        return tallas.stream()
                .map(talla -> new PrendaTallaTiendaDTO(
                        talla.getTalla().getId(),
                        talla.getTalla().getNombre().name(),
                        talla.getTienda().getId(),
                        talla.getTienda().getNombre(),
                        talla.getCantidad()
                ))
                .collect(Collectors.toList());
    }
}