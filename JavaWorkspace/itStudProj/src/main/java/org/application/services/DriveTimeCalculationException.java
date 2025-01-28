package org.application.services;

public class DriveTimeCalculationException extends Exception {
    public DriveTimeCalculationException(String errorMsg, double startLat, double startLon, double endLat, double endLon) {
        super("Unable to calculate drive time for Start: " +
            startLon + ", " + startLat +
            " to " +
            endLon + ", " + endLat + ".\n"
            + errorMsg
        );
    }
}
