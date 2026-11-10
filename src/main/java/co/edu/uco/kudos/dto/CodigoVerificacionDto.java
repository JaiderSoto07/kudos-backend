package co.edu.uco.kudos.dto;

import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

public class CodigoVerificacionDto {

    private String codigo;

    public CodigoVerificacionDto() {
        setCodigo(UtilTexto.VACIO);
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(codigo);
    }
}
