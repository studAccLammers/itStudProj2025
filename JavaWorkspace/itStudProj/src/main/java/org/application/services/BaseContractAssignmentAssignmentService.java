package org.application.services;

import org.application.dtos.Contract;
import org.application.dtos.ContractConfirmation;
import org.application.dtos.Employee;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BaseContractAssignmentAssignmentService implements ContractAssignmentService {

    private static final int ASSIGNMENT_ITERATIONS = 50;

    public List<ContractConfirmation> calculateContractAssignments(List<Contract> contracts, List<Employee> employees, LocalDate weekStart) throws NotEnoughWorkingHoursException, DriveTimeCalculationException {
        List<Contract> orderedContracts = orderContractsByPriority(contracts); // Prefer contracts with highest priority first.
        LocalDate weekEnd = weekStart.plusDays(4);

        List<Contract> unassignedContracts = orderedContracts;

        for (int iteration = 0; iteration < ASSIGNMENT_ITERATIONS; iteration++) {
            for (LocalDate weekDay = weekStart; weekDay.isBefore(weekEnd.plusDays(1)); weekDay = weekDay.plusDays(1)) {
                unassignedContracts = assignContractToCapableEmployee(employees, weekDay, weekStart, weekEnd, unassignedContracts);

                if (unassignedContracts.isEmpty()) {
                    break;
                }
            }

            if (unassignedContracts.isEmpty()) {
                break;
            }
        }

        List<ContractConfirmation> contractConfirmations = new ArrayList<>();
        employees.forEach(employee -> contractConfirmations.addAll(employee.getContracts()));

        if (!everyMinWorkingHoursReached(employees, weekStart)) {
            throw new NotEnoughWorkingHoursException();
        }

        return contractConfirmations;
    }

    private List<Contract> assignContractToCapableEmployee(List<Employee> employees, LocalDate day, LocalDate weekStart, LocalDate weekEnd, List<Contract> contracts) throws DriveTimeCalculationException {
        if (contracts.isEmpty()) {
            return new ArrayList<>();
        }

        List<Contract> unassignedContracts = new ArrayList<>(contracts);

        for (Contract contract : contracts) {
            Employee bestEmployee = null;
            double bestScore = Double.MAX_VALUE;

            for (Employee employee : employees) {
                if (employee.capableForContract(day, weekStart, weekEnd, contract)) {
                    List<ContractConfirmation> alreadyAssignedContractsOnDay = employee.getContracts()
                        .stream()
                        .filter(cc -> cc.getDate().isEqual(day))
                        .toList();

                    Contract lastContract = !alreadyAssignedContractsOnDay.isEmpty() ? alreadyAssignedContractsOnDay.getLast().getContract() : null;

                    double driveTime = lastContract != null ?
                        DriveTimeMatrixHandler.getInstance().getDriveTime(lastContract, contract) :
                        contract.getDriveTimeMainStationInHours();

                    //Minimize Score. Fewer DriveTime and fewer Skills and not reached DayWorkTime is preferred.
                    double score = (driveTime * 10) + employee.getSkills().size();
                    if (employee.minWorkingHoursReached(day)) {
                        score += 1;
                    }

                    if (score < bestScore) {
                        bestScore = score;
                        bestEmployee = employee;
                    }
                }
            }

            if (bestEmployee != null) {
                bestEmployee.assignContract(day, contract);
                unassignedContracts.remove(contract);
            }
        }

        return unassignedContracts;
    }


    private List<Contract> orderContractsByPriority(List<Contract> contracts) {
        return contracts.stream()
            .sorted(Comparator.comparing(Contract::getPriority))
            .collect(Collectors.toList());
    }

    private boolean everyMinWorkingHoursReached(List<Employee> employees, LocalDate weekStart) throws RuntimeException {
        return employees.stream().allMatch(employee -> IntStream.range(0, 5).allMatch(i -> {
            try {
                return employee.minWorkingHoursReached(weekStart.plusDays(i));
            } catch (DriveTimeCalculationException e) {
                throw new RuntimeException(e);
            }
        }));
    }

}
