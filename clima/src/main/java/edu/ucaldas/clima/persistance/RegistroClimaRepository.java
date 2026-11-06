package edu.ucaldas.clima.persistance;

import edu.ucaldas.clima.model.RegistroClima;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface RegistroClimaRepository extends JpaRepository<RegistroClima, Long> {

    List<RegistroClima> findByEstacionId(Long estacionId);

    List<RegistroClima> findByEstacionCiudadId(Long ciudadId);

    List<RegistroClima> findByFechaBetween(LocalDateTime inicio, LocalDateTime fin);

    List<RegistroClima> findByEstacionCiudadIdAndFechaBetween(Long ciudadId, LocalDateTime inicio, LocalDateTime fin);

    List<RegistroClima> findByTemperaturaGreaterThan(Double temperatura);

    @Query("""
        SELECT r
        FROM RegistroClima r
        WHERE r.temperatura > :temperatura
        ORDER BY r.temperatura DESC
    """)
    List<RegistroClima> obtenerMedicionesSuperioresA(@Param("temperatura") Double temperatura);

    @Query("""
        SELECT AVG(r.temperatura)
        FROM RegistroClima r
        WHERE r.estacion.ciudad.id = :ciudadId
    """)
    Double obtenerTemperaturaPromedio(@Param("ciudadId") Long ciudadId);
}
