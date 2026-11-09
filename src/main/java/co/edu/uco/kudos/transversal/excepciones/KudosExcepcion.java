package co.edu.uco.kudos.transversal.excepciones;

import co.edu.uco.kudos.transversal.excepciones.enums.Capa;
import co.edu.uco.kudos.transversal.utilitario.UtilObjeto;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

import java.io.Serial;

public class KudosExcepcion extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -4512873690215478963L;

    private final Capa capa;
    private final String mensajeUsuario;
    private final String mensajeTecnico;
    private final Exception excepcionRaiz;

    protected KudosExcepcion(Capa capa, String mensajeUsuario, String mensajeTecnico, Exception excepcionRaiz) {
        super(UtilTexto.obtenerValorDefecto(mensajeUsuario));
        this.capa = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(capa, Capa.GENERAL);
        this.mensajeUsuario = UtilTexto.obtenerValorDefecto(mensajeUsuario);
        this.mensajeTecnico = UtilTexto.obtenerValorDefecto(mensajeTecnico, this.mensajeUsuario);
        this.excepcionRaiz = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(excepcionRaiz, new Exception(this.mensajeTecnico));
    }

    public Capa getCapa() {
        return capa;
    }

    public String getMensajeUsuario() {
        return mensajeUsuario;
    }

    public String getMensajeTecnico() {
        return mensajeTecnico;
    }

    public Exception getExcepcionRaiz() {
        return excepcionRaiz;
    }
}
