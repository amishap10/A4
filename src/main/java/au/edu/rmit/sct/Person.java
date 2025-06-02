package au.edu.rmit.sct;

import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.util.Scanner;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class Person {
    private String personID;
    private String firstName;
    private String lastName;
    private String address;
    private String birthdate;
    private HashMap<Date, Integer> demeritPoints;
    private boolean isSuspended;

    public Person (String personID, String firstName, String lastName, String address, String birthdate) {
        this.personID = personID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.birthdate = birthdate;
    }
    public Person() {
        this.demeritPoints = new HashMap<>();
        this.isSuspended = false;
    }

    public String getPersonID() { return personID; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getAddress() { return address; }
    public String getBirthdate() { return birthdate; }

    public boolean addPerson() {

        // Condition 1 
        // Checks if personID's length is exactly 10 characters long
        if (personID.length() != 10) {
            return false;
        }
        char firstChar = personID.charAt(0);
        char secondChar = personID.charAt(1);

        // Checks if the first character of personID is a digit that is between 2 and 9
        if (!Character.isDigit(firstChar) || firstChar < '2' || firstChar > '9') {
            return false;
        }

        // Checks if the second character of personID is a digit that is between 2 and 9
        if (!Character.isDigit(secondChar) || secondChar < '2' || secondChar > '9') {
            return false;
        }

        String specialChar = "!@#$%&*()-_+=|<>?{}[]~";
        int count = 0;

        // The loop checks for any special characters between characters 3 and 8 of the personID
        // For every special character found, the count is increased by 1
        for (int i = 2; i < 8; i++) {
            if (specialChar.contains(String.valueOf(personID.charAt(i)))) {
                count++;
            }
        }
        // If less than two special characters are found, then personID is invalid
        if (count < 2) {
            return false;
        }

        // Checks if the last two character of personID are upper casese
        if (!Character.isUpperCase(personID.charAt(personID.length() -1)) || !Character.isUpperCase(personID.charAt(personID.length() -2))) {
            return false;
        }

        // Condition 2 
        // Checks if the address entered is in correct format: Street Number|Street|City|Victoria|Country
        //  \\d = digit
        // "+" = one or more digits
        // "\\|" = the seperator

        String addressMatch = "\\d+\\|[^|]+\\|[^|]+\\|Victoria\\|[^|]+";
        if (!address.matches(addressMatch)) {
            return false;
        }

        // Condition 3 
        // Checks if the birth date entered is in correct format: DD-MM-YYYY
        // "d{2}" = two digits and "d{4}"" = 4 digits

        String dateFormat = "\\d{2}-\\d{2}-\\d{4}";
        if (!birthdate.matches(dateFormat)) {
            return false;
        }

        // If all the condition are met, the infromation is added to a TXT file
        // Any case the conditions arent met, it returns false and closes the TXT file without updating anything

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
        
        // calculates the current age of the person using their current birthdate
        int age = calculateAge(this.birthdate);
        
        //Condition 1: If a person is under 18, their address cannot be changed.
        if (age < 18 && !this.address.equals(newAddress)) {
            return false;
        }
        //Condition 2: If a person's birthday is going to be changed, then no other personal detail (i.e, person's ID, firstName, LastName, address) can be changed.
        boolean birthdayChanged = !this.birthdate.equals(newBirthdate);
        if (birthdayChanged) {
            if (!this.personID.equals(newID) || !this.firstName.equals(newFirstName) || !this.lastName.equals(newLastName) || !this.address.equals(newAddress)) {
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
        //Reads person.txt file and update matching line
        StringBuilder updatedContent = new StringBuilder();
        boolean found = false;

        try (Scanner scanner = new Scanner(new File("person.txt"))) {
            //reads file line by line
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",", -1);
                //matches the line using the original person ID
                if (parts.length == 5 && parts[0].equals(this.personID)) {
                    //build updated line with the new details
                    String updatedLine = newID + "," + newFirstName + "," + newLastName + "," + newAddress + "," + newBirthdate;
                    updatedContent.append(updatedLine).append("\n");
                    found = true;
                } else {
                    updatedContent.append(line).append("\n");
                }
            }
        } catch (IOException e) {
            //if txt file reading fails, it returns false
            return false;
        }
        if (!found) return false;
        //Overwrites person.txt file with updated details
        try (FileWriter writer = new FileWriter("person.txt", false)) {
            writer.write(updatedContent.toString());
        } catch (IOException e) {
            return false;
        }
        //successful changes in file updates objects with new values 
        this.personID = newID;
        this.firstName = newFirstName;
        this.lastName = newLastName;
        this.address = newAddress;
        this.birthdate = newBirthdate;
        return true;
    }
    // Calculates age from the birthdate assuming format is in "DD-MM-YYYY"
    private int calculateAge(String birthdate) {
        int birthYear = 0;
        for (int i = 6; i < 10; i++) {
            birthYear = birthYear * 10 + (birthdate.charAt(i) - '0');
        }
        int currentYear = 2025;  
        return currentYear - birthYear;
    }
    
    public String addDemeritPoints(String offenseDateStr, int points) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        sdf.setLenient(false);

        try {
            // 1) parse & validate offense date format
            Date offenseDate = sdf.parse(offenseDateStr);

            // 2) validate points range
            if (points < 1 || points > 6) {
                return "Failed";
            }

            // 3) calculate age at time of offense
            Date birthDate = sdf.parse(this.birthdate);
            Calendar birthCal = Calendar.getInstance();
            birthCal.setTime(birthDate);
            Calendar offCal = Calendar.getInstance();
            offCal.setTime(offenseDate);
            int age = offCal.get(Calendar.YEAR) - birthCal.get(Calendar.YEAR);
            if (offCal.get(Calendar.DAY_OF_YEAR) < birthCal.get(Calendar.DAY_OF_YEAR)) {
                age--;
            }

            // 4) record the new offense
            demeritPoints.put(offenseDate, points);

            // 5) sum points in rolling 2-year window
            Calendar twoYearsAgo = (Calendar) offCal.clone();
            twoYearsAgo.add(Calendar.YEAR, -2);
            int total = demeritPoints.entrySet().stream()
                .filter(e -> !e.getKey().before(twoYearsAgo.getTime()))
                .mapToInt(Map.Entry::getValue)
                .sum();

            // 6) update suspension flag
            if ((age < 21 && total > 6) || (age >= 21 && total > 12)) {
                isSuspended = true;
            }

            // 7) append record to TXT file
            String line = personID
                        + "|" + offenseDateStr
                        + "|" + points
                        + "|" + isSuspended
                        + System.lineSeparator();
            try (FileWriter fw = new FileWriter("person.txt", true)) {
                fw.write(line);
            }

            return "Success";
        } catch (Exception e) {
            return "Failed";
        }
    }

    // accessors
    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public boolean isSuspended() {
        return isSuspended;
    }
}
