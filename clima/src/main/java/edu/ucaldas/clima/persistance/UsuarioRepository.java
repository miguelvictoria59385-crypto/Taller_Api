package edu.ucaldas.clima.persistance;

import edu.ucaldas.clima.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByCorreoIgnoreCase(String correo);

    @Query("""
        SELECT DISTINCT u
        FROM Usuario u
        JOIN u.roles r
        WHERE LOWER(r.nombre) = LOWER(:nombreRol)
    """)
    List<Usuario> buscarPorNombreRol(@Param("nombreRol") String nombreRol);
}

