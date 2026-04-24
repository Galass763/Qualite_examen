package com.riffo.users.controller;

import com.riffo.users.entity.Partenaire;
import com.riffo.users.service.PartenaireService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/partenaires")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PartenaireRESTController {

    private static final Logger log = LoggerFactory.getLogger(PartenaireRESTController.class);

    @Autowired
    private PartenaireService partenaireService;

    @GetMapping
    public ResponseEntity<List<Partenaire>> getAllPartenaires() {
    log.info("GET /partenaires - Récupération de tous les partenaires");
    return ResponseEntity.ok(partenaireService.getAllPartenaires());
}

    @GetMapping("/{id}")
    public ResponseEntity<Partenaire> getPartenaireById(@PathVariable Long id) {
        log.info("GET /partenaires/{} - Recherche par ID", id);
        return partenaireService.getPartenaireById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search/nom")
    public ResponseEntity<Partenaire> getPartenaireByNom(@RequestParam String nom) {
        log.info("GET /partenaires/search/nom?nom={} - Recherche par nom", nom);
        return partenaireService.getPartenaireByNom(nom)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search/categorie")
    public ResponseEntity<List<Partenaire>> getPartenairesByCategorie(@RequestParam String categorie) {
        log.info("GET /partenaires/search/categorie?categorie={} - Recherche par catégorie", categorie);
        List<Partenaire> partenaires = partenaireService.getPartenairesByCategorie(categorie);
        return ResponseEntity.ok(partenaires);
    }

    @GetMapping("/search/statut")
    public ResponseEntity<List<Partenaire>> getPartenairesByStatut(@RequestParam String statut) {
        log.info("GET /partenaires/search/statut?statut={} - Recherche par statut", statut);
        List<Partenaire> partenaires = partenaireService.getPartenairesByStatut(statut);
        return ResponseEntity.ok(partenaires);
    }

    @GetMapping("/search/ville")
    public ResponseEntity<List<Partenaire>> getPartenairesByVille(@RequestParam String ville) {
        log.info("GET /partenaires/search/ville?ville={} - Recherche par ville", ville);
        List<Partenaire> partenaires = partenaireService.getPartenairesByVille(ville);
        return ResponseEntity.ok(partenaires);
    }

    @GetMapping("/search/email")
    public ResponseEntity<Partenaire> getPartenaireByEmail(@RequestParam String email) {
        log.info("GET /partenaires/search/email?email={} - Recherche par email", email);
        return partenaireService.getPartenaireByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Partenaire> addPartenaire(@Valid @RequestBody Partenaire partenaire) {
        log.info("POST /partenaires - Création d'un partenaire: {}", partenaire.getNom());
        Partenaire nouveauPartenaire = partenaireService.addPartenaire(partenaire);
        return ResponseEntity.status(HttpStatus.CREATED).body(nouveauPartenaire);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Partenaire> updatePartenaire(@PathVariable Long id, 
                                                        @Valid @RequestBody Partenaire partenaire) {
        log.info("PUT /partenaires/{} - Mise à jour du partenaire", id);
        Partenaire partenaireMAJ = partenaireService.updatePartenaire(id, partenaire);
        return ResponseEntity.ok(partenaireMAJ);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePartenaire(@PathVariable Long id) {
        log.info("DELETE /partenaires/{} - Suppression du partenaire", id);
        partenaireService.deletePartenaire(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countPartenaires() {
        log.info("GET /partenaires/count - Comptage des partenaires");
        long count = partenaireService.countPartenaires();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/exists/email")
    public ResponseEntity<Boolean> existsByEmail(@RequestParam String email) {
        log.info("GET /partenaires/exists/email?email={} - Vérification existence email", email);
        boolean exists = partenaireService.existsByEmail(email);
        return ResponseEntity.ok(exists);
    }
}