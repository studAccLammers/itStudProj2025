package org.application.services.testdata;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.application.dtos.Contract;
import org.application.dtos.Employee;
import org.application.dtos.Priority;
import org.application.dtos.Skill;
import org.application.services.ServiceManager;
import org.application.services.drivetime.DriveTimeCalculationException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DataGenerator {
    private static final String BASE_PATH = "C:/Users/nico.lammers/Desktop/TestData";

    private static final double HANNOVER_LAT = 52.3745;
    private static final double HANNOVER_LON = 9.7386;

    private static final double RADIUS_KM = 50.0;

    public static String generateData() throws IOException {
        File targetDirectory = createNextDirectory(BASE_PATH);

        List<Employee> employees = generateRandomEmployees();
        List<Contract> contracts = generateRandomContracts();

        writeJsonFiles(targetDirectory, employees, contracts);

        System.out.println("Die JSON-Dateien wurden erfolgreich in "
            + targetDirectory.getAbsolutePath() + " erstellt.");

        return targetDirectory.getAbsolutePath();
    }


    private static File createNextDirectory(String basePath) throws IOException {
        File baseDir = new File(basePath);
        if (!baseDir.exists() && !baseDir.mkdirs()) {
            throw new IOException("Konnte den Basisordner nicht erstellen: " + baseDir);
        }

        int index = 1;
        File targetDirectory;
        while (true) {
            targetDirectory = new File(baseDir, String.valueOf(index));
            if (!targetDirectory.exists()) {
                if (!targetDirectory.mkdirs()) {
                    throw new IOException("Konnte das Verzeichnis nicht erstellen: " + targetDirectory);
                }
                break;
            }
            index++;
        }

        return targetDirectory;
    }

    private static List<Employee> generateRandomEmployees() {
        Random random = new Random();
        int numberOfEmployees = 1 + random.nextInt(11);

        List<Employee> employees = new ArrayList<>();
        for (int i = 1; i <= numberOfEmployees; i++) {
            List<Skill> randomSkills = getRandomSkills(random);

            Employee employee = new Employee(
                i,
                "m" + i,
                6,
                10,
                40,
                randomSkills
            );

            employees.add(employee);
        }
        return employees;
    }

    private static List<Contract> generateRandomContracts() {
        Random random = new Random();
        int numberOfContracts = 50 + random.nextInt(101);  // min=50, max=150

        List<Contract> contracts = new ArrayList<>();
        for (int i = 1; i <= numberOfContracts; i++) {

            List<Skill> necessarySkills = getRandomSkills(random);
            Priority priority = getRandomPriority(random);

            double[] randomCoords = getRandomCoordinatesInCircle(random);
            double latitude = randomCoords[0];
            double longitude = randomCoords[1];

            Contract contract = new Contract(
                i,
                random.nextInt(8) + 1,
                necessarySkills,
                priority,
                latitude,
                longitude
            );

            contracts.add(contract);
        }
        return contracts;
    }

    private static void writeJsonFiles(File targetDirectory,
                                       List<Employee> employees,
                                       List<Contract> contracts) throws IOException {

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

        File employeesFile = new File(targetDirectory, "employees.json");
        try (FileWriter writer = new FileWriter(employeesFile)) {
            gson.toJson(employees, writer);
        }

        File contractsFile = new File(targetDirectory, "contracts.json");
        try (FileWriter writer = new FileWriter(contractsFile)) {
            gson.toJson(contracts, writer);
        }
    }


    private static List<Skill> getRandomSkills(Random random) {
        Skill[] allSkills = Skill.values();
        int totalSkills = allSkills.length;

        int numberOfSkills = random.nextInt(totalSkills + 1);

        List<Skill> shuffled = new ArrayList<>(List.of(allSkills));
        java.util.Collections.shuffle(shuffled, random);

        return shuffled.subList(0, numberOfSkills);
    }

    private static Priority getRandomPriority(Random random) {
        Priority[] priorities = Priority.values();
        return priorities[random.nextInt(priorities.length)];
    }

    private static double[] getRandomCoordinatesInCircle(Random random) {
        double angle = 2 * Math.PI * random.nextDouble();
        double distance = RADIUS_KM * random.nextDouble();

        double deltaLat = (distance / 111.32) * Math.cos(angle);
        double deltaLon = (distance / (111.32 * Math.cos(Math.toRadians(HANNOVER_LAT)))) * Math.sin(angle);

        double lat = HANNOVER_LAT + deltaLat;
        double lon = HANNOVER_LON + deltaLon;

        try {
            ServiceManager.getInstance().getDriveTimeCalculationService().calculateDepotDriveTime(lat, lon);
        } catch (DriveTimeCalculationException dce) {
            return getRandomCoordinatesInCircle(random);
        }

        return new double[]{lat, lon};
    }
}

