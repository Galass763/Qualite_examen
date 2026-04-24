package com.riffo.users.service.impl;

import com.riffo.users.entity.Partenaire;
import com.riffo.users.exception.EmailAlreadyExistsException;
import com.riffo.users.exception.InvalidPartenaireDataException;
import com.riffo.users.exception.PartenaireNotFoundException;
import com.riffo.users.repository.PartenaireRepository;
import com.riffo.users.service.PartenaireService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PartenaireServiceImpl implements PartenaireService {

    private static final Logger log = LoggerFactory.getLogger(PartenaireServiceImpl.class);

    @Autowired
    private PartenaireRepository partenaireRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Partenaire> getAllPartenaires() {
        log.info("Récupération de tous les partenaires");
        return partenaireRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Partenaire> getPartenaireById(Long id) {
        log.info("Recherche du partenaire avec ID: {}", id);
        return partenaireRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Partenaire> getPartenaireByNom(String nom) {
        log.info("Recherche du partenaire avec nom: {}", nom);
        return partenaireRepository.findByNom(nom);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Partenaire> getPartenairesByCategorie(String categorie) {
        log.info("Recherche des partenaires par catégorie: {}", categorie);
        return partenaireRepository.findByCategorie(categorie);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Partenaire> getPartenairesByStatut(String statut) {
        log.info("Recherche des partenaires par statut: {}", statut);
        return partenaireRepository.findByStatut(statut);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Partenaire> getPartenairesByVille(String ville) {
        log.info("Recherche des partenaires par ville: {}", ville);
        return partenaireRepository.findByVille(ville);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Partenaire> getPartenaireByEmail(String email) {
        log.info("Recherche du partenaire par email: {}", email);
        return partenaireRepository.findByEmail(email);
    }

    @Override
    public Partenaire addPartenaire(Partenaire partenaire) {
        validatePartenaire(partenaire, true);
        
        log.info("Création d'un nouveau partenaire: {}", partenaire.getNom());
        
        if (existsByEmail(partenaire.getEmail())) {
            log.warn("Tentative de création avec email existant: {}", partenaire.getEmail());
            throw new EmailAlreadyExistsException(partenaire.getEmail());
        }
        
        Partenaire saved = partenaireRepository.save(partenaire);
        log.info("Partenaire créé avec succès - ID: {}", saved.getId());
        return saved;
    }

    @Override
    public Partenaire updatePartenaire(Long id, Partenaire partenaire) {
        log.info("Mise à jour du partenaire ID: {}", id);
        
        if (id == null || id <= 0) {
            throw new InvalidPartenaireDataException("L'ID du partenaire est invalide");
        }
        
        if (partenaire == null) {
            throw new InvalidPartenaireDataException("Le partenaire ne peut pas être null");
        }
        
        Partenaire existing = partenaireRepository.findById(id)
            .orElseThrow(() -> new PartenaireNotFoundException(id));
        
        if (!existing.getEmail().equals(partenaire.getEmail()) && 
            partenaireRepository.existsByEmail(partenaire.getEmail())) {
            throw new EmailAlreadyExistsException(partenaire.getEmail());
        }
        
        if (partenaire.getNom() != null) existing.setNom(partenaire.getNom());
        if (partenaire.getCategorie() != null) existing.setCategorie(partenaire.getCategorie());
        if (partenaire.getAdresse() != null) existing.setAdresse(partenaire.getAdresse());
        if (partenaire.getVille() != null) existing.setVille(partenaire.getVille());
        if (partenaire.getTelephone() != null) existing.setTelephone(partenaire.getTelephone());
        if (partenaire.getEmail() != null) existing.setEmail(partenaire.getEmail());
        if (partenaire.getLatitude() != null) existing.setLatitude(partenaire.getLatitude());
        if (partenaire.getLongitude() != null) existing.setLongitude(partenaire.getLongitude());
        if (partenaire.getStatut() != null) existing.setStatut(partenaire.getStatut());
        if (partenaire.getPlafondPriseEnCharge() != null) existing.setPlafondPriseEnCharge(partenaire.getPlafondPriseEnCharge());
        
        Partenaire updated = partenaireRepository.save(existing);
        log.info("Partenaire mis à jour avec succès - ID: {}", id);
        return updated;
    }

    @Override
    public void deletePartenaire(Long id) {
        log.info("Suppression du partenaire ID: {}", id);
        
        if (id == null || id <= 0) {
            throw new InvalidPartenaireDataException("L'ID du partenaire est invalide");
        }
        
        if (!partenaireRepository.existsById(id)) {
            throw new PartenaireNotFoundException(id);
        }
        
        partenaireRepository.deleteById(id);
        log.info("Partenaire supprimé avec succès - ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public long countPartenaires() {
        return partenaireRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return partenaireRepository.existsByEmail(email);
    }

    private void validatePartenaire(Partenaire partenaire, boolean isNew) {
        if (partenaire == null) {
            throw new InvalidPartenaireDataException("Le partenaire ne peut pas être null");
        }
        
        if (isNew && partenaire.getId() != null) {
            throw new InvalidPartenaireDataException("L'ID ne doit pas être fourni pour une création");
        }
    }
}