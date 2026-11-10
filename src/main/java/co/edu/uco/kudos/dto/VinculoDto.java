package co.edu.uco.kudos.dto;

import co.edu.uco.kudos.transversal.utilitario.UtilFecha;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

import java.time.LocalDateTime;
import java.util.UUID;

public class VinculoDto {

    private UUID id;
    private UUID idUsuario;
    private String nombrePersona;
    private String correo;
    private String tipoVinculo;
    private UUID idPrograma;
    private String nombrePrograma;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private String motivoDesvinculacion;
    private boolean vigente;

    public VinculoDto() {
        setId(UtilId.VALOR_DEFECTO);
        setIdUsuario(UtilId.VALOR_DEFECTO);
        setNombrePersona(UtilTexto.VACIO);
        setCorreo(UtilTexto.VACIO);
        setTipoVinculo(UtilTexto.VACIO);
        setIdPrograma(UtilId.VALOR_DEFECTO);
        setNombrePrograma(UtilTexto.VACIO);
        setFechaInicio(UtilFecha.FECHA_HORA_POR_DEFECTO);
        setFechaFin(UtilFecha.FECHA_HORA_POR_DEFECTO);
        setMotivoDesvinculacion(UtilTexto.VACIO);
        setVigente(false);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = UtilId.valorDefecto(id);
    }

    public UUID getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(UUID idUsuario) {
        this.idUsuario = UtilId.valorDefecto(idUsuario);
    }

    public String getNombrePersona() {
        return nombrePersona;
    }

    public void setNombrePersona(String nombrePersona) {
        this.nombrePersona = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(nombrePersona);
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(correo);
    }

    public String getTipoVinculo() {
        return tipoVinculo;
    }

    public void setTipoVinculo(String tipoVinculo) {
        this.tipoVinculo = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(tipoVinculo);
    }

    public UUID getIdPrograma() {
        return idPrograma;
    }

    public void setIdPrograma(UUID idPrograma) {
        this.idPrograma = UtilId.valorDefecto(idPrograma);
    }

    public String getNombrePrograma() {
        return nombrePrograma;
    }

    public void setNombrePrograma(String nombrePrograma) {
        this.nombrePrograma = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(nombrePrograma);
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = UtilFecha.valorDefecto(fechaInicio);
    }

    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDateTime fechaFin) {
        this.fechaFin = UtilFecha.valorDefecto(fechaFin);
    }

    public String getMotivoDesvinculacion() {
        return motivoDesvinculacion;
    }

    public void setMotivoDesvinculacion(String motivoDesvinculacion) {
        this.motivoDesvinculacion = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(motivoDesvinculacion);
    }

    public boolean isVigente() {
        return vigente;
    }

    public void setVigente(boolean vigente) {
        this.vigente = vigente;
    }
}
