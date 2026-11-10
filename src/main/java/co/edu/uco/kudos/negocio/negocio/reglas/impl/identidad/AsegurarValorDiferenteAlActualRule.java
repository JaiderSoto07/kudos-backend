package co.edu.uco.kudos.negocio.negocio.reglas.impl.identidad;

import co.edu.uco.kudos.negocio.negocio.reglas.Rule;
import co.edu.uco.kudos.transversal.excepciones.KudosNegocioExcepcion;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

public final class AsegurarValorDiferenteAlActualRule implements Rule<String> {

    private static final AsegurarValorDiferenteAlActualRule INSTANCIA = new AsegurarValorDiferenteAlActualRule();

    private AsegurarValorDiferenteAlActualRule() {
    }

    public static AsegurarValorDiferenteAlActualRule obtenerInstancia() {
        return INSTANCIA;
    }

    @Override
    public void ejecutar(String... datos) {
        var valorNuevo = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(datos[0]);
        var valorActual = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(datos[1]);
        var mensajeRechazo = datos[2];

        if (valorNuevo.equalsIgnoreCase(valorActual)) {
            throw KudosNegocioExcepcion.crear(mensajeRechazo);
        }
    }
}
