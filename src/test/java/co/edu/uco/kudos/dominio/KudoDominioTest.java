package co.edu.uco.kudos.dominio;

import co.edu.uco.kudos.transversal.excepciones.KudosExcepcion;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Kudo y réplica: ciclo de vida con EstadoContenido (State, RN-201 a RN-209, D-20, D-21)")
class KudoDominioTest {

    private KudoDominio kudoPublicado() {
        var autor = new UsuarioDominio.Builder().id(UtilId.generarId()).build();
        var kudo = new KudoDominio.Builder().id(UtilId.generarId()).autor(autor).contenido("Gracias por tu ayuda").build();
        kudo.publicar();
        return kudo;
    }

    @Test
    @DisplayName("Un Kudo nace CREADO y al publicarse queda VISIBLE (RN-201)")
    void publicar() {
        var kudo = new KudoDominio.Builder().build();
        assertEquals(EstadoContenido.CREADO, kudo.getEstado());
        kudo.publicar();
        assertEquals(EstadoContenido.VISIBLE, kudo.getEstado());
        assertThrows(KudosExcepcion.class, kudo::publicar);
    }

    @Test
    @DisplayName("Reportar, revisar y vetar (RN-202 a RN-204)")
    void reportarRevisarYVetar() {
        var kudo = kudoPublicado();
        kudo.reportar();
        assertEquals(EstadoContenido.REPORTADO, kudo.getEstado());
        kudo.reportar();
        assertEquals(EstadoContenido.REPORTADO, kudo.getEstado(), "un segundo reporte no cambia el estado");
        kudo.iniciarRevision();
        assertEquals(EstadoContenido.EN_REVISION, kudo.getEstado());
        kudo.vetar();
        assertEquals(EstadoContenido.VETADO, kudo.getEstado());
    }

    @Test
    @DisplayName("Liberar devuelve el Kudo a VISIBLE (RN-242)")
    void liberar() {
        var kudo = kudoPublicado();
        kudo.reportar();
        kudo.iniciarRevision();
        kudo.liberar();
        assertEquals(EstadoContenido.VISIBLE, kudo.getEstado());
    }

    @Test
    @DisplayName("Solo un Kudo reportado entra en revisión y solo uno en revisión se libera")
    void cambiosNoPermitidos() {
        var kudo = kudoPublicado();
        assertThrows(KudosExcepcion.class, kudo::iniciarRevision);
        assertThrows(KudosExcepcion.class, kudo::liberar);
    }

    @Test
    @DisplayName("Un Kudo vetado no admite interacción ni más cambios (RN-205, D-21)")
    void vetadoEsFinal() {
        var kudo = kudoPublicado();
        assertTrue(kudo.admiteInteraccion());
        kudo.vetar();
        assertFalse(kudo.admiteInteraccion());
        assertThrows(KudosExcepcion.class, kudo::reportar);
        assertThrows(KudosExcepcion.class, kudo::vetar);
    }

    @Test
    @DisplayName("Saber si un usuario es el autor")
    void esAutor() {
        var kudo = kudoPublicado();
        assertTrue(kudo.esAutor(kudo.getAutor().getId()));
        assertFalse(kudo.esAutor(UtilId.generarId()));
    }

    @Test
    @DisplayName("Cada consulta suma una visualización (RN-229, D-25)")
    void visualizaciones() {
        var kudo = kudoPublicado();
        kudo.registrarVisualizacion();
        kudo.registrarVisualizacion();
        assertEquals(2, kudo.getCantidadVisualizaciones());
    }

    @Test
    @DisplayName("El promedio de calificaciones se calcula con dos decimales (RN-185)")
    void promedioDeCalificaciones() {
        var kudo = kudoPublicado();
        kudo.actualizarResumenCalificaciones(List.of(5, 4, 4));
        assertEquals(new BigDecimal("4.33"), kudo.getPromedioCalificacion());
        assertEquals(3, kudo.getCantidadCalificaciones());
        kudo.actualizarResumenCalificaciones(List.of());
        assertEquals(BigDecimal.ZERO, kudo.getPromedioCalificacion());
    }

    @Test
    @DisplayName("La réplica tiene el mismo ciclo de vida y sabe a quién responde (D-22)")
    void replica() {
        var kudo = kudoPublicado();
        var primerNivel = new ReplicaDominio.Builder().id(UtilId.generarId()).kudo(kudo).build();
        var respuesta = new ReplicaDominio.Builder().id(UtilId.generarId()).kudo(kudo).idReplicaPadre(primerNivel.getId()).build();
        assertTrue(primerNivel.respondeAlKudo());
        assertFalse(respuesta.respondeAlKudo());

        respuesta.publicar();
        respuesta.vetar();
        assertFalse(respuesta.admiteInteraccion());
        respuesta.actualizarResumenCalificaciones(List.of(3));
        assertEquals(1, respuesta.getCantidadCalificaciones());
    }
}
