package co.edu.uco.kudos.controlador.impl;

import co.edu.uco.kudos.controlador.AbstractRestController;
import co.edu.uco.kudos.controlador.respuesta.RespuestaOk;
import co.edu.uco.kudos.dto.ContenidoReportadoDto;
import co.edu.uco.kudos.dto.DecisionDto;
import co.edu.uco.kudos.dto.VetoDto;
import co.edu.uco.kudos.negocio.fachada.ModeracionFachada;
import co.edu.uco.kudos.negocio.fachada.ReporteFachada;
import co.edu.uco.kudos.negocio.fachada.impl.ModeracionFachadaImpl;
import co.edu.uco.kudos.negocio.fachada.impl.ReporteFachadaImpl;
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
@RequestMapping("/api/v1/moderacion")
public class ModeracionControlador extends AbstractRestController {

    private final ModeracionFachada fachada = new ModeracionFachadaImpl();
    private final ReporteFachada reporteFachada = new ReporteFachadaImpl();

    @GetMapping("/reportes")
    public ResponseEntity<RespuestaOk<ContenidoReportadoDto>> consultarReportes(@AuthenticationPrincipal OidcUser datosUsuario) {
        return ResponseEntity.ok(RespuestaOk.crear(reporteFachada.consultarReportesAbiertos(obtenerIdActor(datosUsuario))));
    }

    @GetMapping("/vetos")
    public ResponseEntity<RespuestaOk<VetoDto>> consultarVetos(@AuthenticationPrincipal OidcUser datosUsuario) {
        return ResponseEntity.ok(RespuestaOk.crear(fachada.consultarVetos(obtenerIdActor(datosUsuario))));
    }

    @PostMapping("/kudos/{id}/revision")
    public ResponseEntity<RespuestaOk<String>> iniciarRevisionKudo(@AuthenticationPrincipal OidcUser datosUsuario, @PathVariable UUID id) {
        fachada.iniciarRevisionKudo(obtenerIdActor(datosUsuario), id);
        return responder(CatalogoMensajes.ModeracionControlador.REVISION_INICIADA);
    }

    @PostMapping("/replicas/{id}/revision")
    public ResponseEntity<RespuestaOk<String>> iniciarRevisionReplica(@AuthenticationPrincipal OidcUser datosUsuario, @PathVariable UUID id) {
        fachada.iniciarRevisionReplica(obtenerIdActor(datosUsuario), id);
        return responder(CatalogoMensajes.ModeracionControlador.REVISION_INICIADA);
    }

    @PostMapping("/kudos/{id}/veto")
    public ResponseEntity<RespuestaOk<String>> vetarKudo(@AuthenticationPrincipal OidcUser datosUsuario, @PathVariable UUID id,
            @RequestBody DecisionDto datos) {
        fachada.vetarKudo(obtenerIdActor(datosUsuario), id, datos);
        return responder(CatalogoMensajes.ModeracionControlador.CONTENIDO_VETADO);
    }

    @PostMapping("/replicas/{id}/veto")
    public ResponseEntity<RespuestaOk<String>> vetarReplica(@AuthenticationPrincipal OidcUser datosUsuario, @PathVariable UUID id,
            @RequestBody DecisionDto datos) {
        fachada.vetarReplica(obtenerIdActor(datosUsuario), id, datos);
        return responder(CatalogoMensajes.ModeracionControlador.CONTENIDO_VETADO);
    }

    @PostMapping("/kudos/{id}/liberacion")
    public ResponseEntity<RespuestaOk<String>> liberarKudo(@AuthenticationPrincipal OidcUser datosUsuario, @PathVariable UUID id,
            @RequestBody DecisionDto datos) {
        fachada.liberarKudo(obtenerIdActor(datosUsuario), id, datos);
        return responder(CatalogoMensajes.ModeracionControlador.CONTENIDO_LIBERADO);
    }

    @PostMapping("/replicas/{id}/liberacion")
    public ResponseEntity<RespuestaOk<String>> liberarReplica(@AuthenticationPrincipal OidcUser datosUsuario, @PathVariable UUID id,
            @RequestBody DecisionDto datos) {
        fachada.liberarReplica(obtenerIdActor(datosUsuario), id, datos);
        return responder(CatalogoMensajes.ModeracionControlador.CONTENIDO_LIBERADO);
    }

    private ResponseEntity<RespuestaOk<String>> responder(String mensaje) {
        RespuestaOk<String> respuesta = RespuestaOk.crear(mensaje);
        return ResponseEntity.ok(respuesta);
    }
}
