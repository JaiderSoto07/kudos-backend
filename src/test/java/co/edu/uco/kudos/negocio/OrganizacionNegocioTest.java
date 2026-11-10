package co.edu.uco.kudos.negocio;

import co.edu.uco.kudos.dominio.EstadoCuenta;
import co.edu.uco.kudos.dominio.EstadoFacultad;
import co.edu.uco.kudos.dominio.FacultadDominio;
import co.edu.uco.kudos.dominio.ProgramaAcademicoDominio;
import co.edu.uco.kudos.dominio.Rol;
import co.edu.uco.kudos.negocio.falsos.DAOFactoryFalsa;
import co.edu.uco.kudos.negocio.negocio.FacultadNegocio;
import co.edu.uco.kudos.negocio.negocio.ProgramaNegocio;
import co.edu.uco.kudos.negocio.negocio.impl.CargoNegocioImpl;
import co.edu.uco.kudos.negocio.negocio.impl.FacultadNegocioImpl;
import co.edu.uco.kudos.negocio.negocio.impl.ProgramaNegocioImpl;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosAutorizacionExcepcion;
import co.edu.uco.kudos.transversal.excepciones.KudosExcepcion;
import co.edu.uco.kudos.transversal.utilitario.UtilFecha;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Negocio de organización: Facultades, Programas y coordinadores (CU-028 a CU-031, CU-034, RF-015 a RF-024, D-04, D-05)")
class OrganizacionNegocioTest {

    private DAOFactoryFalsa daoFactory;
    private FacultadNegocio facultades;
    private ProgramaNegocio programas;
    private UUID coordinadorAcademico;
    private UUID idFacultad;
    private UUID idOtraFacultad;
    private UUID idPrograma;
    private UUID idOtroPrograma;

    @BeforeEach
    void preparar() {
        daoFactory = new DAOFactoryFalsa();
        idFacultad = daoFactory.registrarFacultad(EstadoFacultad.ACTIVA);
        idOtraFacultad = daoFactory.registrarFacultad(EstadoFacultad.ACTIVA);
        idPrograma = daoFactory.registrarPrograma(idFacultad);
        idOtroPrograma = daoFactory.registrarPrograma(idOtraFacultad);
        coordinadorAcademico = daoFactory.registrarAutoridad(Rol.COORDINADOR_ACADEMICO, null, null);
        facultades = new FacultadNegocioImpl(daoFactory);
        programas = new ProgramaNegocioImpl(daoFactory);
    }

    private FacultadDominio facultad(String nombre) {
        return new FacultadDominio.Builder().nombre(nombre).build();
    }

    private ProgramaAcademicoDominio programa(String nombre, UUID facultad) {
        return new ProgramaAcademicoDominio.Builder().nombre(nombre).facultad(new FacultadDominio.Builder().id(facultad).build()).build();
    }

    private void assertRechazo(Class<? extends KudosExcepcion> tipo, String mensaje, Executable operacion) {
        var excepcion = assertThrows(tipo, operacion);
        assertEquals(mensaje, excepcion.getMensajeUsuario());
    }

    private UUID idFacultadLlamada(String nombre) {
        return facultades.consultar(coordinadorAcademico).stream().filter(f -> f.getNombre().equals(nombre)).findFirst().orElseThrow().getId();
    }

    @Test
    @DisplayName("Una Facultad nueva nace en conformación y pasa a ACTIVA con su primer Programa (D-05)")
    void facultadNuevaYPrimerPrograma() {
        facultades.registrar(coordinadorAcademico, facultad(" Derecho "));
        var idDerecho = idFacultadLlamada("Derecho");
        assertEquals("EN_CONFORMACION", daoFactory.getFacultadDAOFalso().consultarPorId(idDerecho).getEstado());
        assertEquals("FacultadCreada", daoFactory.getAuditoriaDAOFalso().getRegistros().getLast().getAccion());

        programas.registrar(coordinadorAcademico, programa("Derecho Penal", idDerecho));

        var derecho = facultades.consultar(coordinadorAcademico).stream().filter(f -> f.getId().equals(idDerecho)).findFirst().orElseThrow();
        assertEquals(EstadoFacultad.ACTIVA, derecho.getEstado());
        assertEquals(1, facultades.contarProgramasActivos(idDerecho));
        assertEquals("FacultadActivada", daoFactory.getAuditoriaDAOFalso().getRegistros().getLast().getAccion());
    }

