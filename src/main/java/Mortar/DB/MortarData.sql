DROP DATABASE IF EXISTS MortarCalculator;
CREATE DATABASE MortarCalculator;
USE MortarCalculator;

CREATE TABLE MortarData (
mortar_type VARCHAR(255),
ammunition_type VARCHAR(255),
minElevation INT,
maxElevation INT,
minRange INT,
maxRange INT,
firing_range INT,
elevation_mils INT
);