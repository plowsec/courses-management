package helloworld.entity;

import helloworld.entity.JpaCompositePrimaryKeys.ImpliqueComposite;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "Implique")
public class Implication implements Serializable {

    @EmbeddedId
    @Access(AccessType.PROPERTY)
    private ImpliqueComposite impliqueComposite;

    public Implication() {
    }

    public Implication(ImpliqueComposite impliqueComposite) {
        this.impliqueComposite = impliqueComposite;
    }

    public ImpliqueComposite getImpliqueComposite() {
        return impliqueComposite;
    }

    public void setImpliqueComposite(ImpliqueComposite impliqueComposite) {
        this.impliqueComposite = impliqueComposite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Implication that = (Implication) o;
        return Objects.equals(getImpliqueComposite(), that.getImpliqueComposite());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getImpliqueComposite());
    }
}