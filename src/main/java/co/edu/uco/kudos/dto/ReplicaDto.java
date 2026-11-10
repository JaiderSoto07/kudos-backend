package co.edu.uco.kudos.dto;

import co.edu.uco.kudos.transversal.utilitario.UtilFecha;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import co.edu.uco.kudos.transversal.utilitario.UtilObjeto;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ReplicaDto {

    private UUID id;
    private UUID idKudo;
    private UUID idReplicaPadre;
    private UUID idAutor;
    private String nombreAutor;
    private String contenido;
    private LocalDateTime fechaCreacion;
    private String estado;
    private boolean vetada;
    private BigDecimal promedioCalificacion;
    private int cantidadCalificaciones;
    private List<ReplicaDto> respuestas;

    public ReplicaDto() {
        setId(UtilId.VALOR_DEFECTO);
        setIdKudo(UtilId.VALOR_DEFECTO);
        setIdReplicaPadre(UtilId.VALOR_DEFECTO);
        setIdAutor(UtilId.VALOR_DEFECTO);
        setNombreAutor(UtilTexto.VACIO);
        setContenido(UtilTexto.VACIO);
        setFechaCreacion(UtilFecha.FECHA_HORA_POR_DEFECTO);
        setEstado(UtilTexto.VACIO);
        setVetada(false);
        setPromedioCalificacion(BigDecimal.ZERO);
        setCantidadCalificaciones(0);
        setRespuestas(new ArrayList<>());
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = UtilId.valorDefecto(id);
    }

    public UUID getIdKudo() {
        return idKudo;
    }

    public void setIdKudo(UUID idKudo) {
        this.idKudo = UtilId.valorDefecto(idKudo);
    }

    public UUID getIdReplicaPadre() {
        return idReplicaPadre;
    }

    public void setIdReplicaPadre(UUID idReplicaPadre) {
        this.idReplicaPadre = UtilId.valorDefecto(idReplicaPadre);
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

    public boolean isVetada() {
        return vetada;
    }

    public void setVetada(boolean vetada) {
        this.vetada = vetada;
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

    public List<ReplicaDto> getRespuestas() {
        return respuestas;
    }

    public void setRespuestas(List<ReplicaDto> respuestas) {
        this.respuestas = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(respuestas, new ArrayList<>());
    }
}
