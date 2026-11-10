package co.edu.uco.kudos.negocio.negocio.reglas.impl.conversacion;

import co.edu.uco.kudos.dominio.ReplicaDominio;
import co.edu.uco.kudos.negocio.negocio.reglas.Rule;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosNegocioExcepcion;
import co.edu.uco.kudos.transversal.utilitario.UtilId;

public final class AsegurarReplicaExisteRule implements Rule<ReplicaDominio> {

    private static final AsegurarReplicaExisteRule INSTANCIA = new AsegurarReplicaExisteRule();

    private AsegurarReplicaExisteRule() {
    }

    public static AsegurarReplicaExisteRule obtenerInstancia() {
        return INSTANCIA;
    }

    @Override
    public void ejecutar(ReplicaDominio... datos) {
        if (UtilId.esValorDefecto(datos[0].getId())) {
            throw KudosNegocioExcepcion.crear(CatalogoMensajes.Replica.REPLICA_NO_EXISTE);
        }
    }
}
