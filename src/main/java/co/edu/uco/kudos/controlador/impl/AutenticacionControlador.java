package co.edu.uco.kudos.controlador.impl;

import co.edu.uco.kudos.controlador.AbstractRestController;
import co.edu.uco.kudos.controlador.respuesta.RespuestaOk;
import co.edu.uco.kudos.dto.UsuarioDto;
import co.edu.uco.kudos.negocio.fachada.AutenticacionFachada;
import co.edu.uco.kudos.negocio.fachada.impl.AutenticacionFachadaImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/autenticacion")
public class AutenticacionControlador extends AbstractRestController {

    private final AutenticacionFachada fachada = new AutenticacionFachadaImpl();

    @GetMapping
    public ResponseEntity<RespuestaOk<UsuarioDto>> consultarUsuarioActual(@AuthenticationPrincipal OidcUser datosUsuario) {
        var usuario = fachada.consultarUsuarioActual(datosUsuario.getEmail());
        return ResponseEntity.ok(RespuestaOk.crear(usuario));
    }
}
