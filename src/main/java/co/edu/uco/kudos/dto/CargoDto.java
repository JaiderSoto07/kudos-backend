package co.edu.uco.kudos.dto;

import co.edu.uco.kudos.transversal.utilitario.UtilFecha;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

import java.time.LocalDateTime;
import java.util.UUID;

public class CargoDto {

    private UUID idUsuario;
    private String nombrePersona;
    private String correo;
    private String rol;
    private UUID idFacultad;
    private String nombreFacultad;
    private UUID idPrograma;
    private String nombrePrograma;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private String motivoFin;
    private boolean vigente;

    public CargoDto() {
        setIdUsuario(UtilId.VALOR_DEFECTO);
        setNombrePersona(UtilTexto.VACIO);
        setCorreo(UtilTexto.VACIO);
        setRol(UtilTexto.VACIO);
        setIdFacultad(UtilId.VALOR_DEFECTO);
        setNombreFacultad(UtilTexto.VACIO);
        setIdPrograma(UtilId.VALOR_DEFECTO);
        setNombrePrograma(UtilTexto.VACIO);
        setFechaInicio(UtilFecha.FECHA_HORA_POR_DEFECTO);
        setFechaFin(UtilFecha.FECHA_HORA_POR_DEFECTO);
        setMotivoFin(UtilTexto.VACIO);
        setVigente(false);
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

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(rol);
    }

    public UUID getIdFacultad() {
        return idFacultad;
    }

    public void setIdFacultad(UUID idFacultad) {
        this.idFacultad = UtilId.valorDefecto(idFacultad);
    }

    public String getNombreFacultad() {
        return nombreFacultad;
    }

    public void setNombreFacultad(String nombreFacultad) {
        this.nombreFacultad = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(nombreFacultad);
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

    public String getMotivoFin() {
        return motivoFin;
    }

    public void setMotivoFin(String motivoFin) {
        this.motivoFin = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(motivoFin);
    }

    public boolean isVigente() {
        return vigente;
    }

    public void setVigente(boolean vigente) {
        this.vigente = vigente;
    }
}
