package co.edu.uco.kudos.negocio.negocio.reglas.impl.organizacion;

import co.edu.uco.kudos.dao.factoria.DAOFactory;
import co.edu.uco.kudos.entidad.ProgramaAcademicoEntidad;
import co.edu.uco.kudos.entidad.UsuarioEntidad;
import co.edu.uco.kudos.entidad.VinculacionProgramaEntidad;
import co.edu.uco.kudos.negocio.negocio.reglas.Rule;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosNegocioExcepcion;
import co.edu.uco.kudos.transversal.utilitario.UtilFecha;

import java.util.UUID;

public final class AsegurarVinculoNoRepetidoRule implements Rule<Object> {

    private static final AsegurarVinculoNoRepetidoRule INSTANCIA = new AsegurarVinculoNoRepetidoRule();

    private AsegurarVinculoNoRepetidoRule() {
    }

    public static AsegurarVinculoNoRepetidoRule obtenerInstancia() {
        return INSTANCIA;
    }

    @Override
    public void ejecutar(Object... datos) {
        var idUsuario = (UUID) datos[0];
        var idPrograma = (UUID) datos[1];
        var daoFactory = (DAOFactory) datos[2];

        var filtro = new VinculacionProgramaEntidad.Builder()
                .usuario(new UsuarioEntidad.Builder().id(idUsuario).build())
                .programa(new ProgramaAcademicoEntidad.Builder().id(idPrograma).build())
                .build();
        for (var vinculo : daoFactory.obtenerVinculacionProgramaDAO().consultarPorFiltro(filtro)) {
            if (UtilFecha.esValorDefecto(vinculo.getFechaFin()) && vinculo.getPrograma().getId().equals(idPrograma)
                    && vinculo.getUsuario().getId().equals(idUsuario)) {
                throw KudosNegocioExcepcion.crear(CatalogoMensajes.Organizacion.VINCULO_YA_EXISTE);
            }
        }
    }
}
