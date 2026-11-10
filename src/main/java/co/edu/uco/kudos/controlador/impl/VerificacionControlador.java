package co.edu.uco.kudos.controlador.impl;

import co.edu.uco.kudos.controlador.AbstractRestController;
import co.edu.uco.kudos.controlador.respuesta.Respuesta;
import co.edu.uco.kudos.controlador.respuesta.RespuestaError;
import co.edu.uco.kudos.controlador.respuesta.RespuestaOk;
import co.edu.uco.kudos.dto.CodigoVerificacionDto;
import co.edu.uco.kudos.negocio.fachada.VerificacionFachada;
import co.edu.uco.kudos.negocio.fachada.impl.VerificacionFachadaImpl;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/verificaciones/celular")
public class VerificacionControlador extends AbstractRestController {

    private final VerificacionFachada fachada = new VerificacionFachadaImpl();

    @PostMapping
    public ResponseEntity<Respuesta<String>> verificarCelular(@AuthenticationPrincipal OidcUser datosUsuario,
            @RequestBody CodigoVerificacionDto datos) {
        var verificado = fachada.verificarCelular(obtenerIdUsuarioRegistrado(datosUsuario), datos);
        if (!verificado) {
            var respuestaError = RespuestaError.crear(CatalogoMensajes.Identidad.CODIGO_INCORRECTO);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuestaError);
        }
        RespuestaOk<String> respuesta = RespuestaOk.crear(CatalogoMensajes.IdentidadControlador.CELULAR_VERIFICADO);
        return ResponseEntity.ok(respuesta);
    }

    @PostMapping("/reenvio")
    public ResponseEntity<RespuestaOk<String>> reenviarCodigo(@AuthenticationPrincipal OidcUser datosUsuario) {
        fachada.reenviarCodigoCelular(obtenerIdUsuarioRegistrado(datosUsuario));
        RespuestaOk<String> respuesta = RespuestaOk.crear(CatalogoMensajes.IdentidadControlador.CODIGO_REENVIADO);
        return ResponseEntity.ok(respuesta);
    }
}
