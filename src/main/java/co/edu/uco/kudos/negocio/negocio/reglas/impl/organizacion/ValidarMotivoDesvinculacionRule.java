package co.edu.uco.kudos.negocio.negocio.reglas.impl.organizacion;

import co.edu.uco.kudos.negocio.negocio.reglas.Rule;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosNegocioExcepcion;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

public final class ValidarMotivoDesvinculacionRule implements Rule<String> {

    private static final ValidarMotivoDesvinculacionRule INSTANCIA = new ValidarMotivoDesvinculacionRule();

    private ValidarMotivoDesvinculacionRule() {
    }

    public static ValidarMotivoDesvinculacionRule obtenerInstancia() {
        return INSTANCIA;
    }

    @Override
    public void ejecutar(String... datos) {
        var motivo = datos[0];

        if (UtilTexto.getUtilTexto().esVacia(motivo)) {
            throw KudosNegocioExcepcion.crear(CatalogoMensajes.Organizacion.MOTIVO_DESVINCULACION_OBLIGATORIO);
        }
        if (!UtilTexto.getUtilTexto().longitudCadenaEsValida(motivo, 10, 250, true)) {
            throw KudosNegocioExcepcion.crear(CatalogoMensajes.Organizacion.MOTIVO_DESVINCULACION_LONGITUD_INVALIDA);
        }
    }
}
