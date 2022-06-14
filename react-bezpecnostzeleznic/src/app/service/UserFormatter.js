import React from "react";

const UserFormatter = {
    nameFormatter(cell) {
        return cell.firstname + " " + cell.lastname + " (" + cell.username + ")"
    }
}

export default UserFormatter;