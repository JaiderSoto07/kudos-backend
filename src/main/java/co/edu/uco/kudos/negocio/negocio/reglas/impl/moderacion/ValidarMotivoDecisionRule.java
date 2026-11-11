package co.edu.uco.kudos.negocio.negocio.reglas.impl.moderacion;

import co.edu.uco.kudos.negocio.negocio.reglas.Rule;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosNegocioExcepcion;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

public final class ValidarMotivoDecisionRule implements Rule<String> {

    private static final int LONGITUD_MINIMA = 10;
    private static final int LONGITUD_MAXIMA = 500;

    private static final ValidarMotivoDecisionRule INSTANCIA = new ValidarMotivoDecisionRule();

    private ValidarMotivoDecisionRule() {
    }

    public static ValidarMotivoDecisionRule obtenerInstancia() {
        return INSTANCIA;
    }

    @Override
    public void ejecutar(String... datos) {
        var motivo = datos[0];

        if (UtilTexto.getUtilTexto().esVacia(motivo)) {
            throw KudosNegocioExcepcion.crear(CatalogoMensajes.Moderacion.MOTIVO_DECISION_OBLIGATORIO);
        }
        if (!UtilTexto.getUtilTexto().longitudCadenaEsValida(motivo, LONGITUD_MINIMA, LONGITUD_MAXIMA, true)) {
            throw KudosNegocioExcepcion.crear(CatalogoMensajes.Moderacion.MOTIVO_DECISION_LONGITUD_INVALIDA);
        }
    }
}
