let items = [];
let locations = [];
let units = [];

const sidebar = document.querySelector("#sidebar");
const saveButton = document.querySelector("#save-items");
const addItemForm = document.querySelector("#add-item-form");
const initial = document.querySelector("#init");
const main = document.querySelector("#main");
const content = document.querySelector("#content");
const status = document.querySelector("#status");

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
        }

        renderItems();

        initial.remove();
        content.style.display = "flex";
    } catch (error) {
        alert(`An error occurred loading the configuration files.\n${error}`);
        console.error("Error loading configuration:", error);
    }
}

// Display items
function renderItems() {
    addItemForm.replaceChildren();
    sidebar.replaceChildren();

    items = items.toSorted((a, b) => a.name.localeCompare(b.name));

    const addItemLink = document.createElement("a");
    addItemLink.href = "#add-item-container";
    addItemLink.innerText = "Add Item";
    sidebar.append(addItemLink);

    items.forEach((item) => {
        const itemContainer = document.createElement("div");
        const inputContainer = document.createElement("div");
        const header = document.createElement("div");
        const itemTitle = document.createElement("h4");
        const deleteButton = document.createElement("button");
        const nameInput = buildInput(`n${item.id}`, "Item Name", item, "name");
        const baseUnitSelect = createDropdownInput(`bu${item.id}`, "Base Unit", units, item, "baseUnit");
        const baseLocationSelect = createDropdownInput(
            `bl${item.id}`,
            "Base Location",
            locations,
            item,
            "baseLocation",
        );
        const addUnitsButton = document.createElement("button");
        const itemLink = document.createElement("a");
        sidebar.append(itemLink);

        itemContainer.id = `item${item.id}`;
        itemContainer.classList.add("item");
        inputContainer.classList.add("input");
        header.classList.add("header");

        itemTitle.textContent = item.name;

        itemLink.href = `#item${item.id}`;
        itemLink.textContent = item.name;

        deleteButton.textContent = "Remove Item";
        deleteButton.className = "button";
        deleteButton.type = "button";
        deleteButton.addEventListener("click", () => {
            const dialog = document.querySelector("#item-dialog");
            dialog.showModal();

            dialog.onclose = (event) => {
                if (dialog.returnValue === "remove") {
                    items = items.filter((i) => i.id !== item.id);
                    renderItems();
                    createNotification(`"${item.name}" removed. Don't forget to save when you are finished.`);
                }
            };

            dialog.oncancel = () => {};
        });

        header.append(itemTitle, deleteButton);

        addUnitsButton.textContent = "Add Units";
        addUnitsButton.className = "button";
        addUnitsButton.type = "button";
        addUnitsButton.addEventListener("click", () => {
            addUnitsButton.disabled = true;
            addUnitsButton.classList.add("disabled-button");

            const addUnitContainer = document.createElement("div");
            const conversionInput = buildInput("unit-conversion", "Unit Conversion");
            const unitSelect = createDropdownInput("additional-unit", "Additional Unit", units);
            const unitLocationSelect = createDropdownInput("additional-unit-location", "Unit Location", locations);
            const saveUnitButton = document.createElement("button");
            const cancelUnitButton = document.createElement("button");

            saveUnitButton.innerHTML = "&#10003;";
            saveUnitButton.type = "button";
            saveUnitButton.classList = "button unit-save";
            saveUnitButton.addEventListener("click", () => {
                const conversionInput = document.querySelector("#unit-conversion");

                const conversion = parseFloat(conversionInput.value.trim());
                if (Number.isNaN(conversion)) {
                    alert("The conversion value must be a number.");
                    return;
                }
                const unit = parseInt(document.querySelector("#additional-unit").value);
                const location = parseInt(document.querySelector("#additional-unit-location").value);

                if (!conversion || !unit || !location) {
                    return;
                }

                item.additionalUnits.push({
                    conversion: conversion,
                    unit: unit,
                    location: location,
                });

                renderItems();

                const unitName = units.find((u) => u.id === unit).name;
                const locationName = locations.find((l) => l.id === location).name;
                createNotification(
                    `"${unitName} in ${locationName}" added. Don't forget to save when you are finished.`,
                );
            });

            cancelUnitButton.innerHTML = "&#10006;";
            cancelUnitButton.type = "button";
            cancelUnitButton.classList = "button unit-cancel";
            cancelUnitButton.addEventListener("click", () => {
                renderItems();
            });

            addUnitContainer.classList.add("input");

            addUnitContainer.append(
                ...conversionInput,
                ...unitSelect,
                ...unitLocationSelect,
                saveUnitButton,
                cancelUnitButton,
            );
            itemContainer.append(addUnitContainer);
        });

        inputContainer.append(...nameInput, ...baseUnitSelect, ...baseLocationSelect, addUnitsButton);
        itemContainer.append(header, inputContainer);

        if (item.additionalUnits.length > 0) {
            item.additionalUnits.forEach((additionalUnit, index) => {
                const addUnitContainer = document.createElement("div");
                const conversionInput = buildInput(
                    `au${item.id}c${index}`,
                    "Unit Conversion",
                    additionalUnit,
                    "conversion",
                );
                const unitSelect = createDropdownInput(
                    `au${item.id}u${index}`,
                    "Additional Unit",
                    units,
                    additionalUnit,
                    "unit",
                );
                const unitLocationSelect = createDropdownInput(
                    `au${item.id}ul${index}`,
                    "Unit Location",
                    locations,
                    additionalUnit,
                    "location",
                );
                const deleteButton = document.createElement("button");

                addUnitContainer.classList.add("input");

                deleteButton.textContent = "Remove Unit";
                deleteButton.className = "button";
                deleteButton.type = "button";
                deleteButton.addEventListener("click", () => {
                    const dialog = document.querySelector("#unit-dialog");
                    dialog.showModal();

                    dialog.onclose = (event) => {
                        if (dialog.returnValue === "remove") {
                            item.additionalUnits = item.additionalUnits.filter((unit) => unit !== additionalUnit);
                            renderItems();

                            const unit = units.find((u) => u.id === additionalUnit.unit);
                            const location = locations.find((l) => l.id === additionalUnit.location);

                            createNotification(
                                `"${unit.name} in ${location.name}" was removed from "${item.name}". Don't forget to save when you are finished.`,
                            );
                        }
                    };
                });

                addUnitContainer.append(...conversionInput, ...unitSelect, ...unitLocationSelect, deleteButton);
                itemContainer.append(addUnitContainer);
            });
        }

        addItemForm.append(itemContainer);
    });

    const unitContainer = document.createElement("div");
    const inputContainer = document.createElement("div");
    const nameInput = buildInput("item-name", "Item Name");
    const baseUnitSelect = createDropdownInput("base-unit", "Base Unit", units);
    const baseLocationSelect = createDropdownInput("base-location", "Base Location", locations);
    const addButton = document.createElement("button");

    unitContainer.classList.add("unit");
    unitContainer.id = "add-item-container";
    inputContainer.classList.add("input");

    addButton.textContent = "Add Item";
    addButton.className = "button";
    addButton.type = "submit";

    inputContainer.append(...nameInput, ...baseUnitSelect, ...baseLocationSelect, addButton);
    unitContainer.append(inputContainer);
    addItemForm.append(unitContainer);
}

