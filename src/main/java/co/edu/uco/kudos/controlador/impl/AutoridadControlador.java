package co.edu.uco.kudos.controlador.impl;

import co.edu.uco.kudos.controlador.AbstractRestController;
import co.edu.uco.kudos.controlador.respuesta.RespuestaOk;
import co.edu.uco.kudos.dto.AutoridadDto;
import co.edu.uco.kudos.negocio.fachada.AutoridadFachada;
import co.edu.uco.kudos.negocio.fachada.impl.AutoridadFachadaImpl;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/autoridades")
public class AutoridadControlador extends AbstractRestController {

    private final AutoridadFachada fachada = new AutoridadFachadaImpl();

    @PostMapping
    public ResponseEntity<RespuestaOk<String>> registrarAutoridad(@AuthenticationPrincipal OidcUser datosUsuario,
            @RequestBody AutoridadDto datos) {
        fachada.registrarAutoridad(obtenerIdActor(datosUsuario), datos);
        RespuestaOk<String> respuesta = RespuestaOk.crear(CatalogoMensajes.AprobacionControlador.AUTORIDAD_REGISTRADA);
        return ResponseEntity.ok(respuesta);
    }
}
