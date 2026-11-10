package co.edu.uco.kudos.dto;

import co.edu.uco.kudos.transversal.utilitario.UtilFecha;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class KudoDto {

    private UUID id;
    private UUID idAutor;
    private String nombreAutor;
    private UUID idDestinatario;
    private String nombreDestinatario;
    private UUID idCategoria;
    private String nombreCategoria;
    private String contenido;
    private LocalDateTime fechaCreacion;
    private String estado;
    private int cantidadVisualizaciones;
    private BigDecimal promedioCalificacion;
    private int cantidadCalificaciones;

    public KudoDto() {
        setId(UtilId.VALOR_DEFECTO);
        setIdAutor(UtilId.VALOR_DEFECTO);
        setNombreAutor(UtilTexto.VACIO);
        setIdDestinatario(UtilId.VALOR_DEFECTO);
        setNombreDestinatario(UtilTexto.VACIO);
        setIdCategoria(UtilId.VALOR_DEFECTO);
        setNombreCategoria(UtilTexto.VACIO);
        setContenido(UtilTexto.VACIO);
        setFechaCreacion(UtilFecha.FECHA_HORA_POR_DEFECTO);
        setEstado(UtilTexto.VACIO);
        setCantidadVisualizaciones(0);
        setPromedioCalificacion(BigDecimal.ZERO);
        setCantidadCalificaciones(0);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = UtilId.valorDefecto(id);
    }

    public UUID getIdAutor() {
        return idAutor;
    }

    public void setIdAutor(UUID idAutor) {
        this.idAutor = UtilId.valorDefecto(idAutor);
    }

    public String getNombreAutor() {
        return nombreAutor;
    }

    public void setNombreAutor(String nombreAutor) {
        this.nombreAutor = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(nombreAutor);
    }

    public UUID getIdDestinatario() {
        return idDestinatario;
    }

    public void setIdDestinatario(UUID idDestinatario) {
        this.idDestinatario = UtilId.valorDefecto(idDestinatario);
    }

    public String getNombreDestinatario() {
        return nombreDestinatario;
    }

    public void setNombreDestinatario(String nombreDestinatario) {
        this.nombreDestinatario = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(nombreDestinatario);
    }

    public UUID getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(UUID idCategoria) {
        this.idCategoria = UtilId.valorDefecto(idCategoria);
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(nombreCategoria);
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(contenido);
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = UtilFecha.valorDefecto(fechaCreacion);
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(estado);
    }

    public int getCantidadVisualizaciones() {
        return cantidadVisualizaciones;
    }

    public void setCantidadVisualizaciones(int cantidadVisualizaciones) {
        this.cantidadVisualizaciones = Math.max(cantidadVisualizaciones, 0);
    }

    public BigDecimal getPromedioCalificacion() {
        return promedioCalificacion;
    }

    public void setPromedioCalificacion(BigDecimal promedioCalificacion) {
        this.promedioCalificacion = promedioCalificacion == null ? BigDecimal.ZERO : promedioCalificacion;
    }

    public int getCantidadCalificaciones() {
        return cantidadCalificaciones;
    }

    public void setCantidadCalificaciones(int cantidadCalificaciones) {
        this.cantidadCalificaciones = Math.max(cantidadCalificaciones, 0);
    }
}
