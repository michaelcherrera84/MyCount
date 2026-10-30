let locations = [];

const saveButton = document.querySelector("#save-locations");
const addLocationForm = document.querySelector("#add-location-form");
const initial = document.querySelector("#init");
const main = document.querySelector("#main");
const content = document.querySelector("#content");
const status = document.querySelector("#status");

async function readLocations(event) {
    try {
        const data = await readData(event);

        if (data === null) {
            return;
        }

        const invalidData = data.filter((location) => !isValidLocationObject(location));

        locations = data;

        renderLocations();

        initial.remove();
        content.style.display = "flex";

        if (invalidData.length > 0) {
            alert(
                `${invalidData.length} invalid location${invalidData.length === 1 ? " was" : "s were"} found. Please verify that you loaded the correct file and correct any invalid locations before continuing.`,
            );
        }
    } catch (error) {
        alert(`An error occurred loading the locations file.\n${error}`);
        console.error("Error reading locations:", error);
    }
}

// Display locations
function renderLocations() {
    addLocationForm.replaceChildren();

    locations.forEach((location) => {
        const container = document.createElement("div");
        const input = buildInput(location.id, "Location Name", location, "name");
        const deleteButton = document.createElement("button");

        container.classList.add("input");

        deleteButton.textContent = "Remove";
        deleteButton.className = "button";
        deleteButton.type = "button";
        deleteButton.addEventListener("click", () => {
            const dialog = document.querySelector("dialog");
            dialog.showModal();

            dialog.onclose = (event) => {
                if (dialog.returnValue === "remove") {
                    locations = locations.filter((item) => item.id !== location.id);
                    renderLocations();
                    createNotification(`"${location.name}" removed. Don't forget to save when you are finished.`);
                }
            };

            dialog.oncancel = () => {};
        });

        container.append(...input, deleteButton);
        addLocationForm.append(container);
    });

    const container = document.createElement("div");
    const input = buildInput("location-name", "Location Name");
    const addButton = document.createElement("button");

    container.classList.add("input");

    addButton.textContent = "Add Location";
    addButton.className = "button";
    addButton.type = "submit";

    container.append(...input, addButton);
    addLocationForm.append(container);
}

// Add a location
function addLocation(event) {
    event.preventDefault();

    const locationNameInput = document.querySelector("#location-name");
    const name = locationNameInput.value.trim();

    if (!name) {
        return;
    }

    const nextId = locations.length > 0 ? Math.max(...locations.map((location) => location.id)) + 1 : 1;

    locations.push({
        id: nextId,
        name: name,
    });

    renderLocations();

    createNotification(`"${name}" added. Don't forget to save when you are finished.`);
}

// Save locations.json
function saveLocations() {
    const json = JSON.stringify(locations, null, 2);

    const blob = new Blob([json], {
        type: "application/json",
    });

    const url = URL.createObjectURL(blob);

    const link = document.createElement("a");
    link.href = url;
    link.download = "locations.json";

    document.body.appendChild(link);
    link.click();
    link.remove();

    URL.revokeObjectURL(url);
}

// Event listeners
document.querySelector("#locations-file").addEventListener("change", readLocations);
addLocationForm.addEventListener("submit", addLocation);
saveButton.addEventListener("click", saveLocations);
