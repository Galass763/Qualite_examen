package com.riffo.users.service;

import com.riffo.users.entity.Partenaire;
import com.riffo.users.exception.EmailAlreadyExistsException;
import com.riffo.users.exception.InvalidPartenaireDataException;
import com.riffo.users.exception.PartenaireNotFoundException;
import com.riffo.users.repository.PartenaireRepository;
import com.riffo.users.service.impl.PartenaireServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PartenaireServiceImplTest {

    @Mock
    private PartenaireRepository partenaireRepository;

    @InjectMocks
    private PartenaireServiceImpl partenaireService;

    private Partenaire partenaire;
    private Partenaire partenaire2;

    @BeforeEach
    void setUp() {
        partenaire = new Partenaire();
        // NE PAS SETTER L'ID - Important pour le test de création !
        partenaire.setNom("AXA Assurance");
        partenaire.setEmail("contact@axa.sn");
        partenaire.setTelephone("771234567");
        partenaire.setCategorie("ASSURANCE");
        partenaire.setStatut("ACTIF");
        partenaire.setPlafondPriseEnCharge(1000000.0);
        partenaire.setLatitude(14.7167);
        partenaire.setLongitude(-17.4677);
        partenaire.setAdresse("123 Rue de la République");
        partenaire.setVille("Dakar");

        partenaire2 = new Partenaire();
        partenaire2.setId(2L);
        partenaire2.setNom("Sonatel Academy");
        partenaire2.setEmail("info@sonatelacademy.sn");
        partenaire2.setTelephone("781234567");
        partenaire2.setCategorie("FORMATION");
        partenaire2.setStatut("ACTIF");
        partenaire2.setPlafondPriseEnCharge(500000.0);
        partenaire2.setLatitude(14.7167);
        partenaire2.setLongitude(-17.4677);
        partenaire2.setAdresse("Liberté 6 extension");
        partenaire2.setVille("Dakar");
    }

    @Test
    void addPartenaire_ShouldSucceed_WhenDataIsValid() {
        when(partenaireRepository.existsByEmail(partenaire.getEmail())).thenReturn(false);
        
        Partenaire savedPartenaire = new Partenaire();
        savedPartenaire.setId(1L);
        savedPartenaire.setNom(partenaire.getNom());
        savedPartenaire.setEmail(partenaire.getEmail());
        when(partenaireRepository.save(any(Partenaire.class))).thenReturn(savedPartenaire);

        Partenaire result = partenaireService.addPartenaire(partenaire);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getNom()).isEqualTo("AXA Assurance");
        
        verify(partenaireRepository, times(1)).save(any(Partenaire.class));
    }

    @Test
    void addPartenaire_ShouldThrowException_WhenEmailAlreadyExists() {
        when(partenaireRepository.existsByEmail(partenaire.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> partenaireService.addPartenaire(partenaire))
            .isInstanceOf(EmailAlreadyExistsException.class)
            .hasMessageContaining(partenaire.getEmail());
        
        verify(partenaireRepository, never()).save(any(Partenaire.class));
    }

    @Test
    void getPartenaireById_ShouldReturnPartenaire_WhenExists() {
        Partenaire existingPartenaire = new Partenaire();
        existingPartenaire.setId(1L);
        existingPartenaire.setNom("AXA Assurance");
        
        when(partenaireRepository.findById(1L)).thenReturn(Optional.of(existingPartenaire));

        Optional<Partenaire> result = partenaireService.getPartenaireById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
    }

    @Test
    void getPartenaireById_ShouldReturnEmpty_WhenNotExists() {
        when(partenaireRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Partenaire> result = partenaireService.getPartenaireById(999L);

        assertThat(result).isEmpty();
    }

    @Test
    void deletePartenaire_ShouldSucceed_WhenExists() {
        when(partenaireRepository.existsById(1L)).thenReturn(true);
        doNothing().when(partenaireRepository).deleteById(1L);

        partenaireService.deletePartenaire(1L);

        verify(partenaireRepository, times(1)).deleteById(1L);
    }

    @Test
    void deletePartenaire_ShouldThrowException_WhenNotExists() {
        when(partenaireRepository.existsById(999L)).thenReturn(false);

        assertThatThrownBy(() -> partenaireService.deletePartenaire(999L))
            .isInstanceOf(PartenaireNotFoundException.class);
    }
}