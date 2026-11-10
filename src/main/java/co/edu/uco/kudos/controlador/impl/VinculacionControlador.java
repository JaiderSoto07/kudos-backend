package co.edu.uco.kudos.controlador.impl;

import co.edu.uco.kudos.controlador.AbstractRestController;
import co.edu.uco.kudos.controlador.respuesta.RespuestaOk;
import co.edu.uco.kudos.dto.CargoDto;
import co.edu.uco.kudos.dto.DesvinculacionDto;
import co.edu.uco.kudos.dto.VinculoDto;
import co.edu.uco.kudos.negocio.fachada.VinculacionFachada;
import co.edu.uco.kudos.negocio.fachada.impl.VinculacionFachadaImpl;
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
public class VinculacionControlador extends AbstractRestController {

    private final VinculacionFachada fachada = new VinculacionFachadaImpl();

    @PostMapping("/vinculos/{id}/desvinculacion")
    public ResponseEntity<RespuestaOk<String>> desvincular(@AuthenticationPrincipal OidcUser datosUsuario, @PathVariable UUID id,
            @RequestBody DesvinculacionDto datos) {
        fachada.desvincular(obtenerIdActor(datosUsuario), id, datos);
        RespuestaOk<String> respuesta = RespuestaOk.crear(CatalogoMensajes.OrganizacionControlador.PERSONA_DESVINCULADA);
        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/usuarios/{id}/vinculos")
    public ResponseEntity<RespuestaOk<VinculoDto>> consultarDeUsuario(@AuthenticationPrincipal OidcUser datosUsuario, @PathVariable UUID id) {
        return ResponseEntity.ok(RespuestaOk.crear(fachada.consultarDeUsuario(obtenerIdActor(datosUsuario), id)));
    }

    @GetMapping("/cuenta/vinculos")
    public ResponseEntity<RespuestaOk<VinculoDto>> consultarPropios(@AuthenticationPrincipal OidcUser datosUsuario) {
        return ResponseEntity.ok(RespuestaOk.crear(fachada.consultarPropios(obtenerIdUsuarioRegistrado(datosUsuario))));
    }

    @GetMapping("/estructura")
    public ResponseEntity<RespuestaOk<CargoDto>> consultarEstructura(@AuthenticationPrincipal OidcUser datosUsuario) {
        obtenerIdActor(datosUsuario);
        return ResponseEntity.ok(RespuestaOk.crear(fachada.consultarEstructura()));
    }
}
