package co.edu.uco.kudos.negocio;

import co.edu.uco.kudos.dominio.CanalVerificacion;
import co.edu.uco.kudos.dominio.CodigoVerificacionDominio;
import co.edu.uco.kudos.dominio.EstadoFacultad;
import co.edu.uco.kudos.dominio.ParametroGlobalDominio;
import co.edu.uco.kudos.dominio.Rol;
import co.edu.uco.kudos.negocio.falsos.DAOFactoryFalsa;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.identidad.AsegurarAmbitoSegunRolRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.identidad.AsegurarCelularNoRepetidoRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.identidad.AsegurarCodigoVigenteRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.identidad.AsegurarCorreoNoRepetidoRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.identidad.AsegurarRolAutorregistrableRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.identidad.ValidarCelularRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.identidad.ValidarCorreoInstitucionalRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.identidad.ValidarDatosPersonalesRule;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosExcepcion;
import co.edu.uco.kudos.transversal.excepciones.enums.Capa;
import co.edu.uco.kudos.transversal.utilitario.UtilFecha;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Reglas de identidad: registro, contacto y verificación (RF-001 a RF-011)")
class ReglasIdentidadTest {

    private static final String CORREO = "ana.perez@gmail.com";
    private static final String CELULAR = "3001234567";
    private static final int MAXIMO_INTENTOS = 5;

    private DAOFactoryFalsa daoFactory;

    @BeforeEach
    void preparar() {
        daoFactory = new DAOFactoryFalsa();
        daoFactory.registrarParametro(ParametroGlobalDominio.DOMINIO_CORREO, "gmail.com, uco.net.co");
    }

    private void assertRechazo(String mensajeEsperado, Executable operacion) {
        var excepcion = assertThrows(KudosExcepcion.class, operacion);
        assertEquals(mensajeEsperado, excepcion.getMensajeUsuario());
        assertEquals(Capa.NEGOCIO, excepcion.getCapa());
    }

    @ParameterizedTest(name = "\"{0}\" es un correo permitido")
    @ValueSource(strings = {"ana.perez@gmail.com", "ANA.PEREZ@UCO.NET.CO", "  ana_perez1@uco.net.co  "})
    @DisplayName("Acepta correos de los dominios del parámetro DOMINIO_CORREO, sin importar mayúsculas ni espacios (RN-069)")
    void correoPermitido(String correo) {
        assertDoesNotThrow(() -> ValidarCorreoInstitucionalRule.obtenerInstancia().ejecutar(correo, daoFactory));
    }

    @Test
    @DisplayName("El correo es obligatorio")
    void correoVacio() {
        assertRechazo(CatalogoMensajes.Identidad.CORREO_OBLIGATORIO,
                () -> ValidarCorreoInstitucionalRule.obtenerInstancia().ejecutar("   ", daoFactory));
    }

    @ParameterizedTest(name = "\"{0}\" no tiene formato de correo")
    @ValueSource(strings = {"ana.perez", "ana@", "@gmail.com", "ana perez@gmail.com"})
    @DisplayName("Rechaza textos que no tienen formato de correo")
    void correoMalFormado(String correo) {
        assertRechazo(CatalogoMensajes.Identidad.CORREO_FORMATO_INVALIDO,
                () -> ValidarCorreoInstitucionalRule.obtenerInstancia().ejecutar(correo, daoFactory));
    }

    @Test
    @DisplayName("Rechaza un dominio que no está en el parámetro y muestra los permitidos (RN-071, POL-001)")
    void correoDeOtroDominio() {
        var mensaje = String.format(CatalogoMensajes.Identidad.CORREO_DOMINIO_NO_PERMITIDO, "gmail.com, uco.net.co");
        assertRechazo(mensaje, () -> ValidarCorreoInstitucionalRule.obtenerInstancia().ejecutar("ana@hotmail.com", daoFactory));
    }

    @ParameterizedTest(name = "\"{0}\" es un celular colombiano válido")
    @ValueSource(strings = {"3001234567", " 3209876543 "})
    @DisplayName("Acepta celulares de 10 dígitos que empiezan por 3 (RN-070, D-12)")
    void celularValido(String celular) {
        assertDoesNotThrow(() -> ValidarCelularRule.obtenerInstancia().ejecutar(celular));
    }

    @Test
    @DisplayName("El celular es obligatorio")
    void celularVacio() {
        assertRechazo(CatalogoMensajes.Identidad.CELULAR_OBLIGATORIO, () -> ValidarCelularRule.obtenerInstancia().ejecutar(""));
    }

    @ParameterizedTest(name = "\"{0}\" no es un celular colombiano")
    @ValueSource(strings = {"2001234567", "300123456", "30012345678", "300-123-4567", "300123456a"})
    @DisplayName("Rechaza celulares que no tienen 10 dígitos o no empiezan por 3 (RN-072, POL-002)")
    void celularInvalido(String celular) {
        assertRechazo(CatalogoMensajes.Identidad.CELULAR_FORMATO_INVALIDO, () -> ValidarCelularRule.obtenerInstancia().ejecutar(celular));
    }

