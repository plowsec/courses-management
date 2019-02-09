package helloworld.entity;

import helloworld.entity.JpaCompositePrimaryKeys.InscriptionComposite;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "Inscription")
public class Inscription implements Serializable {

    @Transient
    public static final long NB_REGS_PRIORITY_ONE = 4;

    @Transient
    public static final long NB_REGS_PRIORITY_TWO = 1;

    @EmbeddedId
    @Access(AccessType.PROPERTY)
    private InscriptionComposite inscriptionComposite;

    @Column(name="priorite")
    private int priorite;

    public Inscription() {
    }

    public Inscription(Inscription inscription) {
        this.inscriptionComposite = inscription.getInscriptionComposite();
        this.priorite = inscription.getPriorite();
    }

    public Inscription(int priorite,String fkEleve, int coursId)    {
        this.inscriptionComposite = new InscriptionComposite(fkEleve, coursId);
        this.priorite = priorite;
    }

    public InscriptionComposite getInscriptionComposite() {
        return inscriptionComposite;
    }

    public void setInscriptionComposite(InscriptionComposite inscriptionComposite) {
        this.inscriptionComposite = inscriptionComposite;
    }

    public int getPriorite() {
        return priorite;
    }

    public void setPriorite(int priorite) {
        this.priorite = priorite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Inscription that = (Inscription) o;
        return priorite == that.priorite &&
                Objects.equals(inscriptionComposite, that.inscriptionComposite);
    }

    @Override
    public int hashCode() {
        return Objects.hash(inscriptionComposite, priorite);
    }

    @Override
    public String toString() {
        return "Inscription{" +
                "inscriptionComposite=" + inscriptionComposite +
                ", priorite=" + priorite +
                '}';
    }
}