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
    private boolean includeLowers = false;
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
        this.includeLowers = passwordBuilder.nestedIncludeLowers;
        this.password = new ArrayList<>(passwordLength);
        setPassword();
    }

    private void setPassword() {
        // Create a new ArrayList to hold all of the available characters
        ArrayList<Character> options = new ArrayList<>();
        // Add capitals if checked
        if (this.includeCapitals) {
            options.addAll(uppers);
        }
        // Add lowers if checked
        if (this.includeLowers) {
            options.addAll(lowers);
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
        checkPassword(this.password);
    }

    public void checkPassword(ArrayList<Character> password) {

        if (password.size() == 0) {
            return;
        }

        if (this.includeLowers) {
            final boolean[] isValid = {false};
            password.stream().forEach(x -> {
                if (lowers.contains(x)) {
                    isValid[0] = true;
                }
            });
            if (!isValid[0]) {
                fixPassword(password, lowers);
                checkPassword(this.password);
            }
        }
        if (this.includeCapitals) {
            final boolean[] isValid = {false};
            password.stream().forEach(x -> {
                if (uppers.contains(x)) {
                    isValid[0] = true;
                }
            });
            if (!isValid[0]) {
                fixPassword(password, uppers);
                checkPassword(this.password);
            }
        }
        if (this.includeNumbers) {
            final boolean[] isValid = {false};
            password.stream().forEach(x -> {
                if (numbers.contains(x)) {
                    isValid[0] = true;
                }
            });
            if (!isValid[0]) {
                fixPassword(password, numbers);
                checkPassword(this.password);
            }
        }
        if (this.includeSpecialCharacters) {
            final boolean[] isValid = {false};
            password.stream().forEach(x -> {
                if (specialCharacters.contains(x)) {
                    isValid[0] = true;
                }
            });
            if (!isValid[0]) {
                fixPassword(password, specialCharacters);
                checkPassword(this.password);
            }
        }
        return;
    }

    public void fixPassword(ArrayList<Character> password,
                            ArrayList<Character> option) {

        // Create a HashMap to track character usage
        Map<String, Integer> countMap = new HashMap<>();

        // Break apart the password array
        ArrayList<Character> includedLowers = new ArrayList<>();
        ArrayList<Character> includedUppers = new ArrayList<>();
        ArrayList<Character> includedNumbers = new ArrayList<>();
        ArrayList<Character> includedSpecials = new ArrayList<>();


        // Build the ArrayLists
        password.stream().forEach(x -> {
            if (lowers.contains(x)) {
                includedLowers.add(x);
            } else if (uppers.contains(x)) {
                includedUppers.add(x);
            } else if (numbers.contains(x)) {
                includedNumbers.add(x);
            } else if (specialCharacters.contains(x)) {
                includedSpecials.add(x);
            }
        });

        countMap.put("lower", includedLowers.size());
        countMap.put("upper", includedUppers.size());
        countMap.put("number", includedNumbers.size());
        countMap.put("special", includedSpecials.size());

        // Find the key with the highest value
        String highest = Collections.max(countMap.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();

        // Get a character from the option list
        char opt = option.get(random.nextInt(option.size()));

        // Take add the random element from the option list and remove from the
        // largest ArrayList
        switch (highest) {
            case "upper":
                includedUppers.remove(random.nextInt(includedUppers.size()));
                break;
            case "lower":
                includedLowers.remove(random.nextInt(includedLowers.size()));
                break;
            case "number":
                includedNumbers.remove(random.nextInt(includedNumbers.size()));
                break;
            case "special":
                includedSpecials.remove(random.nextInt(includedSpecials.size()));
                break;
            default:
                break;
        }

        // Rebuilt the arrays
        ArrayList<Character> newPassword = new ArrayList<>();
        newPassword.add(opt);
        newPassword.addAll(includedLowers);
        newPassword.addAll(includedUppers);
        newPassword.addAll(includedNumbers);
        newPassword.addAll(includedSpecials);

        this.password = newPassword;

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
        private boolean nestedIncludeLowers;

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

        public PasswordBuilder includeLowers(boolean includeLowers) {
            this.nestedIncludeLowers = includeLowers;
            return this;
        }

        public Password build() {
            return new Password(this);
        }
    }
}