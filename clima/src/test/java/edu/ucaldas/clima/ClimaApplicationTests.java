package edu.ucaldas.clima;

import edu.ucaldas.clima.domain.*;
import edu.ucaldas.clima.dto.*;
import edu.ucaldas.clima.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ClimaApplicationTests {

    @Autowired
    private CiudadService ciudadService;

    @Autowired
    private EstacionService estacionService;

    @Autowired
    private TipoMedicionService tipoMedicionService;

    @Autowired
    private RegistroClimaService registroClimaService;

    @Autowired
    private RolService rolService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private AlertaService alertaService;

    @Test
    void flujoCompletoSistemaMeteorologico() {
        // 1. Crear Ciudad
        CiudadDTO ciudadDto = new CiudadDTO();
        ciudadDto.setNombre("Manizales");
        ciudadDto.setDepartamento("Caldas");
        ciudadDto.setPoblacion(458000L);
        Ciudad ciudad = ciudadService.crear(ciudadDto);
        assertNotNull(ciudad.getId());

        // 2. Crear Estación con Configuración (Relación 1:N con Ciudad y 1:1 con
        // Configuración)
        ConfiguracionDTO configDto = new ConfiguracionDTO();
        configDto.setIntervaloMedicionMinutos(10);
        configDto.setUnidadTemperatura("CELSIUS");
        configDto.setAlturaMetros(2150.0);
        configDto.setCalibracionAutomatica(true);

        EstacionDTO estacionDto = new EstacionDTO();
        estacionDto.setNombre("Estación Palogrande");
        estacionDto.setCodigo("EST-MZL-001");
        estacionDto.setUbicacion("Campus Central");
        estacionDto.setEstado("ACTIVA");
        estacionDto.setCiudadId(ciudad.getId());
        estacionDto.setConfiguracion(configDto);

        Estacion estacion = estacionService.crear(estacionDto);
        assertNotNull(estacion.getId());
        assertNotNull(estacion.getConfiguracion());
        assertEquals(1, estacionService.listarPorCiudad(ciudad.getId()).size());

        // 3. Crear Tipo de Medición y Mediciones (Relación 1:N con Estación y N:M con
        // TipoMedicion)
        TipoMedicionDTO tipoDto = new TipoMedicionDTO();
        tipoDto.setNombre("Temperatura Exterior");
        tipoDto.setUnidad("C");
        tipoDto.setDescripcion("Sensor termico principal");
        TipoMedicion tipo = tipoMedicionService.crear(tipoDto);

        RegistroClimaDTO reg1Dto = new RegistroClimaDTO();
        reg1Dto.setTemperatura(32.0);
        reg1Dto.setHumedad(60);
        reg1Dto.setPresion(1011.5);
        reg1Dto.setDescripcion("Calor intenso");
        reg1Dto.setFecha(LocalDateTime.of(2026, 10, 30, 12, 0));
        reg1Dto.setEstacionId(estacion.getId());
        reg1Dto.setTipoMedicionIds(Set.of(tipo.getId()));
        RegistroClima reg1 = registroClimaService.crear(reg1Dto);

        RegistroClimaDTO reg2Dto = new RegistroClimaDTO();
        reg2Dto.setTemperatura(20.0);
        reg2Dto.setHumedad(78);
        reg2Dto.setPresion(1015.0);
        reg2Dto.setDescripcion("Templado");
        reg2Dto.setFecha(LocalDateTime.of(2026, 10, 30, 18, 0));
        reg2Dto.setEstacionId(estacion.getId());
        registroClimaService.crear(reg2Dto);

        assertEquals(2, registroClimaService.listarPorEstacion(estacion.getId()).size());

        // 4. Probar Retos de Consultas Personalizadas JPQL
        List<RegistroClima> altas = registroClimaService.obtenerMedicionesTemperaturaSuperior(30.0);
        assertEquals(1, altas.size());
        assertEquals(32.0, altas.get(0).getTemperatura());

        Map<String, Object> promedioMap = registroClimaService.obtenerTemperaturaPromedioCiudad(ciudad.getId());
        assertEquals(26.0, promedioMap.get("temperaturaPromedio"));

        // 5. Usuarios y Roles (Relación N:M)
        RolDTO rolAdminDto = new RolDTO();
        rolAdminDto.setNombre("ADMIN");
        rolAdminDto.setDescripcion("Administrador");
        Rol rolAdmin = rolService.crear(rolAdminDto);

        RolDTO rolOperadorDto = new RolDTO();
        rolOperadorDto.setNombre("OPERADOR");
        rolOperadorDto.setDescripcion("Operador de estacion");
        Rol rolOperador = rolService.crear(rolOperadorDto);

        UsuarioDTO usuarioDto = new UsuarioDTO();
        usuarioDto.setNombre("Miguel");
        usuarioDto.setCorreo("miguel@ucaldas.edu.co");
        usuarioDto.setContrasena("secreto123");
        Usuario usuario = usuarioService.crear(usuarioDto);

        usuarioService.asignarRol(usuario.getId(), rolAdmin.getId());
        usuarioService.asignarRol(usuario.getId(), rolOperador.getId());

        List<Usuario> admins = usuarioService.buscarPorRol("ADMIN");
        assertEquals(1, admins.size());
        assertEquals(2, admins.get(0).getRoles().size());

        // 6. Alertas Climáticas y consulta JPQL de alertas activas por ciudad
        AlertaDTO alertaDto = new AlertaDTO();
        alertaDto.setDescripcion("Alerta por temperatura mayor a 30C");
        alertaDto.setNivel("ALTO");
        alertaDto.setEstado("ACTIVA");
        alertaDto.setRegistroClimaId(reg1.getId());
        Alerta alerta = alertaService.crear(alertaDto);
        assertNotNull(alerta.getId());

        assertEquals(1, alertaService.listarPorNivel("ALTO").size());
        assertEquals(1, alertaService.listarActivasPorCiudad(ciudad.getId()).size());
    }
}
