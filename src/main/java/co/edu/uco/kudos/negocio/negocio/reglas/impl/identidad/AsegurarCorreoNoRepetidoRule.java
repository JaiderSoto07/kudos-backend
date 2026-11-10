package co.edu.uco.kudos.negocio.negocio.reglas.impl.identidad;

import co.edu.uco.kudos.dao.factoria.DAOFactory;
import co.edu.uco.kudos.entidad.UsuarioEntidad;
import co.edu.uco.kudos.negocio.negocio.reglas.Rule;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosNegocioExcepcion;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

import java.util.UUID;

public final class AsegurarCorreoNoRepetidoRule implements Rule<Object> {

    private static final AsegurarCorreoNoRepetidoRule INSTANCIA = new AsegurarCorreoNoRepetidoRule();

    private AsegurarCorreoNoRepetidoRule() {
    }

    public static AsegurarCorreoNoRepetidoRule obtenerInstancia() {
        return INSTANCIA;
    }

    @Override
    public void ejecutar(Object... datos) {
        var correo = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco((String) datos[0]).toLowerCase();
        var idUsuarioPropio = (UUID) datos[1];
        var daoFactory = (DAOFactory) datos[2];

        var filtro = new UsuarioEntidad.Builder().correoInstitucional(correo).build();
        var usuariosConElMismoCorreo = daoFactory.obtenerUsuarioDAO().consultarPorFiltro(filtro);

        for (var usuario : usuariosConElMismoCorreo) {
            if (!usuario.getId().equals(idUsuarioPropio)) {
                throw KudosNegocioExcepcion.crear(CatalogoMensajes.Identidad.CORREO_YA_REGISTRADO);
            }
        }
    }
}
