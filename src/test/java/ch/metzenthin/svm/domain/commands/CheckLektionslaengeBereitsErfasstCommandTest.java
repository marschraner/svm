package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.entities.Lektionsgebuehren;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Martin Schraner
 */
public class CheckLektionslaengeBereitsErfasstCommandTest {

    private CommandInvoker commandInvoker = new CommandInvokerImpl();
    private List<Lektionsgebuehren> bereitsErfassteLektionsgebuehren = new ArrayList<>();

    @Before
    public void setUp() {
        bereitsErfassteLektionsgebuehren.add(new Lektionsgebuehren(57, new BigDecimal("22.50"), new BigDecimal("21.00"), new BigDecimal("19.00"), new BigDecimal("18.00"), new BigDecimal("17.00"), new BigDecimal("16.00")));
        bereitsErfassteLektionsgebuehren.add(new Lektionsgebuehren(67, new BigDecimal("24.50"), new BigDecimal("23.00"), new BigDecimal("21.00"), new BigDecimal("20.00"), new BigDecimal("19.00"), new BigDecimal("17.00")));
        bereitsErfassteLektionsgebuehren.add(new Lektionsgebuehren(77, new BigDecimal("26.50"), new BigDecimal("25.00"), new BigDecimal("23.00"), new BigDecimal("22.00"), new BigDecimal("21.00"), new BigDecimal("20.00")));
    }

    @Test
    public void testExecute_BezeichnungBereitsInVerwendung() throws Exception {
        Lektionsgebuehren lektionsgebuehren = new Lektionsgebuehren(67, new BigDecimal("25.50"), new BigDecimal("24.00"), new BigDecimal("22.00"), new BigDecimal("21.00"), new BigDecimal("19.00"), new BigDecimal("17.00"));
        CheckLektionslaengeBereitsErfasstCommand checkLektionslaengeBereitsErfasstCommand = new CheckLektionslaengeBereitsErfasstCommand(lektionsgebuehren, null, bereitsErfassteLektionsgebuehren);
        commandInvoker.executeCommand(checkLektionslaengeBereitsErfasstCommand);
        assertTrue(checkLektionslaengeBereitsErfasstCommand.isBereitsErfasst());
    }

    @Test
    public void testExecute_BezeichnungNochNichtInVerwendung() throws Exception {
        Lektionsgebuehren lektionsgebuehren = new Lektionsgebuehren(68, new BigDecimal("25.50"), new BigDecimal("24.00"), new BigDecimal("22.00"), new BigDecimal("21.00"), new BigDecimal("19.00"), new BigDecimal("17.00"));
        CheckLektionslaengeBereitsErfasstCommand checkLektionslaengeBereitsErfasstCommand = new CheckLektionslaengeBereitsErfasstCommand(lektionsgebuehren, null, bereitsErfassteLektionsgebuehren);
        commandInvoker.executeCommand(checkLektionslaengeBereitsErfasstCommand);
        assertFalse(checkLektionslaengeBereitsErfasstCommand.isBereitsErfasst());
    }

    @Test
    public void testExecute_LektionsgebuehrenOrigin() throws Exception {
        Lektionsgebuehren lektionsgebuehren = new Lektionsgebuehren(67, new BigDecimal("24.50"), new BigDecimal("23.00"), new BigDecimal("21.00"), new BigDecimal("20.00"), new BigDecimal("19.00"), new BigDecimal("17.00"));
        CheckLektionslaengeBereitsErfasstCommand checkLektionslaengeBereitsErfasstCommand = new CheckLektionslaengeBereitsErfasstCommand(lektionsgebuehren, bereitsErfassteLektionsgebuehren.get(1), bereitsErfassteLektionsgebuehren);
        commandInvoker.executeCommand(checkLektionslaengeBereitsErfasstCommand);
        assertFalse(checkLektionslaengeBereitsErfasstCommand.isBereitsErfasst());
    }
}