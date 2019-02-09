package helloworld.controller;

import helloworld.entity.Cours;
import helloworld.entity.JpaCompositePrimaryKeys.InscriptionComposite;
import helloworld.entity.secureentities.CoursImpProf;
import helloworld.service.ICourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@RestController
//@RequestMapping("gcc")
public class CourseController {

    @Autowired
    private ICourseService courseService;

    @RequestMapping(value = "/courses/{id}", method = RequestMethod.GET)
    public ResponseEntity<Cours> getCourseById(@PathVariable(value = "id") int id) {
        try {
            Cours course = courseService.getCourseById(id);
            HttpStatus status = course == null ? HttpStatus.NOT_FOUND : HttpStatus.OK;
            return new ResponseEntity<>(course, status);
        } catch (Exception e) {
            e.printStackTrace();
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            if (e.getMessage().contains("403"))
                status = HttpStatus.FORBIDDEN;
            return new ResponseEntity<Cours>(status);
        }
    }

    /**
     * access validated : only headteacher allowed
     *
     * @return the courses in the db that have either a state of 'valid' or 'active'
     */
    @GetMapping("/courses")
    public List<Cours> getCourses() {
        return courseService.getCourses();
    }

    /**
     * access validated : only headteacher allowed
     *
     * @return absolutely all the courses in the db
     */
    @RequestMapping(value = "/courses/all", method = RequestMethod.GET)
    public List<Cours> getAllCourses() {
        return courseService.getAllCourses();
    }

    @GetMapping("/courses/end")
    public ResponseEntity<Void> closeRegistrations() {
        try {
            courseService.closeRegistrations();
        } catch (Exception e) {
            e.printStackTrace();
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            if (e.getMessage().contains("403"))
                status = HttpStatus.FORBIDDEN;
            return new ResponseEntity<>(status);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/courses/{id}/active")
    public ResponseEntity<Void> setCourseActive(@PathVariable(value = "id") int coursId, UriComponentsBuilder builder) {
        try {
            courseService.setCourseActive(coursId);
        } catch (Exception e) {
            e.printStackTrace();
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            if (e.getMessage().contains("403"))
                status = HttpStatus.FORBIDDEN;
            return new ResponseEntity<Void>(status);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/courses/{id}/valid")
    public ResponseEntity<Void> setCourseValid(@PathVariable(value = "id") int coursId, UriComponentsBuilder builder) {
        try {
            courseService.setCourseValid(coursId);
        } catch (Exception e) {
            e.printStackTrace();
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            if (e.getMessage().contains("403"))
                status = HttpStatus.FORBIDDEN;
            return new ResponseEntity<Void>(status);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/courses/{id}/draft")
    public ResponseEntity<Void> setCourseDraft(@PathVariable(value = "id") int coursId, UriComponentsBuilder builder) {
        try {
            courseService.setCourseDraft(coursId);
        } catch (Exception e) {
            e.printStackTrace();
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            if (e.getMessage().contains("403"))
                status = HttpStatus.FORBIDDEN;
            return new ResponseEntity<Void>(status);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/courses/update")
    public ResponseEntity<Void> updateCours(@RequestBody CoursImpProf cours) {
        try {
            courseService.updateCourse(cours);
        } catch (Exception e) {
            e.printStackTrace();
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            if (e.getMessage().contains("403"))
                status = HttpStatus.FORBIDDEN;
            return new ResponseEntity<Void>(status);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Deprecated
    @PostMapping("/courses/update/etat")
    public ResponseEntity<Void> updateEtatCours(@RequestBody Cours cours) {
        throw new RuntimeException("I am deprecated");
        /*courseService.updateEtatCourse(cours);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);*/
    }

    @PostMapping("/courses")
    public ResponseEntity<Void> addCours(@RequestBody CoursImpProf coursAndProfs, UriComponentsBuilder builder) {
        try {
            courseService.addCourse(coursAndProfs);
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(builder.path("/courses/{id}").buildAndExpand(coursAndProfs.getCoursId()).toUri());
            return new ResponseEntity<>(headers, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            return new ResponseEntity<Void>(status);
        }
    }

    @RequestMapping(value = "/registrations/count", method = RequestMethod.GET)
    public Map<Integer, Integer> regsCounts() {
        return courseService.regsCounts();
    }


    @RequestMapping(value = "/courses/{id}/registrations", method = RequestMethod.GET)
    public ResponseEntity<List<InscriptionComposite>> getCourseRegistrationsById(@PathVariable(value = "id") int id) {
        List<InscriptionComposite> inscriptions;
        try {
            inscriptions = courseService.getCourseRegistrationsById(id);
            HttpStatus status = HttpStatus.OK;
            return new ResponseEntity<>(inscriptions, status);
        } catch (Exception e) {
            e.printStackTrace();
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            if (e.getMessage().contains("403"))
                status = HttpStatus.FORBIDDEN;
            return new ResponseEntity<>(status);
        }
    }

    /*
    this won't compile....

    @PostMapping("/courses")
    @CrossOrigin(origins = "*")
    public ResponseEntity<Void> addCourse(@RequestBody @Valid Cours cours, UriComponentsBuilder builder) {
        return null;
    }

    @PostMapping("/courses")
    @CrossOrigin(origins = "*")
    public ResponseEntity<Void> updateCourse(@RequestBody @Valid Cours cours, UriComponentsBuilder builder) {
        return null;
    }*/
}
