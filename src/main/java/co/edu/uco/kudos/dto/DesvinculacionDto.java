package co.edu.uco.kudos.dto;

import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

public class DesvinculacionDto {

    private String motivo;

    public DesvinculacionDto() {
        setMotivo(UtilTexto.VACIO);
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(motivo);
    }
}
