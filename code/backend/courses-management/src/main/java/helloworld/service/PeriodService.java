package helloworld.service;

import helloworld.dao.IPeriodDAO;
import helloworld.entity.Periode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PeriodService implements IPeriodService {

    @Autowired
    private IPeriodDAO periodDAO;

    @Override
    public Periode getPeriod() {
        return periodDAO.getPeriod();
    }

    @Override
    public void updatePeriod(Periode period) {
        periodDAO.updatePeriod(period);
    }
}
