package com.proyecto.integrado.vummy.service;

import com.proyecto.integrado.vummy.dto.TiendaDTO;
import com.proyecto.integrado.vummy.dto.PrendaDTO;
import com.proyecto.integrado.vummy.dto.TallaDTO;
import com.proyecto.integrado.vummy.entity.Tienda;
import com.proyecto.integrado.vummy.repository.TiendaRepository;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.ArrayList;

@Service
public class TiendaService {

    private final TiendaRepository tiendaRepository;

    public TiendaService(TiendaRepository tiendaRepository) {
        this.tiendaRepository = tiendaRepository;
    }

    private TiendaDTO convertirATiendaDTO(Tienda tienda) {
      List<PrendaDTO> prendasDTO = tienda.getPrendas() != null
          ? tienda.getPrendas().stream()
              .map(prenda -> new PrendaDTO(
                  prenda.getId(),
                  prenda.getNombre(),
                  prenda.getPrecio(),
                  prenda.getDescripcion(),
                  prenda.getStock(),
                  // prenda.getTalla().getId(),
                  prenda.getTienda().getId(),
                  // prenda.getTalla().getNombre().name(),
                  prenda.getTienda().getNombre()
              ))
              .collect(Collectors.toList())
          : new ArrayList<>();
  
      List<TallaDTO> tallasDTO = tienda.getTallas() != null
          ? tienda.getTallas().stream()
              .map(TallaDTO::new)
              .collect(Collectors.toList())
          : new ArrayList<>();
  
      return new TiendaDTO(
          tienda.getId(),
          tienda.getNombre(),
          tienda.getDescripcion(),
          prendasDTO,
          tallasDTO
      );
  }

    public List<TiendaDTO> listarTiendas() {
        return tiendaRepository.findAll().stream()
            .map(this::convertirATiendaDTO)
            .collect(Collectors.toList());
    }

    public Optional<TiendaDTO> buscarPorId(Long id) {
        Optional<Tienda> tiendaOptional = tiendaRepository.findById(id);
        return tiendaOptional.map(this::convertirATiendaDTO);
    }

    public Optional<TiendaDTO> buscarPorNombre(String nombre) {
        Optional<Tienda> tiendaOptional = tiendaRepository.findByNombre(nombre);
        return tiendaOptional.map(this::convertirATiendaDTO);
    }

    public List<TallaDTO> listarTallasPorTienda(Long tiendaId) {
        Optional<Tienda> tiendaOptional = tiendaRepository.findById(tiendaId);
        if (tiendaOptional.isPresent()) {
          Tienda tienda = tiendaOptional.get();
          return tienda.getTallas().stream()
                  .map(TallaDTO::new)
                  .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }  

    public List<PrendaDTO> listarPrendasPorTienda(Long tiendaId) {
      Optional<Tienda> tiendaOptional = tiendaRepository.findById(tiendaId);
      if (tiendaOptional.isPresent()) {
          Tienda tienda = tiendaOptional.get();
          return tienda.getPrendas().stream()
                  .map(prenda -> new PrendaDTO(
                          prenda.getId(),
                          prenda.getNombre(),
                          prenda.getPrecio(),
                          prenda.getDescripcion(),
                          prenda.getStock(),
                          // prenda.getTalla().getId(),
                          prenda.getTienda().getId(),
                          // prenda.getTalla().getNombre().name(),
                          prenda.getTienda().getNombre()
                  ))
                  .collect(Collectors.toList());
      }
      return new ArrayList<>();
  }  

    public TiendaDTO agregarTienda(TiendaDTO tiendaDTO) {
      Tienda tienda = new Tienda();
      tienda.setNombre(tiendaDTO.getNombre());
      tienda.setDescripcion(tiendaDTO.getDescripcion());
      
  
      Tienda tiendaGuardada = tiendaRepository.save(tienda);
      
      return convertirATiendaDTO(tiendaGuardada);
  }
  

    public Optional<TiendaDTO> actualizarTienda(Long id, Tienda tiendaActualizada) {
        Optional<Tienda> tiendaOptional = tiendaRepository.findById(id);
        if (tiendaOptional.isPresent()) {
            Tienda tienda = tiendaOptional.get();
            tienda.setNombre(tiendaActualizada.getNombre());
            tienda.setDescripcion(tiendaActualizada.getDescripcion());
            Tienda tiendaGuardada = tiendaRepository.save(tienda);
            return Optional.of(convertirATiendaDTO(tiendaGuardada));
        }
        return Optional.empty();
    }

    @Transactional
    public boolean eliminarTienda(Long id) {
        Optional<Tienda> tiendaOptional = tiendaRepository.findById(id);
        if (tiendaOptional.isPresent()) {
            Tienda tienda = tiendaOptional.get();

            // tienda.getTallas().forEach(talla -> {
            //     talla.getPrendas().forEach(prenda -> prenda.setTalla(null));
            //     talla.getPrendas().clear();
            // });

            tienda.getPrendas().clear();

            tienda.getTallas().forEach(talla -> talla.setTienda(null));
            tienda.getTallas().clear();

            tiendaRepository.save(tienda);

            tiendaRepository.delete(tienda);
            return true;
        }
        return false;
    }
}
