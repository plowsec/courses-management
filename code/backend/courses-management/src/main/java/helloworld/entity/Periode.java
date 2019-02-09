package helloworld.entity;

import helloworld.entity.JpaCompositePrimaryKeys.PeriodeInscriptionComposite;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "PeriodeInscription")
public class Periode implements Serializable {

    @EmbeddedId
    @Access(AccessType.PROPERTY)
    private PeriodeInscriptionComposite periodeInscriptionComposite;

    public Periode() {
    }

    public Periode(PeriodeInscriptionComposite periodeInscriptionComposite) {
        this.periodeInscriptionComposite = periodeInscriptionComposite;
    }

    public PeriodeInscriptionComposite getPeriodeInscriptionComposite() {
        return periodeInscriptionComposite;
    }

    public void setPeriodeInscriptionComposite(PeriodeInscriptionComposite periodeInscriptionComposite) {
        this.periodeInscriptionComposite = periodeInscriptionComposite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Periode periode = (Periode) o;
        return Objects.equals(getPeriodeInscriptionComposite(), periode.getPeriodeInscriptionComposite());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPeriodeInscriptionComposite());
    }

    @Override
    public String toString() {
        return "Periode{" +
                "periodeInscriptionComposite=" + periodeInscriptionComposite +
                '}';
    }
}
