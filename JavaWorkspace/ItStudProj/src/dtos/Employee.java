package dtos;

import services.DriveTimeMatrixHandler;

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

    public boolean minWorkingHoursReached(LocalDate date) {
        return getAssignedDailyWorkingHoursWithoutHomeDrive(date) >= minWorkingHours;
    }

    public boolean capableForContract(LocalDate date, LocalDate weekStart, LocalDate weekEnd, Contract contract) {
        double weeklyWorkingHours = getAssignedWeeklyWorkingHoursWithoutHomeDrive(weekStart, weekEnd);

        if (weeklyWorkingHours + contract.getExpectedWorkingHours() + contract.getDriveTimeMainStationInHours() > maxWorkingHoursPerWeek) {
            return false;
        }

        double dailyWorkingHours = getAssignedDailyWorkingHoursWithoutHomeDrive(date);

        return dailyWorkingHours + contract.getExpectedWorkingHours() + contract.getDriveTimeMainStationInHours() <= maxWorkingHours &&
            new HashSet<>(skills).containsAll(contract.getNecessarySkills());
    }

    public double getAssignedDailyWorkingHours(LocalDate date) {
        double homeDriveTime = 0;

        try {
            homeDriveTime = getContracts()
                .stream()
                .filter(x -> x.getDate().isEqual(date))
                .map(ContractConfirmation::getContract)
                .toList()
                .getLast()
                .getDriveTimeMainStationInHours();
        } catch (NoSuchElementException NSE) {
            //do Nothing
        }

        return getAssignedDailyWorkingHoursWithoutHomeDrive(date) + homeDriveTime;
    }

    public double getAssignedWeeklyWorkingHours(LocalDate weekStart, LocalDate weekEnd) {
        double homeDriveTime = 0;

        for (LocalDate weekDay = weekStart; weekDay.isBefore(weekEnd.plusDays(1)); weekDay = weekDay.plusDays(1)) {
            LocalDate finalWeekDay = weekDay;

            try {
                double homeDriveTimeDay = getContracts()
                    .stream()
                    .filter(x -> x.getDate().isEqual(finalWeekDay))
                    .map(ContractConfirmation::getContract)
                    .toList()
                    .getLast()
                    .getDriveTimeMainStationInHours();

                homeDriveTime += homeDriveTimeDay;
            } catch (NoSuchElementException NSE) {
                //do Nothing
            }
        }

        return getAssignedWeeklyWorkingHoursWithoutHomeDrive(weekStart, weekEnd) + homeDriveTime;
    }

    private double getAssignedDailyWorkingHoursWithoutHomeDrive(LocalDate date) {
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

    private double getAssignedWeeklyWorkingHoursWithoutHomeDrive(LocalDate weekStart, LocalDate weekEnd) {
        List<Contract> contracts = getContracts()
            .stream()
            .filter(x -> !x.getDate().isBefore(weekStart) && !x.getDate().isAfter(weekEnd)).map(ContractConfirmation::getContract)
            .toList();

        if (contracts.isEmpty()) {
            return 0;
        }

        double workingHours = sumHoursAndDriveTime(contracts);

        for (LocalDate weekDay = weekStart; weekDay.isBefore(weekEnd); weekDay = weekDay.plusDays(1)) {
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

    private double sumHoursAndDriveTime(List<Contract> contracts) {
        double workingHours = 0;

        for (Contract contract : contracts) {
            workingHours += contract.getExpectedWorkingHours();

            if (!contracts.getFirst().equals(contract)) {
                workingHours += DriveTimeMatrixHandler.getInstance().getDriveTime(contracts.get(contracts.indexOf(contract) - 1), contract);
            }
        }

        return workingHours;
    }

    public void assignContract(LocalDate date, Contract contract) {
        contract.setAssignedEmployee(this);
        contracts.add(new ContractConfirmation(contract, date));
    }

    public List<ContractConfirmation> getContracts() {
        return this.contracts;
    }
}