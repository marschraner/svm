package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.LektionsgebuehrenDao;
import ch.metzenthin.svm.persistence.entities.Lektionsgebuehren;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Martin Schraner
 */
public class FindAllLektionsgebuehrenCommand implements Command {

    private final LektionsgebuehrenDao lektionsgebuehrenDao = new LektionsgebuehrenDao();

    // output
    private List<Lektionsgebuehren> lektionsgebuehrenAllList;
    private Map<Integer, BigDecimal[]> lektionsgebuehrenAllMap = new HashMap<>();

    @Override
    public void execute() {
        lektionsgebuehrenAllList = lektionsgebuehrenDao.findAll();
        for (Lektionsgebuehren lektionsgebuehren : lektionsgebuehrenAllList) {
            BigDecimal[] lektionsgebuehrenArray = new BigDecimal[]{lektionsgebuehren.getBetrag1Kind(), lektionsgebuehren.getBetrag2Kinder(),
                    lektionsgebuehren.getBetrag3Kinder(), lektionsgebuehren.getBetrag4Kinder(), lektionsgebuehren.getBetrag5Kinder(), lektionsgebuehren.getBetrag6Kinder()};
            lektionsgebuehrenAllMap.put(lektionsgebuehren.getLektionslaenge(), lektionsgebuehrenArray);
        }
    }

    public List<Lektionsgebuehren> getLektionsgebuehrenAllList() {
        return lektionsgebuehrenAllList;
    }

    public Map<Integer, BigDecimal[]> getLektionsgebuehrenAllMap() {
        return lektionsgebuehrenAllMap;
    }

}
