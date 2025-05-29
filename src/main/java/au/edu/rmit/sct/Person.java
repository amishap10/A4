package au.edu.rmit.sct;

import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.util.Scanner;

public class Person {
    private String personID;
    private String firstName;
    private String lastName;
    private String address;
    private String birthdate;

    public Person (String personID, String firstName, String lastName, String address, String birthdate) {
        this.personID = personID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.birthdate = birthdate;
    }

    public String getPersonID() { return personID; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getAddress() { return address; }
    public String getBirthdate() { return birthdate; }

    public boolean addPerson() {

        //condition 1 personID
        if (personID.length() == 10) {
            return false;
        }
        char firstChar = personID.charAt(0);
        char secondChar = personID.charAt(1);

        if (!Character.isDigit(firstChar) || firstChar < '2' || firstChar > '9') {
            return false;
        }
        if (!Character.isDigit(secondChar) || secondChar < '2' || secondChar > '9') {
            return false;
        }

        String specialChar = "!@#$%&*()-_+=|<>?{}[]~";
        int count = 0;

        for (int i = 2; i < 8; i++) {
            if (specialChar.contains(String.valueOf(personID.charAt(i)))) {
                count++;
            }
        }
        if (count < 2) {
            return false;
        }
        if (!Character.isUpperCase(personID.charAt(personID.length() -1)) || !Character.isUpperCase(personID.charAt(personID.length() -2))) {
            return false;
        }

        //condition 2 address

        String addressMatch = "\\d+\\|[^|]+\\|[^|]+\\|Victoria\\|[^|]+";
        if (!address.matches(addressMatch)) {
            return false;
        }

        //condition 3 birth date

        String dateFormat = "\\d{2}-\\d{2}-\\d{4}";
        if (!birthdate.matches(dateFormat)) {
            return false;
        }

        //TXT file
        try (FileWriter writer = new FileWriter("person.txt", true)) {
            writer.write(String.format( 
                "%s,%s,%s,%s,%s%n",
                personID, firstName, lastName, address, birthdate
            ));
        } catch (IOException e) {
            return false;
        }

        return true;
    }

    public boolean updatePersonalDetails(String newID, String newFirstName, String newLastName, String newAddress, String newBirthdate) {
        int age = calculateAge(this.birthdate);
        
        //Condition 1: If a person is under 18, their address cannot be changed.
        if (age < 18 && !this.address.equals(newAddress)) {
            return false;
        }
        //Condition 2: If a person's birthday is going to be changed, then no other personal detail (i.e, person's ID, firstName, LastName, address) can be changed.
        boolean birthdayChanged = !this.birthdate.equals(newBirthdate);
        if (birthdayChanged) {
            if (!this.personID.equals(newID) || !this.firstName.equals(newFirstName) || 
                !this.lastName.equals(newLastName) || !this.address.equals(newAddress)) {
                return false;
            }
        }
        //Condition 3: If the first character/digit of a person's ID is an even number, then their ID cannot be changed.
        char firstChar = this.personID.charAt(0);
        if (Character.isDigit(firstChar)) {
            int digit = firstChar - '0';
            if (digit % 2 == 0 && !this.personID.equals(newID)) {
                return false;
            }
        }
        //Reads file and update matching line
        StringBuilder updatedContent = new StringBuilder();
        boolean found = false;

        try (Scanner scanner = new Scanner(new File("person.txt"))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",", -1);
                if (parts.length == 5 && parts[0].equals(this.personID)) {
                    String updatedLine = newID + "," + newFirstName + "," + newLastName + "," + newAddress + "," + newBirthdate;
                    updatedContent.append(updatedLine).append("\n");
                    found = true;
                } else {
                    updatedContent.append(line).append("\n");
                }
            }
        } catch (IOException e) {
            return false;
        }
        if (!found) return false;
        //Overwrites file
        try (FileWriter writer = new FileWriter("person.txt", false)) {
            writer.write(updatedContent.toString());
        } catch (IOException e) {
            return false;
        }
        //Updates current object
        this.personID = newID;
        this.firstName = newFirstName;
        this.lastName = newLastName;
        this.address = newAddress;
        this.birthdate = newBirthdate;
        return true;
    }

    private int calculateAge(String birthdate) {
        int birthYear = 0;
        for (int i = 0; i < 4; i++) {
            birthYear = birthYear * 10 + (birthdate.charAt(i) - '0');
        }
        int currentYear = 2025;  
        return currentYear - birthYear;
    }
}