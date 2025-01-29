package org.application.dtos;

import com.google.gson.annotations.Expose;
import org.application.services.ServiceManager;
import org.application.services.drivetime.DriveTimeCalculationException;

import java.util.List;

public class Contract {
    @Expose
    private final int id;

    @Expose
    private final double expectedWorkingHours;

    @Expose
    private final List<Skill> necessarySkills;

    @Expose
    private final Priority priority;

    @Expose
    private final double longitude;

    @Expose
    private final double latitude;

    private Employee assignedEmployee;
    private Double driveTimeMainStationInHours;

    public Contract(int id, double expectedWorkingHours, List<Skill> necessarySkills, Priority priority, double latitude, double longitude) {
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

    public double getExpectedWorkingHours() {
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
