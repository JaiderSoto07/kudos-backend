package co.edu.uco.kudos.negocio.negocio.reglas.impl.autenticacion;

import co.edu.uco.kudos.dominio.UsuarioDominio;
import co.edu.uco.kudos.negocio.negocio.reglas.Rule;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosAutorizacionExcepcion;
import co.edu.uco.kudos.transversal.utilitario.UtilId;

public final class AsegurarUsuarioRegistradoRule implements Rule<Object> {

    private static final AsegurarUsuarioRegistradoRule INSTANCIA = new AsegurarUsuarioRegistradoRule();

    private AsegurarUsuarioRegistradoRule() {
    }

    public static AsegurarUsuarioRegistradoRule obtenerInstancia() {
        return INSTANCIA;
    }

    @Override
    public void ejecutar(Object... datos) {
        var usuario = (UsuarioDominio) datos[0];

        if (UtilId.esValorDefecto(usuario.getId())) {
            throw KudosAutorizacionExcepcion.crear(CatalogoMensajes.AutenticacionNegocioImpl.USUARIO_NO_REGISTRADO);
        }
    }
}
