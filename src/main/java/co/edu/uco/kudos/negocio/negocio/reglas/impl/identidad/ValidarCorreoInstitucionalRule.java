package co.edu.uco.kudos.negocio.negocio.reglas.impl.identidad;

import co.edu.uco.kudos.dao.factoria.DAOFactory;
import co.edu.uco.kudos.dominio.ParametroGlobalDominio;
import co.edu.uco.kudos.entidad.ParametroGlobalEntidad;
import co.edu.uco.kudos.negocio.negocio.reglas.Rule;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosNegocioExcepcion;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

import java.util.ArrayList;
import java.util.List;

public final class ValidarCorreoInstitucionalRule implements Rule<Object> {

    private static final String PATRON_CORREO = "^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}$";

    private static final ValidarCorreoInstitucionalRule INSTANCIA = new ValidarCorreoInstitucionalRule();

    private ValidarCorreoInstitucionalRule() {
    }

    public static ValidarCorreoInstitucionalRule obtenerInstancia() {
        return INSTANCIA;
    }

    @Override
    public void ejecutar(Object... datos) {
        var correo = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco((String) datos[0]).toLowerCase();
        var daoFactory = (DAOFactory) datos[1];

        if (UtilTexto.getUtilTexto().esVacia(correo)) {
            throw KudosNegocioExcepcion.crear(CatalogoMensajes.Identidad.CORREO_OBLIGATORIO);
        }
        if (!UtilTexto.getUtilTexto().formatoEsValido(correo, PATRON_CORREO)) {
            throw KudosNegocioExcepcion.crear(CatalogoMensajes.Identidad.CORREO_FORMATO_INVALIDO);
        }

        var dominiosPermitidos = consultarDominiosPermitidos(daoFactory);
        var dominioDelCorreo = correo.substring(correo.indexOf('@') + 1);

        if (!dominiosPermitidos.contains(dominioDelCorreo)) {
            throw KudosNegocioExcepcion.crear(String.format(CatalogoMensajes.Identidad.CORREO_DOMINIO_NO_PERMITIDO, String.join(", ", dominiosPermitidos)));
        }
    }

    private List<String> consultarDominiosPermitidos(DAOFactory daoFactory) {
        var filtro = new ParametroGlobalEntidad.Builder().clave(ParametroGlobalDominio.DOMINIO_CORREO).build();
        var parametros = daoFactory.obtenerParametroGlobalDAO().consultarPorFiltro(filtro);

        var dominios = new ArrayList<String>();
        if (parametros.isEmpty()) {
            return dominios;
        }
        for (var dominio : parametros.getFirst().getValor().split(",")) {
            var dominioLimpio = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(dominio).toLowerCase();
            if (!UtilTexto.getUtilTexto().esVacia(dominioLimpio)) {
                dominios.add(dominioLimpio);
            }
        }
        return dominios;
    }
}
