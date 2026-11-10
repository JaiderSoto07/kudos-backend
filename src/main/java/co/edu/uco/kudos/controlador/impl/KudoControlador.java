package co.edu.uco.kudos.controlador.impl;

import co.edu.uco.kudos.controlador.AbstractRestController;
import co.edu.uco.kudos.controlador.respuesta.RespuestaOk;
import co.edu.uco.kudos.dto.KudoDto;
import co.edu.uco.kudos.dto.PaginaKudosDto;
import co.edu.uco.kudos.dto.PersonaDto;
import co.edu.uco.kudos.negocio.fachada.KudoFachada;
import co.edu.uco.kudos.negocio.fachada.impl.KudoFachadaImpl;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class KudoControlador extends AbstractRestController {

    private final KudoFachada fachada = new KudoFachadaImpl();

    @PostMapping("/kudos")
    public ResponseEntity<RespuestaOk<String>> crear(@AuthenticationPrincipal OidcUser datosUsuario, @RequestBody KudoDto datos) {
        fachada.crear(obtenerIdActor(datosUsuario), datos);
        RespuestaOk<String> respuesta = RespuestaOk.crear(CatalogoMensajes.KudoControlador.KUDO_PUBLICADO);
        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/kudos")
    public ResponseEntity<RespuestaOk<PaginaKudosDto>> consultarMuro(@AuthenticationPrincipal OidcUser datosUsuario,
            @RequestParam(required = false) UUID categoria, @RequestParam(required = false) UUID persona,
            @RequestParam(defaultValue = "1") int pagina) {
        obtenerIdActor(datosUsuario);
        return ResponseEntity.ok(RespuestaOk.crear(fachada.consultarMuro(categoria, persona, pagina)));
    }

    @GetMapping("/kudos/{id}")
    public ResponseEntity<RespuestaOk<KudoDto>> consultar(@AuthenticationPrincipal OidcUser datosUsuario, @PathVariable UUID id) {
        return ResponseEntity.ok(RespuestaOk.crear(fachada.consultar(obtenerIdActor(datosUsuario), id)));
    }

    @GetMapping("/usuarios/{id}/kudos")
    public ResponseEntity<RespuestaOk<KudoDto>> consultarHistorial(@AuthenticationPrincipal OidcUser datosUsuario, @PathVariable UUID id,
            @RequestParam(defaultValue = "") String tipo) {
        obtenerIdActor(datosUsuario);
        return ResponseEntity.ok(RespuestaOk.crear(fachada.consultarHistorial(id, tipo)));
    }

    @GetMapping("/personas")
    public ResponseEntity<RespuestaOk<PersonaDto>> buscarPersonas(@AuthenticationPrincipal OidcUser datosUsuario,
            @RequestParam(defaultValue = "") String texto) {
        return ResponseEntity.ok(RespuestaOk.crear(fachada.buscarPersonas(obtenerIdActor(datosUsuario), texto)));
    }
}