function createDropdownInput(id, labelText, options, obj, key) {
    const label = document.createElement("label");
    const input = document.createElement("select");

    label.for = id;
    label.innerText = labelText;
    label.className = "hidden-label";

    input.id = id;

    if (!obj) {
        const placeHoder = document.createElement("option");
        placeHoder.value = "";
        placeHoder.innerText = labelText;
        input.append(placeHoder);
    }

    input.required = true;

    options.forEach((o) => {
        const option = document.createElement("option");
        option.value = o.id;
        option.innerText = o.name;
        input.append(option);
    });

    if (obj) {
        input.value = obj[key];
        input.addEventListener("change", (event) => {
            obj[key] = parseInt(event.target.value);
        });
    }

    return [label, input];
}

function addItem(event) {
    event.preventDefault();

    const itemNameInput = document.querySelector("#item-name");

    const name = itemNameInput.value.trim();
    const baseUnit = parseInt(document.querySelector("#base-unit").value);
    const baseLocation = parseInt(document.querySelector("#base-location").value);

    if (!name || !baseUnit || !baseLocation) {
        return;
    }

    const nextId = items.length > 0 ? Math.max(...items.map((item) => item.id)) + 1 : 1;

    items.push({
        id: nextId,
        name: name,
        baseUnit: baseUnit,
        baseLocation: baseLocation,
        additionalUnits: [],
    });

    renderItems();

    createNotification(`"${name}" added. Don't forget to save when you are finished.`);
}

function saveItems() {
    const json = JSON.stringify(items, null, 2);

    const blob = new Blob([json], {
        type: "application/json",
    });

    const url = URL.createObjectURL(blob);

    const link = document.createElement("a");
    link.href = url;
    link.download = "items.json";

    document.body.appendChild(link);
    link.click();
    link.remove();

    URL.revokeObjectURL(url);
}

// Event listeners
document.querySelector("#items-file").addEventListener("change", readItems);
addItemForm.addEventListener("submit", addItem);
saveButton.addEventListener("click", saveItems);
