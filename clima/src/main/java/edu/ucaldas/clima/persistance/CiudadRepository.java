package edu.ucaldas.clima.persistance;

import edu.ucaldas.clima.model.Ciudad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CiudadRepository extends JpaRepository<Ciudad, Long> {
    List<Ciudad> findByDepartamentoIgnoreCase(String departamento);
    Optional<Ciudad> findByNombreIgnoreCase(String nombre);
}

