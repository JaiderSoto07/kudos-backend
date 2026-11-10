package co.edu.uco.kudos.dto;

import co.edu.uco.kudos.transversal.utilitario.UtilId;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

import java.util.UUID;

public class UsuarioDto {

    private UUID id;
    private String nombres;
    private String apellidos;
    private String correoInstitucional;
    private String estadoCuenta;
    private String rol;
    private boolean registrado;
    private String mensajeEstado;

    public UsuarioDto() {
        setId(UtilId.VALOR_DEFECTO);
        setNombres(UtilTexto.VACIO);
        setApellidos(UtilTexto.VACIO);
        setCorreoInstitucional(UtilTexto.VACIO);
        setEstadoCuenta(UtilTexto.VACIO);
        setRol(UtilTexto.VACIO);
        setRegistrado(false);
        setMensajeEstado(UtilTexto.VACIO);
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

    public String getEstadoCuenta() {
        return estadoCuenta;
    }

    public void setEstadoCuenta(String estadoCuenta) {
        this.estadoCuenta = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(estadoCuenta);
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(rol);
    }

    public boolean isRegistrado() {
        return registrado;
    }

    public void setRegistrado(boolean registrado) {
        this.registrado = registrado;
    }

    public String getMensajeEstado() {
        return mensajeEstado;
    }

    public void setMensajeEstado(String mensajeEstado) {
        this.mensajeEstado = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(mensajeEstado);
    }
}
