package org.application.dtos;

import org.application.services.drivetime.DriveTimeCalculationException;
import org.application.services.drivetime.DriveTimeMatrixHandler;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;

public class Employee {
    private final int id;
    private final String name;
    private final double minWorkingHours;
    private final double maxWorkingHours;
    private final double maxWorkingHoursPerWeek;
    private final List<Skill> skills;
    private final List<ContractConfirmation> contracts = new ArrayList<>();


    public Employee(int id, String name, double minWorkingHours, double maxWorkingHours, double maxWorkingHoursPerWeek, List<Skill> skills) {
        this.id = id;
        this.name = name;
        this.minWorkingHours = minWorkingHours;
        this.maxWorkingHours = maxWorkingHours;
        this.maxWorkingHoursPerWeek = maxWorkingHoursPerWeek;
        this.skills = skills;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public boolean minWorkingHoursReached(LocalDate date) throws DriveTimeCalculationException {
        return getAssignedDailyWorkingHoursWithoutHomeDrive(date) >= minWorkingHours;
    }

    public boolean capableForContract(LocalDate date, LocalDate weekStart, LocalDate weekEnd, Contract contract) throws DriveTimeCalculationException {
        if (new HashSet<>(skills).containsAll(contract.getNecessarySkills())) {
            return false;
        }

        Contract lastAssignedContractOfDay = getLastAssignedContractOfDay(date);

        double driveTimeToContract = lastAssignedContractOfDay != null ?
            DriveTimeMatrixHandler.getInstance().getDriveTime(lastAssignedContractOfDay, contract) :
            contract.getDriveTimeMainStationInHours();

        if (getAssignedWeeklyWorkingHours(weekStart, weekEnd, date) +
            driveTimeToContract +
            contract.getExpectedWorkingHours() +
            contract.getDriveTimeMainStationInHours()
            > maxWorkingHoursPerWeek) {
            return false;
        }

        return getAssignedDailyWorkingHoursWithoutHomeDrive(date) +
            driveTimeToContract +
            contract.getExpectedWorkingHours() +
            contract.getDriveTimeMainStationInHours() <= maxWorkingHours;
    }

    public double getAssignedDailyWorkingHours(LocalDate date) throws DriveTimeCalculationException {
        Contract lastAssignedContractOfDay = getLastAssignedContractOfDay(date);
        double homeDriveTime = lastAssignedContractOfDay != null ? lastAssignedContractOfDay.getDriveTimeMainStationInHours() : 0;
        return getAssignedDailyWorkingHoursWithoutHomeDrive(date) + homeDriveTime;
    }

    public double getAssignedWeeklyWorkingHours(LocalDate weekStart, LocalDate weekEnd, LocalDate exceptHomeDriveTimeFromDate) throws DriveTimeCalculationException {
        double homeDriveTime = 0;

        for (LocalDate weekDay = weekStart; weekDay.isBefore(weekEnd.plusDays(1)); weekDay = weekDay.plusDays(1)) {
            if (exceptHomeDriveTimeFromDate == null || !weekDay.isEqual(exceptHomeDriveTimeFromDate)) {
                Contract lastAssignedContractOfWeekDay = getLastAssignedContractOfDay(weekDay);
                homeDriveTime += lastAssignedContractOfWeekDay != null ? lastAssignedContractOfWeekDay.getDriveTimeMainStationInHours() : 0;
            }
        }

        return getAssignedWeeklyWorkingHoursWithoutHomeDrive(weekStart, weekEnd) + homeDriveTime;
    }

    public void assignContract(LocalDate date, Contract contract) {
        contract.setAssignedEmployee(this);
        contracts.add(new ContractConfirmation(contract, date));
    }

    public List<ContractConfirmation> getContracts() {
        return this.contracts;
    }

    private double getAssignedDailyWorkingHoursWithoutHomeDrive(LocalDate date) throws DriveTimeCalculationException {
        List<Contract> contracts = getContracts()
            .stream()
            .filter(x -> x.getDate().isEqual(date))
            .map(ContractConfirmation::getContract)
            .toList();

        if (contracts.isEmpty()) {
            return 0;
        }

        double workingHours = sumHoursAndDriveTime(contracts);
        return workingHours + contracts.getFirst().getExpectedWorkingHours();
    }

    private double getAssignedWeeklyWorkingHoursWithoutHomeDrive(LocalDate weekStart, LocalDate weekEnd) throws DriveTimeCalculationException {
        List<Contract> contracts = getContracts()
            .stream()
            .filter(x -> !x.getDate().isBefore(weekStart) && !x.getDate().isAfter(weekEnd)).map(ContractConfirmation::getContract)
            .toList();

        if (contracts.isEmpty()) {
            return 0;
        }

        double workingHours = sumHoursAndDriveTime(contracts);

        for (LocalDate weekDay = weekStart; weekDay.isBefore(weekEnd.plusDays(1)); weekDay = weekDay.plusDays(1)) {
            LocalDate finalWeekDay = weekDay;
            Contract firstContractOnDay = getContracts()
                .stream()
                .filter(x -> x.getDate().isEqual(finalWeekDay))
                .map(ContractConfirmation::getContract)
                .findFirst()
                .orElse(null);

            workingHours += firstContractOnDay != null ? firstContractOnDay.getDriveTimeMainStationInHours() : 0;
        }

        return workingHours;
    }

    private double sumHoursAndDriveTime(List<Contract> contracts) throws DriveTimeCalculationException {
        double workingHours = 0;

        for (Contract contract : contracts) {
            workingHours += contract.getExpectedWorkingHours();

            if (!contracts.getFirst().equals(contract)) {
                workingHours += DriveTimeMatrixHandler.getInstance().getDriveTime(contracts.get(contracts.indexOf(contract) - 1), contract);
            }
        }

        return workingHours;
    }

    private Contract getLastAssignedContractOfDay(LocalDate date) {
        try {
            return getContracts()
                .stream()
                .filter(x -> x.getDate().isEqual(date))
                .map(ContractConfirmation::getContract)
                .toList()
                .getLast();
        } catch (NoSuchElementException NSE) {
            return null;
        }
    }
}