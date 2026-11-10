package co.edu.uco.kudos.negocio.negocio.reglas.impl.conversacion;

import co.edu.uco.kudos.dominio.KudoDominio;
import co.edu.uco.kudos.negocio.negocio.reglas.Rule;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosNegocioExcepcion;

public final class AsegurarKudoAdmiteInteraccionRule implements Rule<KudoDominio> {

    private static final AsegurarKudoAdmiteInteraccionRule INSTANCIA = new AsegurarKudoAdmiteInteraccionRule();

    private AsegurarKudoAdmiteInteraccionRule() {
    }

    public static AsegurarKudoAdmiteInteraccionRule obtenerInstancia() {
        return INSTANCIA;
    }

    @Override
    public void ejecutar(KudoDominio... datos) {
        if (!datos[0].admiteInteraccion()) {
            throw KudosNegocioExcepcion.crear(CatalogoMensajes.Replica.KUDO_VETADO);
        }
    }
}
