package co.edu.uco.kudos.transversal.utilitario;

public final class UtilEnum {

    private UtilEnum() {
    }

    public static <E extends Enum<E>> E obtenerValor(Class<E> tipo, String texto, E valorDefecto) {
        var textoLimpio = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(texto);
        for (var valor : tipo.getEnumConstants()) {
            if (valor.name().equalsIgnoreCase(textoLimpio)) {
                return valor;
            }
        }
        return valorDefecto;
    }

    public static <E extends Enum<E>> String obtenerTexto(E valor) {
        return UtilObjeto.esNulo(valor) ? UtilTexto.VACIO : valor.name();
    }
}
