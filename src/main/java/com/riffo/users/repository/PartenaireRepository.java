package com.riffo.users.repository;

import com.riffo.users.entity.Partenaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PartenaireRepository extends JpaRepository<Partenaire, Long> {
    
    Optional<Partenaire> findByNom(String nom);
    
    List<Partenaire> findByCategorie(String categorie);  // Sans accent
    
    List<Partenaire> findByStatut(String statut);
    
    List<Partenaire> findByVille(String ville);
    
    Optional<Partenaire> findByEmail(String email);
    
    List<Partenaire> findByStatutIgnoreCase(String statut);
    
    boolean existsByEmail(String email);
}