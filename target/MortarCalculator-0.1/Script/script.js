window.onload = function () {
    updateMortarOptions();
    fetchMortarData();

    // Bind the event listener directly to the form's submit event
    document.getElementById('mortarDataForm').addEventListener('submit', handleFormSubmit);

    // Add event listener to the mortarType select element
    document.getElementById('mortarType').addEventListener('change', updateAmmoOptions);
    document.getElementById('firingRange').addEventListener('change', validateFiringRange);
};

const mortarNames = {
    'M224A1': '60mm M224A1',
    'M252A1': '81mm M252A1',
    'M120': '120mm Mortar System'
};

const ammunitionNames = {
    // 60mm M224A1
    'M720A2': 'HE (M720A2)',
    'M1061': 'HE (M1061)',
    'M722A1': 'White phosphorus smoke (M722A1)',
    'M721': 'Visible light illumination (M721)',
    'M767': 'Infrared illumination (M767)',
    'M768A1': 'Training HE (M768A1)',
    'M769': 'Full-range practice (M769)',

    // 81mm M252A1
    'M821A3': 'HE (M821A3)',
    'M819': 'Red phosphorus smoke (M819)',
    'M853A1': 'Visible light illumination (M853A1)',
    'M816': 'Infrared illumination (M816)',
    'M889A4': 'Training HE (M889A4)',
    'M879A1': 'Full-range practice (M879A1)',

    // 120mm Mortar System
    'M934A1': 'HE (M934A1)',
    'M929': 'White phosphorus smoke (M929)',
    'M930': 'Visible light illumination (M930)',
    'M983': 'Infrared illumination (M983)',
    'M933A1': 'Training (M933A1)',
    'M931': 'Full-range practice (M931)'
};

function addMortarOptions() {
    var mortarTypeSelect = document.getElementById('mortarType');

    Object.keys(mortarNames).forEach(function (identifier) {
        var mortarName = mortarNames[identifier];
        var option = document.createElement('option');
        option.value = identifier;
        option.text = mortarName;
        mortarTypeSelect.add(option);
    });
}

function updateMortarOptions() {
    var mortarTypeSelect = document.getElementById('mortarType');

    // Clear existing options
    mortarTypeSelect.innerHTML = "";

    // Add default option
    var defaultOption = document.createElement('option');
    defaultOption.value = "";
    defaultOption.text = "Select a Mortar type:";
    mortarTypeSelect.add(defaultOption);

    // Add mortar options
    addMortarOptions(['60mm M224A1', '81mm M252A1', '120mm Mortar System']);
}

function addAmmoOptions(ammoArray) {
    var ammoTypeSelect = document.getElementById('ammoType');

    ammoArray.forEach(function (ammo) {
        var values = ammo.split(" (");
        var identifier = values[1].replace(")", "");
        var ammoName = ammunitionNames[identifier];
        var option = document.createElement('option');
        option.value = identifier;
        option.text = ammoName; // Use the ammo name as the option text
        ammoTypeSelect.add(option);
    });
}

function updateAmmoOptions() {
    var mortarType = document.getElementById('mortarType').value;
    var ammoTypeSelect = document.getElementById('ammoType');

    // Clear existing options
    ammoTypeSelect.innerHTML = "";

    // Add default option
    var defaultOption = document.createElement('option');
    defaultOption.value = "";
    defaultOption.text = "Select an Ammunition type:";
    ammoTypeSelect.add(defaultOption);

    // Add options based on the selected mortarType
    switch (mortarType) {
        case 'M224A1':
            addAmmoOptions(['HE (M720A2)', 'HE (M1061)', 'White phosphorus smoke (M722A1)', 'Visible light illumination (M721)', 'Infrared illumination (M767)', 'Training HE (M768A1)', 'Full-range practice (M769)']);
            break;
        case 'M252A1':
            addAmmoOptions(['HE (M821A3)', 'Red phosphorus smoke (M819)', 'Visible light illumination (M853A1)', 'Infrared illumination (M816)', 'Training HE (M889A4)', 'Full-range practice (M879A1)']);
            break;
        case 'M120':
            addAmmoOptions(['HE (M934A1)', 'White phosphorus smoke (M929)', 'Visible light illumination (M930)', 'Infrared illumination (M983)', 'Training (M933A1)', 'Full-range practice (M931)']);
            break;
        default:
            break;
    }
}

