import dtos.Contract;
import dtos.ContractConfirmation;
import dtos.Employee;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ContractService {

    public List<ContractConfirmation> calculateContractAssignments(List<Contract> contracts, List<Employee> employees, LocalDate weekStart) {
        List<Contract> orderedContracts = orderContractsByPriority(contracts);
        LocalDate weekEnd = weekStart.plusDays(5);
        List<Contract> unassignedContracts = assignContractToCapableEmployee(employees, weekStart, weekEnd, orderedContracts);
        unassignedContracts = assignContractToCapableEmployee(employees, weekStart.plusDays(1), weekEnd, unassignedContracts);
        unassignedContracts = assignContractToCapableEmployee(employees, weekStart.plusDays(2), weekEnd, unassignedContracts);
        unassignedContracts = assignContractToCapableEmployee(employees, weekStart.plusDays(3), weekEnd, unassignedContracts);
        unassignedContracts = assignContractToCapableEmployee(employees, weekStart.plusDays(4), weekEnd, unassignedContracts);
        assignContractToCapableEmployee(employees, weekEnd, weekEnd, unassignedContracts);

        List<ContractConfirmation> contractConfirmations = new ArrayList<>();
        employees.forEach(x -> contractConfirmations.addAll(x.getContracts()));
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
}
