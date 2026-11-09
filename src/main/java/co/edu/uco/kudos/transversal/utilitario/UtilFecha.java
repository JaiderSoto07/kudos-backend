package co.edu.uco.kudos.transversal.utilitario;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public final class UtilFecha {

    public static final ZoneId ZONA_COLOMBIA = ZoneId.of("America/Bogota");

    /** Fecha que representa "sin fecha" (por ejemplo, una asignación que aún no termina). */
    public static final LocalDateTime FECHA_HORA_POR_DEFECTO = LocalDateTime.of(1000, 1, 1, 0, 0);

    private UtilFecha() {
    }

    public static LocalDateTime valorDefecto(LocalDateTime fecha) {
        return UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(fecha, FECHA_HORA_POR_DEFECTO);
    }

    public static boolean esValorDefecto(LocalDateTime fecha) {
        return FECHA_HORA_POR_DEFECTO.equals(valorDefecto(fecha));
    }

    public static LocalDateTime fechaActual() {
        return LocalDateTime.now(ZONA_COLOMBIA).withNano(0);
    }

    public static long fechaParaMillis() {
        return Instant.now().toEpochMilli();
    }
}
