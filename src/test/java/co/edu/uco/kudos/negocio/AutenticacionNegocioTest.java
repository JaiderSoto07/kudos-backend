package co.edu.uco.kudos.negocio;

import co.edu.uco.kudos.dominio.EstadoCuenta;
import co.edu.uco.kudos.dominio.Rol;
import co.edu.uco.kudos.negocio.falsos.DAOFactoryFalsa;
import co.edu.uco.kudos.negocio.negocio.AutenticacionNegocio;
import co.edu.uco.kudos.negocio.negocio.impl.AutenticacionNegocioImpl;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosAutorizacionExcepcion;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Negocio de autenticación: usuario de Google frente a la cuenta en UCO Kudos (RN-084 a RN-089, POL-003)")
class AutenticacionNegocioTest {

    private static final String CORREO = "ana.perez@gmail.com";

    private DAOFactoryFalsa daoFactory;
    private AutenticacionNegocio negocio;

    @BeforeEach
    void preparar() {
        daoFactory = new DAOFactoryFalsa();
        negocio = new AutenticacionNegocioImpl(daoFactory);
    }

    @Test
    @DisplayName("Una cuenta activa obtiene su id de actor y su rol vigente")
    void cuentaActiva() {
        var idUsuario = daoFactory.registrarCuenta(CORREO, EstadoCuenta.ACTIVA, Rol.ADMINISTRADOR);

        assertEquals(idUsuario, negocio.obtenerIdActorActivo(CORREO));
        assertEquals(Rol.ADMINISTRADOR, negocio.consultarRolVigente(idUsuario));
    }

    @Test
    @DisplayName("El correo de Google se compara sin importar mayúsculas ni espacios")
    void correoNormalizado() {
        var idUsuario = daoFactory.registrarCuenta(CORREO, EstadoCuenta.ACTIVA, Rol.DOCENTE);

        assertEquals(idUsuario, negocio.obtenerIdActorActivo("  Ana.Perez@GMAIL.com "));
    }

    @Test
    @DisplayName("Un correo que no está registrado se rechaza con 403 e invita a registrarse")
    void noRegistrado() {
        var excepcion = assertThrows(KudosAutorizacionExcepcion.class, () -> negocio.obtenerIdActorActivo("otra@gmail.com"));

        assertEquals(CatalogoMensajes.AutenticacionNegocioImpl.USUARIO_NO_REGISTRADO, excepcion.getMensajeUsuario());
        assertTrue(UtilId.esValorDefecto(negocio.consultarUsuarioPorCorreo("otra@gmail.com").getId()));
        assertTrue(UtilId.esValorDefecto(negocio.consultarUsuarioPorCorreo(null).getId()));
    }

    @ParameterizedTest(name = "Cuenta {0}: se rechaza indicando lo que falta")
    @CsvSource({
            "PENDIENTE_APROBACION, '" + CatalogoMensajes.Dominio.CUENTA_PENDIENTE_APROBACION + "'",
            "PENDIENTE_VERIFICACION, '" + CatalogoMensajes.Dominio.CUENTA_FALTA_VERIFICAR_CELULAR + "'",
            "SUSPENDIDA, '" + CatalogoMensajes.Dominio.CUENTA_SUSPENDIDA + "'",
            "DESACTIVADA, '" + CatalogoMensajes.Dominio.CUENTA_DESACTIVADA + "'"
    })
    @DisplayName("Una cuenta que no está activa no puede actuar y recibe el motivo (RN-089)")
    void cuentaNoActiva(EstadoCuenta estadoCuenta, String mensajeEsperado) {
        daoFactory.registrarCuenta(CORREO, estadoCuenta, Rol.ESTUDIANTE);

        var excepcion = assertThrows(KudosAutorizacionExcepcion.class, () -> negocio.obtenerIdActorActivo(CORREO));

        assertEquals(mensajeEsperado, excepcion.getMensajeUsuario());
    }

    @Test
    @DisplayName("Sin una asignación de rol vigente no hay rol")
    void sinRolVigente() {
        var idRolCerrado = daoFactory.registrarUsuarioConRolCerrado(Rol.RECTOR);

        assertNull(negocio.consultarRolVigente(idRolCerrado));
        assertNull(negocio.consultarRolVigente(UtilId.generarId()));
    }
}
