
import org.junit.jupiter.api.Test;

import au.edu.rmit.sct.Person;

import static org.junit.jupiter.api.Assertions.*;

public class PersonTest {


    //vaild personID
    @Test
    void validPerson() {
        Person person = new Person(
            "36!@&*XYP",
            "Emily",
            "Smith",
            "124|La Trobe Street|Melbourne|Victoria|Australia",
            "05-05-2005"
            );
        assertTrue(person.addPerson());
    }

    //invalid birthdate format
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

    //invalid address state
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

    //invalid address format
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

    //invalid no special characters
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

    // Test Case 1: Address change not allowed if under 18
    @Test
    public void updatePersonalDetails_testCase1() {
        Person minor = new Person("1@ABC", "Alice", "Young", "123|Circle St|Carlton|Victoria|Australia", "10-05-2010");
        boolean result = minor.updatePersonalDetails("1@ABC", "Alice", "Young", "456|Tree Ave|Carlton|Victoria|Australia", "10-05-2010");
        assertFalse(result);
    }

    // Test Case 2: Only birthday changes — allowed
    @Test
    public void updatePersonalDetails_testCase2() {
        Person adult = new Person("1#XYZ", "Bob", "Smith", "456|Main St|Melbourne|Victoria|Australia", "01-01-2000");
        boolean result = adult.updatePersonalDetails("1#XYZ", "Bob", "Smith", "789|Main St|Melbourne|Victoria|Australia", "01-01-1999");
        assertTrue(result);
    }

    // Test Case 3: Birthday and name change — not allowed
    @Test
    public void updatePersonalDetails_testCase3() {
        Person adult = new Person("1#XYZ", "Bob", "Smith", "789|Main St|Melbourne|Victoria|Australia", "01-01-2000");
        boolean result = adult.updatePersonalDetails("1#XYZ", "Robert", "Smith", "789|Main St|Melbourne|Victoria|Australia", "01-01-1999");
        assertFalse(result);
    }

    // Test Case 4: Birthday and address change — not allowed
    @Test
    public void updatePersonalDetails_testCase4() {
        Person adult = new Person("1#XYZ", "Bob", "Smith", "789|Main St|Melbourne|Victoria|Australia", "01-01-2000");
        boolean result = adult.updatePersonalDetails("1#XYZ", "Bob", "Smith", "456|Old St|Melbourne|Victoria|Australia", "01-01-1999");
        assertFalse(result);
    }

    // Test Case 5: All changes valid if adult is with odd-digit ID
    @Test
    public void updatePersonalDetails_testCase5() {
        Person adult = new Person("3@XYZ", "Jane", "Doe", "321|Square St|Richmond|Victoria|Australia", "15-03-1995");
        boolean result = adult.updatePersonalDetails("3*A#C", "Janet", "Dane", "654|New Rd|Richmond|Victoria|Australia", "15-03-1995");
        assertTrue(result);
    }

}