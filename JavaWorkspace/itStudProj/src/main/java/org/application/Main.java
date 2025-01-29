package org.application;

import org.application.dtos.ContractConfirmation;
import org.application.dtos.Employee;
import org.application.services.testdata.TestDataLoader;
import org.application.services.ServiceManager;
import org.application.services.contractassignment.NotEnoughWorkingHoursException;
import org.application.services.drivetime.DriveTimeCalculationException;
import org.application.services.testdata.DataGenerator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class Main {
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            String path = null;
            try {
                path = DataGenerator.generateData();
                TestDataLoader.getInstance().initialize(path);

                List<ContractConfirmation> contractConfirmations = ServiceManager.getInstance().getContractAssignmentService().calculateContractAssignments(
                    TestDataLoader.getInstance().getContracts(),
                    TestDataLoader.getInstance().getEmployees(),
                    LocalDate.parse("2025-01-06")
                );

                printContractConfirmations(contractConfirmations, path);
            } catch (DriveTimeCalculationException | NotEnoughWorkingHoursException exception) {
                if (path != null) {
                    printException(exception, path);
                }
            } catch (Exception exception) {
                System.out.println(exception.getMessage());
                exception.printStackTrace();
            }
        }
    }

    private static void printException(Exception exception, String path) {
        File errorFile = new File(path, "exception.txt");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(errorFile))) {
            bw.write("Exception Message: " + exception.getMessage());
            bw.newLine();
            bw.newLine();
            bw.write("StackTrace:");
            bw.newLine();

            for (StackTraceElement element : exception.getStackTrace()) {
                bw.write("    at " + element.toString());
                bw.newLine();
            }
        } catch (IOException ioException) {
            System.out.println(ioException.getMessage());
            ioException.printStackTrace();
        }
    }

    private static void printContractConfirmations(List<ContractConfirmation> confirmations, String path) throws DriveTimeCalculationException, IOException {

        File outputFile = new File(path, "contractConfirmations.txt");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {

            Map<DayOfWeek, List<ContractConfirmation>> confirmationsByDay = confirmations.stream()
                .collect(Collectors.groupingBy(confirmation -> confirmation.getDate().getDayOfWeek()));

            for (DayOfWeek day : DayOfWeek.values()) {
                if (confirmationsByDay.containsKey(day)) {
                    writer.write(day.toString());
                    writer.newLine();

                    confirmationsByDay.get(day).stream()
                        .sorted(Comparator.comparingInt(cc -> cc.getContract().getAssignedEmployee().getId()))
                        .forEach(cc -> {
                            try {
                                writer.write(
                                    "m" + cc.getContract().getAssignedEmployee().getId() +
                                        " -> a" + cc.getContract().getId()
                                );
                                writer.newLine();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                    writer.newLine();
                }
            }

            writer.newLine();

            LocalDate weekStart = LocalDate.parse("2025-01-06");

            for (Employee employee : TestDataLoader.getInstance().getEmployees()) {
                writer.write("m" + employee.getId() + " WeekHours: "
                    + employee.getAssignedWeeklyWorkingHours(weekStart, weekStart.plusDays(4), null));
                writer.newLine();
            }

            writer.newLine();
            writer.newLine();

            for (LocalDate weekDay = weekStart; weekDay.isBefore(weekStart.plusDays(5)); weekDay = weekDay.plusDays(1)) {
                writer.write("Day: " + weekDay.getDayOfWeek());
                writer.newLine();

                for (Employee employee : TestDataLoader.getInstance().getEmployees()) {
                    writer.write("m" + employee.getId() + " Hours: " + employee.getAssignedDailyWorkingHours(weekDay));
                    writer.newLine();
                }
                writer.newLine();
            }
        }
    }

}