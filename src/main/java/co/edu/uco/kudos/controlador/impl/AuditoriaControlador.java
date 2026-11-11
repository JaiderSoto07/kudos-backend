package co.edu.uco.kudos.controlador.impl;

import co.edu.uco.kudos.controlador.AbstractRestController;
import co.edu.uco.kudos.controlador.respuesta.RespuestaOk;
import co.edu.uco.kudos.dto.PaginaAuditoriaDto;
import co.edu.uco.kudos.negocio.fachada.AuditoriaFachada;
import co.edu.uco.kudos.negocio.fachada.impl.AuditoriaFachadaImpl;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1")
public class AuditoriaControlador extends AbstractRestController {

    private final AuditoriaFachada fachada = new AuditoriaFachadaImpl();

    @GetMapping("/auditoria")
    public ResponseEntity<RespuestaOk<PaginaAuditoriaDto>> consultar(@AuthenticationPrincipal OidcUser datosUsuario,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
            @RequestParam(defaultValue = "") String actor, @RequestParam(defaultValue = "") String accion,
            @RequestParam(defaultValue = "") String tipoObjeto, @RequestParam(defaultValue = "1") int pagina) {
        return ResponseEntity.ok(RespuestaOk.crear(fachada.consultar(obtenerIdActor(datosUsuario), desde, hasta, actor, accion, tipoObjeto, pagina)));
    }
}
