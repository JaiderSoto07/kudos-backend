package co.edu.uco.kudos.negocio.negocio.reglas.impl.identidad;

import co.edu.uco.kudos.dominio.UsuarioDominio;
import co.edu.uco.kudos.negocio.negocio.reglas.Rule;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosNegocioExcepcion;

public final class AsegurarCelularPendienteDeVerificarRule implements Rule<UsuarioDominio> {

    private static final AsegurarCelularPendienteDeVerificarRule INSTANCIA = new AsegurarCelularPendienteDeVerificarRule();

    private AsegurarCelularPendienteDeVerificarRule() {
    }

    public static AsegurarCelularPendienteDeVerificarRule obtenerInstancia() {
        return INSTANCIA;
    }

    @Override
    public void ejecutar(UsuarioDominio... datos) {
        var usuario = datos[0];

        if (usuario.isCelularVerificado()) {
            throw KudosNegocioExcepcion.crear(CatalogoMensajes.Identidad.CELULAR_YA_VERIFICADO);
        }
    }
}
