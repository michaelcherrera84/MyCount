let count = [];
let items = [];
let units = [];
let totals = new Map();

const main = document.querySelector("#main");
const content = document.querySelector("#content");

function loadCount() {
    try {
        const storedCount = localStorage.getItem("count");
        const storedItems = localStorage.getItem("items");
        const storedUnits = localStorage.getItem("units");

        count = JSON.parse(storedCount);
        items = JSON.parse(storedItems);
        units = JSON.parse(storedUnits);

        localStorage.removeItem("count");
        localStorage.removeItem("items");
        localStorage.removeItem("units");
    } catch (error) {
        console.log(error);
        alert("An error occured loading the count data. Please check you counts and try again.");
        window.close();
    }

    count.forEach((location) => {
        location.counts.forEach((item) => {
            if (totals.has(item.item)) {
                totals.set(
                    item.item,
                    Math.round((totals.get(item.item) + (1.0 / item.conversion) * item.count) * 100) / 100,
                );
            } else {
                totals.set(item.item, Math.round((1 / item.conversion) * item.count * 100) / 100);
            }
        });
    });

    renderCount();
}

function renderCount() {
    const countTable = document.createElement("table");

    totals.forEach((count, itemId) => {
        const item = items.find((i) => i.id === itemId);
        const baseUnit = units.find((unit) => unit.id === item.baseUnit);

        const countRow = document.createElement("tr");
        const itemName = document.createElement("td");
        const itemCount = document.createElement("td");
        const unitName = document.createElement("td");

        itemName.textContent = item.name;
        itemCount.textContent = count;
        unitName.textContent = baseUnit.name;

        countRow.append(itemName, itemCount, unitName);
        countTable.append(countRow);
    });

    content.append(countTable);
}
