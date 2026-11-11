package co.edu.uco.kudos.dto;

import co.edu.uco.kudos.transversal.utilitario.UtilFecha;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

import java.time.LocalDateTime;
import java.util.UUID;

public class AuditoriaDto {

    private UUID id;
    private LocalDateTime fechaHora;
    private UUID idActor;
    private String nombreActor;
    private String accion;
    private String tipoObjeto;
    private UUID idObjeto;
    private String estadoAnterior;
    private String estadoNuevo;
    private String motivo;
    private String detalle;

    public AuditoriaDto() {
        setId(UtilId.VALOR_DEFECTO);
        setFechaHora(UtilFecha.FECHA_HORA_POR_DEFECTO);
        setIdActor(UtilId.VALOR_DEFECTO);
        setNombreActor(UtilTexto.VACIO);
        setAccion(UtilTexto.VACIO);
        setTipoObjeto(UtilTexto.VACIO);
        setIdObjeto(UtilId.VALOR_DEFECTO);
        setEstadoAnterior(UtilTexto.VACIO);
        setEstadoNuevo(UtilTexto.VACIO);
        setMotivo(UtilTexto.VACIO);
        setDetalle(UtilTexto.VACIO);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = UtilId.valorDefecto(id);
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = UtilFecha.valorDefecto(fechaHora);
    }

    public UUID getIdActor() {
        return idActor;
    }

    public void setIdActor(UUID idActor) {
        this.idActor = UtilId.valorDefecto(idActor);
    }

    public String getNombreActor() {
        return nombreActor;
    }

    public void setNombreActor(String nombreActor) {
        this.nombreActor = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(nombreActor);
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(accion);
    }

    public String getTipoObjeto() {
        return tipoObjeto;
    }

    public void setTipoObjeto(String tipoObjeto) {
        this.tipoObjeto = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(tipoObjeto);
    }

    public UUID getIdObjeto() {
        return idObjeto;
    }

    public void setIdObjeto(UUID idObjeto) {
        this.idObjeto = UtilId.valorDefecto(idObjeto);
    }

    public String getEstadoAnterior() {
        return estadoAnterior;
    }

    public void setEstadoAnterior(String estadoAnterior) {
        this.estadoAnterior = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(estadoAnterior);
    }

    public String getEstadoNuevo() {
        return estadoNuevo;
    }

    public void setEstadoNuevo(String estadoNuevo) {
        this.estadoNuevo = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(estadoNuevo);
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(motivo);
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(detalle);
    }
}
