package helloworld.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "Cours")
public class Cours implements Serializable {

    @Transient
    public static final String STATE_DRAFT = "Brouillon";

    @Id
    @Column(name = "coursId")
    protected int coursId;

    @NotNull
    @Column(name = "titre")
    protected String titre;

    @NotNull
    @Column(name = "semestrePref")
    protected String semestrePref;

    @NotNull
    @Column(name = "descriptionXML")
    protected String descriptionXML;

    @NotNull
    @Column(name = "etat")
    protected String etat;

    @Column(name = "etudMax")
    protected int etudMax;

    public Cours() {
    }

    public Cours(Cours cours) {
        this.coursId = cours.getCoursId();
        this.titre = cours.getTitre();
        this.semestrePref = cours.getSemestrePref();
        this.descriptionXML = cours.getDescriptionXML();
        this.etat = cours.getEtat();
        this.etudMax = cours.getEtudMax();
    }

    public int getCoursId() {
        return coursId;
    }

    public void setCoursId(int coursId) {
        this.coursId = coursId;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getSemestrePref() {
        return semestrePref;
    }

    public void setSemestrePref(String semestrePref) {
        this.semestrePref = semestrePref;
    }

    public String getDescriptionXML() {
        return descriptionXML;
    }

    public void setDescriptionXML(String descriptionXML) {
        this.descriptionXML = descriptionXML;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public int getEtudMax() {
        return etudMax;
    }

    public void setEtudMax(int etudMax) {
        this.etudMax = etudMax;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cours cours = (Cours) o;
        return getCoursId() == cours.getCoursId() &&
                getEtudMax() == cours.getEtudMax() &&
                Objects.equals(getTitre(), cours.getTitre()) &&
                Objects.equals(getSemestrePref(), cours.getSemestrePref()) &&
                Objects.equals(getDescriptionXML(), cours.getDescriptionXML()) &&
                Objects.equals(getEtat(), cours.getEtat());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCoursId(), getTitre(), getSemestrePref(), getDescriptionXML(), getEtat(), getEtudMax());
    }
}
