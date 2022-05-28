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

package pathfinder;

import graph.Graph;
import pathfinder.datastructures.Path;
import pathfinder.datastructures.Point;
import pathfinder.parser.CampusBuilding;
import pathfinder.parser.CampusPath;
import pathfinder.parser.CampusPathsParser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The CampusMap class represents a graph of campus paths in the UW campus.
 */
public class CampusMap implements ModelAPI {

    // RI: campusBuildings != null, campusPaths != null, campusGraph != null
    // AF(this) = a campus map with all the campus buildings this.campusBuildings and all the campus paths this.campusPaths
    //              and a graph which represents all the paths in the campus this.campusGraph
    private List<CampusBuilding> campusBuildings;
    private List<CampusPath> campusPaths;

    private Graph<Point, Double> campusGraph;

    private static final boolean DEBUG = false;

    public CampusMap() {
        campusBuildings = CampusPathsParser.parseCampusBuildings("campus_buildings.csv");
        campusPaths = CampusPathsParser.parseCampusPaths("campus_paths.csv");

        campusGraph = new Graph<>();
        for (CampusPath path : campusPaths) {
            Point p1 = new Point(path.getX1(), path.getY1());
            Point p2 = new Point(path.getX2(), path.getY2());
            double distance = path.getDistance();
            campusGraph.addNode(p1);
            campusGraph.addNode(p2);
            campusGraph.addEdge(p1, p2, distance);
            campusGraph.addEdge(p2, p1, distance);
        }
    }

    @Override
    public boolean shortNameExists(String shortName) {
        checkRep();
        for (CampusBuilding building : campusBuildings) {
            if (building.getShortName().equals(shortName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String longNameForShort(String shortName) {
        checkRep();
        for (CampusBuilding building : campusBuildings) {
            if (building.getShortName().equals(shortName)) {
                return building.getLongName();
            }
        }
        throw new IllegalArgumentException("The short name provided does not exist");
    }

    @Override
    public Map<String, String> buildingNames() {
        checkRep();
        Map<String, String> map = new HashMap<>();
        for (CampusBuilding building : campusBuildings) {
            map.put(building.getShortName(), building.getLongName());
        }
        return map;
    }

    @Override
    public Path<Point> findShortestPath(String startShortName, String endShortName) {
        checkRep();
        if (startShortName == null || endShortName == null ||
                !shortNameExists(startShortName) || !shortNameExists(endShortName)) {
            throw new IllegalArgumentException("The building names provided is not valid or do not exist in campus map");
        }
        Point start = null;
        Point end = null;
        for (CampusBuilding building : campusBuildings) {
            String shortName = building.getShortName();
            if (shortName.equals(startShortName)) {
                start = new Point(building.getX(), building.getY());
            } else if (shortName.equals(endShortName)) {
                end = new Point(building.getX(), building.getY());
            }
            if (start != null && end != null) break;
        }
        checkRep();
        return Dijkstra.dijkstra(campusGraph, start, end);
    }

    private void checkRep() {
        if (DEBUG) {
            assert campusBuildings != null;
            assert campusPaths != null;
            assert campusGraph != null;
        }
    }

}
