package co.edu.uco.kudos.dto;

import co.edu.uco.kudos.transversal.utilitario.UtilFecha;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

import java.time.LocalDateTime;
import java.util.UUID;

public class CategoriaDto {

    private UUID id;
    private String nombre;
    private String descripcion;
    private boolean activa;
    private LocalDateTime fechaCreacion;

    public CategoriaDto() {
        setId(UtilId.VALOR_DEFECTO);
        setNombre(UtilTexto.VACIO);
        setDescripcion(UtilTexto.VACIO);
        setActiva(true);
        setFechaCreacion(UtilFecha.FECHA_HORA_POR_DEFECTO);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = UtilId.valorDefecto(id);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(nombre);
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(descripcion);
    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = UtilFecha.valorDefecto(fechaCreacion);
    }
}
