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

import { LatLngExpression } from "leaflet";
import React, { Component } from "react";
import { MapContainer, TileLayer } from "react-leaflet";
import "leaflet/dist/leaflet.css";
import MapLine from "./MapLine";
import { UW_LATITUDE_CENTER, UW_LONGITUDE_CENTER } from "./Constants";
import {ColoredEdge} from "./types";

// This defines the location of the map. These are the coordinates of the UW Seattle campus
const position: LatLngExpression = [UW_LATITUDE_CENTER, UW_LONGITUDE_CENTER];

interface MapProps {
    // the list of edges to be drawn
    edgeList: ColoredEdge[]
}

interface MapState {

}

class Map extends Component<MapProps, MapState> {

    constructor(props: MapProps) {
        super(props);
    }

  render() {

    // Transforms the edge list into an array of MapLines to be displayed in the map
    let lines: JSX.Element[] = []
    for (let i = 0; i < this.props.edgeList.length; i++) {
        let edge = this.props.edgeList[i];
        let mapLine = <MapLine key={i} color={edge.color} x1={edge.x1} y1={edge.y1} x2={edge.x2} y2={edge.y2} />
        lines.push(mapLine);
    }

    return (
      <div id="map">
        <MapContainer
          center={position}
          zoom={15}
          scrollWheelZoom={false}
        >
          <TileLayer
            attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
            url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
          />
          {
              <div>
                  { lines }
              </div>
          }
        </MapContainer>
      </div>
    );
  }
}

export default Map;
