package com.riffo.users.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.riffo.users.entity.Partenaire;
import com.riffo.users.exception.EmailAlreadyExistsException;
import com.riffo.users.exception.PartenaireNotFoundException;
import com.riffo.users.service.PartenaireService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PartenaireRESTController.class)
class PartenaireRESTControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PartenaireService partenaireService;

    private Partenaire partenaire1;
    private Partenaire partenaire2;

    @BeforeEach
    void setUp() {
        partenaire1 = new Partenaire();
        partenaire1.setId(1L);
        partenaire1.setNom("AXA Assurance");
        partenaire1.setEmail("contact@axa.sn");
        partenaire1.setTelephone("771234567");
        partenaire1.setCategorie("ASSURANCE");
        partenaire1.setStatut("ACTIF");
        partenaire1.setPlafondPriseEnCharge(1000000.0);
        partenaire1.setLatitude(14.7167);
        partenaire1.setLongitude(-17.4677);
        partenaire1.setAdresse("123 Rue de la République");
        partenaire1.setVille("Dakar");

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
    void getAllPartenaires_ShouldReturnListOfPartenaires() throws Exception {
        List<Partenaire> partenaires = Arrays.asList(partenaire1, partenaire2);
        when(partenaireService.getAllPartenaires()).thenReturn(partenaires);

        mockMvc.perform(get("/partenaires")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].nom", is("AXA Assurance")));
    }

    @Test
    void getPartenaireById_ShouldReturnPartenaire_WhenExists() throws Exception {
        when(partenaireService.getPartenaireById(1L)).thenReturn(Optional.of(partenaire1));

        mockMvc.perform(get("/partenaires/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nom", is("AXA Assurance")));
    }

    @Test
    void getPartenaireById_ShouldReturn404_WhenNotExists() throws Exception {
        when(partenaireService.getPartenaireById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/partenaires/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getPartenaireByNom_ShouldReturnPartenaire_WhenExists() throws Exception {
        when(partenaireService.getPartenaireByNom("AXA Assurance")).thenReturn(Optional.of(partenaire1));

        mockMvc.perform(get("/partenaires/search/nom")
                .param("nom", "AXA Assurance"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void getPartenairesByCategorie_ShouldReturnList() throws Exception {
        List<Partenaire> partenaires = Arrays.asList(partenaire1);
        when(partenaireService.getPartenairesByCategorie("ASSURANCE")).thenReturn(partenaires);

        mockMvc.perform(get("/partenaires/search/categorie")
                .param("categorie", "ASSURANCE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void getPartenairesByStatut_ShouldReturnList() throws Exception {
        List<Partenaire> partenaires = Arrays.asList(partenaire1, partenaire2);
        when(partenaireService.getPartenairesByStatut("ACTIF")).thenReturn(partenaires);

        mockMvc.perform(get("/partenaires/search/statut")
                .param("statut", "ACTIF"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void getPartenairesByVille_ShouldReturnList() throws Exception {
        List<Partenaire> partenaires = Arrays.asList(partenaire1, partenaire2);
        when(partenaireService.getPartenairesByVille("Dakar")).thenReturn(partenaires);

        mockMvc.perform(get("/partenaires/search/ville")
                .param("ville", "Dakar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void getPartenaireByEmail_ShouldReturnPartenaire_WhenExists() throws Exception {
        when(partenaireService.getPartenaireByEmail("contact@axa.sn")).thenReturn(Optional.of(partenaire1));

        mockMvc.perform(get("/partenaires/search/email")
                .param("email", "contact@axa.sn"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is("contact@axa.sn")));
    }

    @Test
    void addPartenaire_ShouldReturn201_WhenValid() throws Exception {
        Partenaire newPartenaire = createValidPartenaire();

        Partenaire savedPartenaire = new Partenaire();
        savedPartenaire.setId(3L);
        savedPartenaire.setNom("Allianz");
        savedPartenaire.setEmail("contact@allianz.sn");
        savedPartenaire.setTelephone("781234567");
        savedPartenaire.setCategorie("ASSURANCE");
        savedPartenaire.setStatut("ACTIF");
        savedPartenaire.setPlafondPriseEnCharge(2000000.0);
        savedPartenaire.setLatitude(14.7167);
        savedPartenaire.setLongitude(-17.4677);
        savedPartenaire.setAdresse("456 Avenue");
        savedPartenaire.setVille("Dakar");

        when(partenaireService.addPartenaire(any(Partenaire.class))).thenReturn(savedPartenaire);

        mockMvc.perform(post("/partenaires")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newPartenaire)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.nom", is("Allianz")));
    }

    @Test
    void addPartenaire_ShouldReturn409_WhenEmailAlreadyExists() throws Exception {
        Partenaire newPartenaire = createValidPartenaire();
        newPartenaire.setEmail("existing@test.com");

        when(partenaireService.addPartenaire(any(Partenaire.class)))
            .thenThrow(new EmailAlreadyExistsException("existing@test.com"));

        mockMvc.perform(post("/partenaires")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newPartenaire)))
                .andExpect(status().isConflict());
    }

    @Test
    void updatePartenaire_ShouldReturn200_WhenValid() throws Exception {
        Partenaire updatedPartenaire = createValidPartenaire();
        updatedPartenaire.setNom("AXA Assurance Sénégal");

        Partenaire savedPartenaire = new Partenaire();
        savedPartenaire.setId(1L);
        savedPartenaire.setNom("AXA Assurance Sénégal");
        savedPartenaire.setEmail("contact@axa.sn");
        savedPartenaire.setTelephone("771234567");
        savedPartenaire.setCategorie("ASSURANCE");
        savedPartenaire.setStatut("ACTIF");
        savedPartenaire.setPlafondPriseEnCharge(1000000.0);
        savedPartenaire.setLatitude(14.7167);
        savedPartenaire.setLongitude(-17.4677);
        savedPartenaire.setAdresse("123 Rue");
        savedPartenaire.setVille("Dakar");

        when(partenaireService.updatePartenaire(eq(1L), any(Partenaire.class))).thenReturn(savedPartenaire);

        mockMvc.perform(put("/partenaires/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedPartenaire)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nom", is("AXA Assurance Sénégal")));
    }

    @Test
    void updatePartenaire_ShouldReturn404_WhenPartenaireNotFound() throws Exception {
        Partenaire updatedPartenaire = createValidPartenaire();
        updatedPartenaire.setNom("Inexistant");

        when(partenaireService.updatePartenaire(eq(999L), any(Partenaire.class)))
            .thenThrow(new PartenaireNotFoundException(999L));

        mockMvc.perform(put("/partenaires/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedPartenaire)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deletePartenaire_ShouldReturn204_WhenExists() throws Exception {
        doNothing().when(partenaireService).deletePartenaire(1L);

        mockMvc.perform(delete("/partenaires/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deletePartenaire_ShouldReturn404_WhenNotExists() throws Exception {
        doThrow(new PartenaireNotFoundException(999L)).when(partenaireService).deletePartenaire(999L);

        mockMvc.perform(delete("/partenaires/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void countPartenaires_ShouldReturnCount() throws Exception {
        when(partenaireService.countPartenaires()).thenReturn(5L);

        mockMvc.perform(get("/partenaires/count"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));
    }

    @Test
    void existsByEmail_ShouldReturnTrue_WhenEmailExists() throws Exception {
        when(partenaireService.existsByEmail("contact@axa.sn")).thenReturn(true);

        mockMvc.perform(get("/partenaires/exists/email")
                .param("email", "contact@axa.sn"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void existsByEmail_ShouldReturnFalse_WhenEmailNotExists() throws Exception {
        when(partenaireService.existsByEmail("nonexistent@test.com")).thenReturn(false);

        mockMvc.perform(get("/partenaires/exists/email")
                .param("email", "nonexistent@test.com"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    private Partenaire createValidPartenaire() {
        Partenaire partenaire = new Partenaire();
        partenaire.setNom("Allianz");
        partenaire.setEmail("contact@allianz.sn");
        partenaire.setTelephone("781234567");
        partenaire.setCategorie("ASSURANCE");
        partenaire.setStatut("ACTIF");
        partenaire.setPlafondPriseEnCharge(2000000.0);
        partenaire.setLatitude(14.7167);
        partenaire.setLongitude(-17.4677);
        partenaire.setAdresse("456 Avenue de l'Indépendance");
        partenaire.setVille("Dakar");
        return partenaire;
    }
}