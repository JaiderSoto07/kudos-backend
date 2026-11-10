package co.edu.uco.kudos.negocio;

import co.edu.uco.kudos.dominio.CategoriaDominio;
import co.edu.uco.kudos.dominio.EstadoContenido;
import co.edu.uco.kudos.dominio.EstadoCuenta;
import co.edu.uco.kudos.dominio.KudoDominio;
import co.edu.uco.kudos.dominio.Rol;
import co.edu.uco.kudos.dominio.UsuarioDominio;
import co.edu.uco.kudos.entidad.KudoEntidad;
import co.edu.uco.kudos.negocio.falsos.DAOFactoryFalsa;
import co.edu.uco.kudos.negocio.negocio.KudoNegocio;
import co.edu.uco.kudos.negocio.negocio.assembler.impl.KudoEntidadAssembler;
import co.edu.uco.kudos.negocio.negocio.impl.KudoNegocioImpl;
import co.edu.uco.kudos.negocio.negocio.impl.PersonaNegocioImpl;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosExcepcion;
import co.edu.uco.kudos.transversal.utilitario.UtilFecha;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Negocio de Kudos: crear, muro, detalle e historial (CU-001 a CU-003, CU-042, RF-046 a RF-052, RF-090, D-19, D-20, D-25)")
class KudoNegocioTest {

    private static final String CONTENIDO = "Gracias por ayudarme a preparar la sustentación.";

    private DAOFactoryFalsa daoFactory;
    private KudoNegocio negocio;
    private UUID ana;
    private UUID luis;
    private UUID marta;
    private UUID liderazgo;
    private UUID servicio;

    @BeforeEach
    void preparar() {
        daoFactory = new DAOFactoryFalsa();
        ana = daoFactory.registrarPersona("ana@gmail.com", Rol.ESTUDIANTE, EstadoCuenta.ACTIVA);
        luis = daoFactory.registrarPersona("luis@gmail.com", Rol.DOCENTE, EstadoCuenta.ACTIVA);
        marta = daoFactory.registrarPersona("marta@gmail.com", Rol.ESTUDIANTE, EstadoCuenta.ACTIVA);
        liderazgo = daoFactory.registrarCategoria("Liderazgo", true);
        servicio = daoFactory.registrarCategoria("Servicio", true);
        negocio = new KudoNegocioImpl(daoFactory);
    }

    private KudoDominio datos(UUID destinatario, UUID categoria, String contenido) {
        return new KudoDominio.Builder()
                .destinatario(new UsuarioDominio.Builder().id(destinatario).build())
                .categoria(new CategoriaDominio.Builder().id(categoria).build())
                .contenido(contenido)
                .build();
    }

    private void assertRechazo(String mensaje, Executable operacion) {
        var excepcion = assertThrows(KudosExcepcion.class, operacion);
        assertEquals(mensaje, excepcion.getMensajeUsuario());
    }

    private KudoEntidad ultimoKudo() {
        return daoFactory.getKudoDAOFalso().getKudos().getFirst();
    }

    @Test
    @DisplayName("Un Kudo nuevo queda VISIBLE de inmediato, se audita y se le avisa al destinatario (D-20)")
    void crearKudo() {
        negocio.crear(ana, datos(luis, liderazgo, CONTENIDO));

        var kudo = ultimoKudo();
        assertEquals("VISIBLE", kudo.getEstado());
        assertEquals(ana, kudo.getAutor().getId());
        assertEquals(luis, kudo.getDestinatario().getId());
        assertEquals(liderazgo, kudo.getCategoria().getId());
        assertEquals(CONTENIDO, kudo.getContenido());
        var notificacion = daoFactory.getNotificacionDAOFalso().getNotificaciones().getFirst();
        assertEquals("KUDO_RECIBIDO", notificacion.getTipo());
        assertEquals(luis, notificacion.getDestinatario().getId());
        assertTrue(notificacion.getMensaje().contains("Liderazgo"));
        assertEquals("KudoPublicado", daoFactory.getAuditoriaDAOFalso().getRegistros().getLast().getAccion());
    }

    @Test
    @DisplayName("El contenido se guarda completo, sin límite ni recorte (RF-052)")
    void contenidoSinLimite() {
        var largo = "Gracias. ".repeat(3000).trim();

        negocio.crear(ana, datos(luis, liderazgo, largo));

        assertEquals(largo, ultimoKudo().getContenido());
    }

