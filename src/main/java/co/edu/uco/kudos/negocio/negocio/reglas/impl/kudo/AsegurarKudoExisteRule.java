package co.edu.uco.kudos.negocio.negocio.reglas.impl.kudo;

import co.edu.uco.kudos.dominio.KudoDominio;
import co.edu.uco.kudos.negocio.negocio.reglas.Rule;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosNegocioExcepcion;
import co.edu.uco.kudos.transversal.utilitario.UtilId;

public final class AsegurarKudoExisteRule implements Rule<KudoDominio> {

    private static final AsegurarKudoExisteRule INSTANCIA = new AsegurarKudoExisteRule();

    private AsegurarKudoExisteRule() {
    }

    public static AsegurarKudoExisteRule obtenerInstancia() {
        return INSTANCIA;
    }

    @Override
    public void ejecutar(KudoDominio... datos) {
        if (UtilId.esValorDefecto(datos[0].getId())) {
            throw KudosNegocioExcepcion.crear(CatalogoMensajes.Kudo.KUDO_NO_EXISTE);
        }
    }
}
