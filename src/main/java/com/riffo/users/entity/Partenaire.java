package com.riffo.users.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "partenaires")
public class Partenaire implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom est obligatoire")
    @Size(min = 2, max = 100, message = "Le nom doit contenir entre 2 et 100 caractères")
    @Column(nullable = false, length = 100)
    private String nom;

    @NotBlank(message = "La catégorie est obligatoire")
    @Column(nullable = false, length = 50)
    private String categorie;  // Sans accent

    @NotBlank(message = "L'adresse est obligatoire")
    @Size(max = 255, message = "L'adresse ne peut pas dépasser 255 caractères")
    @Column(nullable = false, length = 255)
    private String adresse;

    @NotBlank(message = "La ville est obligatoire")
    @Column(nullable = false, length = 50)
    private String ville;

    @NotBlank(message = "Le téléphone est obligatoire")
    @Pattern(regexp = "^(\\+221|0)?[7-8][0-9]{8}$", message = "Format de téléphone invalide (Sénégal)")
    @Column(nullable = false, length = 20)
    private String telephone;  // Sans accent

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Format d'email invalide")
    @Column(nullable = false, length = 100, unique = true)
    private String email;

    @NotNull(message = "La latitude est obligatoire")
    @DecimalMin(value = "-90.0", message = "Latitude doit être entre -90 et 90")
    @DecimalMax(value = "90.0", message = "Latitude doit être entre -90 et 90")
    @Column(nullable = false)
    private Double latitude;

    @NotNull(message = "La longitude est obligatoire")
    @DecimalMin(value = "-180.0", message = "Longitude doit être entre -180 et 180")
    @DecimalMax(value = "180.0", message = "Longitude doit être entre -180 et 180")
    @Column(nullable = false)
    private Double longitude;

    @NotBlank(message = "Le statut est obligatoire")
    @Pattern(regexp = "^(ACTIF|INACTIF|SUSPENDU)$", message = "Statut doit être ACTIF, INACTIF ou SUSPENDU")
    @Column(nullable = false, length = 20)
    private String statut;

    @NotNull(message = "Le plafond de prise en charge est obligatoire")
    @Positive(message = "Le plafond doit être positif")
    @Column(nullable = false)
    private Double plafondPriseEnCharge;

    // Constructeurs
    public Partenaire() {}

    public Partenaire(String nom, String categorie, String adresse, String ville, 
                      String telephone, String email, Double latitude, Double longitude, 
                      String statut, Double plafondPriseEnCharge) {
        this.nom = nom;
        this.categorie = categorie;
        this.adresse = adresse;
        this.ville = ville;
        this.telephone = telephone;
        this.email = email;
        this.latitude = latitude;
        this.longitude = longitude;
        this.statut = statut;
        this.plafondPriseEnCharge = plafondPriseEnCharge;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getCategorie() {
        return categorie;
    }

    public String getAdresse() {
        return adresse;
    }

    public String getVille() {
        return ville;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getEmail() {
        return email;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getStatut() {
        return statut;
    }

    public Double getPlafondPriseEnCharge() {
        return plafondPriseEnCharge;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public void setPlafondPriseEnCharge(Double plafondPriseEnCharge) {
        this.plafondPriseEnCharge = plafondPriseEnCharge;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Partenaire partenaire = (Partenaire) o;
        return Objects.equals(id, partenaire.id) && 
               Objects.equals(email, partenaire.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }

    @Override
    public String toString() {
        return "Partenaire{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", categorie='" + categorie + '\'' +
                ", adresse='" + adresse + '\'' +
                ", ville='" + ville + '\'' +
                ", telephone='" + telephone + '\'' +
                ", email='" + email + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", statut='" + statut + '\'' +
                ", plafondPriseEnCharge=" + plafondPriseEnCharge +
                '}';
    }
}