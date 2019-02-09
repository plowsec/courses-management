package helloworld.service;

import helloworld.dao.ICourseDAO;
import helloworld.dao.IImplicationDAO;
import helloworld.entity.Cours;
import helloworld.entity.Implication;
import helloworld.entity.JpaCompositePrimaryKeys.ImpliqueComposite;
import helloworld.entity.JpaCompositePrimaryKeys.InscriptionComposite;
import helloworld.entity.Professeur;
import helloworld.entity.secureentities.CoursImpProf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CourseService implements ICourseService {

    @Autowired
    private ICourseDAO courseDAO;

    @Autowired
    private IImplicationDAO implicationDAO;

    @Override
    public Cours getCourseById(int id) throws Exception {
        Cours course = courseDAO.getCourseById(id);
        if (course.getEtat().equals(Cours.STATE_DRAFT)) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String role = authentication.getAuthorities().toArray()[0].toString();
            boolean isProf = role.equals("ROLE_" + Professeur.ROLE_HEADTEACHER) || role.equals("ROLE_" + Professeur.ROLE_TEACHER);
            if (isProf) {
                //check if own course
                boolean isImplicated = implicationDAO
                        .getImplicatedProf(course.getCoursId())
                        .stream()
                        .anyMatch(i -> i.getUserName().equals(authentication.getName()));
                if (!isImplicated) {
                    throw new Exception("Error 403 : a teacher tried to access a course where he is not implicated");
                }
            } else {
                throw new Exception("Error 403 : someone tried to access a draft course");
            }
        }

        return course;
    }

    @Override
    public List<Cours> getAllCourses() {
        return courseDAO.getAllCourses();
    }

    @Override
    public List<Cours> getCourses() {
        List<Cours> safeCourses = new ArrayList<>();
        courseDAO.getCourses().forEach(i -> {
            if (!i.getEtat().equals(Cours.STATE_DRAFT))
                safeCourses.add(i);
        });
        return safeCourses;
    }

    @Override
    public void addCourse(CoursImpProf coursAndProfs) throws Exception {
        if (coursAndProfs.getProfs().isEmpty())
            throw new Exception("Trying to add course without implicated teachers");
        Cours c = new Cours();
        c.setTitre(coursAndProfs.getTitre());
        c.setSemestrePref(coursAndProfs.getSemestrePref());

        String descriptif = coursAndProfs.getDescriptionXML();


        c.setDescriptionXML(descriptif);
        c.setEtudMax(coursAndProfs.getEtudMax());
        c.setEtat(coursAndProfs.getEtat());
        courseDAO.addCourse(c);
        c = courseDAO.getCourseByTitle(coursAndProfs.getTitre());
        Implication i = new Implication();
        ImpliqueComposite ic = new ImpliqueComposite();
        for (Professeur p : coursAndProfs.getProfs()) {
            ic.setFkCours(c.getCoursId());
            ic.setFkProf(p.getUserName());
            i.setImpliqueComposite(ic);
            implicationDAO.addImplication(i);
        }
    }

    private boolean isAuthorizedTeacherForCourse(int coursId, boolean headTeacherAllowed) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String role = authentication.getAuthorities().toArray()[0].toString();
        if (headTeacherAllowed && role.equals("ROLE_" + Professeur.ROLE_HEADTEACHER))
            return true;

        boolean isProf = role.equals("ROLE_" + Professeur.ROLE_TEACHER) || role.equals("ROLE_" + Professeur.ROLE_HEADTEACHER);
        if (isProf) {
            implicationDAO.getImplicatedProf(coursId);
            //check if own course
            return implicationDAO
                    .getImplicatedProf(coursId)
                    .stream()
                    .anyMatch(i -> i.getUserName().equals(authentication.getName()));
        } else {
            return false;
        }
    }

    @Override
    public void updateCourse(CoursImpProf cours) throws Exception {
        if (!isAuthorizedTeacherForCourse(cours.getCoursId(), false))
            throw new Exception("Error 403 : forbidden");
        Cours checkCours = getCourseById(cours.getOldCours().getCoursId());
        if (!checkCours.equals(cours.getOldCours()))
            throw new Exception("Cours descriptif already modified by a other user!");
        Cours c = new Cours();

        String descriptif = cours.getDescriptionXML();
        c.setDescriptionXML(descriptif);
        c.setEtudMax(cours.getEtudMax());
        c.setCoursId(cours.getCoursId());
        c.setEtat(cours.getEtat());
        c.setTitre(cours.getTitre());
        c.setSemestrePref(cours.getSemestrePref());
        courseDAO.updateCourse(c);

        implicationDAO.removeImplications(cours.getCoursId());
        for (Professeur p : cours.getProfs()) {
            Implication i = new Implication();
            ImpliqueComposite ic = new ImpliqueComposite();
            ic.setFkCours(cours.getCoursId());
            ic.setFkProf(p.getUserName());
            i.setImpliqueComposite(ic);
            implicationDAO.addImplication(i);
        }
    }


    @Override
    public void setCourseDraft(int coursId) throws Exception {
        if (!isAuthorizedTeacherForCourse(coursId, true))
            throw new Exception("Error 403 : forbidden");
        courseDAO.setCourseDraft(coursId);
    }

    @Override
    public void setCourseValid(int coursId) throws Exception {
        if (!isAuthorizedTeacherForCourse(coursId, true))
            throw new Exception("Error 403 : forbidden");
        courseDAO.setCourseValid(coursId);
    }

    @Override
    public void setCourseActive(int coursId) throws Exception {
        if (!isAuthorizedTeacherForCourse(coursId, true))
            throw new Exception("Error 403 : forbidden");
        courseDAO.setCourseActive(coursId);
    }

    public Map<Integer, Integer> regsCounts() {
        return courseDAO.regsCounts();
    }

    @Override
    @Deprecated
    public void updateEtatCourse(Cours cours) {
        throw new RuntimeException("I am deprecated");
        //courseDAO.updateEtatCours(cours);
    }

    @Override
    public void closeRegistrations() {
        courseDAO.closeRegistrations();
    }

    @Override
    public List<InscriptionComposite> getCourseRegistrationsById(int id) {
        return courseDAO.getCourseRegistrationsById(id);
    }

}
