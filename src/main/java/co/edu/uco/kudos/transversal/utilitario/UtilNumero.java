package co.edu.uco.kudos.transversal.utilitario;

public final class UtilNumero {

    public static final int CERO = 0;

    private UtilNumero() {
    }

    public static <N extends Number> N obtenerValorDefecto(N valor, N valorDefecto) {
        return UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(valor, valorDefecto);
    }

    public static <N extends Number> N obtenerValorDefectoSiEsNuloONegativo(N valor, N valorDefecto) {
        return UtilObjeto.esNulo(valor) || valor.doubleValue() < 0 ? valorDefecto : valor;
    }

    public static <N extends Number> boolean valorEntreUnRangoIncluyendoAmbos(N minimo, N valor, N maximo) {
        if (UtilObjeto.esNulo(minimo) || UtilObjeto.esNulo(valor) || UtilObjeto.esNulo(maximo)) {
            return false;
        }
        return valor.doubleValue() >= minimo.doubleValue() && valor.doubleValue() <= maximo.doubleValue();
    }
}
