package co.edu.uco.kudos.negocio.negocio.reglas.impl.autenticacion;

import co.edu.uco.kudos.dominio.UsuarioDominio;
import co.edu.uco.kudos.negocio.negocio.reglas.Rule;
import co.edu.uco.kudos.transversal.excepciones.KudosAutorizacionExcepcion;

public final class AsegurarCuentaActivaRule implements Rule<Object> {

    private static final AsegurarCuentaActivaRule INSTANCIA = new AsegurarCuentaActivaRule();

    private AsegurarCuentaActivaRule() {
    }

    public static AsegurarCuentaActivaRule obtenerInstancia() {
        return INSTANCIA;
    }

    @Override
    public void ejecutar(Object... datos) {
        var usuario = (UsuarioDominio) datos[0];

        if (!usuario.estaActiva()) {
            throw KudosAutorizacionExcepcion.crear(usuario.motivoParaNoIngresar());
        }
    }
}
