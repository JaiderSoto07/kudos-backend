package co.edu.uco.kudos.negocio;

import co.edu.uco.kudos.dominio.CategoriaDominio;
import co.edu.uco.kudos.dominio.Rol;
import co.edu.uco.kudos.negocio.falsos.DAOFactoryFalsa;
import co.edu.uco.kudos.negocio.negocio.CategoriaNegocio;
import co.edu.uco.kudos.negocio.negocio.impl.CategoriaNegocioImpl;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosAutorizacionExcepcion;
import co.edu.uco.kudos.transversal.excepciones.KudosExcepcion;
import co.edu.uco.kudos.transversal.excepciones.enums.Capa;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Negocio de categorías (CU-035, RN-132 a RN-145, D-01)")
class CategoriaNegocioTest {

    private static final String NOMBRE = "Liderazgo";
    private static final String DESCRIPCION = "Reconoce a quien inspira y guía a otros con el ejemplo.";

    private DAOFactoryFalsa daoFactory;
    private CategoriaNegocio negocio;
    private UUID idRector;

    @BeforeEach
    void preparar() {
        daoFactory = new DAOFactoryFalsa();
        negocio = new CategoriaNegocioImpl(daoFactory);
        idRector = daoFactory.registrarUsuarioConRol(Rol.RECTOR);
    }

    private CategoriaDominio datos(String nombre, String descripcion) {
        return new CategoriaDominio.Builder().nombre(nombre).descripcion(descripcion).build();
    }

    private UUID crearCategoria(String nombre) {
        negocio.registrarNuevaCategoria(idRector, datos(nombre, DESCRIPCION));
        return negocio.consultarTodas(idRector).stream()
                .filter(categoria -> categoria.getNombre().equals(nombre))
                .findFirst()
                .orElseThrow()
                .getId();
    }

    private void assertRechazo(String mensajeEsperado, Runnable operacion) {
        var excepcion = assertThrows(KudosExcepcion.class, operacion::run);
        assertEquals(mensajeEsperado, excepcion.getMensajeUsuario());
        assertEquals(Capa.NEGOCIO, excepcion.getCapa());
    }

    @ParameterizedTest(name = "{0} puede crear categorías")
    @EnumSource(value = Rol.class, names = {"ADMINISTRADOR", "RECTOR", "COORDINADOR_ACADEMICO"})
    @DisplayName("Administrador, Rector y Coordinador Académico crean categorías activas (RN-134 a RN-136, P-CAT-004)")
    void gestoresCreanCategorias(Rol rol) {
        var idActor = daoFactory.registrarUsuarioConRol(rol);

        negocio.registrarNuevaCategoria(idActor, datos(" " + NOMBRE + " ", DESCRIPCION));

        var creada = daoFactory.getCategoriaDAOFalso().getCategorias().getFirst();
        assertFalse(UtilId.esValorDefecto(creada.getId()));
        assertEquals(NOMBRE, creada.getNombre());
        assertTrue(creada.isActiva());
        assertEquals(1, daoFactory.getAuditoriaDAOFalso().getRegistros().size());
        assertEquals("CategoriaCreada", daoFactory.getAuditoriaDAOFalso().getRegistros().getFirst().getAccion());
        assertEquals(idActor, daoFactory.getAuditoriaDAOFalso().getRegistros().getFirst().getActor().getId());
    }

    @ParameterizedTest(name = "{0} no puede crear categorías")
    @EnumSource(value = Rol.class, names = {"COORDINADOR_FACULTAD", "COORDINADOR_PROGRAMA", "DOCENTE", "ESTUDIANTE"})
    @DisplayName("Los demás roles no gestionan categorías (RN-137 a RN-140)")
    void otrosRolesNoCreanCategorias(Rol rol) {
        var idActor = daoFactory.registrarUsuarioConRol(rol);
        var nuevaCategoria = datos(NOMBRE, DESCRIPCION);

        assertThrows(KudosAutorizacionExcepcion.class, () -> negocio.registrarNuevaCategoria(idActor, nuevaCategoria));
        assertRechazo(CatalogoMensajes.CategoriaNegocioImpl.ACTOR_NO_PUEDE_GESTIONAR_CATEGORIAS,
                () -> negocio.registrarNuevaCategoria(idActor, nuevaCategoria));
        assertTrue(daoFactory.getCategoriaDAOFalso().getCategorias().isEmpty());
        assertTrue(daoFactory.getAuditoriaDAOFalso().getRegistros().isEmpty());
    }

