package co.edu.uco.kudos.negocio;

import co.edu.uco.kudos.dominio.ParametroGlobalDominio;
import co.edu.uco.kudos.dominio.Rol;
import co.edu.uco.kudos.dominio.UsuarioDominio;
import co.edu.uco.kudos.negocio.falsos.DAOFactoryFalsa;
import co.edu.uco.kudos.negocio.negocio.AutoridadNegocio;
import co.edu.uco.kudos.negocio.negocio.impl.AutoridadNegocioImpl;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosAutorizacionExcepcion;
import co.edu.uco.kudos.transversal.excepciones.KudosExcepcion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Negocio de autoridades: el Administrador registra al Rector y a otros Administradores (RF-039, RF-040, RN-094, RN-095)")
class AutoridadNegocioTest {

    private DAOFactoryFalsa daoFactory;
    private AutoridadNegocio negocio;
    private UUID administrador;

    @BeforeEach
    void preparar() {
        daoFactory = new DAOFactoryFalsa();
        daoFactory.registrarParametro(ParametroGlobalDominio.DOMINIO_CORREO, "gmail.com");
        daoFactory.registrarParametro(ParametroGlobalDominio.VIGENCIA_CODIGO_MINUTOS, "15");
        administrador = daoFactory.registrarAutoridad(Rol.ADMINISTRADOR, null, null);
        negocio = new AutoridadNegocioImpl(daoFactory);
    }

    private UsuarioDominio datos(String correo, String celular) {
        return new UsuarioDominio.Builder()
                .nombres("María Fernanda")
                .apellidos("Gómez")
                .correoInstitucional(correo)
                .numeroCelular(celular)
                .build();
    }

    private void assertRechazo(Class<? extends KudosExcepcion> tipo, String mensaje, Executable operacion) {
        var excepcion = assertThrows(tipo, operacion);
        assertEquals(mensaje, excepcion.getMensajeUsuario());
    }

    @Test
    @DisplayName("El Rector queda aprobado con su rol; le falta entrar con Google y verificar el celular")
    void registrarRector() {
        negocio.registrarAutoridad(administrador, "RECTOR", datos(" Rectora@Gmail.com ", "3001112233"));

        var rectora = daoFactory.getUsuarioDAOFalso().consultarTodos().getLast();
        assertEquals("rectora@gmail.com", rectora.getCorreoInstitucional());
        assertTrue(rectora.isAprobada());
        assertFalse(rectora.isCorreoVerificado());
        assertFalse(rectora.isCelularVerificado());
        assertEquals("PENDIENTE_VERIFICACION", rectora.getEstadoCuenta());
        assertEquals("RECTOR", daoFactory.getAsignacionRolDAOFalso().getAsignaciones().getLast().getRol());
        assertEquals("3001112233", daoFactory.getCodigoVerificacionDAOFalso().getCodigos().getFirst().getDestino());
        assertEquals("AutoridadRegistrada", daoFactory.getAuditoriaDAOFalso().getRegistros().getLast().getAccion());
    }

    @Test
    @DisplayName("No puede haber dos Rectores vigentes, pero sí varios Administradores (INV-001)")
    void rectorUnico() {
        negocio.registrarAutoridad(administrador, "RECTOR", datos("rectora@gmail.com", "3001112233"));
        var segundo = datos("otro.rector@gmail.com", "3001112244");

        assertRechazo(KudosExcepcion.class, CatalogoMensajes.Aprobacion.RECTOR_YA_EXISTE,
                () -> negocio.registrarAutoridad(administrador, "RECTOR", segundo));

        negocio.registrarAutoridad(administrador, "ADMINISTRADOR", datos("admin2@gmail.com", "3001112255"));
        assertEquals("ADMINISTRADOR", daoFactory.getAsignacionRolDAOFalso().getAsignaciones().getLast().getRol());
    }

    @Test
    @DisplayName("Solo el Administrador registra autoridades, solo Rector o Administrador, y con datos válidos")
    void rechazos() {
        var rector = daoFactory.registrarAutoridad(Rol.RECTOR, null, null);
        var valido = datos("nuevo@gmail.com", "3001112233");
        var correoAjeno = datos("nuevo@hotmail.com", "3001112233");

        assertRechazo(KudosAutorizacionExcepcion.class, CatalogoMensajes.Aprobacion.ACTOR_NO_REGISTRA_AUTORIDADES,
                () -> negocio.registrarAutoridad(rector, "ADMINISTRADOR", valido));
        assertRechazo(KudosExcepcion.class, CatalogoMensajes.Aprobacion.ROL_NO_ES_AUTORIDAD,
                () -> negocio.registrarAutoridad(administrador, "DOCENTE", valido));
        assertThrows(KudosExcepcion.class, () -> negocio.registrarAutoridad(administrador, "ADMINISTRADOR", correoAjeno));
    }
}
