package co.edu.uco.kudos.negocio.negocio.reglas.impl.organizacion;

import co.edu.uco.kudos.dao.factoria.DAOFactory;
import co.edu.uco.kudos.entidad.ProgramaAcademicoEntidad;
import co.edu.uco.kudos.negocio.negocio.reglas.Rule;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosNegocioExcepcion;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

import java.util.UUID;

public final class AsegurarNombreProgramaNoRepetidoRule implements Rule<Object> {

    private static final AsegurarNombreProgramaNoRepetidoRule INSTANCIA = new AsegurarNombreProgramaNoRepetidoRule();

    private AsegurarNombreProgramaNoRepetidoRule() {
    }

    public static AsegurarNombreProgramaNoRepetidoRule obtenerInstancia() {
        return INSTANCIA;
    }

    @Override
    public void ejecutar(Object... datos) {
        var nombre = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco((String) datos[0]);
        var idPropio = (UUID) datos[1];
        var daoFactory = (DAOFactory) datos[2];

        var filtro = new ProgramaAcademicoEntidad.Builder().nombre(nombre).build();
        for (var encontrado : daoFactory.obtenerProgramaAcademicoDAO().consultarPorFiltro(filtro)) {
            if (encontrado.getNombre().equalsIgnoreCase(nombre) && !encontrado.getId().equals(idPropio)) {
                throw KudosNegocioExcepcion.crear(CatalogoMensajes.Organizacion.NOMBRE_PROGRAMA_YA_EXISTE);
            }
        }
    }
}
