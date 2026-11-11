package co.edu.uco.kudos.negocio;

import co.edu.uco.kudos.dominio.AuditoriaDominio;
import co.edu.uco.kudos.dominio.EstadoCuenta;
import co.edu.uco.kudos.dominio.EstadoFacultad;
import co.edu.uco.kudos.dominio.Rol;
import co.edu.uco.kudos.entidad.AuditoriaEntidad;
import co.edu.uco.kudos.entidad.UsuarioEntidad;
import co.edu.uco.kudos.negocio.falsos.DAOFactoryFalsa;
import co.edu.uco.kudos.negocio.negocio.AuditoriaNegocio;
import co.edu.uco.kudos.negocio.negocio.impl.AuditoriaNegocioImpl;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosExcepcion;
import co.edu.uco.kudos.transversal.utilitario.UtilFecha;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Negocio de auditoría: consultar con filtros según el ámbito de cada rol (CU-038, RF-069, RF-070, RN-116, RN-128)")
class AuditoriaNegocioTest {

    private static final LocalDateTime ENERO = LocalDateTime.of(2026, 1, 10, 9, 0);

    private DAOFactoryFalsa daoFactory;
    private AuditoriaNegocio negocio;
    private UUID rector;
    private UUID academico;
    private UUID administrador;
    private UUID coordinadorFacultad;
    private UUID coordinadorPrograma;
    private UUID estudiante;

    @BeforeEach
    void preparar() {
        daoFactory = new DAOFactoryFalsa();
        var ingenieria = daoFactory.registrarFacultad(EstadoFacultad.ACTIVA);
        var derecho = daoFactory.registrarFacultad(EstadoFacultad.ACTIVA);
        var sistemas = daoFactory.registrarPrograma(ingenieria);
        var leyes = daoFactory.registrarPrograma(derecho);

        rector = daoFactory.registrarPersona("rector@gmail.com", Rol.RECTOR, EstadoCuenta.ACTIVA);
        academico = daoFactory.registrarPersona("academico@gmail.com", Rol.COORDINADOR_ACADEMICO, EstadoCuenta.ACTIVA);
        administrador = daoFactory.registrarPersona("admin@gmail.com", Rol.ADMINISTRADOR, EstadoCuenta.ACTIVA);
        coordinadorFacultad = daoFactory.registrarAutoridad(Rol.COORDINADOR_FACULTAD, ingenieria, null);
        coordinadorPrograma = daoFactory.registrarAutoridad(Rol.COORDINADOR_PROGRAMA, ingenieria, sistemas);
        var coordinadorLeyes = daoFactory.registrarAutoridad(Rol.COORDINADOR_PROGRAMA, derecho, leyes);
        estudiante = daoFactory.registrarPersona("estudiante@gmail.com", Rol.ESTUDIANTE, EstadoCuenta.ACTIVA);
        daoFactory.registrarVinculo(estudiante, sistemas, "ESTUDIANTE");
        var docente = daoFactory.registrarPersona("docente@gmail.com", Rol.DOCENTE, EstadoCuenta.ACTIVA);
        daoFactory.registrarVinculo(docente, leyes, "DOCENTE");

        registrar(estudiante, "Andrés", "Gómez", "KudoPublicado", "Kudo", UtilFecha.fechaActual());
        registrar(docente, "Lucía", "Pérez", "KudoPublicado", "Kudo", UtilFecha.fechaActual());
        registrar(coordinadorLeyes, "Mario", "Ríos", "PersonaVinculada", "VinculacionPrograma", UtilFecha.fechaActual());
        registrar(administrador, "Admin", "UCO", "CategoriaCreada", "Categoria", UtilFecha.fechaActual());
        registrar(rector, "María", "Gómez", "SolicitudAprobada", "SolicitudRegistro", ENERO);
        registrar(coordinadorPrograma, "Juan", "Zuluaga", "PersonaVinculada", "VinculacionPrograma", UtilFecha.fechaActual());
        negocio = new AuditoriaNegocioImpl(daoFactory);
    }

