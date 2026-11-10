package co.edu.uco.kudos.dto;

public class CalificacionDto {

    private int estrellas;

    public CalificacionDto() {
        setEstrellas(0);
    }

    public int getEstrellas() {
        return estrellas;
    }

    public void setEstrellas(int estrellas) {
        this.estrellas = estrellas;
    }
}
