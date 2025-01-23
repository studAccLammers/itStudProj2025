package services;

import dtos.Contract;
import dtos.ContractConfirmation;
import dtos.Employee;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BaseContractAssignmentAssignmentService implements ContractAssignmentService {

    private static final int ASSIGNMENT_ITERATIONS = 20;

    public List<ContractConfirmation> calculateContractAssignments(List<Contract> contracts, List<Employee> employees, LocalDate weekStart) throws NotEnoughWorkingHoursException {
        List<Contract> orderedContracts = orderContractsByPriority(contracts); // Prefer contracts with highest priority first.
        LocalDate weekEnd = weekStart.plusDays(4);

        List<Contract> unassignedContracts = orderedContracts;

        for (int iteration = 0; iteration < ASSIGNMENT_ITERATIONS; iteration++) {
            for (int i = 0; i < 5; i++) {
                LocalDate currentDay = weekStart.plusDays(i);
                //Prefer employees not reached MinHoursForDay and then with fewer Skills to safe for complex contracts
                List<Employee> orderedEmployees = orderEmployeeByMinWorkingHoursThenBySkills(employees, currentDay);
                unassignedContracts = assignContractToCapableEmployee(orderedEmployees, currentDay, weekEnd, unassignedContracts);
            }
        }

        List<ContractConfirmation> contractConfirmations = new ArrayList<>();
        employees.forEach(employee -> contractConfirmations.addAll(employee.getContracts()));

        if (!everyMinWorkingHoursReached(employees, weekStart)) {
            throw new NotEnoughWorkingHoursException();
        }

        return contractConfirmations;
    }

    private List<Contract> assignContractToCapableEmployee(List<Employee> employees, LocalDate day, LocalDate weekEnd, List<Contract> contracts) {
        if (contracts.isEmpty()) {
            return new ArrayList<>();
        }

        List<Contract> unassignedContracts = new ArrayList<>(contracts);

        for (Contract contract : contracts) {
            for (Employee employee : employees) {
                if (employee.capableForContract(day, day, weekEnd, contract)) {
                    employee.assignContract(day, contract);
                    unassignedContracts.remove(contract);
                    break;
                }
            }
        }

        return unassignedContracts;
    }

    private List<Contract> orderContractsByPriority(List<Contract> contracts) {
        return contracts.stream()
            .sorted(Comparator.comparing(Contract::getPriority))
            .collect(Collectors.toList());
    }

    private List<Employee> orderEmployeeByMinWorkingHoursThenBySkills(List<Employee> employees, LocalDate day) {
        return employees.stream()
            .sorted(Comparator
                .comparing((Employee e) -> e.minWorkingHoursReached(day))
                .thenComparing(e -> e.getSkills().size()))
            .collect(Collectors.toList());
    }

    private boolean everyMinWorkingHoursReached(List<Employee> employees, LocalDate weekStart) {
        return employees.stream().allMatch(employee -> IntStream.range(0, 5).allMatch(i -> employee.minWorkingHoursReached(weekStart.plusDays(i))));
    }

}
