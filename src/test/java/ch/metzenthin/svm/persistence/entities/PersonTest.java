package ch.metzenthin.svm.persistence.entities;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Martin Schraner
 */
public class PersonTest {
    
    private Person person = new Angehoeriger();

    @Test
    public void testGetEmailToBeDisplayedInWord() throws Exception {
        
        // Keine Email
        person.setEmail(null);
        assertEquals("", person.getEmailToBeDisplayedInWord());
        
        // 1 Email
        person.setEmail("felix.muster@gmail.com");
        assertEquals("felix.muster@gmail.com", person.getEmailToBeDisplayedInWord());

        // 2 Emails
        person.setEmail("felix.muster@gmail.com, felix@gmx.ch, felix.muster@bluewin.ch");
        assertEquals("felix.muster@gmail.com, felix@gmx.ch", person.getEmailToBeDisplayedInWord());
        
        // 3 Emails (dritte länger als Maximallänge)
        person.setEmail("felix.muster@gmail.com, felix@gmx.ch, felix.muster@bluewin.ch");
        assertEquals("felix.muster@gmail.com, felix@gmx.ch", person.getEmailToBeDisplayedInWord());

        // 1 überlange Email
        person.setEmail("felix.alexander.muster.zuerich@schraneroffsetdruck.com");
        assertEquals("felix.alexander.muster.zuerich@schraneroffsetdruck.com", person.getEmailToBeDisplayedInWord());
    }
}