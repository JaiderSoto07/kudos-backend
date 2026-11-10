package co.edu.uco.kudos.transversal;

import co.edu.uco.kudos.transversal.excepciones.KudosAutorizacionExcepcion;
import co.edu.uco.kudos.transversal.excepciones.KudosControladorExcepcion;
import co.edu.uco.kudos.transversal.excepciones.KudosDatosExcepcion;
import co.edu.uco.kudos.transversal.excepciones.KudosDominioExcepcion;
import co.edu.uco.kudos.transversal.excepciones.KudosDtoExcepcion;
import co.edu.uco.kudos.transversal.excepciones.KudosEntidadExcepcion;
import co.edu.uco.kudos.transversal.excepciones.KudosExcepcion;
import co.edu.uco.kudos.transversal.excepciones.KudosFachadaExcepcion;
import co.edu.uco.kudos.transversal.excepciones.KudosNegocioExcepcion;
import co.edu.uco.kudos.transversal.excepciones.KudosTransversalExcepcion;
import co.edu.uco.kudos.transversal.excepciones.enums.Capa;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

@DisplayName("Jerarquía de excepciones por capa (RNF-006)")
class ExcepcionesTest {

    private static final String USUARIO = "Mensaje para el usuario";
    private static final String TECNICO = "Mensaje técnico";
    private static final Exception RAIZ = new IllegalStateException("raíz");

    private void verificar(Capa capaEsperada, KudosExcepcion soloUsuario, KudosExcepcion conTecnico, KudosExcepcion completa) {
        assertEquals(capaEsperada, soloUsuario.getCapa());
        assertEquals(USUARIO, soloUsuario.getMensajeUsuario());
        assertEquals(USUARIO, soloUsuario.getMensajeTecnico());
        assertEquals(USUARIO, soloUsuario.getMessage());
        assertEquals(TECNICO, conTecnico.getMensajeTecnico());
        assertEquals(TECNICO, conTecnico.getExcepcionRaiz().getMessage());
        assertSame(RAIZ, completa.getExcepcionRaiz());
    }

    @Test
    @DisplayName("Cada capa crea su excepción con mensaje para el usuario, mensaje técnico y causa")
    void cadaCapa() {
        verificar(Capa.CONTROLADOR, KudosControladorExcepcion.crear(USUARIO), KudosControladorExcepcion.crear(USUARIO, TECNICO), KudosControladorExcepcion.crear(USUARIO, TECNICO, RAIZ));
        verificar(Capa.FACHADA, KudosFachadaExcepcion.crear(USUARIO), KudosFachadaExcepcion.crear(USUARIO, TECNICO), KudosFachadaExcepcion.crear(USUARIO, TECNICO, RAIZ));
        verificar(Capa.NEGOCIO, KudosNegocioExcepcion.crear(USUARIO), KudosNegocioExcepcion.crear(USUARIO, TECNICO), KudosNegocioExcepcion.crear(USUARIO, TECNICO, RAIZ));
        verificar(Capa.NEGOCIO, KudosAutorizacionExcepcion.crear(USUARIO), KudosAutorizacionExcepcion.crear(USUARIO, TECNICO), KudosAutorizacionExcepcion.crear(USUARIO, TECNICO, RAIZ));
        verificar(Capa.DOMINIO, KudosDominioExcepcion.crear(USUARIO), KudosDominioExcepcion.crear(USUARIO, TECNICO), KudosDominioExcepcion.crear(USUARIO, TECNICO, RAIZ));
        verificar(Capa.DATOS, KudosDatosExcepcion.crear(USUARIO), KudosDatosExcepcion.crear(USUARIO, TECNICO), KudosDatosExcepcion.crear(USUARIO, TECNICO, RAIZ));
        verificar(Capa.ENTIDAD, KudosEntidadExcepcion.crear(USUARIO), KudosEntidadExcepcion.crear(USUARIO, TECNICO), KudosEntidadExcepcion.crear(USUARIO, TECNICO, RAIZ));
        verificar(Capa.DTO, KudosDtoExcepcion.crear(USUARIO), KudosDtoExcepcion.crear(USUARIO, TECNICO), KudosDtoExcepcion.crear(USUARIO, TECNICO, RAIZ));
        verificar(Capa.TRANSVERSAL, KudosTransversalExcepcion.crear(USUARIO), KudosTransversalExcepcion.crear(USUARIO, TECNICO), KudosTransversalExcepcion.crear(USUARIO, TECNICO, RAIZ));
    }

    @Test
    @DisplayName("Los mensajes nulos se reemplazan por valores por defecto")
    void mensajesNulos() {
        var excepciones = List.of(KudosNegocioExcepcion.crear(null), KudosNegocioExcepcion.crear(null, null, null));
        for (var excepcion : excepciones) {
            assertEquals("", excepcion.getMensajeUsuario());
            assertEquals("", excepcion.getMensajeTecnico());
            assertNotNull(excepcion.getExcepcionRaiz());
        }
    }
}
