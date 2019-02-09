package helloworld.entity;

import javax.persistence.*;

@MappedSuperclass
public abstract class IUser {

    @Id
    @Column(name = "nomUtilisateur")
    protected String userName;

    //@NotNull
    @Column(name = "nom")
    protected String lastName;

    @Column(name = "prenom")
    protected String firstName;

    @Column(name = "motDePasse")
    protected String password;

    @Column(name = "adresse")
    protected String adresse;

    @Transient
    protected String role;



    public IUser() {
    }

    public IUser(IUser user) {
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.password = user.getPassword();
        this.userName = user.getUserName();
        this.adresse = user.getAdresse();
        this.role = user.getRole();
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role)   {
        this.role = role;
    }

    public String getAdresse() {
        return adresse;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getPassword() {
        return password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public void setPassword(String password)    {
        this.password = password;
    }

    /*@ManyToMany
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    public Set<Role> getRoles() {
        return role;
    }*/

    /*public void setRoles(Set<Role> roles) {
        this.role = roles;
    }*/
}
