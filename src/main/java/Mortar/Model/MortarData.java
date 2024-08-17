package Mortar.Model;

public class MortarData {
    private String mortarType;
    private String ammunitionType;
    private int minElevation;
    private int maxElevation;
    private int minRange;
    private int maxRange;
    private int firingRange;
    private int elevationMils;

    // Getters
    public String getMortarType() {
        return mortarType;
    }

    public String getAmmunitionType() {
        return ammunitionType;
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

    public int getFiringRange() {
        return firingRange;
    }

    public int getElevationMils() {
        return elevationMils;
    }

    // Setters
    public void setMortarType(String mortarType) {
        this.mortarType = mortarType;
    }

    public void setAmmunitionType(String ammunitionType) {
        this.ammunitionType = ammunitionType;
    }

    public void setMinElevation(int minElevation) {
        this.minElevation = minElevation;
    }

    public void setMaxElevation(int maxElevation) {
        this.maxElevation = maxElevation;
    }

    public void setMinRange(int minRange) {
        this.minRange = minRange;
    }

    public void setMaxRange(int maxRange) {
        this.maxRange = maxRange;
    }

    public void setFiringRange(int firingRange) {
        this.firingRange = firingRange;
    }

    public void setElevationMils(int elevationMils) {
        this.elevationMils = elevationMils;
    }
}
