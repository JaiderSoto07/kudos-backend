package co.edu.uco.kudos.controlador.impl;

import co.edu.uco.kudos.controlador.AbstractRestController;
import co.edu.uco.kudos.controlador.respuesta.RespuestaOk;
import co.edu.uco.kudos.dto.ReplicaDto;
import co.edu.uco.kudos.negocio.fachada.ReplicaFachada;
import co.edu.uco.kudos.negocio.fachada.impl.ReplicaFachadaImpl;
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
@RequestMapping("/api/v1")
public class ReplicaControlador extends AbstractRestController {

    private final ReplicaFachada fachada = new ReplicaFachadaImpl();

    @PostMapping("/kudos/{id}/replicas")
    public ResponseEntity<RespuestaOk<String>> responderKudo(@AuthenticationPrincipal OidcUser datosUsuario, @PathVariable UUID id,
            @RequestBody ReplicaDto datos) {
        fachada.responderKudo(obtenerIdActor(datosUsuario), id, datos);
        RespuestaOk<String> respuesta = RespuestaOk.crear(CatalogoMensajes.ReplicaControlador.REPLICA_PUBLICADA);
        return ResponseEntity.ok(respuesta);
    }

    @PostMapping("/replicas/{id}/respuestas")
    public ResponseEntity<RespuestaOk<String>> responderReplica(@AuthenticationPrincipal OidcUser datosUsuario, @PathVariable UUID id,
            @RequestBody ReplicaDto datos) {
        fachada.responderReplica(obtenerIdActor(datosUsuario), id, datos);
        RespuestaOk<String> respuesta = RespuestaOk.crear(CatalogoMensajes.ReplicaControlador.REPLICA_PUBLICADA);
        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/kudos/{id}/conversacion")
    public ResponseEntity<RespuestaOk<ReplicaDto>> consultarConversacion(@AuthenticationPrincipal OidcUser datosUsuario, @PathVariable UUID id) {
        obtenerIdActor(datosUsuario);
        return ResponseEntity.ok(RespuestaOk.crear(fachada.consultarConversacion(id)));
    }
}
