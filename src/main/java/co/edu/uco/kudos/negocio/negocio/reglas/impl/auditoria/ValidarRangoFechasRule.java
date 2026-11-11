package co.edu.uco.kudos.negocio.negocio.reglas.impl.auditoria;

import co.edu.uco.kudos.negocio.negocio.reglas.Rule;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosNegocioExcepcion;

import java.time.LocalDate;

public final class ValidarRangoFechasRule implements Rule<LocalDate> {

    private static final ValidarRangoFechasRule INSTANCIA = new ValidarRangoFechasRule();

    private ValidarRangoFechasRule() {
    }

    public static ValidarRangoFechasRule obtenerInstancia() {
        return INSTANCIA;
    }

    @Override
    public void ejecutar(LocalDate... datos) {
        var desde = datos[0];
        var hasta = datos[1];

        if (desde != null && hasta != null && desde.isAfter(hasta)) {
            throw KudosNegocioExcepcion.crear(CatalogoMensajes.Auditoria.RANGO_FECHAS_INVALIDO);
        }
    }
}
