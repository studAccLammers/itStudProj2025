package org.application.services;

public interface DriveTimeCalculationService {
    double calculateDriveTime(double startLat, double startLon, double endLat, double endLon) throws DriveTimeCalculationException;

    double calculateDepotDriveTime(double lat, double lon) throws DriveTimeCalculationException;
}
