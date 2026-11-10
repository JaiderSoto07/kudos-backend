package co.edu.uco.kudos.negocio.negocio.reglas.impl.kudo;

import co.edu.uco.kudos.negocio.negocio.reglas.Rule;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosNegocioExcepcion;
import co.edu.uco.kudos.transversal.utilitario.UtilId;

import java.util.Objects;
import java.util.UUID;

public final class AsegurarDestinatarioDistintoRule implements Rule<UUID> {

    private static final AsegurarDestinatarioDistintoRule INSTANCIA = new AsegurarDestinatarioDistintoRule();

    private AsegurarDestinatarioDistintoRule() {
    }

    public static AsegurarDestinatarioDistintoRule obtenerInstancia() {
        return INSTANCIA;
    }

    @Override
    public void ejecutar(UUID... datos) {
        var idAutor = datos[0];
        var idDestinatario = datos[1];

        if (UtilId.esValorDefecto(idDestinatario)) {
            throw KudosNegocioExcepcion.crear(CatalogoMensajes.Kudo.DESTINATARIO_OBLIGATORIO);
        }
        if (Objects.equals(idDestinatario, idAutor)) {
            throw KudosNegocioExcepcion.crear(CatalogoMensajes.Kudo.DESTINATARIO_ES_EL_AUTOR);
        }
    }
}
