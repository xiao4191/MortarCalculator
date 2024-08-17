package Mortar.Controller;

import Mortar.DB.DBSupport;
import Mortar.Model.AmmunitionType;
import Mortar.Model.MortarType;
import Mortar.Service.CalculatorService;
import Mortar.Util.InputValidator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class MortarCalculatorController {

    private final CalculatorService calculatorService;
    private final Scanner scanner;

    public MortarCalculatorController() {
        this.calculatorService = new CalculatorService();
        this.scanner = new Scanner(System.in);
    }

    public void runCalculator() {
        System.out.println("Welcome to the Mortar Calculator!");

        while (true) {
            // Select mortar type
            System.out.println("\nSelect a Mortar Type:");
            List<MortarType> mortarTypes = List.of(MortarType.values());
            int selectedMortarIndex = displayAndSelectFromList(mortarTypes);
            MortarType selectedMortar = mortarTypes.get(selectedMortarIndex);

            // Get available ammunition types for the selected mortar
            List<AmmunitionType> availableAmmunitionTypes = calculatorService
                    .getAmmunitionTypesForMortar(selectedMortar);

            // Select ammunition type
            System.out.println("\nSelect an Ammunition Type:");
            int selectedAmmoIndex = displayAndSelectFromList(availableAmmunitionTypes);
            AmmunitionType selectedAmmo = availableAmmunitionTypes.get(selectedAmmoIndex);

            // Validate and get firing range
            System.out.println("\nEnter Firing Range for the selected Mortar:");
            int firingRange = InputValidator.getIntegerInputInRangeForMortar(scanner, selectedMortar);

            // Calculate and display elevation
            int elevation = calculatorService.calculateElevation(selectedMortar, selectedAmmo, firingRange);

            // Display selected choices and calculated elevation
            System.out.println("\nSelected Mortar Type: " + selectedMortar.getDisplayName());
            System.out.println("Selected Ammunition Type: " + selectedAmmo.getDisplayName());
            System.out.println("Firing Range Input: " + firingRange);
            System.out.println("Calculated Elevation: " + elevation);

            // Ask the user if they want to save or delete the result
            System.out.print("\nDo you want to save or delete the result? (Enter 'save', 'delete', or 'no'): ");
            String saveOrDeleteChoice = scanner.next().toLowerCase();

            if (saveOrDeleteChoice.equals("save")) {
                // Save the result to the database
                saveResultToDatabase(selectedMortar, selectedAmmo, firingRange, elevation);
            } else if (saveOrDeleteChoice.equals("delete")) {
                // Get firing range and elevation for deletion
                System.out.print("Enter the firing range for deletion: ");
                int deleteFiringRange = InputValidator.getIntegerInput(scanner);

                System.out.print("Enter the elevation for deletion: ");
                int deleteElevation = InputValidator.getIntegerInput(scanner);

                // Delete the result from the database
                deleteResultFromDatabase(selectedMortar, selectedAmmo, deleteFiringRange, deleteElevation);
            }

            // Ask the user if they want to perform another calculation
            System.out.print("\nDo you want to perform another calculation? (Enter 'yes' or 'no'): ");
            String userChoice = scanner.next().toLowerCase();

            if (userChoice.equals("no")) {
                break; // Exit the loop if the user chooses not to perform another calculation
            }
        }

        // Close the scanner
        scanner.close();
        System.out.println("Thank you for using the Mortar Calculator!");
    }

    private void saveResultToDatabase(MortarType selectedMortar, AmmunitionType selectedAmmo,
            int firingRange, int elevation) {
        try {
            // Connect to the database
            Connection conn = DBSupport.establishConnection();

            // Insert data into the MortarData table
            String insertQuery = "INSERT INTO MortarData " +
                    "(mortar_type, ammunition_type, minElevation, maxElevation, " +
                    "minRange, maxRange, firing_range, elevation_mils) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = conn.prepareStatement(insertQuery)) {
                preparedStatement.setString(1, selectedMortar.getDisplayName());
                preparedStatement.setString(2, selectedAmmo.getDisplayName());
                preparedStatement.setInt(3, selectedMortar.getMinElevation());
                preparedStatement.setInt(4, selectedMortar.getMaxElevation());
                preparedStatement.setInt(5, selectedMortar.getMinRange());
                preparedStatement.setInt(6, selectedMortar.getMaxRange());
                preparedStatement.setInt(7, firingRange);
                preparedStatement.setInt(8, elevation);

                preparedStatement.executeUpdate();
            }

            // Close the connection
            conn.close();

            System.out.println("Result saved to the database!");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace(); // Handle the exception according to your application's requirements
        }
    }

    private void deleteResultFromDatabase(MortarType selectedMortar, AmmunitionType selectedAmmo,
            int firingRange, int elevation) {
        try {
            // Connect to the database
            Connection conn = DBSupport.establishConnection();

            // Delete data from the MortarData table based on mortar, ammo, firing range,
            // and elevation
            String deleteQuery = "DELETE FROM MortarData WHERE " +
                    "mortar_type = ? AND ammunition_type = ? AND firing_range = ? AND elevation_mils = ?";
            try (PreparedStatement preparedStatement = conn.prepareStatement(deleteQuery)) {
                preparedStatement.setString(1, selectedMortar.getDisplayName());
                preparedStatement.setString(2, selectedAmmo.getDisplayName());
                preparedStatement.setInt(3, firingRange);
                preparedStatement.setInt(4, elevation);

                int rowsDeleted = preparedStatement.executeUpdate();

                if (rowsDeleted > 0) {
                    System.out.println("Result deleted from the database!");
                } else {
                    System.out.println("No matching result found in the database.");
                }
            }

            // Close the connection
            conn.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace(); // Handle the exception according to your application's requirements
        }
    }

    private int displayAndSelectFromList(List<?> itemList) {
        for (int i = 0; i < itemList.size(); i++) {
            System.out.println((i + 1) + ". " + itemList.get(i).toString());
        }

        System.out.print("Enter the number corresponding to your selection: ");
        int selection = InputValidator.getIntegerInputInRange(scanner, 1, itemList.size()) - 1;

        System.out.println("\nSelected: " + itemList.get(selection).toString());
        return selection;
    }
}
