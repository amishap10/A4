
import org.junit.jupiter.api.Test;

import au.edu.rmit.sct.Person;

import static org.junit.jupiter.api.Assertions.*;

public class PersonTest {


    //Test Case 1: vaild personID
    @Test
    void validPerson() {
        Person person = new Person(
            "36!@&s*XYP",
            "Emily",
            "Smith",
            "124|La Trobe Street|Melbourne|Victoria|Australia",
            "05-05-2005"
            );
        assertTrue(person.addPerson());
    }

    //Test Case 2: invalid birthdate format
    @Test
    void invalidBirthday() {
        Person person = new Person(
            "36!@&*XYP",
            "Emily",
            "Smith",
            "124|La Trobe Street|Melbourne|Victoria|Australia",
            "2005-05-05"
            );
        assertFalse(person.addPerson());
    }

    //Test Case 3: invalid state
    @Test
    void invalidAddress() {
        Person person = new Person(
            "36!@&*XYP",
            "Emily",
            "Smith",
            "124|La Trobe Street|Melbourne|Tasmania|Australia",
            "05-05-2005"
            );
        assertFalse(person.addPerson());
    }

    //Test Case 4: invalid address format
    @Test
    void invalidAddressFormat() {
        Person person = new Person(
            "36!@&*XYP",
            "Emily",
            "Smith",
            "124|La Trobe Street|Melbourne|Victoria",
            "05-05-2005"
            );
        assertFalse(person.addPerson());
    }

    //Test Case 5: invalid no special characters
    @Test
    void invalidNoSpecialChar() {
        Person person = new Person(
            "36lmno12YP",
            "Emily",
            "Smith",
            "124|La Trobe Street|Melbourne|Tasmania|Australia",
            "05-05-2005"
            );
        assertFalse(person.addPerson());
    }

    // Test Case 6: Address change not allowed if under 18
    @Test
    public void updatePersonalDetails_testCase1() {
        Person child = new Person(
            "56s_d%&fAA", 
            "Alice", 
            "Young", 
            "123|Circle St|Carlton|Victoria|Australia", 
            "10-05-2010"
            );
        assertTrue(child.addPerson());
        boolean result = child.updatePersonalDetails(
            "56s_d%&fAA", 
            "Alice", 
            "Young", 
            "456|Tree Ave|Carlton|Victoria|Australia", 
            "10-05-2010"
            );
        assertFalse(result);
    }
    
    // Test Case 7: Only birthday changes — allowed

    @Test
    public void updatePersonalDetails_testCase2() {
        Person adult = new Person(
            "56s_d%&fAB", 
            "Bob", 
            "Smith", 
            "456|Main St|Melbourne|Victoria|Australia", 
            "01-01-2000"
            );
        assertTrue(adult.addPerson());
        boolean result = adult.updatePersonalDetails(
            "56s_d%&fAB", 
            "Bob", 
            "Smith", 
            "789|Main St|Melbourne|Victoria|Australia", 
            "01-01-1999"
            );
        assertFalse(result);
    }

    // Test Case 8: Birthday and name change — not allowed
    @Test
    public void updatePersonalDetails_testCase3() {
        Person adult = new Person(
            "56s_d%&fAC", 
            "Bob", 
            "Smith", 
            "789|Main St|Melbourne|Victoria|Australia", 
            "01-01-2000"
            );
        assertTrue(adult.addPerson());
        boolean result = adult.updatePersonalDetails(
            "56s_d%&fAC", 
            "Robert", 
            "Smith", 
            "789|Main St|Melbourne|Victoria|Australia", 
            "01-01-1999"
            );
        assertFalse(result);
    }

    // Test Case 9: Birthday and address change — not allowed
    @Test
    public void updatePersonalDetails_testCase4() {
        Person adult = new Person(
            "56s_d%&fAD", 
            "Bob", 
            "Smith", 
            "789|Main St|Melbourne|Victoria|Australia", 
            "01-01-2000"
            );
        assertTrue(adult.addPerson());
        boolean result = adult.updatePersonalDetails(
            "56s_d%&fAD", 
            "Bob", 
            "Smith", 
            "456|Old St|Melbourne|Victoria|Australia", 
            "01-01-1999"
            );
        assertFalse(result);
    }

