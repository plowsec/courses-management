package helloworld.entity.JpaCompositePrimaryKeys;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class InscriptionComposite implements Serializable {

    @NotNull
    @Size(max = 20)
    private String fkEleve;

    @NotNull
    @Column(name="fkCours")
    private int fkCours;

    public InscriptionComposite() {
    }

    public InscriptionComposite(String fkEleve, int fkCours) {
        this.fkEleve = fkEleve;
        this.fkCours = fkCours;
    }

    public String getFkEleve() {
        return fkEleve;
    }

    public void setFkEleve(String fkEleve) {
        this.fkEleve = fkEleve;
    }

    public int getFkCours() {
        return fkCours;
    }

    public void setFkCours(int fkCours) {
        this.fkCours = fkCours;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InscriptionComposite that = (InscriptionComposite) o;
        return getFkCours() == that.getFkCours() &&
                Objects.equals(getFkEleve(), that.getFkEleve());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFkEleve(), getFkCours());
    }

    @Override
    public String toString() {
        return "InscriptionComposite{" +
                "fkEleve='" + fkEleve + '\'' +
                ", fkCours=" + fkCours +
                '}';
    }
}
