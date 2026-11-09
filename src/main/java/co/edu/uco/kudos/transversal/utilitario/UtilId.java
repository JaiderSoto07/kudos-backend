package co.edu.uco.kudos.transversal.utilitario;

import java.util.UUID;

public final class UtilId {

    /** Identificador que representa "sin valor" (por ejemplo, una referencia opcional vacía). */
    public static final UUID VALOR_DEFECTO = UUID.fromString("00000000-0000-0000-0000-000000000000");

    private UtilId() {
    }

    public static UUID generarId() {
        return UUID.ofEpochMillis(UtilFecha.fechaParaMillis());
    }

    public static UUID valorDefecto(UUID id) {
        return UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(id, VALOR_DEFECTO);
    }

    public static boolean esValorDefecto(UUID id) {
        return VALOR_DEFECTO.equals(valorDefecto(id));
    }

    public static String generarIdComoTexto() {
        return generarId().toString();
    }
}
