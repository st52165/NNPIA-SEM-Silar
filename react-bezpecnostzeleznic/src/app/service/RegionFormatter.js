import React from "react";

const RegionFormatter = {
    nameFormatter(cell) {
        return cell.name + " (" + cell.shortName + ")"
    }
}

export default RegionFormatter;