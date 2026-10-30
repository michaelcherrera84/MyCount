let units = [];

const saveButton = document.querySelector("#save-units");
const addUnitForm = document.querySelector("#add-unit-form");
const initial = document.querySelector("#init");
const main = document.querySelector("#main");
const content = document.querySelector("#content");
const status = document.querySelector("#status");

// Read units.json
async function readUnits(event) {
    try {
        const data = await readData(event);

        if (data === null) {
            return;
        }

        const invalidData = data.filter((unit) => !isValidUnitObject(unit));

        units = data;

        renderUnits();

        initial.remove();
        content.style.display = "flex";

        if (invalidData.length > 0) {
            alert(
                `${invalidData.length} invalid unit${invalidData.length === 1 ? " was" : "s were"} found. Please verify that you loaded the correct file and correct any invalid units before continuing.`,
            );
        }
    } catch (error) {
        alert(`An error occurred loading the units file.\n${error}`);
        console.error("Error reading locations:", error);
    }
}

// Display units
function renderUnits() {
    addUnitForm.replaceChildren();

    units.forEach((unit) => {
        const container = document.createElement("div");
        const nameInput = buildInput(`n${unit.id}`, "Unit Name", unit, "name");
        const abbreviationInput = buildInput(`a${unit.id}`, "Unit Abbreviation", unit, "abbreviation");
        const deleteButton = document.createElement("button");

        container.classList.add("input");

        deleteButton.innerText = "Remove";
        deleteButton.className = "button";
        deleteButton.type = "button";
        deleteButton.addEventListener("click", () => {
            const dialog = document.querySelector("dialog");
            dialog.showModal();

            dialog.onclose = (event) => {
                if (dialog.returnValue === "remove") {
                    units = units.filter((item) => item.id !== unit.id);
                    renderUnits();
                    createNotification(`"${unit.name}" removed. Don't forget to save when you are finished.`);
                }
            };

            dialog.oncancel = () => {};
        });

        container.append(...nameInput, ...abbreviationInput, deleteButton);
        addUnitForm.append(container);
    });

    const container = document.createElement("div");
    const nameInput = buildInput("unit-name", "Unit Name");
    const abbreviationInput = buildInput("unit-abbreviation", "Unit Abbreviation");
    const addButton = document.createElement("button");

    container.classList.add("input");

    addButton.innerText = "Add Unit";
    addButton.className = "button";
    addButton.type = "submit";

    container.append(...nameInput, ...abbreviationInput, addButton);
    addUnitForm.append(container);
}

// Add a unit
function addUnit(event) {
    event.preventDefault();

    const unitNameInput = document.querySelector("#unit-name");
    const unitAbbreviationInput = document.querySelector("#unit-abbreviation");

    const name = unitNameInput.value.trim();
    const abbreviation = unitAbbreviationInput.value.trim();

    if (!name || !abbreviation) {
        return;
    }

    const nextId = units.length > 0 ? Math.max(...units.map((unit) => unit.id)) + 1 : 1;

    units.push({
        id: nextId,
        name: name,
        abbreviation: abbreviation,
    });

    renderUnits();

    createNotification(`"${name}" added. Don't forget to save when you are finished.`);
}

// Save units.json
function saveUnits() {
    const json = JSON.stringify(units, null, 2);

    const blob = new Blob([json], {
        type: "application/json",
    });

    const url = URL.createObjectURL(blob);

    const link = document.createElement("a");
    link.href = url;
    link.download = "units.json";

    document.body.appendChild(link);
    link.click();
    link.remove();

    URL.revokeObjectURL(url);
}

// Event listeners
document.querySelector("#units-file").addEventListener("change", readUnits);
addUnitForm.addEventListener("submit", addUnit);
saveButton.addEventListener("click", saveUnits);
