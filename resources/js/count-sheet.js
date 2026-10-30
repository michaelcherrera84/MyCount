let items = [];
let locations = [];
let units = [];
let counts = [];
let activeTab;

const saveButton = document.querySelector("#save-count");
const viewCountButton = document.querySelector("#view-count");
const initial = document.querySelector("#init");
const main = document.querySelector("#main");
const content = document.querySelector("#content");
const status = document.querySelector("#status");
const tabs = document.querySelector("#tabs");
const countContainer = document.querySelector("#count");

async function readItems(event) {
    const files = Array.from(event.target.files);

    if (files.length === 0) {
        return;
    }

    try {
        const itemsFile = files.find((file) => file.name === "items.json");
        const locationsFile = files.find((file) => file.name === "locations.json");
        const unitsFile = files.find((file) => file.name === "units.json");

        if (!itemsFile || !locationsFile || !unitsFile) {
            throw new Error("The selected folder must contain items.json, locations.json, and units.json.");
        }

        items = await readData({ target: { files: [itemsFile] } });
        locations = await readData({ target: { files: [locationsFile] } });
        units = await readData({ target: { files: [unitsFile] } });

        const invalidLocations = locations.filter((location) => !isValidLocationObject(location));
        const invalidUnits = units.filter((unit) => !isValidUnitObject(unit));
        const invalidItems = items.filter((item) => !isValidItemObject(item, units, locations));

        if (invalidLocations.length > 0) {
            alert(
                `${invalidLocations.length} invalid location${invalidLocations.length === 1 ? " was" : "s were"} found. Please verify that you loaded the correct file and correct any invalid locations before continuing.`,
            );
            return;
        }

        if (invalidUnits.length > 0) {
            alert(
                `${invalidUnits.length} invalid unit${invalidUnits.length === 1 ? " was" : "s were"} found. Please verify that you loaded the correct file and correct any invalid units before continuing.`,
            );
            return;
        }

        if (invalidItems.length > 0) {
            alert(
                `${invalidItems.length} invlid item${invalidItems.length === 1 ? " was" : "s were"} found. Please verify that you loaded the correct file and correct any invalid itms before continuing.`,
            );
            return;
        }

        if (locations.length === 0) {
            alert("No locations were found in the configuration file.");
            return;
        }

        activeTab = locations[0].id;
        counts = buildCounts();

        renderCountSheet();

        initial.remove();
        content.style.display = "flex";
    } catch (error) {
        alert(`An error occurred loading the configuration files.\n${error}`);
        console.error("Error loading configuration:", error);
    }
}

function buildCounts() {
    return locations.map((location) => {
        const locationCounts = [];

        items.forEach((item) => {
            // Include the base unit
            if (item.baseLocation === location.id) {
                locationCounts.push({
                    item: item.id,
                    unit: item.baseUnit,
                    conversion: 1,
                    count: null,
                });
            }

            // Include additional units
            item.additionalUnits.forEach((unit) => {
                if (unit.location === location.id) {
                    locationCounts.push({
                        item: item.id,
                        unit: unit.unit,
                        conversion: unit.conversion,
                        count: null,
                    });
                }
            });
        });

        return {
            location: location.id,
            counts: locationCounts,
        };
    });
}

function renderCountSheet() {
    tabs.replaceChildren();
    countContainer.replaceChildren();

    // Create the location tabs
    locations.forEach((location) => {
        const tab = document.createElement("div");

        tab.id = `tab${location.id}`;
        tab.textContent = location.name;
        tab.className = "tab";

        if (location.id === activeTab) {
            tab.classList.add("active");
        } else {
            tab.classList.add("inactive");
        }

        tab.addEventListener("click", () => {
            activeTab = location.id;
            renderCountSheet();
        });

        tabs.append(tab);
    });

    // Get the saved count data for the selected location
    const locationData = counts.find((entry) => entry.location === activeTab);

    if (!locationData) return;

    // Display each item and its units
    const table = document.createElement("table");
    countContainer.append(table);

    items.forEach((item) => {
        const itemCounts = locationData.counts.filter((entry) => entry.item === item.id);

        // Skip items that aren't in this location
        if (itemCounts.length === 0) return;

        const row = document.createElement("tr");
        const col = document.createElement("td");
        const heading = document.createElement("h4");

        heading.textContent = item.name;
        col.append(heading);
        row.append(col);

        let i = 0;
        itemCounts.forEach((entry) => {
            const unit = units.find((unit) => unit.id === entry.unit);

            const col1 = document.createElement("td");
            const col2 = document.createElement("td");

            const label = document.createElement("label");
            const input = document.createElement("input");

            label.textContent = unit.name;
            label.htmlFor = `i${++i}`;

            input.id = `i${++i}`;
            input.type = "number";
            input.min = "0";
            input.step = "any";
            input.value = entry.count ?? "";

            input.addEventListener("input", () => {
                entry.count = input.value === "" ? null : Number(input.value);
            });

            col1.append(label);
            col2.append(input);
            row.append(col1, col2);
        });

        table.append(row);
    });
}

function saveCount() {
    const json = JSON.stringify(counts, null, 2);

    const blob = new Blob([json], {
        type: "application/json",
    });

    const url = URL.createObjectURL(blob);

    const link = document.createElement("a");
    link.href = url;
    link.download = `count_${new Date().toLocaleDateString("en-CA")}`;

    document.body.appendChild(link);
    link.click();
    link.remove();

    URL.revokeObjectURL(url);
}

function viewCount() {
    localStorage.setItem("count", JSON.stringify(counts));
    localStorage.setItem("items", JSON.stringify(items));
    localStorage.setItem("units", JSON.stringify(units));
    window.open("view-count.html", "_blank");
}

document.querySelector("#items-file").addEventListener("change", readItems);
saveButton.addEventListener("click", saveCount);
viewCountButton.addEventListener("click", viewCount);
