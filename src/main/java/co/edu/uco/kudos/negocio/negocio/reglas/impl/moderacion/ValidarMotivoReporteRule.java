package co.edu.uco.kudos.negocio.negocio.reglas.impl.moderacion;

import co.edu.uco.kudos.dominio.MotivoReporte;
import co.edu.uco.kudos.negocio.negocio.reglas.Rule;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosNegocioExcepcion;
import co.edu.uco.kudos.transversal.utilitario.UtilEnum;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

public final class ValidarMotivoReporteRule implements Rule<String> {

    private static final int LONGITUD_MAXIMA_DESCRIPCION = 500;

    private static final ValidarMotivoReporteRule INSTANCIA = new ValidarMotivoReporteRule();

    private ValidarMotivoReporteRule() {
    }

    public static ValidarMotivoReporteRule obtenerInstancia() {
        return INSTANCIA;
    }

    @Override
    public void ejecutar(String... datos) {
        var motivo = UtilEnum.obtenerValor(MotivoReporte.class, datos[0], null);
        var descripcion = datos[1];

        if (motivo == null) {
            throw KudosNegocioExcepcion.crear(CatalogoMensajes.Moderacion.MOTIVO_OBLIGATORIO);
        }
        if (motivo.exigeDescripcion() && UtilTexto.getUtilTexto().esVacia(descripcion)) {
            throw KudosNegocioExcepcion.crear(CatalogoMensajes.Moderacion.DESCRIPCION_OBLIGATORIA);
        }
        if (UtilTexto.getUtilTexto().obtenerLongitudCadena(descripcion, true) > LONGITUD_MAXIMA_DESCRIPCION) {
            throw KudosNegocioExcepcion.crear(CatalogoMensajes.Moderacion.DESCRIPCION_MUY_LARGA);
        }
    }
}
