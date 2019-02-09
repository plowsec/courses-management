package helloworld.entity.JpaCompositePrimaryKeys;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ImpliqueComposite implements Serializable {

    @NotNull
    @Size(max = 20)
    private String fkProf;

    @NotNull
    private int fkCours;

    public ImpliqueComposite() {
    }

    public ImpliqueComposite(String fkProf, int fkCours) {
        this.fkProf = fkProf;
        this.fkCours = fkCours;
    }

    public String getFkProf() {
        return fkProf;
    }

    public void setFkProf(String fkProf) {
        this.fkProf = fkProf;
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
        ImpliqueComposite that = (ImpliqueComposite) o;
        return getFkCours() == that.getFkCours() &&
                Objects.equals(getFkProf(), that.getFkProf());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFkProf(), getFkCours());
    }
}
