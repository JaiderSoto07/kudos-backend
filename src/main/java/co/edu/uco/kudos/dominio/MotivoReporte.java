package co.edu.uco.kudos.dominio;

public enum MotivoReporte {

    LENGUAJE_OFENSIVO, ACOSO, DISCRIMINACION, SPAM, INFORMACION_FALSA, OTRO;

    public boolean exigeDescripcion() {
        return this == OTRO;
    }
}
