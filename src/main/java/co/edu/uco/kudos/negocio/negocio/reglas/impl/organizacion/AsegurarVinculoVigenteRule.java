package co.edu.uco.kudos.negocio.negocio.reglas.impl.organizacion;

import co.edu.uco.kudos.dominio.VinculacionProgramaDominio;
import co.edu.uco.kudos.negocio.negocio.reglas.Rule;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosNegocioExcepcion;
import co.edu.uco.kudos.transversal.utilitario.UtilId;

public final class AsegurarVinculoVigenteRule implements Rule<VinculacionProgramaDominio> {

    private static final AsegurarVinculoVigenteRule INSTANCIA = new AsegurarVinculoVigenteRule();

    private AsegurarVinculoVigenteRule() {
    }

    public static AsegurarVinculoVigenteRule obtenerInstancia() {
        return INSTANCIA;
    }

    @Override
    public void ejecutar(VinculacionProgramaDominio... datos) {
        var vinculo = datos[0];

        if (UtilId.esValorDefecto(vinculo.getId())) {
            throw KudosNegocioExcepcion.crear(CatalogoMensajes.Organizacion.VINCULO_NO_EXISTE);
        }
        if (!vinculo.estaVigente()) {
            throw KudosNegocioExcepcion.crear(CatalogoMensajes.Organizacion.VINCULO_YA_CERRADO);
        }
    }
}
