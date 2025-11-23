package com.discografia.discos;
import com.discografia.artistas.IArtistaRepository;
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
public class DiscoController {

    @Autowired
    private IDiscoRepository repo;

    @Autowired
    private IArtistaRepository artistaRepo;

    @PostMapping(
        value = "/disco",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> crear(@RequestBody Disco body) {
        if (body.getIdArtista() == null || !artistaRepo.existsById(body.getIdArtista())) {
            return ResponseEntity.badRequest().body(Map.of("error", "El idArtista no existe"));
        }
        Disco saved = repo.insert(body);
        return ResponseEntity.created(URI.create("/api/disco/" + saved.getId())).body(saved);
    }

    @GetMapping(value = "/discos", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> listar() {
        return ResponseEntity.ok(repo.findAll());
    }

    @GetMapping(value = "/disco/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> obtener(@PathVariable String id) {
        Optional<Disco> opt = repo.findById(id);
        return opt.<ResponseEntity<?>>map(ResponseEntity::ok)
                  .orElseGet(() -> ResponseEntity.status(404).body(Map.of("error", "Disco no encontrado")));
    }

    @GetMapping(value = "/artista/{id}/discos", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> listarPorArtista(@PathVariable String id) {
        if (!artistaRepo.existsById(id)) {
            return ResponseEntity.status(404).body(Map.of("error", "Artista no encontrado"));
        }
        return ResponseEntity.ok(repo.findDiscosByIdArtista(id));
    }
}