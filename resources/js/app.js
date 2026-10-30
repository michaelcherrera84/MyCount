const title = "My Count";
const folderName = "MyCount";

// Display a notification
function createNotification(message) {
    const notification = document.createElement("p");
    notification.classList = "notification";
    notification.textContent = message;
    status.append(notification);
    setTimeout(() => {
        notification.remove();
    }, 4000);
}

async function readData(event) {
    const file = event.target.files[0];

    if (!file) {
        return null;
    }

    const data = JSON.parse(await file.text());

    if (!Array.isArray(data)) {
        throw new Error("The JSON file must contain an array.");
    }

    return data;
}

function isValidLocationObject(obj) {
    if (typeof obj !== "object" || obj === null) return false;

    const allowedKeys = ["id", "name"];
    const actualKeys = Object.keys(obj);

    if (actualKeys.length !== allowedKeys.length) return false;
    const hasValidKeys = actualKeys.every((key) => allowedKeys.includes(key));
    if (!hasValidKeys) return false;

    const hasValidId = typeof obj.id === "number";
    const hasValidName = typeof obj.name === "string" && obj.name.trim() !== "";

    return hasValidId && hasValidName;
}

function isValidUnitObject(obj) {
    if (typeof obj !== "object" || obj === null) return false;

    const allowedKeys = ["id", "name", "abbreviation"];
    const actualKeys = Object.keys(obj);

    if (actualKeys.length !== allowedKeys.length) return false;
    const hasValidKeys = actualKeys.every((key) => allowedKeys.includes(key));
    if (!hasValidKeys) return false;

    const hasValidId = typeof obj.id === "number";
    const hasValidName = typeof obj.name === "string" && obj.name.trim() !== "";
    const hasValidAbbr = typeof obj.abbreviation === "string" && obj.abbreviation.trim() !== "";

    return hasValidId && hasValidName && hasValidAbbr;
}

function isValidItemObject(obj, units, locations) {
    if (typeof obj !== "object" || obj === null) return false;

    const allowedKeys = ["id", "name", "baseUnit", "baseLocation", "additionalUnits"];
    const actualKeys = Object.keys(obj);

    if (actualKeys.length !== allowedKeys.length) return false;
    const hasValidKeys = actualKeys.every((key) => allowedKeys.includes(key));
    if (!hasValidKeys) return false;

    const hasValidId = typeof obj.id === "number";
    const hasValidName = typeof obj.name === "string" && obj.name.trim() !== "";
    const hasValidBaseUnit = typeof obj.baseUnit === "number";
    const hasValidBaseLocation = typeof obj.baseLocation === "number";

    if (!hasValidId || !hasValidName || !hasValidBaseUnit || !hasValidBaseLocation) return false;

    const isValidBaseUnit = units.some((unit) => unit.id === obj.baseUnit);
    const isValidBaseLocation = locations.some((location) => location.id === obj.baseLocation);

    if (!isValidBaseUnit || !isValidBaseLocation) return false;

    const additionalUnits = obj.additionalUnits;

    if (!Array.isArray(additionalUnits)) return false;

    const hasValidAdditionalUnits = additionalUnits.every((unit) => {
        if (typeof unit !== "object" || unit === null || Array.isArray(unit)) {
            return false;
        }

        const allowedKeys = ["conversion", "unit", "location"];
        const actualKeys = Object.keys(unit);

        if (actualKeys.length !== allowedKeys.length) return false;

        const hasValidKeys = actualKeys.every((key) => allowedKeys.includes(key));
        if (!hasValidKeys) return false;

        const hasValidConversion = typeof unit.conversion === "number" && unit.conversion > 0;
        const hasValidUnit = typeof unit.unit === "number";
        const hasValidLocation = typeof unit.location === "number";

        if (!hasValidConversion || !hasValidUnit || !hasValidLocation) {
            return false;
        }

        const isValidUnit = units.some((u) => u.id === unit.unit);
        const isValidLocation = locations.some((l) => l.id === unit.location);

        return isValidUnit && isValidLocation;
    });

    if (!hasValidAdditionalUnits) return false;

    return true;
}

function buildInput(id, labelText, obj, key) {
    const label = document.createElement("label");
    const input = document.createElement("input");

    label.htmlFor = id;
    label.textContent = labelText;
    label.className = "hidden-label";

    input.id = id;
    input.type = "text";
    input.required = true;
    input.value = obj && key ? (obj[key] ?? "") : "";
    input.placeholder = labelText;

    if (obj && key) {
        input.addEventListener("input", () => {
            obj[key] = input.value;
        });
    }

    return [label, input];
}
