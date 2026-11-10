package co.edu.uco.kudos.dto;

import co.edu.uco.kudos.transversal.utilitario.UtilId;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

import java.util.UUID;

public class ProgramaAcademicoDto {

    private UUID id;
    private String nombre;
    private UUID idFacultad;
    private String nombreFacultad;
    private boolean activo;
    private String nombreCoordinador;

    public ProgramaAcademicoDto() {
        setId(UtilId.VALOR_DEFECTO);
        setNombre(UtilTexto.VACIO);
        setIdFacultad(UtilId.VALOR_DEFECTO);
        setNombreFacultad(UtilTexto.VACIO);
        setActivo(true);
        setNombreCoordinador(UtilTexto.VACIO);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = UtilId.valorDefecto(id);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(nombre);
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

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public String getNombreCoordinador() {
        return nombreCoordinador;
    }

    public void setNombreCoordinador(String nombreCoordinador) {
        this.nombreCoordinador = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(nombreCoordinador);
    }
}
