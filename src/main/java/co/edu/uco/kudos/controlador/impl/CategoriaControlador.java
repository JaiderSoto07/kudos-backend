package co.edu.uco.kudos.controlador.impl;

import co.edu.uco.kudos.controlador.AbstractRestController;
import co.edu.uco.kudos.controlador.respuesta.RespuestaOk;
import co.edu.uco.kudos.dto.CategoriaDto;
import co.edu.uco.kudos.negocio.fachada.CategoriaFachada;
import co.edu.uco.kudos.negocio.fachada.impl.CategoriaFachadaImpl;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/categorias")
public class CategoriaControlador extends AbstractRestController {

    private final CategoriaFachada fachada = new CategoriaFachadaImpl();

    @GetMapping
    public ResponseEntity<RespuestaOk<CategoriaDto>> consultarActivas() {
        var categorias = fachada.consultarActivas();
        return ResponseEntity.ok(RespuestaOk.crear(categorias));
    }

    @GetMapping("/todas")
    public ResponseEntity<RespuestaOk<CategoriaDto>> consultarTodas(@AuthenticationPrincipal OidcUser datosUsuario) {
        var categorias = fachada.consultarTodas(obtenerIdActor(datosUsuario));
        return ResponseEntity.ok(RespuestaOk.crear(categorias));
    }

    @PostMapping
    public ResponseEntity<RespuestaOk<String>> registrarNuevaCategoria(@AuthenticationPrincipal OidcUser datosUsuario,
            @RequestBody CategoriaDto datos) {
        fachada.registrarNuevaCategoria(obtenerIdActor(datosUsuario), datos);
        RespuestaOk<String> respuesta = RespuestaOk.crear(CatalogoMensajes.CategoriaControlador.CATEGORIA_REGISTRADA);
        return ResponseEntity.ok(respuesta);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RespuestaOk<String>> modificarCategoria(@AuthenticationPrincipal OidcUser datosUsuario,
            @PathVariable UUID id, @RequestBody CategoriaDto datos) {
        fachada.modificarCategoria(obtenerIdActor(datosUsuario), id, datos);
        RespuestaOk<String> respuesta = RespuestaOk.crear(CatalogoMensajes.CategoriaControlador.CATEGORIA_MODIFICADA);
        return ResponseEntity.ok(respuesta);
    }

    @PostMapping("/{id}/activacion")
    public ResponseEntity<RespuestaOk<String>> activarCategoria(@AuthenticationPrincipal OidcUser datosUsuario,
            @PathVariable UUID id) {
        fachada.activarCategoria(obtenerIdActor(datosUsuario), id);
        RespuestaOk<String> respuesta = RespuestaOk.crear(CatalogoMensajes.CategoriaControlador.CATEGORIA_ACTIVADA);
        return ResponseEntity.ok(respuesta);
    }

    @PostMapping("/{id}/desactivacion")
    public ResponseEntity<RespuestaOk<String>> desactivarCategoria(@AuthenticationPrincipal OidcUser datosUsuario,
            @PathVariable UUID id) {
        fachada.desactivarCategoria(obtenerIdActor(datosUsuario), id);
        RespuestaOk<String> respuesta = RespuestaOk.crear(CatalogoMensajes.CategoriaControlador.CATEGORIA_DESACTIVADA);
        return ResponseEntity.ok(respuesta);
    }
}
