import {Component} from "react";

interface BuildingChooserProps {
    chooseStart(startName: string): void;
    chooseEnd(endName: string): void;
    findPath(): void;
    reset(): void;

    buildings: object;
    startSelectVal: string;
    endSelectVal: string;
}

class BuildingChooser extends Component<BuildingChooserProps, {}> {

    render() {
        // Transforms the buildings object into dropdown list
        let buildingObj = this.props.buildings;
        let buildingJSX: JSX.Element[] = [];
        buildingJSX.push(<option value="" key="" disabled>------</option>)
        for (let building in buildingObj) {
            let option = <option value={building} key={building}>{building}</option>;
            buildingJSX.push(option);
        }

        return (
            <div id="buildingChooser">
                <div>
                    <label htmlFor="startBuildings">Choose a start building</label>
                    <select name="startBuildings" id="startSelect" value={this.props.startSelectVal} onChange={(evt) => { this.props.chooseStart(evt.target.value) }}>
                        { buildingJSX }
                    </select>
                </div>
                <div>
                    <label htmlFor="endBuildings">Choose an end building</label>
                    <select name="endBuildings" id="endSelect" value={this.props.endSelectVal} onChange={(evt) => { this.props.chooseEnd(evt.target.value) }}>
                        { buildingJSX }
                    </select>
                </div>
                <button onClick={() => { this.props.findPath(); }}>Find Shortest Path!</button>
                <button onClick={() => { this.props.reset(); }}>Reset</button>
            </div>
        )
    }
}

export default BuildingChooser;