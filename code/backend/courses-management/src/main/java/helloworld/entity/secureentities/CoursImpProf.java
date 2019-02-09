package helloworld.entity.secureentities;

import helloworld.entity.Cours;
import helloworld.entity.Professeur;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class CoursImpProf implements Serializable {

    protected Cours oldCours;

    protected int coursId;

    protected String titre;

    protected String semestrePref;

    protected String descriptionXML;

    protected String etat;

    protected int etudMax;

    protected List<Professeur> profs;

    public CoursImpProf() {
    }

    @Override
    public String toString() {
        return "CoursImpProf{" +
                "coursId=" + coursId +
                ", titre='" + titre + '\'' +
                ", semestrePref='" + semestrePref + '\'' +
                ", descriptionXML='" + descriptionXML + '\'' +
                ", etat='" + etat + '\'' +
                ", etudMax=" + etudMax +
                ", profs=" + profs +
                '}';
    }

    public CoursImpProf(CoursImpProf cours, List<Professeur> profs) {
        this.coursId = cours.getCoursId();
        this.titre = cours.getTitre();
        this.semestrePref = cours.getSemestrePref();
        this.descriptionXML = cours.getDescriptionXML();
        this.etat = cours.getEtat();
        this.etudMax = cours.getEtudMax();
        this.profs = profs;
    }

    public Cours getOldCours() {
        return oldCours;
    }

    public void setOldCours(Cours oldCours) {
        this.oldCours = oldCours;
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

    public List<Professeur> getProfs() {
        return profs;
    }

    public void setProfs(List<Professeur> profs) {
        this.profs = profs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CoursImpProf that = (CoursImpProf) o;
        return coursId == that.coursId &&
                etudMax == that.etudMax &&
                Objects.equals(titre, that.titre) &&
                Objects.equals(semestrePref, that.semestrePref) &&
                Objects.equals(descriptionXML, that.descriptionXML) &&
                Objects.equals(etat, that.etat) &&
                Objects.equals(profs, that.profs);
    }

    @Override
    public int hashCode() {

        return Objects.hash(coursId, titre, semestrePref, descriptionXML, etat, etudMax, profs);
    }
}