    @Test
    @DisplayName("Rechaza un correo que ya usa otra cuenta, aunque cambien las mayúsculas (RN-073)")
    void correoRepetido() {
        daoFactory.registrarUsuarioConContacto(CORREO, CELULAR);

        assertRechazo(CatalogoMensajes.Identidad.CORREO_YA_REGISTRADO,
                () -> AsegurarCorreoNoRepetidoRule.obtenerInstancia().ejecutar("Ana.Perez@gmail.com", UtilId.VALOR_DEFECTO, daoFactory));
    }

    @Test
    @DisplayName("El usuario puede conservar su propio correo al actualizar sus datos")
    void correoPropio() {
        var idUsuario = daoFactory.registrarUsuarioConContacto(CORREO, CELULAR);

        assertDoesNotThrow(() -> AsegurarCorreoNoRepetidoRule.obtenerInstancia().ejecutar(CORREO, idUsuario, daoFactory));
    }

    @Test
    @DisplayName("Rechaza un celular que ya usa otra cuenta (RN-074)")
    void celularRepetido() {
        daoFactory.registrarUsuarioConContacto(CORREO, CELULAR);

        assertRechazo(CatalogoMensajes.Identidad.CELULAR_YA_REGISTRADO,
                () -> AsegurarCelularNoRepetidoRule.obtenerInstancia().ejecutar(CELULAR, UtilId.VALOR_DEFECTO, daoFactory));
    }

    @Test
    @DisplayName("El usuario puede conservar su propio celular y otro celular libre se acepta")
    void celularPropioOLibre() {
        var idUsuario = daoFactory.registrarUsuarioConContacto(CORREO, CELULAR);

        assertDoesNotThrow(() -> AsegurarCelularNoRepetidoRule.obtenerInstancia().ejecutar(CELULAR, idUsuario, daoFactory));
        assertDoesNotThrow(() -> AsegurarCelularNoRepetidoRule.obtenerInstancia().ejecutar("3110000000", UtilId.VALOR_DEFECTO, daoFactory));
    }

    @ParameterizedTest(name = "nombres \"{0}\" y apellidos \"{1}\" se rechazan")
    @CsvSource({
            "'', Pérez, " + CatalogoMensajes.Identidad.NOMBRES_OBLIGATORIOS,
            "Ana, '   ', " + CatalogoMensajes.Identidad.APELLIDOS_OBLIGATORIOS
    })
    @DisplayName("Nombres y apellidos son obligatorios (RN-102)")
    void datosPersonalesVacios(String nombres, String apellidos, String mensaje) {
        assertRechazo(mensaje, () -> ValidarDatosPersonalesRule.obtenerInstancia().ejecutar(nombres, apellidos));
    }

    @Test
    @DisplayName("Nombres y apellidos tienen máximo 60 caracteres")
    void datosPersonalesLargos() {
        var texto61 = "a".repeat(61);

        assertRechazo(CatalogoMensajes.Identidad.NOMBRES_LONGITUD_INVALIDA,
                () -> ValidarDatosPersonalesRule.obtenerInstancia().ejecutar(texto61, "Pérez"));
        assertRechazo(CatalogoMensajes.Identidad.APELLIDOS_LONGITUD_INVALIDA,
                () -> ValidarDatosPersonalesRule.obtenerInstancia().ejecutar("Ana", texto61));
        assertDoesNotThrow(() -> ValidarDatosPersonalesRule.obtenerInstancia().ejecutar("a".repeat(60), "Pérez"));
    }

    @ParameterizedTest(name = "{0} se puede autorregistrar")
    @EnumSource(value = Rol.class, names = {"ESTUDIANTE", "DOCENTE", "COORDINADOR_PROGRAMA", "COORDINADOR_FACULTAD", "COORDINADOR_ACADEMICO"})
    @DisplayName("Estudiante, Docente y los tres coordinadores se autorregistran (D-17)")
    void rolAutorregistrable(Rol rol) {
        assertDoesNotThrow(() -> AsegurarRolAutorregistrableRule.obtenerInstancia().ejecutar(rol.name()));
    }

    @ParameterizedTest(name = "{0} no se puede autorregistrar")
    @EnumSource(value = Rol.class, names = {"RECTOR", "ADMINISTRADOR"})
    @DisplayName("Rector y Administrador no se autorregistran (RN-094, RN-095)")
    void rolNoAutorregistrable(Rol rol) {
        assertRechazo(CatalogoMensajes.Identidad.ROL_NO_AUTORREGISTRABLE, () -> AsegurarRolAutorregistrableRule.obtenerInstancia().ejecutar(rol.name()));
    }

    @Test
    @DisplayName("El rol solicitado es obligatorio y debe existir")
    void rolVacioOInexistente() {
        assertRechazo(CatalogoMensajes.Identidad.ROL_OBLIGATORIO, () -> AsegurarRolAutorregistrableRule.obtenerInstancia().ejecutar("  "));
        assertRechazo(CatalogoMensajes.Identidad.ROL_NO_VALIDO, () -> AsegurarRolAutorregistrableRule.obtenerInstancia().ejecutar("DECANO"));
    }

