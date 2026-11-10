package co.edu.uco.kudos.dto;

import co.edu.uco.kudos.transversal.utilitario.UtilId;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

import java.util.UUID;

public class RegistroDto {

    private String nombres;
    private String apellidos;
    private String numeroCelular;
    private String rolSolicitado;
    private UUID idFacultad;
    private UUID idPrograma;

    public RegistroDto() {
        setNombres(UtilTexto.VACIO);
        setApellidos(UtilTexto.VACIO);
        setNumeroCelular(UtilTexto.VACIO);
        setRolSolicitado(UtilTexto.VACIO);
        setIdFacultad(UtilId.VALOR_DEFECTO);
        setIdPrograma(UtilId.VALOR_DEFECTO);
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

    public String getNumeroCelular() {
        return numeroCelular;
    }

    public void setNumeroCelular(String numeroCelular) {
        this.numeroCelular = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(numeroCelular);
    }

    public String getRolSolicitado() {
        return rolSolicitado;
    }

    public void setRolSolicitado(String rolSolicitado) {
        this.rolSolicitado = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(rolSolicitado);
    }

    public UUID getIdFacultad() {
        return idFacultad;
    }

    public void setIdFacultad(UUID idFacultad) {
        this.idFacultad = UtilId.valorDefecto(idFacultad);
    }

    public UUID getIdPrograma() {
        return idPrograma;
    }

    public void setIdPrograma(UUID idPrograma) {
        this.idPrograma = UtilId.valorDefecto(idPrograma);
    }
}