    @Test
    @DisplayName("Un rol que ya terminó no da permiso, ni tampoco un usuario sin rol")
    void rolCerradoOSinRolNoGestiona() {
        var idRectorAnterior = daoFactory.registrarUsuarioConRolCerrado(Rol.RECTOR);
        var idSinRol = UtilId.generarId();
        var nuevaCategoria = datos(NOMBRE, DESCRIPCION);

        assertRechazo(CatalogoMensajes.CategoriaNegocioImpl.ACTOR_NO_PUEDE_GESTIONAR_CATEGORIAS,
                () -> negocio.registrarNuevaCategoria(idRectorAnterior, nuevaCategoria));
        assertRechazo(CatalogoMensajes.CategoriaNegocioImpl.ACTOR_NO_PUEDE_GESTIONAR_CATEGORIAS,
                () -> negocio.consultarTodas(idSinRol));
    }

    @Test
    @DisplayName("El nombre y la descripción son obligatorios y respetan su longitud (P-CAT-001)")
    void validaDatos() {
        assertRechazo(CatalogoMensajes.CategoriaNegocioImpl.DATOS_CATEGORIA_OBLIGATORIOS,
                () -> negocio.registrarNuevaCategoria(idRector, null));
        assertRechazo(CatalogoMensajes.CategoriaNegocioImpl.NOMBRE_CATEGORIA_OBLIGATORIO,
                () -> negocio.registrarNuevaCategoria(idRector, datos("   ", DESCRIPCION)));
        assertRechazo(CatalogoMensajes.CategoriaNegocioImpl.NOMBRE_CATEGORIA_LONGITUD_INVALIDA,
                () -> negocio.registrarNuevaCategoria(idRector, datos("Li", DESCRIPCION)));
        assertRechazo(CatalogoMensajes.CategoriaNegocioImpl.NOMBRE_CATEGORIA_LONGITUD_INVALIDA,
                () -> negocio.registrarNuevaCategoria(idRector, datos("L".repeat(51), DESCRIPCION)));
        assertRechazo(CatalogoMensajes.CategoriaNegocioImpl.DESCRIPCION_CATEGORIA_OBLIGATORIA,
                () -> negocio.registrarNuevaCategoria(idRector, datos(NOMBRE, "")));
        assertRechazo(CatalogoMensajes.CategoriaNegocioImpl.DESCRIPCION_CATEGORIA_LONGITUD_INVALIDA,
                () -> negocio.registrarNuevaCategoria(idRector, datos(NOMBRE, "Corta")));
        assertRechazo(CatalogoMensajes.CategoriaNegocioImpl.DESCRIPCION_CATEGORIA_LONGITUD_INVALIDA,
                () -> negocio.registrarNuevaCategoria(idRector, datos(NOMBRE, "D".repeat(251))));
        assertTrue(daoFactory.getCategoriaDAOFalso().getCategorias().isEmpty());
    }

    @Test
    @DisplayName("No pueden existir dos categorías con el mismo nombre (P-CAT-002, UQ-CAT-1)")
    void nombreUnico() {
        crearCategoria(NOMBRE);
        var repetida = datos("liderazgo", DESCRIPCION);

        assertRechazo(CatalogoMensajes.CategoriaNegocioImpl.NOMBRE_CATEGORIA_YA_EXISTE,
                () -> negocio.registrarNuevaCategoria(idRector, repetida));
        assertEquals(1, daoFactory.getCategoriaDAOFalso().getCategorias().size());
    }

