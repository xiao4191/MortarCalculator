package Mortar.Model;

import java.util.ArrayList;
import java.util.List;

public enum AmmunitionType {
    M720A2("HE (M720A2)"),
    M1061("HE (M1061)"),
    M722A1("White phosphorus smoke (M722A1)"),
    M721("Visible light illumination (M721)"),
    M767("Infrared illumination (M767)"),
    M768A1("Training HE (M768A1)"),
    M769("Full-range practice (FRP) (M769)"),

    M821A3("HE (M821A3)"),
    M819("Red phosphorus smoke (M819)"),
    M853A1("Visible light illumination (M853A1)"),
    M816("Infrared illumination (M816)"),
    M889A4("Training HE (M889A4)"),
    M879A1("Full-range practice (FRP) (M879A1)"),

    M934A1("HE (M934A1)"),
    M929("White phosphorus smoke (M929)"),
    M930("Visible light illumination (M930)"),
    M983("Infrared illumination (M983)"),
    M933A1("Training (M933A1)"),
    M931("Full-range practice (FRP) (M931)");

    private final String displayName;

    AmmunitionType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    // Updated method to get all ammunition types with details
    public static List<String> getAllAmmunitionTypesWithDetails() {
        List<String> ammoTypes = new ArrayList<>();
        for (AmmunitionType ammo : values()) {
            ammoTypes.add(ammo.toString()); // Use the toString method to display all details
        }
        return ammoTypes;
    }

    @Override
    public String toString() {
        return displayName;
    }
}