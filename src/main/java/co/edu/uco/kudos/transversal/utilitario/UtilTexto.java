package co.edu.uco.kudos.transversal.utilitario;

public final class UtilTexto {

    public static final String VACIO = "";

    public static final String SOLO_LETRAS_ESPACIOS = "^[a-zA-Z ñÑáÁéÉíÍóÓúÚüÜ]*$";

    private static final UtilTexto INSTANCIA = new UtilTexto();

    private UtilTexto() {
    }

    public static UtilTexto getUtilTexto() {
        return INSTANCIA;
    }

    public static String obtenerValorDefecto(String valor, String valorDefecto) {
        return UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(valor, valorDefecto);
    }

    public static String obtenerValorDefecto(String valor) {
        return obtenerValorDefecto(valor, VACIO);
    }

    public boolean esNula(String texto) {
        return UtilObjeto.esNulo(texto);
    }

    public boolean esVacia(String texto) {
        return VACIO.equals(quitarEspaciosEnBlanco(texto));
    }

    public String quitarEspaciosEnBlanco(String valor) {
        return obtenerValorDefecto(valor).trim();
    }

    public String quitarEspaciosEnBlancoOValorDefecto(String valor, String valorDefecto) {
        var valorSanitizado = quitarEspaciosEnBlanco(valor);
        return esVacia(valorSanitizado) ? valorDefecto : valorSanitizado;
    }

    public String primeraLetraMayuscula(String valor) {
        var valorSanitizado = quitarEspaciosEnBlanco(valor).toLowerCase();
        return esVacia(valorSanitizado)
                ? VACIO
                : valorSanitizado.substring(0, 1).toUpperCase() + valorSanitizado.substring(1);
    }

    public int obtenerLongitudCadena(String valor) {
        return obtenerValorDefecto(valor).length();
    }

    public int obtenerLongitudCadena(String valor, boolean quitarEspaciosEnBlanco) {
        return quitarEspaciosEnBlanco
                ? obtenerLongitudCadena(quitarEspaciosEnBlanco(valor))
                : obtenerLongitudCadena(valor);
    }

    public boolean longitudCadenaEsValida(String valor, int longitudMinima, int longitudMaxima, boolean quitarEspaciosEnBlanco) {
        var longitud = obtenerLongitudCadena(valor, quitarEspaciosEnBlanco);
        return longitud >= longitudMinima && longitud <= longitudMaxima;
    }

    public boolean formatoEsValido(String valor, String patron) {
        return obtenerValorDefecto(valor).matches(obtenerValorDefecto(patron));
    }

    public String concatenar(Object... valores) {
        var resultado = new StringBuilder();
        for (var valor : valores) {
            resultado.append(UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(valor, VACIO));
        }
        return resultado.toString();
    }
}
