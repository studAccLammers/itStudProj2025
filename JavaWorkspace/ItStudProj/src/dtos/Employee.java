package dtos;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Employee {
    private final int id;
    private final String name;
    private final int maxWorkingHours;
    private final int maxWorkingHoursPerWeek;
    private final List<Skill> skills;
    private final List<ContractConfirmation> contracts = new ArrayList<>();


    public Employee(int id, String name, int maxWorkingHours, int maxWorkingHoursPerWeek, List<Skill> skills) {
        this.id = id;
        this.name = name;
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

    public boolean capableForContract(LocalDate date, LocalDate weekStart, LocalDate weekEnd, Contract contract) {
        List<ContractConfirmation> assignedContractsForWeekTuples = contracts.stream().filter(x ->
                x.getDate().getDayOfWeek().getValue() >= weekStart.getDayOfWeek().getValue() &&
                    x.getDate().getMonthValue() >= weekStart.getMonthValue() &&
                    x.getDate().getYear() >= weekStart.getYear() &&
                    x.getDate().getDayOfWeek().getValue() <= weekEnd.getDayOfWeek().getValue() &&
                    x.getDate().getMonthValue() <= weekEnd.getMonthValue() &&
                    x.getDate().getYear() <= weekEnd.getYear())
            .toList();

        int weeklyWorkingHours = 0;

        List<Contract> assignedContractsForWeek = assignedContractsForWeekTuples.stream().map(ContractConfirmation::getContract).toList();

        for (Contract assignedContractInWeek : assignedContractsForWeek) {
            weeklyWorkingHours += assignedContractInWeek.getExpectedWorkingHours();
        }

        if (weeklyWorkingHours + contract.getExpectedWorkingHours() > maxWorkingHoursPerWeek) {
            return false;
        }

        List<Contract> assignedContractsForDay = contracts.stream().filter(x ->
                x.getDate().getDayOfWeek().getValue() == date.getDayOfWeek().getValue() &&
                    x.getDate().getMonthValue() == date.getMonthValue() &&
                    x.getDate().getYear() == date.getYear())
            .map(ContractConfirmation::getContract)
            .toList();

        int dailyWorkingHours = 0;

        for (Contract assignedContractInDay : assignedContractsForDay) {
            dailyWorkingHours += assignedContractInDay.getExpectedWorkingHours();
        }

        return dailyWorkingHours + contract.getExpectedWorkingHours() <= maxWorkingHours &&
            new HashSet<>(skills).containsAll(contract.getNecessarySkills());
    }

    public void assignContract(LocalDate date, Contract contract) {
        contract.setAssignedEmployee(this);
        contracts.add(new ContractConfirmation(contract, date));
    }

    public List<ContractConfirmation> getContracts() {
        return this.contracts;
    }
}