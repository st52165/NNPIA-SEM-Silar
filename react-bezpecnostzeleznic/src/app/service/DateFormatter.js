const DateFormatter = {
    formatDateWithTime(cell) {
        const date = new Date(cell);
        const options = {
            year: 'numeric', month: 'long', day: 'numeric',
            hour: 'numeric', minute: 'numeric', second: 'numeric',
            hour12: false,
        };
        if (cell) {
            return new Intl.DateTimeFormat('cs-CZ', options).format(date);
        } else {
            return null;
        }

    },
    formatDate(input) {
        const date = new Date(input);
        const options = {
            year: 'numeric', month: 'numeric', day: 'numeric',
        };
        return new Intl.DateTimeFormat('cs-CZ', options).format(date);
    }
}

export default DateFormatter;