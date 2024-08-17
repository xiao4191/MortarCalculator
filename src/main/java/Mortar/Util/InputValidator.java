package Mortar.Util;

import java.util.Scanner;

import Mortar.Model.AmmunitionType;
import Mortar.Model.MortarType;

public class InputValidator {

    public static boolean isValidMortar(MortarType mortar, int firingRange) {
        if (mortar == null) {
            throw new IllegalArgumentException("Mortar object must not be null.");
        }

        int minElevation = mortar.getMinElevation();
        int maxElevation = mortar.getMaxElevation();
        int minRange = mortar.getMinRange();
        int maxRange = mortar.getMaxRange();

        if (!(minElevation >= 0 && minElevation <= maxElevation && minRange >= 0 && minRange <= maxRange)) {
            throw new IllegalArgumentException("Invalid mortar properties: Check elevation and range values.");
        }

        return true;
    }

    public static boolean isValidFiringRange(int firingRange, MortarType mortar) {
        if (mortar == null) {
            throw new IllegalArgumentException("Mortar must not be null.");
        }

        if (!(firingRange >= mortar.getMinRange() && firingRange <= mortar.getMaxRange())) {
            throw new IllegalArgumentException("Invalid firing range for the given mortar: "
                    + "Must be within the range " + mortar.getMinRange() + " to " + mortar.getMaxRange() + ".");
        }

        return true;
    }

    public static boolean isValidAmmunition(AmmunitionType ammunition) {
        // Validate ammunition properties, return true if valid, false otherwise
        return ammunition != null && ammunition.getDisplayName() != null
                && !ammunition.getDisplayName().trim().isEmpty();
    }

    public static int getIntegerInput(Scanner scanner) {
        // Get integer input from the user
        while (!scanner.hasNextInt()) {
            System.out.println("Error: Please enter a valid integer.");
            scanner.next(); // Consume the invalid input
        }
        return scanner.nextInt();
    }

    public static int getIntegerInputInRange(Scanner scanner, int min, int max) {
        // Get integer input from the user within a specified range
        int input;
        do {
            System.out.printf("Please enter an integer between %d and %d (inclusive): ", min, max);
            input = getIntegerInput(scanner);
        } while (input < min || input > max);
        return input;
    }

    public static int getIntegerInputInRangeForMortar(Scanner scanner, MortarType mortar) {
        // Get integer input from the user within the range of a specific mortar
        return getIntegerInputInRange(scanner, mortar.getMinRange(), mortar.getMaxRange());
    }
}
