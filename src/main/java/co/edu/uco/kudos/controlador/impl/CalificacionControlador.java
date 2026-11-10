package co.edu.uco.kudos.controlador.impl;

import co.edu.uco.kudos.controlador.AbstractRestController;
import co.edu.uco.kudos.controlador.respuesta.RespuestaOk;
import co.edu.uco.kudos.dto.CalificacionDto;
import co.edu.uco.kudos.dto.ValoracionesDto;
import co.edu.uco.kudos.negocio.fachada.CalificacionFachada;
import co.edu.uco.kudos.negocio.fachada.impl.CalificacionFachadaImpl;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class CalificacionControlador extends AbstractRestController {

    private final CalificacionFachada fachada = new CalificacionFachadaImpl();

    @PutMapping("/kudos/{id}/calificacion")
    public ResponseEntity<RespuestaOk<String>> calificarKudo(@AuthenticationPrincipal OidcUser datosUsuario, @PathVariable UUID id,
            @RequestBody CalificacionDto datos) {
        fachada.calificarKudo(obtenerIdActor(datosUsuario), id, datos);
        RespuestaOk<String> respuesta = RespuestaOk.crear(CatalogoMensajes.CalificacionControlador.CALIFICACION_GUARDADA);
        return ResponseEntity.ok(respuesta);
    }

    @PutMapping("/replicas/{id}/calificacion")
    public ResponseEntity<RespuestaOk<String>> calificarReplica(@AuthenticationPrincipal OidcUser datosUsuario, @PathVariable UUID id,
            @RequestBody CalificacionDto datos) {
        fachada.calificarReplica(obtenerIdActor(datosUsuario), id, datos);
        RespuestaOk<String> respuesta = RespuestaOk.crear(CatalogoMensajes.CalificacionControlador.CALIFICACION_GUARDADA);
        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/kudos/{id}/valoraciones")
    public ResponseEntity<RespuestaOk<ValoracionesDto>> consultarValoracionesKudo(@AuthenticationPrincipal OidcUser datosUsuario, @PathVariable UUID id) {
        return ResponseEntity.ok(RespuestaOk.crear(fachada.consultarValoracionesKudo(obtenerIdActor(datosUsuario), id)));
    }

    @GetMapping("/replicas/{id}/valoraciones")
    public ResponseEntity<RespuestaOk<ValoracionesDto>> consultarValoracionesReplica(@AuthenticationPrincipal OidcUser datosUsuario, @PathVariable UUID id) {
        return ResponseEntity.ok(RespuestaOk.crear(fachada.consultarValoracionesReplica(obtenerIdActor(datosUsuario), id)));
    }
}
