import dtos.ContractConfirmation;
import dtos.Employee;
import dtos.TestDataHolder;
import services.BaseContractAssignmentAssignmentService;
import services.ContractAssignmentService;
import services.NotEnoughWorkingHoursException;

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
        ContractAssignmentService contractAssignmentService = new BaseContractAssignmentAssignmentService();

        try {
            List<ContractConfirmation> contractConfirmations = contractAssignmentService.calculateContractAssignments(
                TestDataHolder.getInstance().getContracts(),
                TestDataHolder.getInstance().getEmployees(),
                LocalDate.parse("2025-01-06")
            );

            printContractConfirmations(contractConfirmations);
        } catch (NotEnoughWorkingHoursException notEnoughWorkingHoursException) {
            System.out.println(notEnoughWorkingHoursException.getMessage());
        }
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

        System.out.println();

        LocalDate weekStart = LocalDate.parse("2025-01-06");

        for (Employee employee : TestDataHolder.getInstance().getEmployees()) {
            System.out.println("m" + employee.getId() + "WeekHours: " + employee.getAssignedWeeklyWorkingHours(weekStart, weekStart.plusDays(4), null));
        }

        System.out.println();
        System.out.println();

        for (LocalDate weekDay = weekStart; weekDay.isBefore(weekStart.plusDays(5)); weekDay = weekDay.plusDays(1)) {
            System.out.println("Day: " + weekDay.getDayOfWeek().toString());

            for (Employee employee : TestDataHolder.getInstance().getEmployees()) {
                System.out.println("m" + employee.getId() + " Hours: " + employee.getAssignedDailyWorkingHours(weekDay));
            }

            System.out.println();
        }
    }
}