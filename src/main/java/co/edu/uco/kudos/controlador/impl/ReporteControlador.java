package co.edu.uco.kudos.controlador.impl;

import co.edu.uco.kudos.controlador.AbstractRestController;
import co.edu.uco.kudos.controlador.respuesta.RespuestaOk;
import co.edu.uco.kudos.dto.ReporteDto;
import co.edu.uco.kudos.negocio.fachada.ReporteFachada;
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
@RequestMapping("/api/v1")
public class ReporteControlador extends AbstractRestController {

    private final ReporteFachada fachada = new ReporteFachadaImpl();

    @PostMapping("/kudos/{id}/reportes")
    public ResponseEntity<RespuestaOk<String>> reportarKudo(@AuthenticationPrincipal OidcUser datosUsuario, @PathVariable UUID id,
            @RequestBody ReporteDto datos) {
        fachada.reportarKudo(obtenerIdActor(datosUsuario), id, datos);
        RespuestaOk<String> respuesta = RespuestaOk.crear(CatalogoMensajes.ModeracionControlador.REPORTE_REGISTRADO);
        return ResponseEntity.ok(respuesta);
    }

    @PostMapping("/replicas/{id}/reportes")
    public ResponseEntity<RespuestaOk<String>> reportarReplica(@AuthenticationPrincipal OidcUser datosUsuario, @PathVariable UUID id,
            @RequestBody ReporteDto datos) {
        fachada.reportarReplica(obtenerIdActor(datosUsuario), id, datos);
        RespuestaOk<String> respuesta = RespuestaOk.crear(CatalogoMensajes.ModeracionControlador.REPORTE_REGISTRADO);
        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/reportes/mios")
    public ResponseEntity<RespuestaOk<ReporteDto>> consultarMisReportes(@AuthenticationPrincipal OidcUser datosUsuario) {
        return ResponseEntity.ok(RespuestaOk.crear(fachada.consultarMisReportes(obtenerIdActor(datosUsuario))));
    }
}
