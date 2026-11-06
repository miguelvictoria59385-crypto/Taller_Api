-- Script SQL para PostgreSQL - Sistema de Gestión Meteorológica (Taller API REST + JPA)

CREATE TABLE IF NOT EXISTS ciudades (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    departamento VARCHAR(100) NOT NULL,
    poblacion BIGINT
);

CREATE TABLE IF NOT EXISTS configuraciones_estacion (
    id BIGSERIAL PRIMARY KEY,
    intervalo_medicion_minutos INTEGER NOT NULL DEFAULT 15,
    unidad_temperatura VARCHAR(20) NOT NULL DEFAULT 'CELSIUS',
    altura_metros DOUBLE PRECISION,
    calibracion_automatica BOOLEAN DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS estaciones (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    codigo VARCHAR(50) NOT NULL UNIQUE,
    ubicacion VARCHAR(150),
    estado VARCHAR(30) NOT NULL DEFAULT 'ACTIVA',
    ciudad_id BIGINT NOT NULL REFERENCES ciudades(id) ON DELETE CASCADE,
    configuracion_id BIGINT UNIQUE REFERENCES configuraciones_estacion(id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS registro_clima (
    id BIGSERIAL PRIMARY KEY,
    temperatura DOUBLE PRECISION NOT NULL,
    humedad INTEGER NOT NULL,
    presion DOUBLE PRECISION,
    fecha TIMESTAMP,
    descripcion VARCHAR(150),
    estacion_id BIGINT REFERENCES estaciones(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS tipos_medicion (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(80) NOT NULL UNIQUE,
    unidad VARCHAR(30) NOT NULL,
    descripcion VARCHAR(200)
);

CREATE TABLE IF NOT EXISTS registro_clima_tipo_medicion (
    registro_clima_id BIGINT NOT NULL REFERENCES registro_clima(id) ON DELETE CASCADE,
    tipo_medicion_id BIGINT NOT NULL REFERENCES tipos_medicion(id) ON DELETE CASCADE,
    PRIMARY KEY (registro_clima_id, tipo_medicion_id)
);

CREATE TABLE IF NOT EXISTS alertas (
    id BIGSERIAL PRIMARY KEY,
    descripcion VARCHAR(255) NOT NULL,
    nivel VARCHAR(30) NOT NULL,
    fecha TIMESTAMP NOT NULL,
    estado VARCHAR(30) NOT NULL DEFAULT 'ACTIVA',
    registro_clima_id BIGINT NOT NULL REFERENCES registro_clima(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS usuarios (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    correo VARCHAR(120) NOT NULL UNIQUE,
    contrasena VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS roles (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    descripcion VARCHAR(200)
);

CREATE TABLE IF NOT EXISTS usuario_rol (
    usuario_id BIGINT NOT NULL REFERENCES usuarios(id) ON DELETE CASCADE,
    rol_id BIGINT NOT NULL REFERENCES roles(id) ON DELETE CASCADE,
    PRIMARY KEY (usuario_id, rol_id)
);

