package co.edu.uco.kudos.controlador.impl;

import co.edu.uco.kudos.controlador.AbstractRestController;
import co.edu.uco.kudos.controlador.respuesta.RespuestaOk;
import co.edu.uco.kudos.dto.NotificacionDto;
import co.edu.uco.kudos.negocio.fachada.NotificacionFachada;
import co.edu.uco.kudos.negocio.fachada.impl.NotificacionFachadaImpl;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/notificaciones")
public class NotificacionControlador extends AbstractRestController {

    private final NotificacionFachada fachada = new NotificacionFachadaImpl();

    @GetMapping
    public ResponseEntity<RespuestaOk<NotificacionDto>> consultarMias(@AuthenticationPrincipal OidcUser datosUsuario) {
        return ResponseEntity.ok(RespuestaOk.crear(fachada.consultarMias(obtenerIdActor(datosUsuario))));
    }

    @PostMapping("/{id}/lectura")
    public ResponseEntity<RespuestaOk<String>> marcarLeida(@AuthenticationPrincipal OidcUser datosUsuario, @PathVariable UUID id) {
        fachada.marcarLeida(obtenerIdActor(datosUsuario), id);
        RespuestaOk<String> respuesta = RespuestaOk.crear(CatalogoMensajes.NotificacionControlador.NOTIFICACION_LEIDA);
        return ResponseEntity.ok(respuesta);
    }

    @PostMapping("/lectura")
    public ResponseEntity<RespuestaOk<String>> marcarTodasLeidas(@AuthenticationPrincipal OidcUser datosUsuario) {
        fachada.marcarTodasLeidas(obtenerIdActor(datosUsuario));
        RespuestaOk<String> respuesta = RespuestaOk.crear(CatalogoMensajes.NotificacionControlador.NOTIFICACIONES_LEIDAS);
        return ResponseEntity.ok(respuesta);
    }
}
