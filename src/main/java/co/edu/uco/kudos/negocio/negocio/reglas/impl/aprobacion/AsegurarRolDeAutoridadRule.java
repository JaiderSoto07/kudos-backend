package co.edu.uco.kudos.negocio.negocio.reglas.impl.aprobacion;

import co.edu.uco.kudos.dominio.Rol;
import co.edu.uco.kudos.negocio.negocio.reglas.Rule;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosNegocioExcepcion;
import co.edu.uco.kudos.transversal.utilitario.UtilEnum;

public final class AsegurarRolDeAutoridadRule implements Rule<String> {

    private static final AsegurarRolDeAutoridadRule INSTANCIA = new AsegurarRolDeAutoridadRule();

    private AsegurarRolDeAutoridadRule() {
    }

    public static AsegurarRolDeAutoridadRule obtenerInstancia() {
        return INSTANCIA;
    }

    @Override
    public void ejecutar(String... datos) {
        var rol = UtilEnum.obtenerValor(Rol.class, datos[0], null);

        if (rol != Rol.RECTOR && rol != Rol.ADMINISTRADOR) {
            throw KudosNegocioExcepcion.crear(CatalogoMensajes.Aprobacion.ROL_NO_ES_AUTORIDAD);
        }
    }
}
