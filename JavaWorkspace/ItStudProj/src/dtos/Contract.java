package dtos;

import java.util.List;

public class Contract {
    private final int id;
    private final int expectedWorkingHours;
    private final List<Skill> necessarySkills;
    private final Priority priority;
    private Employee assignedEmployee;
    private double driveTimeMainStationInHours;


    public Contract(int id, int expectedWorkingHours, List<Skill> necessarySkills, Priority priority, double driveTimeMainStationInHours) {
        this.id = id;
        this.expectedWorkingHours = expectedWorkingHours;
        this.necessarySkills = necessarySkills;
        this.priority = priority;
        this.driveTimeMainStationInHours = driveTimeMainStationInHours;
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

    public double getDriveTimeMainStationInHours() {
        return driveTimeMainStationInHours;
    }

    public void setDriveTimeMainStationInHours(double driveTimeMainStationInHours) {
        this.driveTimeMainStationInHours = driveTimeMainStationInHours;
    }
}
