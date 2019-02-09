package helloworld.controller;

import helloworld.entity.Periode;
import helloworld.service.IPeriodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;

@RestController
//@RequestMapping("gcc")
public class PeriodController {

    @Autowired
    private IPeriodService periodService;

    @RequestMapping(value = "/period", method = RequestMethod.GET)
    public ResponseEntity<Periode> getPeriod() {
        Periode periode = periodService.getPeriod();
        HttpStatus status;
        if (periode == null)
            status = HttpStatus.NOT_FOUND;
        else
            status = HttpStatus.OK;
        return new ResponseEntity<>(periode, status);
    }

    @PutMapping("/period")
    public ResponseEntity<Void> updatePeriod(@RequestBody @Valid Periode periode, UriComponentsBuilder builder) {
        periodService.updatePeriod(periode);
        return null;
    }
}
