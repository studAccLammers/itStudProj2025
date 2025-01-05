package services;

import dtos.Contract;
import dtos.ContractConfirmation;
import dtos.Employee;

import java.time.LocalDate;
import java.util.List;

public interface ContractService {
    List<ContractConfirmation> calculateContractAssignments(List<Contract> contracts, List<Employee> employees, LocalDate weekStart);
}
