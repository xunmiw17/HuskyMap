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
import {ColoredEdge} from "./types";

interface EdgeListProps {
    // The callback when the "draw" button is clicked, which passes the raw user input to its parent
    onChange(edges: String): void;
    // The callback when the "clear" button is clicked, which clears the edge list
    clear(): void;
}

interface EdgeListState {
    // The text user input displayed in the text box
    text: string;
}

/**
 * A text field that allows the user to enter the list of edges.
 * Also contains the buttons that the user will use to interact with the app.
 */
class EdgeList extends Component<EdgeListProps, EdgeListState> {

    constructor(props: EdgeListProps) {
        super(props);
        this.state = {
            text: ""
        }
    }

    render() {
        return (
            <div id="edge-list">
                Edges <br/>
                <textarea
                    rows={5}
                    cols={30}
                    onChange={(e) => { this.setState({ text: e.target.value }); }}
                    value={this.state.text}
                /> <br/>
                <button onClick={() => { this.props.onChange(this.state.text); }}>Draw</button>
                <button onClick={() => {
                    this.setState({ text: "" });
                    this.props.clear();
                }}>Clear</button>
            </div>
        );
    }
}

export default EdgeList;
