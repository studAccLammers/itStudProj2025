package org.application;

import org.application.dtos.ContractConfirmation;
import org.application.dtos.Employee;
import org.application.dtos.TestDataHolder;
import org.application.services.DriveTimeCalculationException;
import org.application.services.ServiceManager;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class Main {
    public static void main(String[] args) {
        try {
            List<ContractConfirmation> contractConfirmations = ServiceManager.getInstance().getContractAssignmentService().calculateContractAssignments(
                TestDataHolder.getInstance().getContracts(),
                TestDataHolder.getInstance().getEmployees(),
                LocalDate.parse("2025-01-06")
            );

            printContractConfirmations(contractConfirmations);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }

    public static void printContractConfirmations(List<ContractConfirmation> confirmations) throws DriveTimeCalculationException {
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