package co.edu.uco.kudos.dto;

import co.edu.uco.kudos.transversal.utilitario.UtilId;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

import java.util.UUID;

public class CuentaDto {

    private UUID id;
    private String nombres;
    private String apellidos;
    private String correoInstitucional;
    private boolean correoVerificado;
    private String numeroCelular;
    private boolean celularVerificado;
    private boolean aprobada;
    private String estadoCuenta;
    private String mensajeEstado;
    private String rol;
    private String rolSolicitado;
    private String estadoSolicitud;
    private String motivoRechazo;
    private String ultimoCodigoEnviado;

    public CuentaDto() {
        setId(UtilId.VALOR_DEFECTO);
        setNombres(UtilTexto.VACIO);
        setApellidos(UtilTexto.VACIO);
        setCorreoInstitucional(UtilTexto.VACIO);
        setCorreoVerificado(false);
        setNumeroCelular(UtilTexto.VACIO);
        setCelularVerificado(false);
        setAprobada(false);
        setEstadoCuenta(UtilTexto.VACIO);
        setMensajeEstado(UtilTexto.VACIO);
        setRol(UtilTexto.VACIO);
        setRolSolicitado(UtilTexto.VACIO);
        setEstadoSolicitud(UtilTexto.VACIO);
        setMotivoRechazo(UtilTexto.VACIO);
        setUltimoCodigoEnviado(UtilTexto.VACIO);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = UtilId.valorDefecto(id);
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(nombres);
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(apellidos);
    }

    public String getCorreoInstitucional() {
        return correoInstitucional;
    }

    public void setCorreoInstitucional(String correoInstitucional) {
        this.correoInstitucional = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(correoInstitucional);
    }

    public boolean isCorreoVerificado() {
        return correoVerificado;
    }

    public void setCorreoVerificado(boolean correoVerificado) {
        this.correoVerificado = correoVerificado;
    }

    public String getNumeroCelular() {
        return numeroCelular;
    }

    public void setNumeroCelular(String numeroCelular) {
        this.numeroCelular = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(numeroCelular);
    }

    public boolean isCelularVerificado() {
        return celularVerificado;
    }

    public void setCelularVerificado(boolean celularVerificado) {
        this.celularVerificado = celularVerificado;
    }

    public boolean isAprobada() {
        return aprobada;
    }

    public void setAprobada(boolean aprobada) {
        this.aprobada = aprobada;
    }

    public String getEstadoCuenta() {
        return estadoCuenta;
    }

    public void setEstadoCuenta(String estadoCuenta) {
        this.estadoCuenta = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(estadoCuenta);
    }

    public String getMensajeEstado() {
        return mensajeEstado;
    }

    public void setMensajeEstado(String mensajeEstado) {
        this.mensajeEstado = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(mensajeEstado);
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(rol);
    }

    public String getRolSolicitado() {
        return rolSolicitado;
    }

    public void setRolSolicitado(String rolSolicitado) {
        this.rolSolicitado = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(rolSolicitado);
    }

    public String getEstadoSolicitud() {
        return estadoSolicitud;
    }

    public void setEstadoSolicitud(String estadoSolicitud) {
        this.estadoSolicitud = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(estadoSolicitud);
    }

    public String getMotivoRechazo() {
        return motivoRechazo;
    }

    public void setMotivoRechazo(String motivoRechazo) {
        this.motivoRechazo = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(motivoRechazo);
    }

    public String getUltimoCodigoEnviado() {
        return ultimoCodigoEnviado;
    }

    public void setUltimoCodigoEnviado(String ultimoCodigoEnviado) {
        this.ultimoCodigoEnviado = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(ultimoCodigoEnviado);
    }
}
