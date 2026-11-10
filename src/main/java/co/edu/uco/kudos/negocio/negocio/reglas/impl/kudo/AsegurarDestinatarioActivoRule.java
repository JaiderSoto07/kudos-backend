package co.edu.uco.kudos.negocio.negocio.reglas.impl.kudo;

import co.edu.uco.kudos.dominio.UsuarioDominio;
import co.edu.uco.kudos.negocio.negocio.reglas.Rule;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosNegocioExcepcion;
import co.edu.uco.kudos.transversal.utilitario.UtilId;

public final class AsegurarDestinatarioActivoRule implements Rule<UsuarioDominio> {

    private static final AsegurarDestinatarioActivoRule INSTANCIA = new AsegurarDestinatarioActivoRule();

    private AsegurarDestinatarioActivoRule() {
    }

    public static AsegurarDestinatarioActivoRule obtenerInstancia() {
        return INSTANCIA;
    }

    @Override
    public void ejecutar(UsuarioDominio... datos) {
        var destinatario = datos[0];

        if (UtilId.esValorDefecto(destinatario.getId()) || !destinatario.estaActiva()) {
            throw KudosNegocioExcepcion.crear(CatalogoMensajes.Kudo.DESTINATARIO_NO_ACTIVO);
        }
    }
}
