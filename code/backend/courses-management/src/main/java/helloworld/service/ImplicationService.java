package helloworld.service;

import helloworld.dao.IImplicationDAO;
import helloworld.entity.Implication;
import helloworld.entity.Professeur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImplicationService implements IImplicationService {

    @Autowired
    private IImplicationDAO implicationDAO;


    @Override
    public List<Professeur> getImplicatedProf(int coursId) {

        return implicationDAO.getImplicatedProf(coursId);
    }


    @Override
    public void addImplication(Implication implication) {
        //todo : access validation
        implicationDAO.addImplication(implication);
    }

    @Override
    public void updateImplication(Implication implication) {
        //todo : access validation
    }

    private boolean isAuthorizedTeacherForCourse(int coursId, boolean headTeacherAllowed)   {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String role = authentication.getAuthorities().toArray()[0].toString();
        if(headTeacherAllowed && role.equals("ROLE_"+Professeur.ROLE_HEADTEACHER))
            return true;

        boolean isProf = role.equals("ROLE_"+Professeur.ROLE_TEACHER) || role.equals("ROLE_"+Professeur.ROLE_HEADTEACHER);
        if(isProf)  {
            //check if own course
            return implicationDAO
                    .getImplicatedProf(coursId)
                    .stream()
                    .anyMatch(i -> i.getUserName().equals(authentication.getName()));
        }
        else    {
            return false;
        }
    }
}
