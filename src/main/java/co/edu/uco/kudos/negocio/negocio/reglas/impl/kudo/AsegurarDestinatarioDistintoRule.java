package co.edu.uco.kudos.negocio.negocio.reglas.impl.kudo;

import co.edu.uco.kudos.negocio.negocio.reglas.Rule;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosNegocioExcepcion;
import co.edu.uco.kudos.transversal.utilitario.UtilId;

import java.util.UUID;

public final class AsegurarDestinatarioDistintoRule implements Rule<Object> {

    private static final AsegurarDestinatarioDistintoRule INSTANCIA = new AsegurarDestinatarioDistintoRule();

    private AsegurarDestinatarioDistintoRule() {
    }

    public static AsegurarDestinatarioDistintoRule obtenerInstancia() {
        return INSTANCIA;
    }

    @Override
    public void ejecutar(Object... datos) {
        var idAutor = (UUID) datos[0];
        var idDestinatario = (UUID) datos[1];

        if (UtilId.esValorDefecto(idDestinatario)) {
            throw KudosNegocioExcepcion.crear(CatalogoMensajes.Kudo.DESTINATARIO_OBLIGATORIO);
        }
        if (idDestinatario.equals(idAutor)) {
            throw KudosNegocioExcepcion.crear(CatalogoMensajes.Kudo.DESTINATARIO_ES_EL_AUTOR);
        }
    }
}
