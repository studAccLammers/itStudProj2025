package services;

import dtos.Contract;
import dtos.ContractConfirmation;
import dtos.Employee;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class BaseContractService implements ContractService {

    public List<ContractConfirmation> calculateContractAssignments(List<Contract> contracts, List<Employee> employees, LocalDate weekStart) {
        List<Contract> orderedContracts = orderContractsByPriority(contracts); //prefer in iteration contracts with highes priority and so on.
        List<Employee> orderedEmployees = orderEmployeeBySkills(employees); //prefer in iteration employees with fewer skills to have more capacity at high skilled employees for hard cases.

        LocalDate weekEnd = weekStart.plusDays(4);
        List<Contract> unassignedContracts = assignContractToCapableEmployee(orderedEmployees, weekStart, weekEnd, orderedContracts);
        unassignedContracts = assignContractToCapableEmployee(orderedEmployees, weekStart.plusDays(1), weekEnd, unassignedContracts);
        unassignedContracts = assignContractToCapableEmployee(orderedEmployees, weekStart.plusDays(2), weekEnd, unassignedContracts);
        unassignedContracts = assignContractToCapableEmployee(orderedEmployees, weekStart.plusDays(3), weekEnd, unassignedContracts);
        assignContractToCapableEmployee(orderedEmployees, weekEnd, weekEnd, unassignedContracts);

        List<ContractConfirmation> contractConfirmations = new ArrayList<>();
        orderedEmployees.forEach(x -> contractConfirmations.addAll(x.getContracts()));
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

    private List<Employee> orderEmployeeBySkills(List<Employee> employees) {
        return employees.stream()
            .sorted(Comparator.comparingInt(e -> e.getSkills().size()))
            .collect(Collectors.toList());
    }
}
