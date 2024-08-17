package Mortar.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Mortar.Model.AmmunitionType;
import Mortar.Model.MortarType;

public class CalculatorService {

    private static final double DEFAULT_AMMO_FACTOR = 1.0;

    // Create a mapping between mortar types and their corresponding ammunition types
    private static final Map<MortarType, List<AmmunitionType>> MORTAR_AMMO_MAP = new HashMap<>();

    static {
        MORTAR_AMMO_MAP.put(MortarType.M224A1, List.of(
                AmmunitionType.M720A2, AmmunitionType.M1061, AmmunitionType.M722A1,
                AmmunitionType.M721, AmmunitionType.M767, AmmunitionType.M768A1, AmmunitionType.M769));
        MORTAR_AMMO_MAP.put(MortarType.M252A1, List.of(
                AmmunitionType.M821A3, AmmunitionType.M819, AmmunitionType.M853A1,
                AmmunitionType.M816, AmmunitionType.M889A4, AmmunitionType.M879A1));
        MORTAR_AMMO_MAP.put(MortarType.M120, List.of(
                AmmunitionType.M934A1, AmmunitionType.M929, AmmunitionType.M930,
                AmmunitionType.M983, AmmunitionType.M933A1, AmmunitionType.M931));
    }

    public int calculateElevation(MortarType mortar, AmmunitionType ammunition, int firingRange) {
        // Placeholder logic, replace with your actual elevation calculation algorithm
        int elevation = customElevationCalculation(mortar, ammunition, firingRange);
        return elevation;
    }

    private int customElevationCalculation(MortarType mortar, AmmunitionType ammunition, int firingRange) {
        // Calculate the percentage of firing range within the mortar's range
        double rangePercentage = (double) (firingRange - mortar.getMinRange()) / (mortar.getMaxRange() - mortar.getMinRange());

        // Calculate the corresponding percentage of elevation within the mortar's elevation range
        int elevation = (int) (mortar.getMinElevation() + rangePercentage * (mortar.getMaxElevation() - mortar.getMinElevation()));

        // Ensure the calculated elevation is within the mortar's elevation range
        elevation = Math.max(mortar.getMinElevation(), Math.min(elevation, mortar.getMaxElevation()));

        // Apply a factor based on ammunition characteristics
        double ammoFactor = calculateAmmoFactor(ammunition);
        elevation = (int) (elevation * ammoFactor);

        return elevation;
    }

    private double calculateAmmoFactor(AmmunitionType ammunition) {
        // Placeholder method to calculate a factor based on ammunition characteristics
        // Replace this with your actual logic to determine the ammunition factor
        return DEFAULT_AMMO_FACTOR; // No additional factor by default
    }

    public List<AmmunitionType> getAmmunitionTypesForMortar(MortarType mortar) {
        return MORTAR_AMMO_MAP.getOrDefault(mortar, List.of());
    }
}