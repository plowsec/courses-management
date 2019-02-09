package helloworld.controller;

import helloworld.entity.Implication;
import helloworld.entity.Professeur;
import helloworld.service.IImplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
public class ImplicationController {

    @Autowired
    private IImplicationService implicationService;

    @RequestMapping(value = "/courses/{id}/implication", method = RequestMethod.GET)
    public List<Professeur> getProfessorsImplicated(@PathVariable(value = "id") int coursId) {
        List<Professeur> profs = implicationService.getImplicatedProf(coursId);
        HttpStatus status = profs == null ? HttpStatus.NOT_FOUND : HttpStatus.OK;
        return profs;
    }

    @PostMapping("/courses/implication")
    ResponseEntity<Void> addImplicatedProfessor(@RequestBody Implication implication, UriComponentsBuilder builder) {
        implicationService.addImplication(implication);
        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

}
