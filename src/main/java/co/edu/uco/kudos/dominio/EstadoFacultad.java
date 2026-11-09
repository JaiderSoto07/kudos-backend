package co.edu.uco.kudos.dominio;

public enum EstadoFacultad {

    EN_CONFORMACION, ACTIVA, INACTIVA;

    public boolean admiteProgramas() {
        return this != INACTIVA;
    }

    public boolean admiteCoordinador() {
        return this == ACTIVA;
    }
}
