package co.edu.uco.kudos.dto;

import co.edu.uco.kudos.transversal.utilitario.UtilFecha;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

import java.time.LocalDateTime;
import java.util.UUID;

public class SolicitudDto {

    private UUID id;
    private UUID idSolicitante;
    private String nombreSolicitante;
    private String correoSolicitante;
    private String rolSolicitado;
    private String nombreFacultad;
    private String nombrePrograma;
    private String estado;
    private LocalDateTime fechaSolicitud;
    private String motivoRechazo;

    public SolicitudDto() {
        setId(UtilId.VALOR_DEFECTO);
        setIdSolicitante(UtilId.VALOR_DEFECTO);
        setNombreSolicitante(UtilTexto.VACIO);
        setCorreoSolicitante(UtilTexto.VACIO);
        setRolSolicitado(UtilTexto.VACIO);
        setNombreFacultad(UtilTexto.VACIO);
        setNombrePrograma(UtilTexto.VACIO);
        setEstado(UtilTexto.VACIO);
        setFechaSolicitud(UtilFecha.FECHA_HORA_POR_DEFECTO);
        setMotivoRechazo(UtilTexto.VACIO);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = UtilId.valorDefecto(id);
    }

    public UUID getIdSolicitante() {
        return idSolicitante;
    }

    public void setIdSolicitante(UUID idSolicitante) {
        this.idSolicitante = UtilId.valorDefecto(idSolicitante);
    }

    public String getNombreSolicitante() {
        return nombreSolicitante;
    }

    public void setNombreSolicitante(String nombreSolicitante) {
        this.nombreSolicitante = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(nombreSolicitante);
    }

    public String getCorreoSolicitante() {
        return correoSolicitante;
    }

    public void setCorreoSolicitante(String correoSolicitante) {
        this.correoSolicitante = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(correoSolicitante);
    }

    public String getRolSolicitado() {
        return rolSolicitado;
    }

    public void setRolSolicitado(String rolSolicitado) {
        this.rolSolicitado = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(rolSolicitado);
    }

    public String getNombreFacultad() {
        return nombreFacultad;
    }

    public void setNombreFacultad(String nombreFacultad) {
        this.nombreFacultad = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(nombreFacultad);
    }

    public String getNombrePrograma() {
        return nombrePrograma;
    }

    public void setNombrePrograma(String nombrePrograma) {
        this.nombrePrograma = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(nombrePrograma);
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(estado);
    }

    public LocalDateTime getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(LocalDateTime fechaSolicitud) {
        this.fechaSolicitud = UtilFecha.valorDefecto(fechaSolicitud);
    }

    public String getMotivoRechazo() {
        return motivoRechazo;
    }

    public void setMotivoRechazo(String motivoRechazo) {
        this.motivoRechazo = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(motivoRechazo);
    }
}
