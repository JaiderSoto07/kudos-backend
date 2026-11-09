package co.edu.uco.kudos.transversal;

import co.edu.uco.kudos.dominio.EstadoCuenta;
import co.edu.uco.kudos.transversal.utilitario.UtilEnum;
import co.edu.uco.kudos.transversal.utilitario.UtilFecha;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import co.edu.uco.kudos.transversal.utilitario.UtilNumero;
import co.edu.uco.kudos.transversal.utilitario.UtilObjeto;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Utilitarios transversales")
class UtilitariosTest {

    private static final UtilTexto TEXTO = UtilTexto.getUtilTexto();

    @Test
    @DisplayName("UtilTexto limpia, valida longitudes, formatos y concatena")
    void texto() {
        assertEquals("", UtilTexto.obtenerValorDefecto(null));
        assertEquals("x", UtilTexto.obtenerValorDefecto(null, "x"));
        assertTrue(TEXTO.esNula(null));
        assertTrue(TEXTO.esVacia("   "));
        assertFalse(TEXTO.esVacia(" a "));
        assertEquals("a b", TEXTO.quitarEspaciosEnBlanco("  a b  "));
        assertEquals("defecto", TEXTO.quitarEspaciosEnBlancoOValorDefecto("  ", "defecto"));
        assertEquals("valor", TEXTO.quitarEspaciosEnBlancoOValorDefecto(" valor ", "defecto"));
        assertEquals("Liderazgo", TEXTO.primeraLetraMayuscula("  LIDERAZGO "));
        assertEquals("", TEXTO.primeraLetraMayuscula(null));
        assertEquals(5, TEXTO.obtenerLongitudCadena(" abc ", false));
        assertEquals(3, TEXTO.obtenerLongitudCadena(" abc ", true));
        assertTrue(TEXTO.longitudCadenaEsValida(" abc ", 3, 3, true));
        assertFalse(TEXTO.longitudCadenaEsValida(" abc ", 3, 3, false));
        assertTrue(TEXTO.formatoEsValido("Ana María", UtilTexto.SOLO_LETRAS_ESPACIOS));
        assertFalse(TEXTO.formatoEsValido("Ana123", UtilTexto.SOLO_LETRAS_ESPACIOS));
        assertEquals("a1", TEXTO.concatenar("a", null, 1));
    }

    @Test
    @DisplayName("UtilEnum convierte texto en enum sin importar mayúsculas y usa el valor por defecto si no existe")
    void enumeraciones() {
        assertEquals(EstadoCuenta.ACTIVA, UtilEnum.obtenerValor(EstadoCuenta.class, " activa ", EstadoCuenta.SUSPENDIDA));
        assertEquals(EstadoCuenta.SUSPENDIDA, UtilEnum.obtenerValor(EstadoCuenta.class, "NO_EXISTE", EstadoCuenta.SUSPENDIDA));
        assertNull(UtilEnum.obtenerValor(EstadoCuenta.class, null, null));
        assertEquals("ACTIVA", UtilEnum.obtenerTexto(EstadoCuenta.ACTIVA));
        assertEquals("", UtilEnum.<EstadoCuenta>obtenerTexto(null));
    }

    @Test
    @DisplayName("UtilId, UtilFecha, UtilObjeto y UtilNumero manejan valores por defecto")
    void valoresPorDefecto() {
        var id = UtilId.generarId();
        assertFalse(UtilId.esValorDefecto(id));
        assertTrue(UtilId.esValorDefecto(null));
        assertEquals(UtilId.VALOR_DEFECTO, UtilId.valorDefecto(null));
        assertNotEquals(UtilId.generarIdComoTexto(), UtilId.VALOR_DEFECTO.toString());

        assertTrue(UtilFecha.esValorDefecto(null));
        assertFalse(UtilFecha.esValorDefecto(UtilFecha.fechaActual()));
        assertTrue(UtilFecha.fechaParaMillis() > 0);

        assertTrue(UtilObjeto.esNulo(null));
        assertEquals("b", UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(null, "b"));

        assertEquals(7, UtilNumero.obtenerValorDefecto(null, 7));
        assertEquals(0, UtilNumero.obtenerValorDefectoSiEsNuloONegativo(-3, UtilNumero.CERO));
        assertEquals(4, UtilNumero.obtenerValorDefectoSiEsNuloONegativo(4, UtilNumero.CERO));
        assertTrue(UtilNumero.valorEntreUnRangoIncluyendoAmbos(1, 5, 5));
        assertFalse(UtilNumero.valorEntreUnRangoIncluyendoAmbos(1, 6, 5));
        assertFalse(UtilNumero.valorEntreUnRangoIncluyendoAmbos(null, 3, 5));
    }
}
