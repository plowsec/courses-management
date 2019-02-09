package helloworld.dao.dto;

public class NbCourses {
    int fkCours;
    int nbRegistrations;


    public NbCourses(int fkCours, int nbRegistrations) {
        this.fkCours = fkCours;
        this.nbRegistrations = nbRegistrations;
    }

    public NbCourses(int fkCours, long nbRegistrations) {
        this.fkCours = fkCours;
        this.nbRegistrations = (int)nbRegistrations;
    }


    public int getFkCours() {
        return fkCours;
    }

    public void setFkCours(int fkCours) {
        this.fkCours = fkCours;
    }

    public int getNbRegistrations() {
        return nbRegistrations;
    }

    public void setNbRegistrations(int nbRegistrations) {
        this.nbRegistrations = nbRegistrations;
    }
}
