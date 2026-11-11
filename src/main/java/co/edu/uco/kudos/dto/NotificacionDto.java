package co.edu.uco.kudos.dto;

import co.edu.uco.kudos.transversal.utilitario.UtilFecha;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

import java.time.LocalDateTime;
import java.util.UUID;

public class NotificacionDto {

    private UUID id;
    private String tipo;
    private String mensaje;
    private String tipoReferencia;
    private UUID idReferencia;
    private LocalDateTime fechaCreacion;
    private boolean leida;

    public NotificacionDto() {
        setId(UtilId.VALOR_DEFECTO);
        setTipo(UtilTexto.VACIO);
        setMensaje(UtilTexto.VACIO);
        setTipoReferencia(UtilTexto.VACIO);
        setIdReferencia(UtilId.VALOR_DEFECTO);
        setFechaCreacion(UtilFecha.FECHA_HORA_POR_DEFECTO);
        setLeida(false);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = UtilId.valorDefecto(id);
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(tipo);
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(mensaje);
    }

    public String getTipoReferencia() {
        return tipoReferencia;
    }

    public void setTipoReferencia(String tipoReferencia) {
        this.tipoReferencia = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(tipoReferencia);
    }

    public UUID getIdReferencia() {
        return idReferencia;
    }

    public void setIdReferencia(UUID idReferencia) {
        this.idReferencia = UtilId.valorDefecto(idReferencia);
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = UtilFecha.valorDefecto(fechaCreacion);
    }

    public boolean isLeida() {
        return leida;
    }

    public void setLeida(boolean leida) {
        this.leida = leida;
    }
}
