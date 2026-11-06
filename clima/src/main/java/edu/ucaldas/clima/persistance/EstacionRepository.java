package edu.ucaldas.clima.persistance;

import edu.ucaldas.clima.model.Estacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EstacionRepository extends JpaRepository<Estacion, Long> {
    List<Estacion> findByCiudadId(Long ciudadId);
    Optional<Estacion> findByCodigoIgnoreCase(String codigo);
    List<Estacion> findByEstadoIgnoreCase(String estado);
}