function getMinRange(mortarType) {
    switch (mortarType) {
        case 'M224A1':
            return 70;
        case 'M252A1':
            return 83;
        case 'M120':
            return 200;
        default:
            return 0;
    }
}

function getMaxRange(mortarType) {
    switch (mortarType) {
        case 'M224A1':
            return 3490;
        case 'M252A1':
            return 5844;
        case 'M120':
            return 7200;
        default:
            return 0;
    }
}

function getMinElevation(mortarType) {
    switch (mortarType) {
        case 'M224A1':
            return 800;
        case 'M252A1':
            return 800;
        case 'M120':
            return 710;
        default:
            return 0;
    }
}

function getMaxElevation(mortarType) {
    switch (mortarType) {
        case 'M224A1':
            return 1511;
        case 'M252A1':
            return 1515;
        case 'M120':
            return 1510;
        default:
            return 0;
    }
}

function isValidMortarType(mortarType) {
    // Add any additional validation logic for mortar types
    if (mortarType === '') {
        // Show a validation message for the "Select a Mortar type" option
        document.getElementById('mortarType').setCustomValidity('Please select a valid mortar type.');
        return false;
    } else {
        // Reset the validation message
        document.getElementById('mortarType').setCustomValidity('');
        return true;
    }
}

function isValidAmmoType(ammoType) {
    return ammoType !== '';
}

function validateFiringRange() {
    var firingRangeInput = document.getElementById('firingRange');
    var selectedMortarType = document.getElementById('mortarType').value;
    var isValid = isValidFiringRange(firingRangeInput.value, selectedMortarType);

    if (!isValid) {
        alert('Invalid firing range for the selected mortar type. Please enter a valid range.');
        firingRangeInput.value = '';
        return false;
    }
    return true;
}

function isValidFiringRange(firingRange, selectedMortarType) {
    var minRange = getMinRange(selectedMortarType);
    var maxRange = getMaxRange(selectedMortarType);
    var firingRangeNumber = Number(firingRange);

    return firingRangeNumber >= minRange && firingRangeNumber <= maxRange;
}

function calculateElevation(mortarType, firingRange) {
    let minRange = getMinRange(mortarType);
    let maxRange = getMaxRange(mortarType);
    let minElevation = getMinElevation(mortarType);
    let maxElevation = getMaxElevation(mortarType);

    if (firingRange < minRange || firingRange > maxRange) {
        return "Invalid firing range";
    }

    // Linear interpolation between the min and max ranges and elevations
    let elevation = minElevation + (maxElevation - minElevation) * ((firingRange - minRange) / (maxRange - minRange));
    return elevation.toFixed(2); // Round the result to 2 decimal places
}

function handleFormSubmit(event) {
    var selectedMortarType = document.getElementById('mortarType').value;
    var selectedAmmoType = document.getElementById('ammoType').value;
    var firingRange = document.getElementById('firingRange').value;
    event.preventDefault(); // Prevent the form from submitting traditionally

    // Validate input
    if (!isValidMortarType(selectedMortarType) || !isValidAmmoType(selectedAmmoType) || !validateFiringRange()) {
        displayErrorMessage("Invalid input. Please check your selections and firing range.");
        return; // Prevent form submission
    }

    // Send a request to the server to calculate elevation
    fetch('/MortarCalculator/Calculator', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: 'action=calculate&mortarType=' + selectedMortarType + '&ammunitionType=' + selectedAmmoType + '&firingRange=' + firingRange
    })
            .then(response => response.text())
            .then(elevation => {
                // Update the result table with received elevation
                updateResultTable(selectedMortarType, selectedAmmoType, firingRange, elevation);
            })
            .catch(error => {
                console.error('Error:', error);
                displayErrorMessage("Error in calculating elevation. Please try again.");
            });
}

function updateResultTable(mortarType, ammoType, range, elevation) {
    // Use the mappings to get the full names
    var mortarDisplayName = mortarNames[mortarType] || mortarType;
    var ammoDisplayName = ammunitionNames[ammoType] || ammoType;

    // Update the result table
    document.getElementById('selectedMortar').textContent = mortarDisplayName;
    document.getElementById('selectedAmmo').textContent = ammoDisplayName;
    document.getElementById('selectedRange').textContent = range;
    document.getElementById('result').textContent = elevation;

    // display the result table if it's initially hidden
    var resultTable = document.getElementById('resultTable');
    resultTable.style.display = 'table';
}

