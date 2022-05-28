/*
 * Copyright (C) 2022 Kevin Zatloukal.  All rights reserved.  Permission is
 * hereby granted to students registered for University of Washington
 * CSE 331 for use solely during Spring Quarter 2022 for purposes of
 * the course.  No other use, copying, distribution, or modification
 * is permitted without prior written consent. Copyrights for
 * third-party components of this work must be honored.  Instructors
 * interested in reusing these course materials should contact the
 * author.
 */

package campuspaths;

import campuspaths.utils.CORSFilter;
import com.google.gson.Gson;
import pathfinder.CampusMap;
import pathfinder.datastructures.Path;
import pathfinder.datastructures.Point;
import spark.Spark;

import java.util.Map;

public class SparkServer {

    public static void main(String[] args) {
        CORSFilter corsFilter = new CORSFilter();
        corsFilter.apply();
        // The above two lines help set up some settings that allow the
        // React application to make requests to the Spark server, even though it
        // comes from a different server.
        // You should leave these two lines at the very beginning of main().

        CampusMap campusMap = new CampusMap();

        Spark.get("/buildings", (req, resp) -> {
            Map<String, String> buildings = campusMap.buildingNames();
            Gson gson = new Gson();
            return gson.toJson(buildings);
        });

        Spark.get("/shortestPath", (req, resp) -> {
            String startName = req.queryParams("start");
            String endName = req.queryParams("end");

            Path<Point> shortestPath = null;
            try {
                shortestPath = campusMap.findShortestPath(startName, endName);
            } catch (IllegalArgumentException e) {
                Spark.halt(400, e.getMessage());
            }
            Gson gson = new Gson();
            return gson.toJson(shortestPath);
        });
    }

}
