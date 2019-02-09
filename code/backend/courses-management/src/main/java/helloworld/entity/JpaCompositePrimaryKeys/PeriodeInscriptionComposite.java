package helloworld.entity.JpaCompositePrimaryKeys;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Embeddable
public class PeriodeInscriptionComposite implements Serializable {

    @NotNull
    private Date dateDebut;

    @NotNull
    private Date dateFin;

    public PeriodeInscriptionComposite() {
    }

    public PeriodeInscriptionComposite(@NotNull Date dateDebut, @NotNull Date dateFin) {
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PeriodeInscriptionComposite that = (PeriodeInscriptionComposite) o;
        return Objects.equals(getDateDebut(), that.getDateDebut()) &&
                Objects.equals(getDateFin(), that.getDateFin());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDateDebut(), getDateFin());
    }

    @Override
    public String toString() {
        return "PeriodeInscriptionComposite{" +
                "dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                '}';
    }
}
