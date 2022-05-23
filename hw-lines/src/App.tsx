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

import React, { Component } from "react";
import EdgeList from "./EdgeList";
import Map from "./Map";
import {ColoredEdge} from "./types";

// Allows us to write CSS styles inside App.css, any styles will apply to all components inside <App />
import "./App.css";

interface AppState {
    // the list of edges generated based on user input
    edgeList: ColoredEdge[]
}

class App extends Component<{}, AppState> { // <- {} means no props.

  constructor(props: any) {
    super(props);
    this.state = {
        edgeList: []
    };
  }

  render() {
    return (
      <div>
        <h1 id="app-title">Line Mapper!</h1>
        <div>
          <Map edgeList={this.state.edgeList} />
        </div>
        <EdgeList
          onChange={(value) => {
            let lines = value.split("\n");
            let edges = [];
            for (let i = 0; i < lines.length; i++) {
                let params = lines[i].trim().split(" ");
                if (params.length != 5) {
                    alert("Please type properly formatted edges: x1 y1 x2 y2 COLOR");
                    this.setState({ edgeList: [] });
                    return;
                }
                let x1 = parseFloat(params[0]);
                let y1 = parseFloat(params[1]);
                let x2 = parseFloat(params[2]);
                let y2 = parseFloat(params[3]);
                let color = params[4];
                if (isNaN(x1) || isNaN(x2) || isNaN(y1) || isNaN(y2)) {
                    alert("The coordinates should be numbers");
                    this.setState({ edgeList: [] });
                    return;
                }
                if (!isNaN(parseFloat(color))) {
                    alert("The color should be a string, not a number");
                    this.setState({ edgeList: [] });
                    return;
                }
                if (x1 < 0 || x2 < 0 || y1 < 0 || y2 < 0 || x1 > 4000 || x2 > 4000 || y1 > 4000 || y2 > 4000) {
                    alert("The coordinates should be within UW Campus: from (0,0) to (4000,4000)");
                    this.setState({ edgeList: [] });
                    return;
                }
                let edge: ColoredEdge = {
                    x1: x1,
                    y1: y1,
                    x2: x2,
                    y2: y2,
                    color: params[4],
                    key: i
                };
                edges.push(edge);
            }
            this.setState({ edgeList: edges })
          }}
          clear={() => {
              this.setState({ edgeList: [] })
          }}
        />
      </div>
    );
  }
}

export default App;
