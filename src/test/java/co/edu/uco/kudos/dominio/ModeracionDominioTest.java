package co.edu.uco.kudos.dominio;

import co.edu.uco.kudos.transversal.excepciones.KudosExcepcion;
import co.edu.uco.kudos.transversal.utilitario.UtilFecha;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Reportes, moderación, calificaciones, códigos, notificaciones y parámetros (D-10, D-11, D-23, D-24, D-27)")
class ModeracionDominioTest {

    @Test
    @DisplayName("Un reporte pasa a revisión y se acepta una sola vez (RN-210 a RN-212)")
    void reporteAceptado() {
        var reporte = new ReporteDominio.Builder().kudo(new KudoDominio.Builder().id(UtilId.generarId()).build())
                .motivo(MotivoReporte.SPAM).build();
        var idModeracion = UtilId.generarId();
        assertTrue(reporte.esSobreKudo());
        assertEquals(EstadoReporte.PENDIENTE, reporte.getEstado());
        reporte.pasarARevision(idModeracion);
        assertEquals(EstadoReporte.EN_REVISION, reporte.getEstado());
        assertThrows(KudosExcepcion.class, () -> reporte.pasarARevision(idModeracion));
        reporte.aceptar(idModeracion);
        assertEquals(EstadoReporte.ACEPTADO, reporte.getEstado());
        assertEquals(idModeracion, reporte.getIdModeracion());
        assertThrows(KudosExcepcion.class, () -> reporte.rechazar(idModeracion));
    }

    @Test
    @DisplayName("Un reporte pendiente puede rechazarse directamente")
    void reporteRechazado() {
        var reporte = new ReporteDominio.Builder().build();
        assertFalse(reporte.esSobreKudo());
        reporte.rechazar(UtilId.generarId());
        assertEquals(EstadoReporte.RECHAZADO, reporte.getEstado());
        assertFalse(reporte.estaAbierto());
    }

    @Test
    @DisplayName("El motivo OTRO exige descripción (D-24)")
    void motivoOtro() {
        assertTrue(MotivoReporte.OTRO.exigeDescripcion());
        assertFalse(MotivoReporte.SPAM.exigeDescripcion());
    }

    @Test
    @DisplayName("Una moderación se cierra una sola vez con su decisión y motivo (RN-249, RN-250)")
    void moderacion() {
        var vetada = new ModeracionDominio.Builder().build();
        assertTrue(vetada.estaEnCurso());
        assertNull(vetada.getDecision());
        vetada.vetar("Lenguaje ofensivo contra un estudiante");
        assertEquals(EstadoModeracion.CERRADA, vetada.getEstado());
        assertTrue(vetada.terminoEnVeto());
        assertFalse(UtilFecha.esValorDefecto(vetada.getFechaCierre()));
        assertThrows(KudosExcepcion.class, () -> vetada.liberar("Otro motivo"));

        var liberada = new ModeracionDominio.Builder().build();
        liberada.liberar("Cumple las políticas");
        assertEquals(DecisionModeracion.LIBERAR, liberada.getDecision());
        assertFalse(liberada.terminoEnVeto());
        assertFalse(liberada.esSobreKudo());
    }

    @Test
    @DisplayName("Volver a calificar modifica las estrellas de la calificación activa (RN-178 a RN-181, RN-237)")
    void calificacion() {
        var calificacion = new CalificacionDominio.Builder().estrellas(3).build();
        assertTrue(calificacion.isActiva());
        calificacion.modificarEstrellas(5);
        assertEquals(5, calificacion.getEstrellas());
        assertFalse(UtilFecha.esValorDefecto(calificacion.getFechaModificacion()));
        assertFalse(calificacion.esSobreKudo());
    }

    @Test
    @DisplayName("Un código vence, agota sus intentos y se marca como usado (D-10)")
    void codigoVerificacion() {
        var ahora = UtilFecha.fechaActual();
        var codigo = new CodigoVerificacionDominio.Builder().fechaGeneracion(ahora).fechaExpiracion(ahora.plusMinutes(15)).build();
        assertFalse(codigo.estaVencido(ahora));
        assertTrue(codigo.estaVencido(ahora.plusMinutes(15)));
        codigo.registrarIntentoFallido();
        codigo.registrarIntentoFallido();
        assertFalse(codigo.intentosAgotados(3));
        codigo.registrarIntentoFallido();
        assertTrue(codigo.intentosAgotados(3));
        codigo.marcarUsado();
        assertTrue(codigo.isUsado());
    }

    @Test
    @DisplayName("Una notificación se marca leída una sola vez (D-27)")
    void notificacion() {
        var notificacion = new NotificacionDominio.Builder().mensaje("Recibió un Kudo").build();
        notificacion.marcarLeida();
        var primeraLectura = notificacion.getFechaLectura();
        notificacion.marcarLeida();
        assertTrue(notificacion.isLeida());
        assertEquals(primeraLectura, notificacion.getFechaLectura());
    }

    @Test
    @DisplayName("Un parámetro entero se lee como número (D-11)")
    void parametroGlobal() {
        var parametro = new ParametroGlobalDominio.Builder().clave(ParametroGlobalDominio.VIGENCIA_CODIGO_MINUTOS)
                .valor("15").tipoDato(TipoParametro.ENTERO).build();
        assertEquals(15, parametro.valorComoEntero());
        parametro.cambiarValor("diez");
        assertThrows(KudosExcepcion.class, parametro::valorComoEntero);
        var texto = new ParametroGlobalDominio.Builder().valor("uco.edu.co").build();
        assertThrows(KudosExcepcion.class, texto::valorComoEntero);
    }
}
