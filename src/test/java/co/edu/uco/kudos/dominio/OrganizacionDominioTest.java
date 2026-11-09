package co.edu.uco.kudos.dominio;

import co.edu.uco.kudos.transversal.excepciones.KudosExcepcion;
import co.edu.uco.kudos.transversal.utilitario.UtilFecha;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Facultades, programas, categorías, roles, vínculos y solicitudes (RN-039 a RN-145, D-01, D-05, D-15, D-16, D-17)")
class OrganizacionDominioTest {

    private static final String MOTIVO = "Cambio de coordinador";

    @Test
    @DisplayName("La Facultad nace en conformación y se activa con su primer programa (RN-045, D-05)")
    void facultad() {
        var facultad = new FacultadDominio.Builder().nombre("Ingeniería").build();
        assertEquals(EstadoFacultad.EN_CONFORMACION, facultad.getEstado());
        assertTrue(facultad.admiteProgramas());
        assertFalse(facultad.admiteCoordinador());

        facultad.activar();
        assertEquals(EstadoFacultad.ACTIVA, facultad.getEstado());
        assertTrue(facultad.admiteCoordinador());

        facultad.renombrar(" Ingeniería y Ciencias ");
        assertEquals("Ingeniería y Ciencias", facultad.getNombre());

        facultad.inactivar();
        assertFalse(facultad.admiteProgramas());
        assertThrows(KudosExcepcion.class, facultad::inactivar);
        facultad.activar();
        assertEquals(EstadoFacultad.INACTIVA, facultad.getEstado(), "una Facultad inactiva no se reactiva (D-05)");
    }

    @Test
    @DisplayName("Un programa se renombra y se inactiva una sola vez")
    void programa() {
        var programa = new ProgramaAcademicoDominio.Builder().nombre("Sistemas").build();
        assertTrue(programa.isActivo());
        programa.renombrar("Ingeniería de Sistemas");
        assertEquals("Ingeniería de Sistemas", programa.getNombre());
        programa.inactivar();
        assertFalse(programa.isActivo());
        assertThrows(KudosExcepcion.class, programa::inactivar);
    }

    @Test
    @DisplayName("Una categoría se activa y desactiva; no dos veces seguidas (RN-142, RN-143)")
    void categoria() {
        var categoria = new CategoriaDominio.Builder().nombre("Liderazgo").build();
        assertTrue(categoria.isActiva());
        assertThrows(KudosExcepcion.class, categoria::activar);
        categoria.desactivar();
        assertFalse(categoria.isActiva());
        assertThrows(KudosExcepcion.class, categoria::desactivar);
        categoria.activar();
        categoria.modificar("Liderazgo positivo", "Reconoce a quien inspira");
        assertEquals("Liderazgo positivo", categoria.getNombre());
    }

    @Test
    @DisplayName("Cerrar una asignación de rol conserva el historial (RN-043, RN-051, D-15)")
    void asignacionRol() {
        var asignacion = new AsignacionRolDominio.Builder().rol(Rol.COORDINADOR_PROGRAMA).fechaInicio(UtilFecha.fechaActual()).build();
        assertTrue(asignacion.estaVigente());
        asignacion.cerrar(MOTIVO);
        assertFalse(asignacion.estaVigente());
        assertEquals(MOTIVO, asignacion.getMotivoFin());
        assertThrows(KudosExcepcion.class, () -> asignacion.cerrar(MOTIVO));
    }

    @Test
    @DisplayName("Desvincular cierra el vínculo sin borrarlo (RN-057, INV-013)")
    void vinculacion() {
        var vinculo = new VinculacionProgramaDominio.Builder().tipoVinculo(TipoVinculo.DOCENTE).build();
        assertTrue(vinculo.estaVigente());
        vinculo.desvincular("Terminó su contrato");
        assertFalse(vinculo.estaVigente());
        assertThrows(KudosExcepcion.class, () -> vinculo.desvincular("Otra vez"));
    }

    @Test
    @DisplayName("Una solicitud se decide una sola vez y guarda quién la decidió (D-17)")
    void solicitud() {
        var autoridad = new UsuarioDominio.Builder().nombres("Juan").build();
        var aprobada = new SolicitudRegistroDominio.Builder().rolSolicitado(Rol.ESTUDIANTE).build();
        assertTrue(aprobada.estaPendiente());
        aprobada.aprobar(autoridad);
        assertEquals(EstadoSolicitud.APROBADA, aprobada.getEstado());
        assertEquals(autoridad, aprobada.getDecididaPor());
        assertThrows(KudosExcepcion.class, () -> aprobada.aprobar(autoridad));

        var rechazada = new SolicitudRegistroDominio.Builder().build();
        rechazada.rechazar(autoridad, "No pertenece al programa");
        assertEquals(EstadoSolicitud.RECHAZADA, rechazada.getEstado());
        assertEquals("No pertenece al programa", rechazada.getMotivoRechazo());
    }

    @Test
    @DisplayName("Cada rol conoce quién lo aprueba y qué puede hacer (RN-090 a RN-095, D-01 a D-03)")
    void roles() {
        assertEquals(Rol.COORDINADOR_PROGRAMA, Rol.ESTUDIANTE.rolAprobador());
        assertEquals(Rol.COORDINADOR_PROGRAMA, Rol.DOCENTE.rolAprobador());
        assertEquals(Rol.COORDINADOR_FACULTAD, Rol.COORDINADOR_PROGRAMA.rolAprobador());
        assertEquals(Rol.COORDINADOR_ACADEMICO, Rol.COORDINADOR_FACULTAD.rolAprobador());
        assertEquals(Rol.RECTOR, Rol.COORDINADOR_ACADEMICO.rolAprobador());
        assertEquals(Rol.ADMINISTRADOR, Rol.RECTOR.rolAprobador());
        assertEquals(Rol.ADMINISTRADOR, Rol.ADMINISTRADOR.rolAprobador());

        assertTrue(Rol.RECTOR.puedeModerar());
        assertTrue(Rol.COORDINADOR_ACADEMICO.puedeModerar());
        assertFalse(Rol.ADMINISTRADOR.puedeModerar());
        assertTrue(Rol.ADMINISTRADOR.puedeGestionarCategorias());
        assertFalse(Rol.DOCENTE.puedeGestionarCategorias());
        assertFalse(Rol.RECTOR.esAutorregistrable());
        assertTrue(Rol.ESTUDIANTE.esAutorregistrable());
    }
}
