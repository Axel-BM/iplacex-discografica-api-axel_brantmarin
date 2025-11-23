package com.discografia.artistas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin 
public class ArtistaController {

    @Autowired
    private IArtistaRepository repo;

    @PostMapping(
        value = "/artista",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> crear(@RequestBody Artista body) {
        Artista saved = repo.insert(body);
        return ResponseEntity.created(URI.create("/api/artista/" + saved.getId())).body(saved);
    }

    @GetMapping(value = "/artistas", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> listar() {
        return ResponseEntity.ok(repo.findAll());
    }

    @GetMapping(value = "/artista/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> obtener(@PathVariable String id) {
        Optional<Artista> opt = repo.findById(id);
        return opt.<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404).body(Map.of("error", "Artista no encontrado")));
    }

    @PutMapping(
        value = "/artista/{id}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> actualizar(@PathVariable String id, @RequestBody Artista body) {
        if (!repo.existsById(id)) {
            return ResponseEntity.status(404).body(Map.of("error", "Artista no encontrado"));
        }
        body.setId(id);
        Artista updated = repo.save(body);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping(value = "/artista/{id}")
    public ResponseEntity<?> eliminar(@PathVariable String id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.status(404).body(Map.of("error", "Artista no encontrado"));
        }
        repo.deleteById(id);
        return ResponseEntity.noContent().build(); // 204
    }
}
