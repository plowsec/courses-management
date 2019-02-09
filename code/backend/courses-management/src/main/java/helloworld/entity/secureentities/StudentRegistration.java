package helloworld.entity.secureentities;

import helloworld.entity.Cours;

public class StudentRegistration {

    int priorite;

    Cours cours;

    public StudentRegistration(int priorite, Cours cours)    {
        this.priorite = priorite;
        this.cours = cours;
    }

    public int getPriorite() {
        return priorite;
    }

    public void setPriorite(int priorite) {
        this.priorite = priorite;
    }

    public Cours getCours() {
        return cours;
    }

    public void setCours(Cours cours) {
        this.cours = cours;
    }
}
