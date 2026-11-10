package co.edu.uco.kudos.dto;

import co.edu.uco.kudos.transversal.utilitario.UtilId;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

import java.util.UUID;

public class FacultadDto {

    private UUID id;
    private String nombre;
    private String estado;

    public FacultadDto() {
        setId(UtilId.VALOR_DEFECTO);
        setNombre(UtilTexto.VACIO);
        setEstado(UtilTexto.VACIO);
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(estado);
    }
}
