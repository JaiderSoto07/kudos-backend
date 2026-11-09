package co.edu.uco.kudos.negocio.negocio.reglas;

public interface Rule<O> {

    void ejecutar(O... datos);
}
