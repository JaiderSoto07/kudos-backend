package co.edu.uco.kudos.controlador;

import co.edu.uco.kudos.controlador.respuesta.EstadoRespuesta;
import co.edu.uco.kudos.transversal.excepciones.KudosAutorizacionExcepcion;
import co.edu.uco.kudos.transversal.excepciones.KudosNegocioExcepcion;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Traducción de excepciones a códigos HTTP (RNF-006)")
class AbstractRestControllerTest {

    private final AbstractRestController controlador = new AbstractRestController() {
    };

    @Test
    @DisplayName("Una regla de negocio incumplida responde 400")
    void negocioResponde400() {
        var excepcion = KudosNegocioExcepcion.crear("El nombre ya existe");

        var respuesta = controlador.controlarKudosExcepcion(excepcion);

        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertEquals(List.of("El nombre ya existe"), respuesta.getBody().getMensajes());
        assertEquals(EstadoRespuesta.ERROR, respuesta.getBody().getEstado());
    }

    @Test
    @DisplayName("Sin permiso para la operación responde 403 con el mensaje para el usuario")
    void autorizacionResponde403() {
        var excepcion = (KudosAutorizacionExcepcion) KudosAutorizacionExcepcion.crear("No tienes permiso", "Rol DOCENTE");

        var respuesta = controlador.controlarKudosAutorizacionExcepcion(excepcion);

        assertEquals(HttpStatus.FORBIDDEN, respuesta.getStatusCode());
        assertEquals(List.of("No tienes permiso"), respuesta.getBody().getMensajes());
        assertEquals(EstadoRespuesta.ERROR, respuesta.getBody().getEstado());
    }
}
