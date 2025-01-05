import dtos.ContractConfirmation;
import dtos.TestDataHolder;
import services.BaseContractService;
import services.ContractService;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        ContractService contractService = new BaseContractService();
        List<ContractConfirmation> contractConfirmations = contractService.calculateContractAssignments(
            TestDataHolder.getContracts(),
            TestDataHolder.getEmployees(),
            LocalDate.parse("2025-01-06")
        );

        printContractConfirmations(contractConfirmations);
    }

    public static void printContractConfirmations(List<ContractConfirmation> confirmations) {
        Map<DayOfWeek, List<ContractConfirmation>> confirmationsByDay = confirmations.stream()
            .collect(Collectors.groupingBy(confirmation -> confirmation.getDate().getDayOfWeek()));

        for (DayOfWeek day : DayOfWeek.values()) {
            if (confirmationsByDay.containsKey(day)) {
                System.out.println(day);

                confirmationsByDay.get(day).stream()
                    .sorted(Comparator.comparingInt(cc -> cc.getContract().getAssignedEmployee().getId()))
                    .forEach(cc -> System.out.println(
                        "m" + cc.getContract().getAssignedEmployee().getId() +
                            " -> a" + cc.getContract().getId()));

                System.out.println();
            }
        }
    }
}