    @Test
    @DisplayName("Nombres obligatorios, con límites y únicos sin importar mayúsculas")
    void validacionesDeNombres() {
        facultades.registrar(coordinadorAcademico, facultad("Derecho"));

        assertRechazo(KudosExcepcion.class, CatalogoMensajes.Organizacion.NOMBRE_FACULTAD_OBLIGATORIO,
                () -> facultades.registrar(coordinadorAcademico, facultad(" ")));
        assertRechazo(KudosExcepcion.class, CatalogoMensajes.Organizacion.NOMBRE_FACULTAD_LONGITUD_INVALIDA,
                () -> facultades.registrar(coordinadorAcademico, facultad("Ab")));
        assertRechazo(KudosExcepcion.class, CatalogoMensajes.Organizacion.NOMBRE_FACULTAD_YA_EXISTE,
                () -> facultades.registrar(coordinadorAcademico, facultad("DERECHO")));
        assertRechazo(KudosExcepcion.class, CatalogoMensajes.Organizacion.NOMBRE_PROGRAMA_OBLIGATORIO,
                () -> programas.registrar(coordinadorAcademico, programa("", idFacultad)));
        assertRechazo(KudosExcepcion.class, CatalogoMensajes.Organizacion.NOMBRE_PROGRAMA_LONGITUD_INVALIDA,
                () -> programas.registrar(coordinadorAcademico, programa("a".repeat(101), idFacultad)));
        programas.registrar(coordinadorAcademico, programa("Sistemas", idFacultad));
        assertRechazo(KudosExcepcion.class, CatalogoMensajes.Organizacion.NOMBRE_PROGRAMA_YA_EXISTE,
                () -> programas.registrar(coordinadorAcademico, programa("sistemas", idOtraFacultad)));
        assertRechazo(KudosExcepcion.class, CatalogoMensajes.Organizacion.FACULTAD_NO_EXISTE,
                () -> programas.registrar(coordinadorAcademico, programa("Medicina", UtilId.generarId())));
    }

    @Test
    @DisplayName("Modificar el nombre conserva el propio y rechaza el de otra Facultad o Programa")
    void modificarNombres() {
        facultades.registrar(coordinadorAcademico, facultad("Derecho"));
        var idDerecho = idFacultadLlamada("Derecho");

        facultades.modificar(coordinadorAcademico, idDerecho, facultad("Derecho"));
        facultades.modificar(coordinadorAcademico, idDerecho, facultad("Ciencias Jurídicas"));
        programas.modificar(coordinadorAcademico, idPrograma, programa("Ingeniería de Software", idFacultad));

        assertEquals("Ciencias Jurídicas", daoFactory.getFacultadDAOFalso().consultarPorId(idDerecho).getNombre());
        assertEquals("Ingeniería de Software", programas.consultarPorId(coordinadorAcademico, idPrograma).getNombre());
        var nombreAjeno = daoFactory.getFacultadDAOFalso().consultarPorId(idFacultad).getNombre();
        assertRechazo(KudosExcepcion.class, CatalogoMensajes.Organizacion.NOMBRE_FACULTAD_YA_EXISTE,
                () -> facultades.modificar(coordinadorAcademico, idDerecho, facultad(nombreAjeno)));
        assertRechazo(KudosExcepcion.class, CatalogoMensajes.Organizacion.FACULTAD_NO_EXISTE,
                () -> facultades.modificar(coordinadorAcademico, UtilId.generarId(), facultad("Medicina")));
        assertRechazo(KudosExcepcion.class, CatalogoMensajes.Organizacion.PROGRAMA_NO_EXISTE,
                () -> programas.modificar(coordinadorAcademico, UtilId.generarId(), programa("Medicina", idFacultad)));
    }

    @Test
    @DisplayName("Solo el Coordinador Académico crea, modifica o inactiva (D-04)")
    void soloElCoordinadorAcademicoGestiona() {
        var rector = daoFactory.registrarAutoridad(Rol.RECTOR, null, null);

        assertRechazo(KudosAutorizacionExcepcion.class, CatalogoMensajes.Organizacion.ACTOR_NO_GESTIONA_ORGANIZACION,
                () -> facultades.registrar(rector, facultad("Derecho")));
        assertRechazo(KudosAutorizacionExcepcion.class, CatalogoMensajes.Organizacion.ACTOR_NO_GESTIONA_ORGANIZACION,
                () -> programas.inactivar(rector, idPrograma));
    }

    @Test
    @DisplayName("Cada rol ve solo su ámbito: todo, su Facultad o su Programa (RF-016, RF-021, RN-118)")
    void visibilidadPorRol() {
        var coordinadorFacultad = daoFactory.registrarAutoridad(Rol.COORDINADOR_FACULTAD, idFacultad, null);
        var coordinadorPrograma = daoFactory.registrarAutoridad(Rol.COORDINADOR_PROGRAMA, null, idPrograma);
        var estudiante = daoFactory.registrarAutoridad(Rol.ESTUDIANTE, null, null);

        assertEquals(2, facultades.consultar(coordinadorAcademico).size());
        assertEquals(1, facultades.consultar(coordinadorFacultad).size());
        assertEquals(idFacultad, facultades.consultar(coordinadorPrograma).getFirst().getId());
        assertEquals(2, programas.consultar(coordinadorAcademico).size());
        assertEquals(idPrograma, programas.consultar(coordinadorFacultad).getFirst().getId());
        assertEquals(1, programas.consultar(coordinadorPrograma).size());
        assertRechazo(KudosAutorizacionExcepcion.class, CatalogoMensajes.Organizacion.PROGRAMA_FUERA_DE_AMBITO,
                () -> programas.consultarPorId(coordinadorFacultad, idOtroPrograma));
        assertRechazo(KudosAutorizacionExcepcion.class, CatalogoMensajes.Organizacion.FACULTAD_FUERA_DE_AMBITO,
                () -> facultades.consultarCoordinadores(coordinadorFacultad, idOtraFacultad));
        assertRechazo(KudosAutorizacionExcepcion.class, CatalogoMensajes.Organizacion.ACTOR_NO_CONSULTA_ORGANIZACION,
                () -> facultades.consultar(estudiante));
    }