    @Test
    @DisplayName("Modificar cambia nombre y descripción; puede conservar su propio nombre (RF-042)")
    void modificar() {
        var idCategoria = crearCategoria(NOMBRE);
        var otraId = crearCategoria("Trabajo en equipo");

        negocio.modificarCategoria(idRector, idCategoria, datos(NOMBRE, "Reconoce el liderazgo positivo en la comunidad."));
        negocio.modificarCategoria(idRector, otraId, datos("Colaboración", DESCRIPCION));

        var modificada = daoFactory.getCategoriaDAOFalso().consultarPorId(otraId);
        assertEquals("Colaboración", modificada.getNombre());
        assertEquals("Reconoce el liderazgo positivo en la comunidad.", daoFactory.getCategoriaDAOFalso().consultarPorId(idCategoria).getDescripcion());
        var ultimaAuditoria = daoFactory.getAuditoriaDAOFalso().getRegistros().getLast();
        assertEquals("CategoriaModificada", ultimaAuditoria.getAccion());
        assertEquals("Trabajo en equipo", ultimaAuditoria.getEstadoAnterior());
        assertEquals("Colaboración", ultimaAuditoria.getEstadoNuevo());
    }

    @Test
    @DisplayName("Modificar rechaza un nombre que ya usa otra categoría y una categoría inexistente")
    void modificarRechazos() {
        crearCategoria(NOMBRE);
        var otraId = crearCategoria("Trabajo en equipo");
        var conNombreRepetido = datos(NOMBRE, DESCRIPCION);
        var idInexistente = UtilId.generarId();

        assertRechazo(CatalogoMensajes.CategoriaNegocioImpl.NOMBRE_CATEGORIA_YA_EXISTE,
                () -> negocio.modificarCategoria(idRector, otraId, conNombreRepetido));
        assertRechazo(CatalogoMensajes.CategoriaNegocioImpl.CATEGORIA_NO_EXISTE,
                () -> negocio.modificarCategoria(idRector, idInexistente, conNombreRepetido));
    }

    @Test
    @DisplayName("Desactivar y activar cambian el estado una sola vez y quedan auditados (RN-142, RN-143)")
    void activarYDesactivar() {
        var idCategoria = crearCategoria(NOMBRE);

        negocio.desactivarCategoria(idRector, idCategoria);
        assertFalse(daoFactory.getCategoriaDAOFalso().consultarPorId(idCategoria).isActiva());
        assertThrows(KudosExcepcion.class, () -> negocio.desactivarCategoria(idRector, idCategoria));

        negocio.activarCategoria(idRector, idCategoria);
        assertTrue(daoFactory.getCategoriaDAOFalso().consultarPorId(idCategoria).isActiva());
        assertThrows(KudosExcepcion.class, () -> negocio.activarCategoria(idRector, idCategoria));

        var registros = daoFactory.getAuditoriaDAOFalso().getRegistros();
        assertEquals("CategoriaDesactivada", registros.get(1).getAccion());
        assertEquals("CategoriaActivada", registros.get(2).getAccion());
        assertEquals(3, registros.size());
    }

    @Test
    @DisplayName("Activar o desactivar una categoría inexistente se rechaza")
    void activarInexistente() {
        var idInexistente = UtilId.generarId();

        assertRechazo(CatalogoMensajes.CategoriaNegocioImpl.CATEGORIA_NO_EXISTE,
                () -> negocio.activarCategoria(idRector, idInexistente));
        assertRechazo(CatalogoMensajes.CategoriaNegocioImpl.CATEGORIA_NO_EXISTE,
                () -> negocio.desactivarCategoria(idRector, idInexistente));
    }

    @Test
    @DisplayName("Todos consultan las activas; solo los gestores consultan también las inactivas (P-CAT-008)")
    void consultar() {
        crearCategoria(NOMBRE);
        var idInactiva = crearCategoria("Trabajo en equipo");
        negocio.desactivarCategoria(idRector, idInactiva);
        var idEstudiante = daoFactory.registrarUsuarioConRol(Rol.ESTUDIANTE);

        assertEquals(1, negocio.consultarActivas().size());
        assertEquals(2, negocio.consultarTodas(idRector).size());
        assertRechazo(CatalogoMensajes.CategoriaNegocioImpl.ACTOR_NO_PUEDE_GESTIONAR_CATEGORIAS,
                () -> negocio.consultarTodas(idEstudiante));
    }
}
