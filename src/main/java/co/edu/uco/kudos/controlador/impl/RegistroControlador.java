package co.edu.uco.kudos.controlador.impl;

import co.edu.uco.kudos.controlador.AbstractRestController;
import co.edu.uco.kudos.controlador.respuesta.RespuestaOk;
import co.edu.uco.kudos.dto.OpcionesRegistroDto;
import co.edu.uco.kudos.dto.RegistroDto;
import co.edu.uco.kudos.negocio.fachada.RegistroFachada;
import co.edu.uco.kudos.negocio.fachada.impl.RegistroFachadaImpl;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/registros")
public class RegistroControlador extends AbstractRestController {

    private final RegistroFachada fachada = new RegistroFachadaImpl();

    @GetMapping("/opciones")
    public ResponseEntity<RespuestaOk<OpcionesRegistroDto>> consultarOpciones() {
        var opciones = fachada.consultarOpciones();
        return ResponseEntity.ok(RespuestaOk.crear(opciones));
    }

    @PostMapping
    public ResponseEntity<RespuestaOk<String>> registrarSolicitud(@AuthenticationPrincipal OidcUser datosUsuario,
            @RequestBody RegistroDto datos) {
        fachada.registrarSolicitud(datosUsuario.getEmail(), datos);
        RespuestaOk<String> respuesta = RespuestaOk.crear(CatalogoMensajes.IdentidadControlador.SOLICITUD_REGISTRADA);
        return ResponseEntity.ok(respuesta);
    }

    @PostMapping("/nueva-solicitud")
    public ResponseEntity<RespuestaOk<String>> registrarNuevaSolicitud(@AuthenticationPrincipal OidcUser datosUsuario,
            @RequestBody RegistroDto datos) {
        fachada.registrarNuevaSolicitud(obtenerIdUsuarioRegistrado(datosUsuario), datos);
        RespuestaOk<String> respuesta = RespuestaOk.crear(CatalogoMensajes.IdentidadControlador.NUEVA_SOLICITUD_REGISTRADA);
        return ResponseEntity.ok(respuesta);
    }
}