    // Test Case 10: All changes valid if adult is with odd-digit ID
    @Test
    public void updatePersonalDetails_testCase5() {
        Person adult = new Person(
            "56s_d%&fAE", 
            "Jane", 
            "Doe", 
            "321|Square St|Richmond|Victoria|Australia", 
            "15-03-1995"
            );
        assertTrue(adult.addPerson());
        boolean result = adult.updatePersonalDetails(
            "56s_d%&fAE", 
            "Janet", 
            "Dane", 
            "654|New Rd|Richmond|Victoria|Australia", 
            "15-03-1995"
            );
        assertTrue(result);
    }

}
class PersonDemeritPointsTest {

    // Test Case 11:  validate suspension 
    @Test
    void testCase1_under21_validNoSuspend() {
        Person p = new Person();
        p.setPersonID("ab#@aX1");
        p.setBirthdate("01-01-2005"); // age 18 in 2023

        String result = p.addDemeritPoints("01-06-2023", 3);
        assertEquals("Success", result, "Under-21 with 3 points should succeed");
        assertFalse(p.isSuspended(), "Under-21 with only 3 points should not be suspended");
    }

    // Test Case 12: suspend after threhold over 21
    @Test
    void testCase2_over21_flowNoSuspendThenSuspend() {
        Person p = new Person();
        p.setPersonID("ab#@aX2");
        p.setBirthdate("01-01-1980"); // age 43 in 2023

        // 1) First offense: 5 points → total = 5 (<12), no suspension
        assertEquals("Success", p.addDemeritPoints("15-11-2023", 5),
                     "Over-21 first offense (5 pts) should succeed");
        assertFalse(p.isSuspended(), "5 points < 12 → not suspended");

        // 2) Second offense: 5 points → total = 10 (<12), still no suspension
        assertEquals("Success", p.addDemeritPoints("01-06-2024", 5),
                     "Second offense (5 pts) should succeed");
        assertFalse(p.isSuspended(), "10 points < 12 → not suspended");

        // 3) Third offense: 5 points → total = 15 (>12) → suspension
        assertEquals("Success", p.addDemeritPoints("01-12-2024", 5),
                     "Third offense pushing total above 12 should succeed");
        assertTrue(p.isSuspended(), "15 points > 12 → should now be suspended");
    }

    // Test Case 13: invalid birthdate format
    @Test
    void testCase3_invalidDateFormat() {
        Person p = new Person();
        p.setPersonID("ab#@aX3");
        p.setBirthdate("01-01-2000"); // age 23 in 2023

        String result = p.addDemeritPoints("2023/11/15", 4);
        assertEquals("Failed", result, "Invalid date format should fail");
    }

    // Test Case 14: points out of range
    @Test
    void testCase4_pointsOutOfRange() {
        Person p = new Person();
        p.setPersonID("ab#@aX4");
        p.setBirthdate("01-01-2000"); // age 23 in 2023

        String resultLow  = p.addDemeritPoints("01-12-2023", 0);
        String resultHigh = p.addDemeritPoints("01-12-2023", 7);
        assertAll(
            () -> assertEquals("Failed", resultLow,  "0 points is below valid range"),
            () -> assertEquals("Failed", resultHigh, "7 points is above valid range")
        );
    }

    // Test Case 15: suspend after threshold under 21
    @Test
    void testCase5_under21_suspendAfterThreshold() {
        Person p = new Person();
        p.setPersonID("ab#@aX5");
        p.setBirthdate("01-01-2005"); // age 18 in 2023

        // First offense: 4 points → total = 4 (<6), no suspension
        assertEquals("Success", p.addDemeritPoints("01-01-2023", 4),
                     "Under-21 first offense (4 pts) should succeed");
        assertFalse(p.isSuspended(), "4 points < 6 → not suspended");

        // Second offense: 4 points → total = 8 (>6) → suspension
        assertEquals("Success", p.addDemeritPoints("02-01-2023", 4),
                     "Second offense pushing total above 6 should succeed");
        assertTrue(p.isSuspended(), "8 points > 6 → should now be suspended");
    }
}
