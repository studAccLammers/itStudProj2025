package org.application.services;

import org.application.dtos.Contract;
import org.application.dtos.ContractConfirmation;
import org.application.dtos.Employee;

import java.time.LocalDate;
import java.util.List;

public interface ContractAssignmentService {
    List<ContractConfirmation> calculateContractAssignments(List<Contract> contracts, List<Employee> employees, LocalDate weekStart) throws DriveTimeCalculationException;
}