function displayErrorMessage(message) {
    // Update the DOM to display the error message
    document.getElementById('error-message').textContent = message;
    // Optionally, make the error message visible
    document.getElementById('error-message').style.display = 'block';
}

function fetchMortarData() {
    fetch('/MortarCalculator/Calculator?action=fetchData')
            .then(response => response.text())
            .then(data => {
                let headerHtml = '<tr>' +
                        '<th>Select</th>' +
                        '<th>Mortar Type</th>' +
                        '<th>Ammunition Type</th>' +
                        '<th>Min Elevation</th>' +
                        '<th>Max Elevation</th>' +
                        '<th>Min Range</th>' +
                        '<th>Max Range</th>' +
                        '<th>Firing Range</th>' +
                        '<th>Elevation (mils)</th>' +
                        '</tr>';

                let rows = data.split('</tr>').filter(row => row.trim().length > 0);

                let updatedRows = rows.map(row => {
                    let columnValues = row.match(/<td>(.*?)<\/td>/gi);
                    if (columnValues) {
                        let rowData = columnValues.map(td => td.replace(/<\/?td>/gi, ''));
                        let radioValue = rowData.join('_');
                        let radioButtonCell = `<td><input type="radio" name="selectRow" value="${radioValue}"></td>`;
                        let dataCells = columnValues.join('');
                        return `<tr>${radioButtonCell}${dataCells}</tr>`;
                    }
                    return row; // In case there is a row that doesn't match, return it unchanged
                });

                document.getElementById('DataTable').innerHTML = headerHtml + updatedRows.join('');
            })
            .catch(error => {
                console.error('Error:', error);
            });
}

function populateTable(data) {
    let dataTable = document.getElementById('DataTable');
    data.forEach(rowData => {
        let row = dataTable.insertRow();
        Object.values(rowData).forEach(text => {
            let cell = row.insertCell();
            cell.textContent = text;
        });
    });
}

function handleSaveData() {
    var selectedMortarType = document.getElementById('mortarType').value;
    var selectedAmmoType = document.getElementById('ammoType').value;
    var minElevation = getMinElevation(selectedMortarType);
    var maxElevation = getMaxElevation(selectedMortarType);
    var minRange = getMinRange(selectedMortarType);
    var maxRange = getMaxRange(selectedMortarType);
    var firingRange = document.getElementById('firingRange').value;
    var elevationMils = document.getElementById('result').textContent;

    // Add validation here to ensure none of the values are null or empty
    if (!selectedMortarType || !selectedAmmoType || !minElevation || !maxElevation || !minRange || !maxRange || !firingRange || !elevationMils) {
        alert("Please fill out all fields before saving.");
        return;
    }

    var data = {
        action: 'saveData',
        mortarType: selectedMortarType,
        ammunitionType: selectedAmmoType,
        minElevation: minElevation,
        maxElevation: maxElevation,
        minRange: minRange,
        maxRange: maxRange,
        firingRange: firingRange,
        elevationMils: elevationMils
    };

    postData('/MortarCalculator/Calculator', data);
}

function handleDeleteData() {
    var selectedRow = document.querySelector('input[name="selectRow"]:checked');
    if (!selectedRow) {
        alert("Please select a row to delete.");
        return;
    }

    var [selectedMortarType, selectedAmmoType, minElevation, maxElevation, minRange, maxRange, firingRange, elevationMils] = selectedRow.value.split("_");
    // Add validation here to ensure none of the values are null or empty
    if (!selectedMortarType || !selectedAmmoType || !minElevation || !maxElevation || !minRange || !maxRange || !firingRange || !elevationMils) {
        alert("Please fill out all fields before Deleteing.");
        return;
    }

    var data = {
        action: 'deleteData',
        mortarType: selectedMortarType,
        ammunitionType: selectedAmmoType,
        minElevation: minElevation,
        maxElevation: maxElevation,
        minRange: minRange,
        maxRange: maxRange,
        firingRange: firingRange,
        elevationMils: elevationMils
    };

    // Send a request to the server to delete the selected data
    postData('/MortarCalculator/Calculator', data);
}

function postData(url, data) {
    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: Object.keys(data).map(key => encodeURIComponent(key) + '=' + encodeURIComponent(data[key])).join('&')
    })
            .then(response => response.text())
            .then(result => {
                alert(result); // Show success or error message
                fetchMortarData(); // Refresh the data table
            })
            .catch(error => {
                console.error('Error:', error);
                alert("Error in processing request.");
            });
}
