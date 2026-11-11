package co.edu.uco.kudos.negocio.negocio.reglas.impl.notificacion;

import co.edu.uco.kudos.dominio.NotificacionDominio;
import co.edu.uco.kudos.negocio.negocio.reglas.Rule;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosNegocioExcepcion;
import co.edu.uco.kudos.transversal.utilitario.UtilId;

public final class AsegurarNotificacionExisteRule implements Rule<NotificacionDominio> {

    private static final AsegurarNotificacionExisteRule INSTANCIA = new AsegurarNotificacionExisteRule();

    private AsegurarNotificacionExisteRule() {
    }

    public static AsegurarNotificacionExisteRule obtenerInstancia() {
        return INSTANCIA;
    }

    @Override
    public void ejecutar(NotificacionDominio... datos) {
        if (UtilId.esValorDefecto(datos[0].getId())) {
            throw KudosNegocioExcepcion.crear(CatalogoMensajes.Notificacion.NOTIFICACION_NO_EXISTE);
        }
    }
}
