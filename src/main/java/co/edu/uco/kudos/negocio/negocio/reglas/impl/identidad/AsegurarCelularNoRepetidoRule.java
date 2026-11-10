package co.edu.uco.kudos.negocio.negocio.reglas.impl.identidad;

import co.edu.uco.kudos.dao.factoria.DAOFactory;
import co.edu.uco.kudos.entidad.UsuarioEntidad;
import co.edu.uco.kudos.negocio.negocio.reglas.Rule;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosNegocioExcepcion;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

import java.util.UUID;

public final class AsegurarCelularNoRepetidoRule implements Rule<Object> {

    private static final AsegurarCelularNoRepetidoRule INSTANCIA = new AsegurarCelularNoRepetidoRule();

    private AsegurarCelularNoRepetidoRule() {
    }

    public static AsegurarCelularNoRepetidoRule obtenerInstancia() {
        return INSTANCIA;
    }

    @Override
    public void ejecutar(Object... datos) {
        var celular = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco((String) datos[0]);
        var idUsuarioPropio = (UUID) datos[1];
        var daoFactory = (DAOFactory) datos[2];

        var filtro = new UsuarioEntidad.Builder().numeroCelular(celular).build();
        var usuariosConElMismoCelular = daoFactory.obtenerUsuarioDAO().consultarPorFiltro(filtro);

        for (var usuario : usuariosConElMismoCelular) {
            if (!usuario.getId().equals(idUsuarioPropio)) {
                throw KudosNegocioExcepcion.crear(CatalogoMensajes.Identidad.CELULAR_YA_REGISTRADO);
            }
        }
    }
}
