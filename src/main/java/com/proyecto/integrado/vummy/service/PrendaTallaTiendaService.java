package com.proyecto.integrado.vummy.service;

import com.proyecto.integrado.vummy.dto.PrendaTallaTiendaCreateDTO;
import com.proyecto.integrado.vummy.dto.PrendaTallaTiendaDTO;
import com.proyecto.integrado.vummy.entity.Prenda;
import com.proyecto.integrado.vummy.entity.PrendaTallaTienda;
import com.proyecto.integrado.vummy.entity.Talla;
import com.proyecto.integrado.vummy.entity.Tienda;
import com.proyecto.integrado.vummy.repository.PrendaTallaTiendaRepository;
import com.proyecto.integrado.vummy.repository.TallaRepository;

import lombok.RequiredArgsConstructor;

import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrendaTallaTiendaService {

    private final PrendaTallaTiendaRepository prendaTallaTiendaRepository;
    private final TallaRepository tallaRepository;

    public void asociarTallas(Long prendaId, List<PrendaTallaTiendaCreateDTO> tallasDto) {
        Long tiendaId = prendaTallaTiendaRepository.findTiendaIdByPrendaId(prendaId);
        if (tiendaId == null) {
            throw new ServiceException("No se encontró la tienda para la prenda " + prendaId);
        }

        for (PrendaTallaTiendaCreateDTO dto : tallasDto) {
            try {
                Talla talla = tallaRepository.findById(dto.getTallaId())
                        .orElseThrow(() -> new ServiceException("Talla no encontrada: " + dto.getTallaId()));

                Prenda prenda = new Prenda();
                prenda.setId(prendaId);

                Tienda tienda = new Tienda();
                tienda.setId(tiendaId);

                PrendaTallaTienda ptt = new PrendaTallaTienda();
                ptt.setPrenda(prenda);
                ptt.setTienda(tienda);
                ptt.setTalla(talla);
                ptt.setCantidad(dto.getCantidad());

                prendaTallaTiendaRepository.save(ptt);

            } catch (Exception ex) {
                throw new ServiceException(
                        String.format("Error asociando talla %d a prenda %d: %s",
                                dto.getTallaId(), prendaId, ex.getMessage()),
                        ex);
            }
        }
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
                        talla.getCantidad()))
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
                        talla.getCantidad()))
                .collect(Collectors.toList());
    }
}