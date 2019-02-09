package helloworld.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "Eleve")
public class Eleve extends IUser implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String ROLE = "STUDENT";

    public Eleve() {
    }

    @Override
    public String toString() {
        return "Eleve{" +
                "userName='" + userName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", password='" + password + '\'' +
                ", adresse='" + adresse + '\'' +
                '}';
    }

    public Eleve(IUser user) {
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.password = user.getPassword();
        this.userName = user.getUserName();
        this.adresse = user.getAdresse();
        this.role = ROLE;
    }
}
