package org.application.services.drivetime;

import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.GraphHopper;
import com.graphhopper.config.Profile;
import com.graphhopper.util.Parameters;

public class BaseDriveTimeCalculationService implements DriveTimeCalculationService {

    private static final String GRAPH_HOPPER_DIR = "mapData/graphhopper";
    private static final String GRAPH_HOPPER_OSM_PATH = "mapData/niedersachsen-latest.osm.pbf";
    private static final String CAR_PROFILE_NAME = "car";

    private static final Double DEPOT_LAT = 52.1333333;
    private static final Double DEPOT_LON = 9.975;

    private GraphHopper hopper;

    public BaseDriveTimeCalculationService() {
        initializeGraphhopper();
    }

    private void initializeGraphhopper() {
        hopper = new GraphHopper();
        hopper.setGraphHopperLocation(GRAPH_HOPPER_DIR);
        hopper.setOSMFile(GRAPH_HOPPER_OSM_PATH);
        hopper.setProfiles(new Profile(CAR_PROFILE_NAME).setVehicle("car").setWeighting("fastest"));
        hopper.importOrLoad();
    }

    @Override
    public double calculateDriveTime(double startLat, double startLon, double endLat, double endLon) throws DriveTimeCalculationException {
        try {
            GHRequest request = new GHRequest(startLat, startLon, endLat, endLon)
                .setProfile(CAR_PROFILE_NAME)
                .setAlgorithm(Parameters.Algorithms.DIJKSTRA_BI);
            GHResponse response = hopper.route(request);

            if (response.hasErrors()) {
                throw new DriveTimeCalculationException(response.getErrors().toString(), startLat, startLon, endLat, endLon);
            }

            long timeInMs = response.getBest().getTime(); // driveTime in ms
            long timeInSeconds = timeInMs / 1000;
            return timeInSeconds / 60.0 / 60.0;
        } catch (Exception e) {
            throw new DriveTimeCalculationException(e.getMessage(), startLat, startLon, endLat, endLon);
        }
    }

    @Override
    public double calculateDepotDriveTime(double lat, double lon) throws DriveTimeCalculationException {
        return calculateDriveTime(lat, lon, DEPOT_LAT, DEPOT_LON);
    }
}
