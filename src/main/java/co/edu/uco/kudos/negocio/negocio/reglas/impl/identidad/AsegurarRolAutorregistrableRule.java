package co.edu.uco.kudos.negocio.negocio.reglas.impl.identidad;

import co.edu.uco.kudos.dominio.Rol;
import co.edu.uco.kudos.negocio.negocio.reglas.Rule;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosNegocioExcepcion;
import co.edu.uco.kudos.transversal.utilitario.UtilEnum;
import co.edu.uco.kudos.transversal.utilitario.UtilObjeto;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

public final class AsegurarRolAutorregistrableRule implements Rule<String> {

    private static final AsegurarRolAutorregistrableRule INSTANCIA = new AsegurarRolAutorregistrableRule();

    private AsegurarRolAutorregistrableRule() {
    }

    public static AsegurarRolAutorregistrableRule obtenerInstancia() {
        return INSTANCIA;
    }

    @Override
    public void ejecutar(String... datos) {
        var rolSolicitado = datos[0];

        if (UtilTexto.getUtilTexto().esVacia(rolSolicitado)) {
            throw KudosNegocioExcepcion.crear(CatalogoMensajes.Identidad.ROL_OBLIGATORIO);
        }
        var rol = UtilEnum.obtenerValor(Rol.class, rolSolicitado, null);
        if (UtilObjeto.esNulo(rol)) {
            throw KudosNegocioExcepcion.crear(CatalogoMensajes.Identidad.ROL_NO_VALIDO);
        }
        if (!rol.esAutorregistrable()) {
            throw KudosNegocioExcepcion.crear(CatalogoMensajes.Identidad.ROL_NO_AUTORREGISTRABLE);
        }
    }
}
