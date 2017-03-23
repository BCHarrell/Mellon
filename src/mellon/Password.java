package mellon;

import java.util.*;

/**
 * Created by Thomas Hodges on 3/18/2017.
 * Mellon.Password.java
 */
public class Password {

    private ArrayList<Character> password;
    private int passwordLength;
    private boolean includeSpecialCharacters = false;
    private boolean includeNumbers = false;
    private boolean includeCapitals = false;
    private Random random;

    // Using ArrayLists to have complete control over the characters
    private ArrayList<Character> specialCharacters = new ArrayList<>(Arrays.asList(
            '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '-', '_',
            '=', '+', ',', '.', '<', '>', '?', '/'));
    private ArrayList<Character> numbers = new ArrayList<>(Arrays.asList(
            '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'));
    private ArrayList<Character> lowers = new ArrayList<>(Arrays.asList(
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l',
            'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
            'y', 'z'));
    private ArrayList<Character> uppers = new ArrayList<>(Arrays.asList(
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
            'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
            'Y', 'Z'));

    private Password(PasswordBuilder passwordBuilder) {
        this.passwordLength = passwordBuilder.nestedLength;
        this.includeSpecialCharacters = passwordBuilder.nestedIncludeSpecialCharacters;
        this.includeNumbers = passwordBuilder.nestedIncludeNumbers;
        this.includeCapitals = passwordBuilder.nestedIncludeCapitals;
        this.password = new ArrayList<>(passwordLength);
        setPassword();
    }

    private void setPassword() {
        // Create a new ArrayList to hold all of the available characters
        ArrayList<Character> options = new ArrayList<>(lowers);
        // Add capitals if checked
        if (this.includeCapitals) {
            options.addAll(uppers);
        }
        // Add numbers if checked
        if (this.includeNumbers) {
            options.addAll(numbers);
        }
        // Add special characters if checked
        if (this.includeSpecialCharacters) {
            options.addAll(specialCharacters);
        }
        // Now that the options list includes all available characters, iterate through
        // the length of the password and randomly choose characters to insert
        for (int i = 0; i < passwordLength; i++) {
            random = new Random();
            this.password.add(options.get(random.nextInt(options.size())));
        }
        // Now there is a possibility that the generated password does not contain
        // capitals, numbers, or special characters even when it was specified to
        // do so.
        //checkPassword(this.password);
    }

    public void checkPassword(ArrayList<Character> password) {

        // TODO - this method needs work.
        // Goal is to ensure that when an option is checked, the intended result
        // includes that option. For example, if include numbers is checked, there
        // is still a chance that the end result will not have numbers. So this
        // method should check for the most common attribute, and replace a character
        // with the option, then check itself again until we're sure that the
        // password is good.


        if (this.includeCapitals && password.contains(uppers)) {
            // Continue
        } else {
            // TODO - Fix
            checkPassword(password);
        }
        if (this.includeNumbers && password.contains(numbers)) {
            // Continue
        } else {
            // TODO - Fix
            checkPassword(password);
        }
        if (this.includeSpecialCharacters && password.contains(specialCharacters)) {
            // Continue
        } else {
            // TODO - Fix
            checkPassword(password);
        }

    }

    public String getPasswordString() {
        StringBuilder passwordString = new StringBuilder();
        password.stream().forEach(x -> passwordString.append(x.toString()));
        return passwordString.toString();
    }

    // Builder pattern inner class
    public static class PasswordBuilder {

        private int nestedLength;
        private boolean nestedIncludeSpecialCharacters;
        private boolean nestedIncludeNumbers;
        private boolean nestedIncludeCapitals;

        public PasswordBuilder(int length) {
            this.nestedLength = length;
        }

        public PasswordBuilder includeSpecialCharacters(boolean includeSpecialCharacters) {
            this.nestedIncludeSpecialCharacters = includeSpecialCharacters;
            return this;
        }

        public PasswordBuilder includeNumbers(boolean includeNumbers) {
            this.nestedIncludeNumbers = includeNumbers;
            return this;
        }

        public PasswordBuilder includeCapitals(boolean includeCapitals) {
            this.nestedIncludeCapitals = includeCapitals;
            return this;
        }

        public Password build() {
            return new Password(this);
        }
    }
}