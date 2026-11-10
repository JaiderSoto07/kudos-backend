package co.edu.uco.kudos.negocio.negocio.reglas.impl.organizacion;

import co.edu.uco.kudos.dominio.UsuarioDominio;
import co.edu.uco.kudos.negocio.negocio.reglas.Rule;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosNegocioExcepcion;
import co.edu.uco.kudos.transversal.utilitario.UtilId;

public final class AsegurarPersonaRegistradaRule implements Rule<UsuarioDominio> {

    private static final AsegurarPersonaRegistradaRule INSTANCIA = new AsegurarPersonaRegistradaRule();

    private AsegurarPersonaRegistradaRule() {
    }

    public static AsegurarPersonaRegistradaRule obtenerInstancia() {
        return INSTANCIA;
    }

    @Override
    public void ejecutar(UsuarioDominio... datos) {
        if (UtilId.esValorDefecto(datos[0].getId())) {
            throw KudosNegocioExcepcion.crear(CatalogoMensajes.Organizacion.PERSONA_NO_REGISTRADA);
        }
    }
}
