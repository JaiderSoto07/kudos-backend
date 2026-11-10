package co.edu.uco.kudos.negocio;

import co.edu.uco.kudos.dominio.EstadoCuenta;
import co.edu.uco.kudos.dominio.ParametroGlobalDominio;
import co.edu.uco.kudos.entidad.CodigoVerificacionEntidad;
import co.edu.uco.kudos.entidad.UsuarioEntidad;
import co.edu.uco.kudos.negocio.falsos.DAOFactoryFalsa;
import co.edu.uco.kudos.negocio.negocio.VerificacionNegocio;
import co.edu.uco.kudos.negocio.negocio.assembler.impl.UsuarioEntidadAssembler;
import co.edu.uco.kudos.negocio.negocio.impl.VerificacionNegocioImpl;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosExcepcion;
import co.edu.uco.kudos.transversal.utilitario.UtilFecha;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Negocio de verificación del celular con código simulado (CU-017, RF-007, RF-080, D-10)")
class VerificacionNegocioTest {

    private static final String CELULAR = "3001234567";
    private static final String CODIGO_ERRADO = "000000x";

    private DAOFactoryFalsa daoFactory;
    private VerificacionNegocio negocio;

    @BeforeEach
    void preparar() {
        daoFactory = new DAOFactoryFalsa();
        daoFactory.registrarParametro(ParametroGlobalDominio.VIGENCIA_CODIGO_MINUTOS, "15");
        daoFactory.registrarParametro(ParametroGlobalDominio.MAX_INTENTOS_VERIFICACION, "5");
        negocio = new VerificacionNegocioImpl(daoFactory);
    }

    private UUID registrarUsuario(EstadoCuenta estado, boolean aprobada, boolean celularVerificado) {
        var idUsuario = UtilId.generarId();
        daoFactory.getUsuarioDAOFalso().crear(new UsuarioEntidad.Builder()
                .id(idUsuario)
                .nombres("Ana")
                .apellidos("Pérez")
                .correoInstitucional("ana.perez@gmail.com")
                .correoVerificado(true)
                .numeroCelular(CELULAR)
                .celularVerificado(celularVerificado)
                .aprobada(aprobada)
                .estadoCuenta(estado.name())
                .build());
        return idUsuario;
    }

    private UUID registrarUsuarioConCodigo(EstadoCuenta estado, boolean aprobada) {
        var idUsuario = registrarUsuario(estado, aprobada, false);
        var usuario = UsuarioEntidadAssembler.getInstance().convertirADominio(daoFactory.getUsuarioDAOFalso().consultarPorId(idUsuario));
        negocio.enviarCodigoCelular(usuario);
        return idUsuario;
    }

    private CodigoVerificacionEntidad ultimoCodigo() {
        return daoFactory.getCodigoVerificacionDAOFalso().getCodigos().getFirst();
    }

    private UsuarioEntidad usuario(UUID idUsuario) {
        return daoFactory.getUsuarioDAOFalso().consultarPorId(idUsuario);
    }

    @Test
    @DisplayName("Con el código correcto el celular queda verificado, el código usado y se audita")
    void codigoCorrecto() {
        var idUsuario = registrarUsuarioConCodigo(EstadoCuenta.PENDIENTE_APROBACION, false);

        assertTrue(negocio.verificarCelular(idUsuario, " " + ultimoCodigo().getCodigo() + " "));

        assertTrue(usuario(idUsuario).isCelularVerificado());
        assertEquals("PENDIENTE_APROBACION", usuario(idUsuario).getEstadoCuenta());
        assertTrue(ultimoCodigo().isUsado());
        assertEquals("CelularVerificado", daoFactory.getAuditoriaDAOFalso().getRegistros().getLast().getAccion());
    }

    @Test
    @DisplayName("Si ya estaba aprobada y con correo verificado, verificar el celular activa la cuenta (RN-084 a RN-089)")
    void verificarActivaLaCuenta() {
        var idUsuario = registrarUsuarioConCodigo(EstadoCuenta.PENDIENTE_VERIFICACION, true);

        negocio.verificarCelular(idUsuario, ultimoCodigo().getCodigo());

        assertEquals("ACTIVA", usuario(idUsuario).getEstadoCuenta());
    }

