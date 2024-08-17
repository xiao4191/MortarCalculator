package Mortar.Model;

import java.util.ArrayList;
import java.util.List;

public enum MortarType {
    M224A1("60mm M224A1", 800, 1511, 70, 3490),
    M252A1("81mm M252A1", 800, 1515, 83, 5844),
    M120("120mm Mortar System", 710, 1510, 200, 7200);

    private final String displayName;
    private final int minElevation;
    private final int maxElevation;
    private final int minRange;
    private final int maxRange;

    MortarType(String displayName, int minElevation, int maxElevation, int minRange, int maxRange) {
        this.displayName = displayName;
        this.minElevation = minElevation;
        this.maxElevation = maxElevation;
        this.minRange = minRange;
        this.maxRange = maxRange;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getMinElevation() {
        return minElevation;
    }

    public int getMaxElevation() {
        return maxElevation;
    }

    public int getMinRange() {
        return minRange;
    }

    public int getMaxRange() {
        return maxRange;
    }

    // Updated method name for clarity
    public String getMortarTypeNameIdentifier() {
        // Assuming the mortar type identifier is the part before the first space
        return getDisplayName().split(" ", 2)[0];
    }

    // Example method to get all mortar type identifiers
    public static List<String> getAllMortarTypeIdentifiers() {
        List<String> mortarTypeIdentifiers = new ArrayList<>();
        for (MortarType mortarType : values()) {
            mortarTypeIdentifiers.add(mortarType.getMortarTypeNameIdentifier());
        }
        return mortarTypeIdentifiers;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
