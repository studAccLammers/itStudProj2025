package org.application.dtos;

import org.application.services.DriveTimeCalculationException;
import org.application.services.ServiceManager;

import java.util.List;

public class Contract {
    private final int id;
    private final int expectedWorkingHours;
    private final List<Skill> necessarySkills;
    private final Priority priority;
    private final double longitude;
    private final double latitude;
    private Employee assignedEmployee;
    private Double driveTimeMainStationInHours;

    public Contract(int id, int expectedWorkingHours, List<Skill> necessarySkills, Priority priority, double latitude, double longitude) {
        this.id = id;
        this.expectedWorkingHours = expectedWorkingHours;
        this.necessarySkills = necessarySkills;
        this.priority = priority;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public int getId() {
        return id;
    }

    public int getExpectedWorkingHours() {
        return expectedWorkingHours;
    }

    public Employee getAssignedEmployee() {
        return assignedEmployee;
    }

    public void setAssignedEmployee(Employee assignedEmployee) {
        this.assignedEmployee = assignedEmployee;
    }

    public List<Skill> getNecessarySkills() {
        return necessarySkills;
    }

    public Priority getPriority() {
        return priority;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getDriveTimeMainStationInHours() throws DriveTimeCalculationException {
        if (driveTimeMainStationInHours == null) {
            this.driveTimeMainStationInHours = ServiceManager.getInstance().getDriveTimeCalculationService().calculateDepotDriveTime(this.latitude, this.longitude);
        }

        return driveTimeMainStationInHours;
    }
}
