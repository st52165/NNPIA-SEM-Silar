import React from "react";

const IncidentFormatter = {
    gpsFormatter(cell) {
        return <a href={'https://mapy.cz/zakladni?q='
            + cell.lat + (cell.lat < 0 ? 'S' : 'N')
            + ',' + cell.lon + (cell.lon < 0 ? 'W' : 'E')} target={"_blank"}>{
            cell.lat}° {cell.lat < 0 ? 'S' : 'N'}, {
            cell.lon}° {cell.lon < 0 ? 'W' : 'E'}</a>
    },
    booleanFormatter(isTrue) {
        return (isTrue != null && isTrue) ? "Ano" : "Ne";
    }
}

export default IncidentFormatter;