package edu.ucaldas.clima.persistance;

import edu.ucaldas.clima.model.Configuracion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfiguracionRepository extends JpaRepository<Configuracion, Long> {
}
