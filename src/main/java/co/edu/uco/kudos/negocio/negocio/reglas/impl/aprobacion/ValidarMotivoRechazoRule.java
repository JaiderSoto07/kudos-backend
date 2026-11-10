package co.edu.uco.kudos.negocio.negocio.reglas.impl.aprobacion;

import co.edu.uco.kudos.negocio.negocio.reglas.Rule;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosNegocioExcepcion;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

public final class ValidarMotivoRechazoRule implements Rule<String> {

    private static final int LONGITUD_MINIMA = 10;
    private static final int LONGITUD_MAXIMA = 250;

    private static final ValidarMotivoRechazoRule INSTANCIA = new ValidarMotivoRechazoRule();

    private ValidarMotivoRechazoRule() {
    }

    public static ValidarMotivoRechazoRule obtenerInstancia() {
        return INSTANCIA;
    }

    @Override
    public void ejecutar(String... datos) {
        var motivo = datos[0];

        if (UtilTexto.getUtilTexto().esVacia(motivo)) {
            throw KudosNegocioExcepcion.crear(CatalogoMensajes.Aprobacion.MOTIVO_RECHAZO_OBLIGATORIO);
        }
        if (!UtilTexto.getUtilTexto().longitudCadenaEsValida(motivo, LONGITUD_MINIMA, LONGITUD_MAXIMA, true)) {
            throw KudosNegocioExcepcion.crear(CatalogoMensajes.Aprobacion.MOTIVO_RECHAZO_LONGITUD_INVALIDA);
        }
    }
}
