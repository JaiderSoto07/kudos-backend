package co.edu.uco.kudos.negocio.negocio.reglas.impl.organizacion;

import co.edu.uco.kudos.dominio.Rol;
import co.edu.uco.kudos.dominio.TipoVinculo;
import co.edu.uco.kudos.negocio.negocio.reglas.Rule;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosNegocioExcepcion;
import co.edu.uco.kudos.transversal.utilitario.UtilEnum;
import co.edu.uco.kudos.transversal.utilitario.UtilObjeto;

public final class AsegurarRolCoincideConVinculoRule implements Rule<Object> {

    private static final AsegurarRolCoincideConVinculoRule INSTANCIA = new AsegurarRolCoincideConVinculoRule();

    private AsegurarRolCoincideConVinculoRule() {
    }

    public static AsegurarRolCoincideConVinculoRule obtenerInstancia() {
        return INSTANCIA;
    }

    @Override
    public void ejecutar(Object... datos) {
        var rolVigente = (Rol) datos[0];
        var tipoVinculo = UtilEnum.obtenerValor(TipoVinculo.class, (String) datos[1], null);

        if (UtilObjeto.esNulo(tipoVinculo)) {
            throw KudosNegocioExcepcion.crear(CatalogoMensajes.Organizacion.TIPO_VINCULO_INVALIDO);
        }
        if (UtilObjeto.esNulo(rolVigente) || !rolVigente.name().equals(tipoVinculo.name())) {
            throw KudosNegocioExcepcion.crear(CatalogoMensajes.Organizacion.ROL_NO_COINCIDE_CON_VINCULO);
        }
    }
}
