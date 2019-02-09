package helloworld.dao;

import helloworld.entity.Periode;

public interface IPeriodDAO {

    // -----------------------------------------
    // READ
    // -----------------------------------------

    Periode getPeriod();

    // -----------------------------------------
    // UPDATE
    // -----------------------------------------

    void updatePeriod(Periode period);
}
