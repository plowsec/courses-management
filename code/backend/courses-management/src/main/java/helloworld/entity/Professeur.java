package helloworld.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "Professeur")
public class Professeur extends IUser implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String ROLE_TEACHER = "TEACHER";

    public static final String ROLE_HEADTEACHER = "HEADTEACHER";

    @Column(name = "estChefFiliere")
    private boolean isHeadTeacher;

    public boolean isHeadTeacher() {
        return isHeadTeacher;
    }

    public void setHeadTeacher(boolean headTeacher) {
        isHeadTeacher = headTeacher;
    }

    public Professeur() {
    }

    public Professeur(IUser user) {
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.password = user.getPassword();
        this.userName = user.getUserName();
        this.adresse = user.getAdresse();
    }

    @Override
    public String toString() {
        return "Professeur{" +
                "isHeadTeacher=" + isHeadTeacher +
                ", userName='" + userName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", password='" + password + '\'' +
                ", adresse='" + adresse + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
