package co.edu.uco.kudos.controlador.impl;

import co.edu.uco.kudos.controlador.AbstractRestController;
import co.edu.uco.kudos.controlador.respuesta.RespuestaOk;
import co.edu.uco.kudos.dto.RechazoDto;
import co.edu.uco.kudos.dto.SolicitudDto;
import co.edu.uco.kudos.negocio.fachada.SolicitudFachada;
import co.edu.uco.kudos.negocio.fachada.impl.SolicitudFachadaImpl;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/solicitudes")
public class SolicitudControlador extends AbstractRestController {

    private final SolicitudFachada fachada = new SolicitudFachadaImpl();

    @GetMapping("/pendientes")
    public ResponseEntity<RespuestaOk<SolicitudDto>> consultarPendientes(@AuthenticationPrincipal OidcUser datosUsuario) {
        var solicitudes = fachada.consultarPendientes(obtenerIdActor(datosUsuario));
        return ResponseEntity.ok(RespuestaOk.crear(solicitudes));
    }

    @PostMapping("/{id}/aprobacion")
    public ResponseEntity<RespuestaOk<String>> aprobar(@AuthenticationPrincipal OidcUser datosUsuario, @PathVariable UUID id) {
        fachada.aprobar(obtenerIdActor(datosUsuario), id);
        RespuestaOk<String> respuesta = RespuestaOk.crear(CatalogoMensajes.AprobacionControlador.SOLICITUD_APROBADA);
        return ResponseEntity.ok(respuesta);
    }

    @PostMapping("/{id}/rechazo")
    public ResponseEntity<RespuestaOk<String>> rechazar(@AuthenticationPrincipal OidcUser datosUsuario, @PathVariable UUID id,
            @RequestBody RechazoDto datos) {
        fachada.rechazar(obtenerIdActor(datosUsuario), id, datos);
        RespuestaOk<String> respuesta = RespuestaOk.crear(CatalogoMensajes.AprobacionControlador.SOLICITUD_RECHAZADA);
        return ResponseEntity.ok(respuesta);
    }
}
