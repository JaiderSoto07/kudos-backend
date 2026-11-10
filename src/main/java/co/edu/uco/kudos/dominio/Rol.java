package co.edu.uco.kudos.dominio;

public enum Rol {

    ADMINISTRADOR,
    RECTOR,
    COORDINADOR_ACADEMICO,
    COORDINADOR_FACULTAD,
    COORDINADOR_PROGRAMA,
    DOCENTE,
    ESTUDIANTE;

    public Rol rolAprobador() {
        return switch (this) {
            case ADMINISTRADOR, RECTOR -> ADMINISTRADOR;
            case COORDINADOR_ACADEMICO -> RECTOR;
            case COORDINADOR_FACULTAD -> COORDINADOR_ACADEMICO;
            case COORDINADOR_PROGRAMA -> COORDINADOR_FACULTAD;
            case DOCENTE, ESTUDIANTE -> COORDINADOR_PROGRAMA;
        };
    }

    public boolean puedeModerar() {
        return this == RECTOR || this == COORDINADOR_ACADEMICO;
    }

    public boolean esAutorregistrable() {
        return this != ADMINISTRADOR && this != RECTOR;
    }
}
