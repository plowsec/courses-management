package helloworld.service;

import helloworld.entity.*;

public interface IPeriodService {

    // -----------------------------------------
    // READ
    // -----------------------------------------

    Periode getPeriod();

    // -----------------------------------------
    // UPDATE
    // -----------------------------------------

    void updatePeriod(Periode period);
}