    @Test
    @DisplayName("Un código errado suma un intento fallido sin verificar el celular")
    void codigoErrado() {
        var idUsuario = registrarUsuarioConCodigo(EstadoCuenta.PENDIENTE_APROBACION, false);

        assertFalse(negocio.verificarCelular(idUsuario, CODIGO_ERRADO));

        assertEquals(1, ultimoCodigo().getIntentosFallidos());
        assertFalse(ultimoCodigo().isUsado());
        assertFalse(usuario(idUsuario).isCelularVerificado());
    }

    @Test
    @DisplayName("Tras 5 intentos fallidos el código ya no sirve, aunque se escriba bien (RN-080)")
    void intentosAgotados() {
        var idUsuario = registrarUsuarioConCodigo(EstadoCuenta.PENDIENTE_APROBACION, false);
        for (var intento = 0; intento < 5; intento++) {
            negocio.verificarCelular(idUsuario, CODIGO_ERRADO);
        }
        var codigoCorrecto = ultimoCodigo().getCodigo();

        var excepcion = assertThrows(KudosExcepcion.class, () -> negocio.verificarCelular(idUsuario, codigoCorrecto));

        assertEquals(CatalogoMensajes.Identidad.CODIGO_INTENTOS_AGOTADOS, excepcion.getMensajeUsuario());
    }

    @Test
    @DisplayName("Un código vencido se rechaza (15 minutos de vigencia)")
    void codigoVencido() {
        var idUsuario = registrarUsuarioConCodigo(EstadoCuenta.PENDIENTE_APROBACION, false);
        var codigo = ultimoCodigo();
        var vencido = new CodigoVerificacionEntidad.Builder()
                .id(codigo.getId())
                .usuario(codigo.getUsuario())
                .canal(codigo.getCanal())
                .destino(codigo.getDestino())
                .codigo(codigo.getCodigo())
                .fechaGeneracion(UtilFecha.fechaActual().minusMinutes(20))
                .fechaExpiracion(UtilFecha.fechaActual().minusMinutes(5))
                .build();
        daoFactory.getCodigoVerificacionDAOFalso().actualizar(codigo.getId(), vencido);

        var excepcion = assertThrows(KudosExcepcion.class, () -> negocio.verificarCelular(idUsuario, codigo.getCodigo()));

        assertEquals(CatalogoMensajes.Identidad.CODIGO_VENCIDO, excepcion.getMensajeUsuario());
    }

    @Test
    @DisplayName("Reenviar invalida el código anterior y genera uno nuevo que sí funciona (RF-080)")
    void reenviarCodigo() {
        var idUsuario = registrarUsuarioConCodigo(EstadoCuenta.PENDIENTE_APROBACION, false);
        var codigoAnterior = ultimoCodigo();

        negocio.reenviarCodigoCelular(idUsuario);

        var codigos = daoFactory.getCodigoVerificacionDAOFalso().getCodigos();
        assertEquals(2, codigos.size());
        assertTrue(daoFactory.getCodigoVerificacionDAOFalso().consultarPorId(codigoAnterior.getId()).isUsado());
        assertNotEquals(codigoAnterior.getId(), ultimoCodigo().getId());
        assertEquals(2, daoFactory.getNotificacionDAOFalso().getNotificaciones().size());
        assertTrue(negocio.verificarCelular(idUsuario, ultimoCodigo().getCodigo()));
    }

    @Test
    @DisplayName("No se reenvía código si el celular ya está verificado, y sin código pendiente no se puede verificar")
    void sinCodigoPendiente() {
        var idUsuario = registrarUsuario(EstadoCuenta.ACTIVA, true, true);

        var yaVerificado = assertThrows(KudosExcepcion.class, () -> negocio.reenviarCodigoCelular(idUsuario));
        var sinCodigo = assertThrows(KudosExcepcion.class, () -> negocio.verificarCelular(idUsuario, "123456"));

        assertEquals(CatalogoMensajes.Identidad.CELULAR_YA_VERIFICADO, yaVerificado.getMensajeUsuario());
        assertEquals(CatalogoMensajes.Identidad.CODIGO_NO_SOLICITADO, sinCodigo.getMensajeUsuario());
    }
}
