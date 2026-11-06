package edu.ucaldas.clima.persistance;

import edu.ucaldas.clima.model.Alerta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AlertaRepository extends JpaRepository<Alerta, Long> {

    List<Alerta> findByNivelIgnoreCase(String nivel);

    List<Alerta> findByEstadoIgnoreCase(String estado);

    List<Alerta> findByRegistroClimaId(Long registroClimaId);

    @Query("""
        SELECT a
        FROM Alerta a
        WHERE a.registroClima.estacion.ciudad.id = :ciudadId
          AND UPPER(a.estado) = 'ACTIVA'
    """)
    List<Alerta> obtenerAlertasActivasPorCiudad(@Param("ciudadId") Long ciudadId);
}

