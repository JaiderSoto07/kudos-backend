package co.edu.uco.kudos.negocio.negocio.reglas.impl.conversacion;

import co.edu.uco.kudos.dominio.ReplicaDominio;
import co.edu.uco.kudos.negocio.negocio.reglas.Rule;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosNegocioExcepcion;

public final class AsegurarReplicaAdmiteInteraccionRule implements Rule<ReplicaDominio> {

    private static final AsegurarReplicaAdmiteInteraccionRule INSTANCIA = new AsegurarReplicaAdmiteInteraccionRule();

    private AsegurarReplicaAdmiteInteraccionRule() {
    }

    public static AsegurarReplicaAdmiteInteraccionRule obtenerInstancia() {
        return INSTANCIA;
    }

    @Override
    public void ejecutar(ReplicaDominio... datos) {
        if (!datos[0].admiteInteraccion()) {
            throw KudosNegocioExcepcion.crear(CatalogoMensajes.Replica.REPLICA_VETADA);
        }
    }
}