    private void registrar(UUID idActor, String nombres, String apellidos, String accion, String tipoObjeto, LocalDateTime fecha) {
        daoFactory.getAuditoriaDAOFalso().crear(new AuditoriaEntidad.Builder()
                .id(UtilId.generarId())
                .fechaHora(fecha)
                .actor(new UsuarioEntidad.Builder().id(idActor).nombres(nombres).apellidos(apellidos).build())
                .accion(accion)
                .tipoObjeto(tipoObjeto)
                .idObjeto(UtilId.generarId())
                .build());
    }

    private List<String> acciones(List<AuditoriaDominio> registros) {
        return registros.stream().map(registro -> registro.getActor().nombreCompleto() + " " + registro.getAccion()).toList();
    }

    private List<AuditoriaDominio> todo(UUID idActor) {
        return negocio.consultar(idActor, null, null, "", "", "");
    }

    @Test
    @DisplayName("El Rector y el Coordinador Académico ven toda la auditoría")
    void rectorYAcademicoVenTodo() {
        assertEquals(6, todo(rector).size());
        assertEquals(6, todo(academico).size());
    }

    @Test
    @DisplayName("El Administrador ve solo lo técnico y de cuentas: usuarios, roles, categorías y solicitudes")
    void administradorVeLoTecnico() {
        assertEquals(List.of("Admin UCO CategoriaCreada", "María Gómez SolicitudAprobada"), acciones(todo(administrador)));
    }

    @Test
    @DisplayName("Cada coordinador ve lo que hicieron él y las personas de su ámbito (RN-116)")
    void coordinadoresVenSuAmbito() {
        assertEquals(List.of("Andrés Gómez KudoPublicado", "Juan Zuluaga PersonaVinculada"), acciones(todo(coordinadorPrograma)));
        assertEquals(List.of("Andrés Gómez KudoPublicado", "Juan Zuluaga PersonaVinculada"), acciones(todo(coordinadorFacultad)));
    }

    @Test
    @DisplayName("Filtra por acción, tipo de objeto, nombre del actor (sin importar tildes) y rango de fechas")
    void filtros() {
        assertEquals(2, negocio.consultar(rector, null, null, "", "KudoPublicado", "").size());
        assertEquals(2, negocio.consultar(rector, null, null, "", "", "VinculacionPrograma").size());
        assertEquals(List.of("Andrés Gómez KudoPublicado", "María Gómez SolicitudAprobada"),
                acciones(negocio.consultar(rector, null, null, "  gomez ", "", "")));
        assertEquals(List.of("Andrés Gómez KudoPublicado"), acciones(negocio.consultar(rector, null, null, "ANDRES", "", "")));

        var enero = LocalDate.of(2026, 1, 10);
        assertEquals(List.of("María Gómez SolicitudAprobada"), acciones(negocio.consultar(rector, enero, enero, "", "", "")));
        assertEquals(5, negocio.consultar(rector, enero.plusDays(1), null, "", "", "").size());
        assertEquals(1, negocio.consultar(rector, null, enero, "", "", "").size());
    }

    @Test
    @DisplayName("Un estudiante no consulta la auditoría y la fecha inicial no puede ser posterior a la final")
    void rechazos() {
        var excepcion = assertThrows(KudosExcepcion.class, () -> todo(estudiante));
        assertEquals(CatalogoMensajes.Auditoria.ACTOR_NO_PUEDE_CONSULTAR, excepcion.getMensajeUsuario());

        var hoy = LocalDate.now();
        var rango = assertThrows(KudosExcepcion.class, () -> negocio.consultar(rector, hoy, hoy.minusDays(1), "", "", ""));
        assertEquals(CatalogoMensajes.Auditoria.RANGO_FECHAS_INVALIDO, rango.getMensajeUsuario());
        assertTrue(negocio.consultar(rector, hoy, hoy, "nadie con este nombre", "", "").isEmpty());
    }
}
