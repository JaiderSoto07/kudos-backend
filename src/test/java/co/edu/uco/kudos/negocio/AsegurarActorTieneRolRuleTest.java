package co.edu.uco.kudos.negocio;

import co.edu.uco.kudos.dominio.Rol;
import co.edu.uco.kudos.negocio.falsos.DAOFactoryFalsa;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.autorizacion.AsegurarActorTieneRolRule;
import co.edu.uco.kudos.transversal.excepciones.KudosAutorizacionExcepcion;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Regla de permisos por rol vigente (RNF-002, POL-005)")
class AsegurarActorTieneRolRuleTest {

    private static final List<Rol> MODERADORES = List.of(Rol.RECTOR, Rol.COORDINADOR_ACADEMICO);
    private static final String MENSAJE = "No tiene permiso para moderar contenido.";

    private DAOFactoryFalsa daoFactory;
    private AsegurarActorTieneRolRule regla;

    @BeforeEach
    void preparar() {
        daoFactory = new DAOFactoryFalsa();
        regla = AsegurarActorTieneRolRule.obtenerInstancia();
    }

    private void assertSinPermiso(UUID idActor) {
        var excepcion = assertThrows(KudosAutorizacionExcepcion.class,
                () -> regla.ejecutar(idActor, daoFactory, MODERADORES, MENSAJE));
        assertEquals(MENSAJE, excepcion.getMensajeUsuario());
    }

    @ParameterizedTest(name = "{0} tiene permiso")
    @EnumSource(value = Rol.class, names = {"RECTOR", "COORDINADOR_ACADEMICO"})
    @DisplayName("Un rol vigente que está en la lista tiene permiso")
    void rolPermitido(Rol rol) {
        var idActor = daoFactory.registrarUsuarioConRol(rol);

        assertDoesNotThrow(() -> regla.ejecutar(idActor, daoFactory, MODERADORES, MENSAJE));
    }

    @ParameterizedTest(name = "{0} no tiene permiso")
    @EnumSource(value = Rol.class, names = {"ADMINISTRADOR", "COORDINADOR_FACULTAD", "COORDINADOR_PROGRAMA", "DOCENTE", "ESTUDIANTE"})
    @DisplayName("Un rol que no está en la lista se rechaza con 403 y el mensaje recibido")
    void rolNoPermitido(Rol rol) {
        assertSinPermiso(daoFactory.registrarUsuarioConRol(rol));
    }

    @Test
    @DisplayName("Un rol que ya terminó no da permiso")
    void rolCerrado() {
        assertSinPermiso(daoFactory.registrarUsuarioConRolCerrado(Rol.RECTOR));
    }

    @Test
    @DisplayName("Un usuario sin ningún rol no tiene permiso")
    void sinRol() {
        assertSinPermiso(UtilId.generarId());
    }
}
