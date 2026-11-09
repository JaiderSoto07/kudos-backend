package co.edu.uco.kudos.controlador;

import co.edu.uco.kudos.controlador.respuesta.RespuestaError;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosExcepcion;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public abstract class AbstractRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractRestController.class);

    @ExceptionHandler(KudosExcepcion.class)
    public ResponseEntity<RespuestaError<String>> controlarKudosExcepcion(KudosExcepcion excepcion) {
        LOGGER.warn("Capa: {} - Mensaje técnico: {}", excepcion.getCapa(), excepcion.getMensajeTecnico(), excepcion.getExcepcionRaiz());
        var respuesta = RespuestaError.crear(excepcion.getMensajeUsuario());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<RespuestaError<String>> controlarRecursoNoEncontrado(NoResourceFoundException excepcion) {
        LOGGER.info("Recurso no encontrado: {} por el método: {}", excepcion.getResourcePath(), excepcion.getHttpMethod());
        var mensaje = UtilTexto.getUtilTexto().concatenar(CatalogoMensajes.AbstractRestController.USUARIO_ERROR_RECURSO_NO_EXISTE,
                excepcion.getResourcePath(), " por el método ", excepcion.getHttpMethod());
        var respuesta = RespuestaError.crear(mensaje);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<RespuestaError<String>> controlarExcepcion(Exception excepcion) {
        LOGGER.error("Se presentó un problema inesperado en el controlador", excepcion);
        var respuesta = RespuestaError.crear(CatalogoMensajes.AbstractRestController.USUARIO_ERROR_PROBLEMA_INESPERADO_EN_CONTROLADOR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
    }
}
