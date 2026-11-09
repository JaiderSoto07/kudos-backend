package co.edu.uco.kudos.dominio;

public enum EstadoReporte {

    PENDIENTE, EN_REVISION, ACEPTADO, RECHAZADO;

    public boolean estaAbierto() {
        return this == PENDIENTE || this == EN_REVISION;
    }
}
