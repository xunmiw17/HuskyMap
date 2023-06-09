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

import React, {Component} from 'react';

// Allows us to write CSS styles inside App.css, any styles will apply to all components inside <App />
import "./App.css";
import {ColoredEdge} from "./types";
import Map from "./Map";
import BuildingChooser from "./BuildingChooser";

interface AppState {
    // the list of edges generated based on user input
    edgeList: ColoredEdge[];
    startName: string;
    endName: string;
    // all building names on campus
    buildings: object;

    startSelectVal: string;
    endSelectVal: string;
}

class App extends Component<{}, AppState> {

    constructor(props: any) {
        super(props);
        this.state = {
            edgeList: [],
            startName: "",
            endName: "",
            buildings: {},
            startSelectVal: "",
            endSelectVal: ""
        }
    }

    componentDidMount() {
        this.getBuildings();
    }

    /**
     * Gets all the building names from the server
     */
    async getBuildings() {
        let resp = await fetch("http://localhost:4567/buildings");
        if (!resp.ok) {
            alert("Status code: " + resp.status + ", there has been a problem with fetching all the buildings from the server");
            return;
        }
        let respJson = await resp.json();
        this.setState({ buildings: respJson });
    }

    /**
     * Finds the shortest path from the given start building to end building
     */
    async findShortestPath() {
        let resp = await fetch("http://localhost:4567/shortestPath?start=" +
                                            this.state.startName + "&end=" + this.state.endName);
        if (!resp.ok) {
            alert("Status code: " + resp.status + ", there has been a problem with searching the shortest path");
            return;
        }
        let respJson = await resp.json();
        // Gets the shortest path object and turns it into a list of colored edges
        let path = respJson.path;
        let coloredEdges = [];
        for (let i = 0; i < path.length; i++) {
            let edge = path[i];
            let coloredEdge: ColoredEdge = {
                x1: edge.start.x,
                y1: edge.start.y,
                x2: edge.end.x,
                y2: edge.end.y,
                color: "black",
                key: i
            };
            coloredEdges.push(coloredEdge);
        }
        this.setState({ edgeList: coloredEdges });
    }

    render() {
        return (
            <div>
                <h1 id="app-title">Campus Map Path Finder</h1>
                <div>
                    <BuildingChooser chooseStart={(value) => {
                        this.setState({ startName: value })
                        this.setState({ startSelectVal: value })
                    }}
                    chooseEnd={value => {
                        this.setState({ endName: value })
                        this.setState({ endSelectVal: value })
                    }}
                    findPath={() => {
                        let start = this.state.startName;
                        let end = this.state.endName;
                        if (start === "" || end === "") {
                            alert("You must choose a start and end building to find the shortest path");
                            return;
                        }
                        this.findShortestPath()
                    }}
                    reset={() => {
                        this.setState({ edgeList: [] });
                        this.setState({ startName: "" });
                        this.setState({ endName: "" });
                        this.setState({ startSelectVal: "" });
                        this.setState({ endSelectVal: "" });
                    }}
                    buildings={ this.state.buildings }
                    startSelectVal={this.state.startSelectVal}
                    endSelectVal={this.state.endSelectVal}/>
                </div>
                <div>
                    <Map edgeList={ this.state.edgeList }/>
                </div>
            </div>
        );
    }
}

export default App;
