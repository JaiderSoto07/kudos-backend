package co.edu.uco.kudos.dominio;

import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosExcepcion;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("UsuarioDominio: aprobación, activación y estado de la cuenta (RN-084 a RN-111, D-07, D-17)")
class UsuarioDominioTest {

    private UsuarioDominio usuarioNuevo() {
        return new UsuarioDominio.Builder().nombres("Ana").apellidos("Pérez").correoInstitucional("ana@uco.edu.co")
                .numeroCelular("3001234567").build();
    }

    private UsuarioDominio usuarioActivo() {
        var usuario = usuarioNuevo();
        usuario.verificarCorreo();
        usuario.verificarCelular();
        usuario.aprobar();
        return usuario;
    }

    @Test
    @DisplayName("Un usuario nuevo queda pendiente de aprobación")
    void usuarioNuevoPendiente() {
        assertEquals(EstadoCuenta.PENDIENTE_APROBACION, usuarioNuevo().getEstadoCuenta());
    }

    @Test
    @DisplayName("Aprobada no significa activa: falta verificar correo y celular (RN-084)")
    void aprobadaNoEsActiva() {
        var usuario = usuarioNuevo();
        usuario.aprobar();
        assertEquals(EstadoCuenta.PENDIENTE_VERIFICACION, usuario.getEstadoCuenta());
        assertFalse(usuario.estaActiva());
    }

    @Test
    @DisplayName("Con aprobación, correo y celular verificados la cuenta queda ACTIVA (RN-085 a RN-088)")
    void activaConLasTresCondiciones() {
        var usuario = usuarioNuevo();
        usuario.aprobar();
        usuario.verificarCorreo();
        assertFalse(usuario.estaActiva());
        usuario.verificarCelular();
        assertTrue(usuario.estaActiva());
    }

    @Test
    @DisplayName("Verificar antes de ser aprobado no activa la cuenta")
    void verificarSinAprobacionNoActiva() {
        var usuario = usuarioNuevo();
        usuario.verificarCorreo();
        usuario.verificarCelular();
        assertEquals(EstadoCuenta.PENDIENTE_APROBACION, usuario.getEstadoCuenta());
        usuario.aprobar();
        assertTrue(usuario.estaActiva());
    }

    @Test
    @DisplayName("No se aprueba dos veces")
    void noSeApruebaDosVeces() {
        var usuario = usuarioActivo();
        assertThrows(KudosExcepcion.class, usuario::aprobar);
    }

    @Test
    @DisplayName("Cambiar el correo exige verificarlo otra vez (RN-081, RN-110)")
    void cambiarCorreo() {
        var usuario = usuarioActivo();
        usuario.cambiarCorreo(" NUEVO@uco.edu.co ");
        assertEquals("nuevo@uco.edu.co", usuario.getCorreoInstitucional());
        assertFalse(usuario.isCorreoVerificado());
        assertEquals(EstadoCuenta.PENDIENTE_VERIFICACION, usuario.getEstadoCuenta());
        usuario.verificarCorreo();
        assertTrue(usuario.estaActiva());
    }

    @Test
    @DisplayName("Cambiar el celular exige verificarlo otra vez (RN-082, RN-111)")
    void cambiarCelular() {
        var usuario = usuarioActivo();
        usuario.cambiarCelular("3119998877");
        assertFalse(usuario.isCelularVerificado());
        assertFalse(usuario.estaActiva());
    }

    @Test
    @DisplayName("Suspender y reactivar devuelve la cuenta a ACTIVA (D-07)")
    void suspenderYReactivar() {
        var usuario = usuarioActivo();
        usuario.suspender();
        assertEquals(EstadoCuenta.SUSPENDIDA, usuario.getEstadoCuenta());
        assertThrows(KudosExcepcion.class, usuario::suspender);
        usuario.reactivar();
        assertTrue(usuario.estaActiva());
        assertThrows(KudosExcepcion.class, usuario::reactivar);
    }

    @Test
    @DisplayName("Reactivar una cuenta sin aprobar la deja pendiente de aprobación")
    void reactivarSinAprobacion() {
        var usuario = usuarioNuevo();
        usuario.suspender();
        usuario.reactivar();
        assertEquals(EstadoCuenta.PENDIENTE_APROBACION, usuario.getEstadoCuenta());
    }

    @Test
    @DisplayName("Reactivar una cuenta aprobada sin verificar la deja pendiente de verificación")
    void reactivarSinVerificar() {
        var usuario = usuarioNuevo();
        usuario.aprobar();
        usuario.suspender();
        usuario.reactivar();
        assertEquals(EstadoCuenta.PENDIENTE_VERIFICACION, usuario.getEstadoCuenta());
    }

    @Test
    @DisplayName("Desactivar es definitivo (D-07)")
    void desactivar() {
        var usuario = usuarioActivo();
        usuario.desactivar();
        assertEquals(EstadoCuenta.DESACTIVADA, usuario.getEstadoCuenta());
        assertThrows(KudosExcepcion.class, usuario::desactivar);
        assertThrows(KudosExcepcion.class, usuario::suspender);
    }

    @Test
    @DisplayName("Actualizar los datos personales quita los espacios")
    void actualizarDatosPersonales() {
        var usuario = usuarioActivo();
        usuario.actualizarDatosPersonales(" Carlos ", " Restrepo ");
        assertEquals("Carlos Restrepo", usuario.nombreCompleto());
    }

    @Test
    @DisplayName("El Builder pone valores por defecto como en Sigma")
    void valoresPorDefecto() {
        var usuario = new UsuarioDominio.Builder().nombres(null).build();
        assertEquals("", usuario.getNombres());
        assertEquals(EstadoCuenta.PENDIENTE_APROBACION, usuario.getEstadoCuenta());
    }

    @Test
    @DisplayName("La cuenta dice qué le falta para ingresar según su estado (RN-089, P-USU-017)")
    void motivoParaNoIngresar() {
        var pendiente = usuarioNuevo();
        assertEquals(CatalogoMensajes.Dominio.CUENTA_PENDIENTE_APROBACION, pendiente.motivoParaNoIngresar());

        pendiente.aprobar();
        assertEquals(CatalogoMensajes.Dominio.CUENTA_FALTA_VERIFICAR_CORREO_Y_CELULAR, pendiente.motivoParaNoIngresar());

        pendiente.verificarCelular();
        assertEquals(CatalogoMensajes.Dominio.CUENTA_FALTA_VERIFICAR_CORREO, pendiente.motivoParaNoIngresar());

        var faltaCelular = usuarioNuevo();
        faltaCelular.aprobar();
        faltaCelular.verificarCorreo();
        assertEquals(CatalogoMensajes.Dominio.CUENTA_FALTA_VERIFICAR_CELULAR, faltaCelular.motivoParaNoIngresar());

        var activo = usuarioActivo();
        assertEquals("", activo.motivoParaNoIngresar());

        activo.suspender();
        assertEquals(CatalogoMensajes.Dominio.CUENTA_SUSPENDIDA, activo.motivoParaNoIngresar());

        activo.desactivar();
        assertEquals(CatalogoMensajes.Dominio.CUENTA_DESACTIVADA, activo.motivoParaNoIngresar());
    }
}