    @Test
    @DisplayName("Coordinador vigente e historial con fechas (RF-018, RF-019, RF-023, RF-024)")
    void coordinadoresEHistorial() {
        var anterior = daoFactory.registrarAutoridad(Rol.COORDINADOR_PROGRAMA, null, idPrograma);
        new CargoNegocioImpl(daoFactory).cerrarCargoVigente(coordinadorAcademico, Rol.COORDINADOR_PROGRAMA, UtilId.VALOR_DEFECTO,
                idPrograma, "Terminó su periodo.");
        var actual = daoFactory.registrarAutoridad(Rol.COORDINADOR_PROGRAMA, null, idPrograma);
        daoFactory.registrarAutoridad(Rol.COORDINADOR_FACULTAD, idFacultad, null);

        var historial = programas.consultarCoordinadores(coordinadorAcademico, idPrograma);

        assertEquals(2, historial.size());
        var cerrado = historial.stream().filter(a -> a.getUsuario().getId().equals(anterior)).findFirst().orElseThrow();
        assertFalse(cerrado.estaVigente());
        assertEquals("Terminó su periodo.", cerrado.getMotivoFin());
        assertTrue(historial.stream().anyMatch(a -> a.getUsuario().getId().equals(actual) && a.estaVigente()));
        assertEquals(1, facultades.consultarCoordinadores(coordinadorAcademico, idFacultad).size());
        assertEquals(3, new CargoNegocioImpl(daoFactory).consultarCargosVigentes().size());
    }

    @Test
    @DisplayName("Inactivar Programa: no con vínculos; si tiene coordinador, su cargo se cierra (RF-084)")
    void inactivarPrograma() {
        var estudiante = daoFactory.registrarPersona("ana@gmail.com", Rol.ESTUDIANTE, EstadoCuenta.ACTIVA);
        var idVinculo = daoFactory.registrarVinculo(estudiante, idOtroPrograma, "ESTUDIANTE");
        var coordinador = daoFactory.registrarAutoridad(Rol.COORDINADOR_PROGRAMA, null, idPrograma);

        assertRechazo(KudosExcepcion.class, CatalogoMensajes.Organizacion.PROGRAMA_CON_VINCULOS,
                () -> programas.inactivar(coordinadorAcademico, idOtroPrograma));

        programas.inactivar(coordinadorAcademico, idPrograma);

        assertFalse(programas.consultarPorId(coordinadorAcademico, idPrograma).isActivo());
        assertEquals("DESACTIVADA", daoFactory.getUsuarioDAOFalso().consultarPorId(coordinador).getEstadoCuenta());
        assertFalse(UtilFecha.esValorDefecto(programas.consultarCoordinadores(coordinadorAcademico, idPrograma).getFirst().getFechaFin()));
        assertRechazo(KudosExcepcion.class, CatalogoMensajes.Organizacion.PROGRAMA_INACTIVO,
                () -> programas.inactivar(coordinadorAcademico, idPrograma));
        assertTrue(UtilFecha.esValorDefecto(daoFactory.getVinculacionProgramaDAOFalso().consultarPorId(idVinculo).getFechaFin()));
    }

    @Test
    @DisplayName("Inactivar Facultad: no con Programas activos; una inactiva no admite Programas (RF-083)")
    void inactivarFacultad() {
        facultades.registrar(coordinadorAcademico, facultad("Derecho"));
        var idDerecho = idFacultadLlamada("Derecho");

        assertRechazo(KudosExcepcion.class, CatalogoMensajes.Organizacion.FACULTAD_CON_PROGRAMAS_ACTIVOS,
                () -> facultades.inactivar(coordinadorAcademico, idFacultad));

        facultades.inactivar(coordinadorAcademico, idDerecho);

        assertEquals("INACTIVA", daoFactory.getFacultadDAOFalso().consultarPorId(idDerecho).getEstado());
        assertRechazo(KudosExcepcion.class, CatalogoMensajes.Organizacion.FACULTAD_YA_INACTIVA,
                () -> facultades.inactivar(coordinadorAcademico, idDerecho));
        assertRechazo(KudosExcepcion.class, CatalogoMensajes.Organizacion.FACULTAD_NO_ADMITE_PROGRAMAS,
                () -> programas.registrar(coordinadorAcademico, programa("Derecho Penal", idDerecho)));
    }
}
