package co.edu.uco.kudos.negocio.negocio.reglas.impl.identidad;

import co.edu.uco.kudos.dominio.CodigoVerificacionDominio;
import co.edu.uco.kudos.negocio.negocio.reglas.Rule;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosNegocioExcepcion;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import co.edu.uco.kudos.transversal.utilitario.UtilObjeto;

import java.time.LocalDateTime;

public final class AsegurarCodigoVigenteRule implements Rule<Object> {

    private static final AsegurarCodigoVigenteRule INSTANCIA = new AsegurarCodigoVigenteRule();

    private AsegurarCodigoVigenteRule() {
    }

    public static AsegurarCodigoVigenteRule obtenerInstancia() {
        return INSTANCIA;
    }

    @Override
    public void ejecutar(Object... datos) {
        var codigo = (CodigoVerificacionDominio) datos[0];
        var ahora = (LocalDateTime) datos[1];
        var maximoIntentos = (Integer) datos[2];

        if (UtilObjeto.esNulo(codigo) || UtilId.esValorDefecto(codigo.getId())) {
            throw KudosNegocioExcepcion.crear(CatalogoMensajes.Identidad.CODIGO_NO_SOLICITADO);
        }
        if (codigo.isUsado()) {
            throw KudosNegocioExcepcion.crear(CatalogoMensajes.Identidad.CODIGO_YA_USADO);
        }
        if (codigo.estaVencido(ahora)) {
            throw KudosNegocioExcepcion.crear(CatalogoMensajes.Identidad.CODIGO_VENCIDO);
        }
        if (codigo.intentosAgotados(maximoIntentos)) {
            throw KudosNegocioExcepcion.crear(CatalogoMensajes.Identidad.CODIGO_INTENTOS_AGOTADOS);
        }
    }
}
