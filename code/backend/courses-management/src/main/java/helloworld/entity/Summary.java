package helloworld.entity;

import helloworld.entity.secureentities.StudentRegistration;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Summary {

    List<Cours> courses;

    Map<String, List<StudentRegistration>> regs;

    public Summary(){}

    public Summary(List<Cours> courses, Map<String, List<StudentRegistration>> registrations){

        this.courses = courses;
        this.regs = registrations;
    }

    public List<Cours> getCourses() {
        return courses;
    }

    public void setCourses(List<Cours> courses) {
        this.courses = courses;
    }

    public Map<String, List<StudentRegistration>> getRegs() {
        return regs;
    }

    public void setRegs(Map<String, List<StudentRegistration>> regs) {
        this.regs = regs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Summary summary = (Summary) o;
        return Objects.equals(courses, summary.courses) &&
                Objects.equals(regs, summary.regs);
    }

    @Override
    public int hashCode() {

        return Objects.hash(courses, regs);
    }
}
