package co.edu.uco.kudos.dto;

import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

public class AutoridadDto {

    private String rol;
    private String nombres;
    private String apellidos;
    private String correoInstitucional;
    private String numeroCelular;

    public AutoridadDto() {
        setRol(UtilTexto.VACIO);
        setNombres(UtilTexto.VACIO);
        setApellidos(UtilTexto.VACIO);
        setCorreoInstitucional(UtilTexto.VACIO);
        setNumeroCelular(UtilTexto.VACIO);
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(rol);
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

    public String getNumeroCelular() {
        return numeroCelular;
    }

    public void setNumeroCelular(String numeroCelular) {
        this.numeroCelular = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(numeroCelular);
    }
}
