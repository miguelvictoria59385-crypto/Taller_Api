package edu.ucaldas.clima.persistance;

import edu.ucaldas.clima.model.TipoMedicion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TipoMedicionRepository extends JpaRepository<TipoMedicion, Long> {
    Optional<TipoMedicion> findByNombreIgnoreCase(String nombre);
}