    @Test
    @DisplayName("Rechaza Kudo a uno mismo, destinatario inactivo o sin elegir, categoría inactiva o sin elegir y contenido vacío (D-19)")
    void rechazos() {
        var suspendida = daoFactory.registrarPersona("sus@gmail.com", Rol.ESTUDIANTE, EstadoCuenta.SUSPENDIDA);
        var inactiva = daoFactory.registrarCategoria("Antigua", false);
        var aMiMismo = datos(ana, liderazgo, CONTENIDO);
        var sinDestinatario = datos(null, liderazgo, CONTENIDO);
        var aSuspendida = datos(suspendida, liderazgo, CONTENIDO);
        var aInexistente = datos(UtilId.generarId(), liderazgo, CONTENIDO);
        var sinCategoria = datos(luis, null, CONTENIDO);
        var categoriaInactiva = datos(luis, inactiva, CONTENIDO);
        var categoriaInexistente = datos(luis, UtilId.generarId(), CONTENIDO);
        var vacio = datos(luis, liderazgo, "   ");

        assertRechazo(CatalogoMensajes.Kudo.DESTINATARIO_ES_EL_AUTOR, () -> negocio.crear(ana, aMiMismo));
        assertRechazo(CatalogoMensajes.Kudo.DESTINATARIO_OBLIGATORIO, () -> negocio.crear(ana, sinDestinatario));
        assertRechazo(CatalogoMensajes.Kudo.DESTINATARIO_NO_ACTIVO, () -> negocio.crear(ana, aSuspendida));
        assertRechazo(CatalogoMensajes.Kudo.DESTINATARIO_NO_ACTIVO, () -> negocio.crear(ana, aInexistente));
        assertRechazo(CatalogoMensajes.Kudo.CATEGORIA_OBLIGATORIA, () -> negocio.crear(ana, sinCategoria));
        assertRechazo(CatalogoMensajes.Kudo.CATEGORIA_NO_DISPONIBLE, () -> negocio.crear(ana, categoriaInactiva));
        assertRechazo(CatalogoMensajes.Kudo.CATEGORIA_NO_DISPONIBLE, () -> negocio.crear(ana, categoriaInexistente));
        assertRechazo(CatalogoMensajes.Kudo.CONTENIDO_OBLIGATORIO, () -> negocio.crear(ana, vacio));
        assertTrue(daoFactory.getKudoDAOFalso().getKudos().isEmpty());
    }

    @Test
    @DisplayName("El muro va del más reciente al más antiguo, sin vetados, y filtra por categoría y por persona (RF-090)")
    void muro() {
        negocio.crear(ana, datos(luis, liderazgo, "Primero"));
        negocio.crear(luis, datos(marta, servicio, "Segundo"));
        negocio.crear(marta, datos(ana, liderazgo, "Tercero"));
        var vetado = KudoEntidadAssembler.getInstance().convertirADominio(ultimoKudo());
        vetado.vetar();
        daoFactory.getKudoDAOFalso().actualizar(vetado.getId(), KudoEntidadAssembler.getInstance().convertirAEntidad(vetado));
        var reportado = KudoEntidadAssembler.getInstance().convertirADominio(daoFactory.getKudoDAOFalso().getKudos().get(1));
        reportado.reportar();
        daoFactory.getKudoDAOFalso().actualizar(reportado.getId(), KudoEntidadAssembler.getInstance().convertirAEntidad(reportado));

        var todos = negocio.consultarMuro(null, null);
        assertEquals(2, todos.size());
        assertEquals(EstadoContenido.REPORTADO, todos.getFirst().getEstado());
        assertEquals(1, negocio.consultarMuro(liderazgo, null).size());
        assertEquals(2, negocio.consultarMuro(null, luis).size());
        assertEquals(1, negocio.consultarMuro(servicio, luis).size());
        assertEquals(1, negocio.consultarMuro(null, ana).size());
    }

    @Test
    @DisplayName("Abrir un Kudo suma una visualización, salvo que lo abra su autor (D-25)")
    void visualizaciones() {
        negocio.crear(ana, datos(luis, liderazgo, CONTENIDO));
        var idKudo = ultimoKudo().getId();

        negocio.consultar(luis, idKudo);
        negocio.consultar(marta, idKudo);
        var kudo = negocio.consultar(ana, idKudo);

        assertEquals(2, kudo.getCantidadVisualizaciones());
        assertEquals(2, ultimoKudo().getCantidadVisualizaciones());
        var inexistente = UtilId.generarId();
        assertRechazo(CatalogoMensajes.Kudo.KUDO_NO_EXISTE, () -> negocio.consultar(ana, inexistente));
    }

    @Test
    @DisplayName("El historial muestra los Kudos recibidos, los emitidos o ambos (RF-048 a RF-050)")
    void historial() {
        negocio.crear(ana, datos(luis, liderazgo, "Para Luis"));
        negocio.crear(marta, datos(ana, servicio, "Para Ana"));
        negocio.crear(ana, datos(marta, servicio, "Para Marta"));

        assertEquals(1, negocio.consultarHistorial(ana, "recibidos").size());
        assertEquals(2, negocio.consultarHistorial(ana, "EMITIDOS").size());
        assertEquals(3, negocio.consultarHistorial(ana, "").size());
        assertTrue(negocio.consultarHistorial(ana, null).stream().anyMatch(k -> k.getContenido().equals("Para Marta")));
    }

    @Test
    @DisplayName("El buscador de personas trae solo cuentas activas, sin el actor y sin importar tildes")
    void buscarPersonas() {
        daoFactory.registrarPersona("susp@gmail.com", Rol.ESTUDIANTE, EstadoCuenta.SUSPENDIDA);
        var personas = new PersonaNegocioImpl(daoFactory);

        assertEquals(2, personas.buscarActivas(ana, "").size());
        assertEquals(1, personas.buscarActivas(ana, " LUIS ").size());
        assertEquals(2, personas.buscarActivas(ana, "PERSONA").size());
        assertEquals(1, personas.buscarActivas(ana, "docénte").size());
        assertTrue(personas.buscarActivas(ana, "nadie").isEmpty());
        assertTrue(personas.buscarActivas(ana, "ana@").isEmpty());
        for (var indice = 0; indice < 25; indice++) {
            daoFactory.registrarPersona("p" + indice + "@gmail.com", Rol.ESTUDIANTE, EstadoCuenta.ACTIVA);
        }
        assertEquals(20, personas.buscarActivas(ana, "").size());
        assertTrue(UtilFecha.esValorDefecto(UtilFecha.FECHA_HORA_POR_DEFECTO));
    }
}