    @ParameterizedTest(name = "{0} se registra en un Programa activo")
    @EnumSource(value = Rol.class, names = {"ESTUDIANTE", "DOCENTE", "COORDINADOR_PROGRAMA"})
    @DisplayName("Estudiante, Docente y Coordinador de Programa eligen un Programa activo (RN-058, RN-066)")
    void rolesDePrograma(Rol rol) {
        var idPrograma = daoFactory.registrarPrograma(true);
        var idProgramaInactivo = daoFactory.registrarPrograma(false);
        var idInexistente = UtilId.generarId();
        var regla = AsegurarAmbitoSegunRolRule.obtenerInstancia();

        assertDoesNotThrow(() -> regla.ejecutar(rol, null, idPrograma, daoFactory));
        assertRechazo(CatalogoMensajes.Identidad.PROGRAMA_OBLIGATORIO, () -> regla.ejecutar(rol, null, null, daoFactory));
        assertRechazo(CatalogoMensajes.Identidad.PROGRAMA_NO_EXISTE, () -> regla.ejecutar(rol, null, idInexistente, daoFactory));
        assertRechazo(CatalogoMensajes.Identidad.PROGRAMA_INACTIVO, () -> regla.ejecutar(rol, null, idProgramaInactivo, daoFactory));
    }

    @Test
    @DisplayName("El Coordinador de Facultad elige una Facultad activa (D-05)")
    void coordinadorDeFacultad() {
        var idActiva = daoFactory.registrarFacultad(EstadoFacultad.ACTIVA);
        var idEnConformacion = daoFactory.registrarFacultad(EstadoFacultad.EN_CONFORMACION);
        var idInexistente = UtilId.generarId();
        var regla = AsegurarAmbitoSegunRolRule.obtenerInstancia();
        var rol = Rol.COORDINADOR_FACULTAD;

        assertDoesNotThrow(() -> regla.ejecutar(rol, idActiva, null, daoFactory));
        assertRechazo(CatalogoMensajes.Identidad.FACULTAD_OBLIGATORIA, () -> regla.ejecutar(rol, null, null, daoFactory));
        assertRechazo(CatalogoMensajes.Identidad.FACULTAD_NO_EXISTE, () -> regla.ejecutar(rol, idInexistente, null, daoFactory));
        assertRechazo(CatalogoMensajes.Identidad.FACULTAD_NO_ADMITE_COORDINADOR, () -> regla.ejecutar(rol, idEnConformacion, null, daoFactory));
    }

    @Test
    @DisplayName("El Coordinador Académico no necesita Facultad ni Programa")
    void coordinadorAcademico() {
        assertDoesNotThrow(() -> AsegurarAmbitoSegunRolRule.obtenerInstancia().ejecutar(Rol.COORDINADOR_ACADEMICO, null, null, daoFactory));
    }

    private CodigoVerificacionDominio codigo(LocalDateTime expiracion, int intentosFallidos, boolean usado) {
        return new CodigoVerificacionDominio.Builder()
                .id(UtilId.generarId())
                .canal(CanalVerificacion.CELULAR)
                .destino(CELULAR)
                .codigo("123456")
                .fechaGeneracion(expiracion.minusMinutes(15))
                .fechaExpiracion(expiracion)
                .intentosFallidos(intentosFallidos)
                .usado(usado)
                .build();
    }

    @Test
    @DisplayName("Un código sin usar, sin vencer y con intentos disponibles está vigente (RN-078, RN-080)")
    void codigoVigente() {
        var ahora = UtilFecha.fechaActual();
        var vigente = codigo(ahora.plusMinutes(10), MAXIMO_INTENTOS - 1, false);

        assertDoesNotThrow(() -> AsegurarCodigoVigenteRule.obtenerInstancia().ejecutar(vigente, ahora, MAXIMO_INTENTOS));
    }

    @Test
    @DisplayName("Rechaza el código que no existe, ya se usó, venció o agotó sus intentos (D-10)")
    void codigoNoVigente() {
        var ahora = UtilFecha.fechaActual();
        var regla = AsegurarCodigoVigenteRule.obtenerInstancia();
        var inexistente = new CodigoVerificacionDominio.Builder().build();
        var usado = codigo(ahora.plusMinutes(10), 0, true);
        var vencido = codigo(ahora, 0, false);
        var agotado = codigo(ahora.plusMinutes(10), MAXIMO_INTENTOS, false);

        assertRechazo(CatalogoMensajes.Identidad.CODIGO_NO_SOLICITADO, () -> regla.ejecutar(inexistente, ahora, MAXIMO_INTENTOS));
        assertRechazo(CatalogoMensajes.Identidad.CODIGO_YA_USADO, () -> regla.ejecutar(usado, ahora, MAXIMO_INTENTOS));
        assertRechazo(CatalogoMensajes.Identidad.CODIGO_VENCIDO, () -> regla.ejecutar(vencido, ahora, MAXIMO_INTENTOS));
        assertRechazo(CatalogoMensajes.Identidad.CODIGO_INTENTOS_AGOTADOS, () -> regla.ejecutar(agotado, ahora, MAXIMO_INTENTOS));
    }
}
