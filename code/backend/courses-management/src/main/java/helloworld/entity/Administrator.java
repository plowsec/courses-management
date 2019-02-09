package helloworld.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "Administrator")
public class Administrator extends IUser implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String ROLE = "ADMIN";

    public Administrator() {
    }

    public Administrator(IUser user) {
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.password = user.getPassword();
        this.userName = user.getUserName();
        this.adresse = user.getAdresse();
    }
}
