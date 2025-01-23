package dtos;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Employee {
    private final int id;
    private final String name;
    private final int minWorkingHours;
    private final int maxWorkingHours;
    private final int maxWorkingHoursPerWeek;
    private final List<Skill> skills;
    private final List<ContractConfirmation> contracts = new ArrayList<>();


    public Employee(int id, String name, int minWorkingHours, int maxWorkingHours, int maxWorkingHoursPerWeek, List<Skill> skills) {
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
        return getAssignedDailyWorkingHours(date) >= minWorkingHours;
    }

    public boolean capableForContract(LocalDate date, LocalDate weekStart, LocalDate weekEnd, Contract contract) {
        int weeklyWorkingHours = getAssignedWeeklyWorkingHours(weekStart, weekEnd);

        if (weeklyWorkingHours + contract.getExpectedWorkingHours() > maxWorkingHoursPerWeek) {
            return false;
        }

        int dailyWorkingHours = getAssignedDailyWorkingHours(date);

        return dailyWorkingHours + contract.getExpectedWorkingHours() <= maxWorkingHours &&
            new HashSet<>(skills).containsAll(contract.getNecessarySkills());
    }

    private int getAssignedDailyWorkingHours(LocalDate date) {
        return contracts.stream()
            .filter(x -> x.getDate().isEqual(date))
            .map(ContractConfirmation::getContract)
            .mapToInt(Contract::getExpectedWorkingHours)
            .sum();
    }

    private int getAssignedWeeklyWorkingHours(LocalDate weekStart, LocalDate weekEnd) {
        return contracts.stream()
            .filter(x -> !x.getDate().isBefore(weekStart) && !x.getDate().isAfter(weekEnd))
            .map(ContractConfirmation::getContract)
            .mapToInt(Contract::getExpectedWorkingHours)
            .sum();
    }

    public void assignContract(LocalDate date, Contract contract) {
        contract.setAssignedEmployee(this);
        contracts.add(new ContractConfirmation(contract, date));
    }

    public List<ContractConfirmation> getContracts() {
        return this.contracts;
    }
}