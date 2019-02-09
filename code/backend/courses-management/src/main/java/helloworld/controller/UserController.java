package helloworld.controller;

import helloworld.entity.*;
import helloworld.entity.secureentities.StudentRegistration;
import helloworld.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * nom de l'application : hello
 * add user :          POST /hello/users/
 * get users :         GET /hello/users
 * get user by id :    GET /hello/users/id
 * delete user by id : DELETE /hello/users/id
 */
@RestController
//@RequestMapping("gcc")
public class UserController {

    @Autowired
    private IUserService userService;

    @Deprecated
    @RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
    public ResponseEntity<IUser> getUserById(@PathVariable(value = "id") String userId) {
        throw new RuntimeException("I am deprecated");
        /*IUser user = userService.getUserById(userId);
        HttpStatus status = user == null ? HttpStatus.NOT_FOUND : HttpStatus.OK;
        return new ResponseEntity<>(user, status);*/
    }

    @RequestMapping(value = "/users/{id}", method = RequestMethod.DELETE)
    @Deprecated
    public ResponseEntity<Void> deleteUserById(@PathVariable(value = "id") String userId) {
        throw new RuntimeException("I am deprecated");
        //userService.deleteUserById(userId);
        //return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/users")
    @Deprecated
    public List<IUser> getUsers() {
        throw new RuntimeException("I am deprecated");
        //return userService.getUsers();
    }

    @RequestMapping(value = "/users/students", method = RequestMethod.GET)
    public ResponseEntity<List<Eleve>> getStudents() {
        try {
            return new ResponseEntity<>(userService.getStudents(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            if (e.getMessage().contains("403"))
                status = HttpStatus.FORBIDDEN;
            return new ResponseEntity<>(status);
        }
    }

    @RequestMapping(value = "/users/teachers", method = RequestMethod.GET)
    public ResponseEntity<List<Professeur>> getTeachers() {
        try {
            return new ResponseEntity<>(userService.getTeachers(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            if (e.getMessage().contains("403"))
                status = HttpStatus.FORBIDDEN;
            return new ResponseEntity<>(status);
        }
    }

    @PostMapping("/users")
    public ResponseEntity<Void> addUser(@RequestBody @Valid User user, UriComponentsBuilder builder) {
        userService.addUser(user);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(builder.path("/users/{id}").buildAndExpand(user.getUserName()).toUri());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/users/{id}/registrations", method = RequestMethod.GET)
    public ResponseEntity<Map<String, List<StudentRegistration>>> getRegistrations(@PathVariable(value = "id") String userName) {
        Map<String, List<StudentRegistration>> courses;
        try {
            courses = userService.getRegistrations(userName);
            HttpStatus status = courses == null ? HttpStatus.NOT_FOUND : HttpStatus.OK;
            return new ResponseEntity<>(courses, status);
        } catch (Exception e) {
            e.printStackTrace();
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            if (e.getMessage().contains("403"))
                status = HttpStatus.FORBIDDEN;
            return new ResponseEntity<>(new HashMap<>(), status);
        }
    }

    @RequestMapping(value = "/summary", method = RequestMethod.GET)
    public ResponseEntity<Summary> getSummary() {
        Summary summary = userService.getSummary();
        HttpStatus status = summary == null ? HttpStatus.NOT_FOUND : HttpStatus.OK;
        return new ResponseEntity<>(summary, status);
    }

    @PutMapping("/users/{id}/registrations")
    public ResponseEntity<Void> updateRegistrations(@PathVariable(value = "id") String userName, @RequestBody Inscription[] inscriptions) {
        try {
            userService.updateRegistrations(Arrays.asList(inscriptions));
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            if (e.getMessage().contains("403"))
                status = HttpStatus.FORBIDDEN;
            return new ResponseEntity<Void>(status);
        }
    }

    @GetMapping("/users/{id}/courses")
    public ResponseEntity<List<Cours>> getCoursesByTeacher(@PathVariable(value = "id") String userName) {
        try {
            HttpStatus status = HttpStatus.OK;
            List<Cours> courses = userService.getCoursesByTeacher(userName);
            return new ResponseEntity<List<Cours>>(courses, status);
        } catch (Exception e) {
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            if (e.getMessage().contains("403"))
                status = HttpStatus.FORBIDDEN;
            return new ResponseEntity<List<Cours>>(status);
        }
    }
}
