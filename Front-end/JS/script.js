let produtosOrcamento = [];
let servicosOrcamento = [];
let servicoTerceirosOrcamento = [];


let editingRow = null;

SK = 17.5
C = 12.5
L = 10

const copyVehicleData = async () => {
    let veiculo = document.getElementById('inpVeiculo').value;
    let ano = document.getElementById('inpAno').value;
    let pecas = '';
    for (let i = 0; i < produtosOrcamento.length; i++) {
        const e = produtosOrcamento[i];
        pecas = `${pecas}-${e.costName}\n`;
    }
    let dadosVeiculo = `Veículo: ${veiculo} - Ano: ${ano} \nPeças:\n${pecas}`;

    await navigator.clipboard.writeText(dadosVeiculo);
}
//Parts Functions
const calculateParts = () => {
    let quantity = parseFloat(document.getElementById("inpQtdeProduto").value);
    let purchaseValue = parseFloat(document.getElementById("inpValCompraProduto").value);
    let margin = parseFloat(document.getElementById("inpMargProduto").value);
    let supplier = document.getElementById("inpFornecedorProduto").value;
    let unitDiscount = parseFloat(document.getElementById("inpDescontoProduto").value);
    let supplierIncreaseValue

    document.getElementById("inpQtdeProduto").style.borderColor = 'black'
    document.getElementById("inpValCompraProduto").style.borderColor = 'black'
    document.getElementById("inpMargProduto").style.borderColor = 'black'
    document.getElementById("inpFornecedorProduto").style.borderColor = 'black'
    document.getElementById("inpDescontoProduto").style.borderColor = 'black'

    if (supplier == "SK") {
        supplierIncreaseValue = SK
    } else if (supplier == "C") {
        supplierIncreaseValue = C
    } else {
        supplierIncreaseValue = L
    }

    if (unitDiscount == "") {
        unitDiscount = 0
    }

    let calculatedValue = purchaseValue + supplierIncreaseValue;

    calculatedValue = calculatedValue + (calculatedValue * margin / 100);
    let discountValue = calculatedValue * unitDiscount / 100;

    const unitSellValue = calculatedValue - discountValue;

    const total = quantity * (calculatedValue - discountValue);

    document.getElementById("inpValorUnidadeProduto").value = unitSellValue.toFixed(2);
    document.getElementById("inpTotalProduto").value = total.toFixed(2); 
}

// let quantity = parseFloat(document.getElementById("inpQtdeProduto").value);
// let purchaseValueParts = parseFloat(document.getElementById("inpValCompraProduto").value);
// let marginParts = parseFloat(document.getElementById("inpMargProduto").value);
// let supplierParts = document.getElementById("inpFornecedorProduto").value;
// let unitDiscountParts = parseFloat(document.getElementById("inpDescontoProduto").value);

document.getElementById("inpQtdeProduto").addEventListener('input', calculateParts);
document.getElementById("inpValCompraProduto").addEventListener('input', calculateParts);
document.getElementById("inpMargProduto").addEventListener('input', calculateParts);
document.getElementById("inpFornecedorProduto").addEventListener('input', calculateParts);
document.getElementById("inpDescontoProduto").addEventListener('input', calculateParts);

let uniqueIds = [];

const addPartsToTable = () => {
    const quantity = document.getElementById("inpQtdeProduto").value;
    const productName = document.getElementById("inpNomeProduto").value;
    const purchaseValue = document.getElementById("inpValCompraProduto").value;
    const supplier = document.getElementById("inpFornecedorProduto").value;
    const margin = document.getElementById("inpMargProduto").value;
    const unitDiscount = document.getElementById("inpDescontoProduto").value;
    const unitValue = document.getElementById("inpValorUnidadeProduto").value;
    const totalValue = document.getElementById("inpTotalProduto").value;

    if (quantity == "") {
        document.getElementById('inpQtdeProduto').style.borderColor = 'red'
        document.getElementById('hRejeicaoPeca').textContent = 'Quantidade não pode ser vazia!'
        return
        // } else if (productName == "") {
        //     document.getElementById('inpNomeProduto').style.borderColor = 'red'
        //     document.getElementById('hRejeicaoPeca').textContent = 'Nome do produto não pode ser vazio!'
        //     return
    } else if (purchaseValue == "") {
        document.getElementById('inpValCompraProduto').style.borderColor = 'red'
        document.getElementById('hRejeicaoPeca').textContent = 'Valor de Compra não pode zer vazio!'
        return
    } else if (supplier == "") {
        document.getElementById('inpFornecedorProduto').style.borderColor = 'red'
        document.getElementById('hRejeicaoPeca').textContent = 'Fornecedor não pode ser vazio!'
        return
    } else {
        try {
            const uniqueId = new Date().getTime();
            document.getElementById('hRejeicaoPeca').textContent = '';

            const newRow = document.createElement("tr");
            uniqueIds.push(uniqueId);
            newRow.innerHTML = `
                <td id="quantity-${uniqueId}">${quantity}</td>
                <td id="productName-${uniqueId}">${productName}</td>
                <td id="purchaseValue-${uniqueId}">${purchaseValue}</td>
                <td id="supplier-${uniqueId}">${supplier}</td>
                <td id="margin-${uniqueId}">${margin}</td>
                <td id="unitDiscount-${uniqueId}">${unitDiscount}</td>
                <td id="unitValue-${uniqueId}">${unitValue}</td>
                <td id="totalValue-${uniqueId}">${totalValue}</td>
                <td>
                    <button class="edit-button" onclick="editProduct(this, ${uniqueId})">✎</button>
                    <button class="delete-button" onclick="deleteProduct(this, ${uniqueId})">✖</button>
                </td>
            `;
            document.getElementById("tabela-adicionados-pecas").querySelector("tbody").appendChild(newRow);
            calculateBudget();
        } catch (error) {
            console.log(error);
        }
    }
}

const editProduct = (button, uniqueId) => {
    if (editingRow !== null) return;

    editingRow = button.parentElement.parentElement;
    const cells = editingRow.querySelectorAll("td");

    for (let i = 0; i < cells.length - 1; i++) {
        const value = cells[i].innerText;
        cells[i].innerHTML = `<input type="text" id="edit-${uniqueId}-${i}" value="${value}" />`;
    }
    const actionsCell = cells[cells.length - 1];
    actionsCell.innerHTML = `
        <button class="accept-button" onclick="acceptEdit(this, ${uniqueId})">✔</button>
        <button class="cancel-button" onclick="cancelEdit(this, ${uniqueId})">✖</button>
    `;

    document.getElementById(`quantity-${uniqueId}`).addEventListener('input', () => calculateEditedParts(uniqueId));
    document.getElementById(`purchaseValue-${uniqueId}`).addEventListener('input', () => calculateEditedParts(uniqueId));
    document.getElementById(`supplier-${uniqueId}`).addEventListener('input', () => calculateEditedParts(uniqueId));
    document.getElementById(`margin-${uniqueId}`).addEventListener('input', () => calculateEditedParts(uniqueId));
    document.getElementById(`unitDiscount-${uniqueId}`).addEventListener('input', () => calculateEditedParts(uniqueId));


}

const deleteProduct = (button, uniqueId) => {
    const row = button.parentElement.parentElement;

    row.remove();

    uniqueIds = uniqueIds.filter(id => id !== uniqueId);
    produtosOrcamento = produtosOrcamento.filter(id => id !== uniqueId);
}

const calculateEditedParts = (uniqueId) => { 
    let quantity = parseFloat(document.getElementById(`quantity-${uniqueId}`).value);
    let purchaseValue = parseFloat(document.getElementById(`purchaseValue-${uniqueId}`).value);
    let margin = parseFloat(document.getElementById(`margin-${uniqueId}`).value);
    let supplier = document.getElementById(`supplier-${uniqueId}`).value;
    let unitDiscount = parseFloat(document.getElementById(`edit-${uniqueId}-5`).value);
    let supplierIncreaseValue;

    if (supplier == "SK") {
        supplierIncreaseValue = SK;
    } else if (supplier == "C") {
        supplierIncreaseValue = C;
    } else {
        supplierIncreaseValue = L;
    }

    if (isNaN(unitDiscount)) {
        unitDiscount = 0;
    }
    let calculatedValue = purchaseValue + supplierIncreaseValue;
    calculatedValue = calculatedValue + (calculatedValue * margin / 100);
    let discountValue = calculatedValue * unitDiscount / 100;

    const unitSellValue = calculatedValue - discountValue;
    const total = quantity * (calculatedValue - discountValue);
    document.getElementById(`unitValue-${uniqueId}`).value = unitSellValue.toFixed(2);
    document.getElementById(`totalValue-${uniqueId}`).value = total.toFixed(2);

    
}

const acceptEdit = (button, uniqueId) => {
    const cells = button.parentElement.parentElement.querySelectorAll("td");
    const values = [];
    // Coletar valores dos inputs
    for (let i = 0; i < cells.length - 1; i++) {
        const input = cells[i].querySelector("input");
        values.push(input.value);
    }
    // Atualizar linha com novos valores calculados
    cells[0].innerText = values[0];
    cells[1].innerText = values[1];
    cells[2].innerText = values[2];
    cells[3].innerText = values[3];
    cells[4].innerText = values[4];
    cells[5].innerText = values[5];
    cells[6].innerText = values[6];
    cells[7].innerText = values[7];
    // Atualizar o objeto no array produtosOrcamento
    const produtoIndex = produtosOrcamento.findIndex(p =>
        p.quantidade == editingRow.querySelector(`#quantity-${uniqueId}`).innerText &&
        p.nome == editingRow.querySelector(`#productName-${uniqueId}`).innerText &&
        p.valorCompra == editingRow.querySelector(`#purchaseValue-${uniqueId}`).innerText &&
        p.fornecedor == editingRow.querySelector(`#supplier-${uniqueId}`).innerText &&
        p.margem == editingRow.querySelector(`#margin-${uniqueId}`).innerText &&
        p.desconto == editingRow.querySelector(`#unitDiscount-${uniqueId}`).innerText &&
        p.valorUnidade == editingRow.querySelector(`#unitValue-${uniqueId}`).innerText &&
        p.valorTotal == editingRow.querySelector(`#totalValue-${uniqueId}`).innerText
    );
    if (produtoIndex !== -1) {
        produtosOrcamento[produtoIndex] = {
            quantidade: values[0],
            nome: values[1],
            valorCompra: values[2],
            fornecedor: values[3],
            margem: values[4],
            desconto: values[5],
            valorUnidade: values[6],
            valorTotal: values[7]
        };
    }
    // Restaurar a célula de ações
    const actionsCell = cells[cells.length - 1];
    actionsCell.innerHTML = `
        <button class="edit-button" onclick="editProduct(this, ${uniqueId})">✎</button>
        <button class="delete-button" onclick="deleteProduct(this)">✖</button>
    `;
    editingRow = null;

    calculateBudget
    console.log("Lista de produtos vindo da tabela adicionados " + JSON.stringify(produtosOrcamento , null , 2));
}

const cancelEdit = (button, uniqueId) => {
    const row = button.parentElement.parentElement;
    const cells = row.querySelectorAll("td");

    // Restaurar os valores originais da linha a partir do clone armazenado
    const originalRow = editingRow.cloneNode(true);
    const originalCells = originalRow.querySelectorAll("td");

    for (let i = 0; i < cells.length - 1; i++) {
        cells[i].innerHTML = originalCells[i].innerHTML;
    }

    // Restaurar a célula de ações
    const actionsCell = cells[cells.length - 1];
    actionsCell.innerHTML = `
        <button class="edit-button" onclick="editProduct(this, ${uniqueId})">✎</button>
        <button class="delete-button" onclick="deleteProduct(this)">✖</button>
    `;

    editingRow = null;
}

//Third Party Services Functions

const openThirdPartyService = () => {
    document.getElementById('dServicoTerceiros').showModal();
}

const closeThirdPartyService = () => {
    document.getElementById('dServicoTerceiros').close();
}

const addServiceThirdPartyToTable = () => {
    console.log("Adicionando serviços terceiros na tabela")
    // quantidadeTerceiros"></td>
    //                         <td><input type="text" id="servicoTerceiros" ></td>
    //                         <td><input type="number" id="margemTerceiros"></td>
    //                         <td><input type="number" id="descontoTerceiros"></td>
    //                         <td><input type="number" id="valorTerceiros" ></td>
    //                         <td><input type="number" id="valorFinalTerceiros

    let quantity = document.getElementById("quantidadeTerceiros").value;
    let descThirds = document.getElementById("servicoTerceiros").value;
    let margin = document.getElementById("margemTerceiros").value;
    let discount = document.getElementById("descontoTerceiros").value;
    let value = document.getElementById("valorTerceiros").value;
    let finalValue = document.getElementById("valorFinalTerceiros").value;


    if (descThirds == "") {
        document.getElementById('servicoTerceiros').style.borderColor = 'red'
        document.getElementById('hRejeicaoServicoTerceiros').textContent = 'Serviço não pode ser vazio!'
        return
    } else if (value == "") {
        document.getElementById('valorTerceiros').style.borderColor = 'red'
        document.getElementById('hRejeicaoServicoTerceiros').textContent = 'Valor não pode ser vazio!'
        return
    } else if (quantity == "") {
        document.getElementById('quantidadeTerceiros').style.borderColor = 'red'
        document.getElementById('hRejeicaoServicoTerceiros').textContent = 'Quantidade não pode ser vazio!'
        return
    } else if (margin == "") {
        document.getElementById('margemTerceiros').style.borderColor = 'red'
        document.getElementById('hRejeicaoServicoTerceiros').textContent = 'Margem não pode ser vazio!'
    } else {
        try {
            servicoTerceirosOrcamento.push({
                nome: descThirds,
                quantidade: quantity,
                valorCompra: value,
                fornecedor: 'SERVICE',
                margem: margin,
                desconto: discount,
                valorUnidade: value,
                valorTotal: finalValue
            });
            document.getElementById('hRejeicaoServicoTerceiros').textContent = '';
            const newRow = document.createElement("tr");
            newRow.innerHTML = `
                <td>${quantity}</td>
                <td>${descThirds}</td>
                <td>${margin}</td>
                <td>${discount}</td>
                <td>${value}</td>
                <td>${finalValue}</td>
            `;
            document.getElementById("tabela-adicionados-terceiros").querySelector("tbody").appendChild(newRow);
        } catch (error) {
            console.log(error);
        }
    }
}

const calculateThirdParty = () => {
    let quantity = parseFloat(document.getElementById("quantidadeTerceiros").value);
    let service = parseFloat(document.getElementById("servicoTerceiros").value);
    let margin = parseFloat(document.getElementById("margemTerceiros").value);
    let discount = parseFloat(document.getElementById("descontoTerceiros").value);
    let value = parseFloat(document.getElementById("valorTerceiros").value);

    document.getElementById("quantidadeTerceiros").style.borderColor = 'black'
    document.getElementById("servicoTerceiros").style.borderColor = 'black'
    document.getElementById("margemTerceiros").style.borderColor = 'black'
    document.getElementById("valorTerceiros").style.borderColor = 'black'

    let calculatedValue = value + (value * margin / 100);
    if (discount > 0) {
        calculatedValue = calculatedValue - (calculatedValue * discount / 100)
    }
    calculatedValue = calculatedValue * quantity;
    document.getElementById("valorFinalTerceiros").value = calculatedValue.toFixed(2);

}

document.getElementById("quantidadeTerceiros").addEventListener('input', calculateThirdParty);
document.getElementById("margemTerceiros").addEventListener('input', calculateThirdParty);
document.getElementById("descontoTerceiros").addEventListener('input', calculateThirdParty);
document.getElementById("valorTerceiros").addEventListener('input', calculateThirdParty);

// Service functions
const addServiceToTable = () => {
    const servHours = document.getElementById("inpHorasServ").value;
    const servName = document.getElementById("inpServ").value;
    const servDiscount = document.getElementById("inpDescontoServ").value;
    let servValue = 0;

    let categoryVal = document.getElementById("inpTipoCarro").value;

    if(categoryVal == "economico"){
        servValue = servHours * 120;
    }else if(categoryVal == "intermediario"){
        servValue = servHours * 150;
    }else{
        servValue = servHours * 180;
    }	

    if (servHours == "") {
        document.getElementById('inpHorasServ').style.borderColor = 'red'
        document.getElementById('hRejeicaoServico').textContent = 'Horas não pode ser vazio!'
        return
    } else if (servName == "") {
        document.getElementById('inpServ').style.borderColor = 'red'
        document.getElementById('hRejeicaoServico').textContent = 'Serviço não pode ser vazio!'
        return
    } else {
        try {
            servicosOrcamento.push({
                quantidade: servHours,
                nome: servName,
                desconto: servDiscount,
                valorTotal: servValue
            });

            const newRow = document.createElement("tr");
            const uniqueId = new Date().getTime();
            newRow.innerHTML = `
                <td>${servHours}</td>
                <td>${servName}</td>
                <td>${servDiscount}</td>
                <td>${servValue.toFixed(2)}</td>
                <td>
                    <button class="edit-button" onclick="editService(this, ${uniqueId})">✎</button>
                    <button class="delete-button" onclick="deleteService(this)">✖</button>
                </td>
            `;
            document.getElementById('inpHorasServ').style.borderColor = 'black'
            document.getElementById('inpServ').style.borderColor = 'black'
            document.getElementById('hRejeicaoServico').textContent = '';
            document.getElementById("tabela-adicionados-servicos").querySelector("tbody").appendChild(newRow);
        } catch (error) {
            console.log(error);
        }
    }
}

const editService = (button, uniqueId) => {
    if (editingRow !== null) return;

    editingRow = button.parentElement.parentElement;
    const cells = editingRow.querySelectorAll("td");

    for (let i = 0; i < cells.length - 1; i++) {
        const value = cells[i].innerText;
        cells[i].innerHTML = `<input type="text" id="edit-service-${uniqueId}-${i}" value="${value}" />`;
    }
    const actionsCell = cells[cells.length - 1];
    actionsCell.innerHTML = `
        <button class="accept-button" onclick="acceptServiceEdit(this, ${uniqueId})">✔</button>
        <button class="cancel-button" onclick="cancelServiceEdit(this, ${uniqueId})">✖</button>
    `;

    document.getElementById(`edit-service-${uniqueId}-0`).addEventListener('input', () => calculateEditedService(uniqueId));
    document.getElementById(`edit-service-${uniqueId}-1`).addEventListener('input', () => calculateEditedService(uniqueId));
    document.getElementById(`edit-service-${uniqueId}-2`).addEventListener('input', () => calculateEditedService(uniqueId));
}

const deleteService = (button) => {
    const row = button.parentElement.parentElement;
    row.remove();
}

const calculateEditedService = (uniqueId) => {
    let servHours = parseFloat(document.getElementById(`edit-service-${uniqueId}-0`).value);
    let servName = document.getElementById(`edit-service-${uniqueId}-1`).value;
    let servDiscount = parseFloat(document.getElementById(`edit-service-${uniqueId}-2`).value);
    let servValue;

    let categoryVal = document.getElementById("inpTipoCarro").value;

    if(categoryVal == "economico"){
        servValue = servHours * 120;
    }else if(categoryVal == "intermediario"){
        servValue = servHours * 150;
    }else{
        servValue = servHours * 180;
    }

    if (isNaN(servDiscount)) {
        servDiscount = 0;
    }

    const total = servValue * (1 - servDiscount / 100);

    document.getElementById(`edit-service-${uniqueId}-3`).value = total.toFixed(2);
}

const acceptServiceEdit = (button, uniqueId) => {
    const cells = button.parentElement.parentElement.querySelectorAll("td");
    const values = [];
    // Coletar valores dos inputs
    for (let i = 0; i < cells.length - 1; i++) {
        const input = cells[i].querySelector("input");
        values.push(input.value);
    }
    // Atualizar linha com novos valores calculados
    cells[0].innerText = values[0];
    cells[1].innerText = values[1];
    cells[2].innerText = values[2];
    cells[3].innerText = values[3];

    // Atualizar o objeto no array servicosOrcamento
    const servicoIndex = servicosOrcamento.findIndex(s =>
        s.quantidade == editingRow.querySelector(`#quantity-${uniqueId}`).innerText &&
        s.nome == editingRow.querySelector(`#serviceName-${uniqueId}`).innerText &&
        s.desconto == editingRow.querySelector(`#serviceDiscount-${uniqueId}`).innerText &&
        s.valorTotal == editingRow.querySelector(`#serviceTotalValue-${uniqueId}`).innerText
    );
    if (servicoIndex !== -1) {
        servicosOrcamento[servicoIndex] = {
            quantidade: values[0],
            nome: values[1],
            desconto: values[2],
            valorTotal: values[3]
        };
    }
    // Restaurar a célula de ações
    const actionsCell = cells[cells.length - 1];
    actionsCell.innerHTML = `
        <button class="edit-button" onclick="editService(this, ${uniqueId})">✎</button>
        <button class="delete-button" onclick="deleteService(this)">✖</button>
    `;
    editingRow = null;
}

const cancelServiceEdit = (button, uniqueId) => {
    const row = button.parentElement.parentElement;
    const cells = row.querySelectorAll("td");

    // Restaurar os valores originais da linha a partir do clone armazenado
    const originalRow = editingRow.cloneNode(true);
    const originalCells = originalRow.querySelectorAll("td");

    for (let i = 0; i < cells.length - 1; i++) {
        cells[i].innerHTML = originalCells[i].innerHTML;
    }

    // Restaurar a célula de ações
    const actionsCell = cells[cells.length - 1];
    actionsCell.innerHTML = `
        <button class="edit-button" onclick="editService(this, ${uniqueId})">✎</button>
        <button class="delete-button" onclick="deleteService(this)">✖</button>
    `;

    editingRow = null;
}

const cost = {
    id: null,
    costName: null,
    quantity: null,
    purchaseValue: null,
    supplier: null,
    margin: null,
    percentageDiscount: null,
    realDiscount: null,
    unitValue: null,
    totalValue: null,
    discountedValue: null,
    costType: null
};

const budget = {
    budgetId: null,
    clientName: "",
    vehicle: "",
    phone: "",
    plate: "",
    year: "",
    partsCost: 0.00,
    servicesCost: 0.00,
    thirdPartyCost: 0.00,
    partsDiscount: 0.00,
    servicesDiscount: 0.00,
    thirdPartyDiscount: 0.00,
    totalBudgetCost: 0.00,
    totalDiscountedBudgetCost: 0.00,
    costList: [cost]
};

const putBudget = () => {
    const clientName = document.getElementById('inpCliente').value;
    const phone = document.getElementById('inpTelefone').value;
    const vehicle = document.getElementById('inpVeiculo').value;
    const plate = document.getElementById('inpPlaca').value;
    const year = document.getElementById('inpAno').value;

    let partsDiscount = parseFloat(document.getElementById("descontoPeca").value)

    let servicesDiscount = parseFloat(document.getElementById("descontoServico").value)

    let thirdPartyDiscount = parseFloat(document.getElementById("descontoServicoTerceiro").value)

    const partsRows = document.querySelectorAll('#tabela-adicionados-pecas tbody tr');
    const partsData = extractCostData(partsRows, 'PECA');

    const servicesRows = document.querySelectorAll('#tabela-adicionados-servicos tbody tr');
    const servicesData = extractCostData(servicesRows, 'SERVICO');

    const thirdPartyRows = document.querySelectorAll('#tabela-adicionados-terceiros tbody tr');
    const thirdPartyData = extractCostData(thirdPartyRows, 'SERV_TERCEIROS');

    const costList = partsData.concat(servicesData, thirdPartyData);

    const data = {
        budgetId: budget.budgetId,
        clientName,
        phone,
        vehicle,
        plate,
        year,
        partsCost: 0,
        servicesCost: 0,
        thirdPartyCost: 0,
        discountParts: partsDiscount,
        discountServices: servicesDiscount,
        discountThirdParty: thirdPartyDiscount,
        totalBudgetCost: 0,
        totalDiscountedBudgetCost: 0,
        costList
    };

    return fetch(`http://localhost:8082/api/budget/${budget.budgetId}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(responseData => {
            budget.budgetId = responseData.budgetId;
            budget.clientName = responseData.clientName;
            budget.vehicle = responseData.vehicle;
            budget.phone = responseData.phone;
            budget.plate = responseData.plate;
            budget.year = responseData.year;
            budget.partsCost = responseData.partsCost;
            budget.servicesCost = responseData.servicesCost;
            budget.thirdPartyCost = responseData.thirdPartyCost;
            budget.partsDiscount = responseData.partsDiscount;
            budget.servicesDiscount = responseData.servicesDiscount;
            budget.thirdPartyDiscount = responseData.thirdPartyDiscount;
            budget.totalBudgetCost = responseData.totalBudgetCost;
            budget.totalDiscountedBudgetCost = responseData.totalDiscountedBudgetCost;
            budget.costList = responseData.costList;

            produtosOrcamento = responseData.costList.filter(cost => cost.costType === 'PECA');
            servicosOrcamento = responseData.costList.filter(cost => cost.costType === 'SERVICO');
            servicoTerceirosOrcamento = responseData.costList.filter(cost => cost.costType === 'SERV_TERCEIROS');

            console.log('Produtos:', produtosOrcamento);
            console.log('Serviços:', servicosOrcamento);
            console.log('Serviços Terceiros:', servicoTerceirosOrcamento);

            return responseData;
        })
        .catch(error => {
            console.error('There was a problem with the fetch operation:', error);
        });
};

const postBudget = () => {
    const clientName = document.getElementById('inpCliente').value;
    const phone = document.getElementById('inpTelefone').value;
    const vehicle = document.getElementById('inpVeiculo').value;
    const plate = document.getElementById('inpPlaca').value;
    const year = document.getElementById('inpAno').value;

    let partsDiscount = parseFloat(document.getElementById("descontoPeca").value)

    let servicesDiscount = parseFloat(document.getElementById("descontoServico").value)

    let thirdPartyDiscount = parseFloat(document.getElementById("descontoServicoTerceiro").value)

    const partsRows = document.querySelectorAll('#tabela-adicionados-pecas tbody tr');
    const partsData = extractCostData(partsRows, 'PECA');

    const servicesRows = document.querySelectorAll('#tabela-adicionados-servicos tbody tr');
    const servicesData = extractCostData(servicesRows, 'SERVICO');

    const thirdPartyRows = document.querySelectorAll('#tabela-adicionados-terceiros tbody tr');
    const thirdPartyData = extractCostData(thirdPartyRows, 'SERV_TERCEIROS');

    const costList = partsData.concat(servicesData, thirdPartyData);


    const data = {
        clientName,
        phone,
        vehicle,
        plate,
        year,
        partsCost: 0,
        servicesCost: 0,
        thirdPartyCost: 0,
        discountParts: partsDiscount,
        discountServices: servicesDiscount,
        discountThirdParty: thirdPartyDiscount,
        totalBudgetCost: 0,
        totalDiscountedBudgetCost: 0,
        costList
    };
    return fetch('http://localhost:8082/api/budget', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(responseData => {
            // Atualizar o objeto budget com os dados recebidos
            budget.budgetId = responseData.budgetId;
            budget.clientName = responseData.clientName;
            budget.vehicle = responseData.vehicle;
            budget.phone = responseData.phone;
            budget.plate = responseData.plate;
            budget.year = responseData.year;
            budget.partsCost = responseData.partsCost;
            budget.servicesCost = responseData.servicesCost;
            budget.thirdPartyCost = responseData.thirdPartyCost;
            budget.partsDiscount = responseData.partsDiscount;
            budget.servicesDiscount = responseData.servicesDiscount;
            budget.thirdPartyDiscount = responseData.thirdPartyDiscount;
            budget.totalBudgetCost = responseData.totalBudgetCost;
            budget.totalDiscountedBudgetCost = responseData.totalDiscountedBudgetCost;
            budget.costList = responseData.costList;

            produtosOrcamento = [];
            servicosOrcamento = [];
            servicoTerceirosOrcamento = [];

            responseData.costList.forEach(cost => {
                if (cost.costType === 'PECA') {
                    produtosOrcamento.push({
                        costName: cost.costName,
                        unitValue: cost.unitValue,
                        totalValue: cost.totalValue,
                        quantity: cost.quantity
                    });
                } else if (cost.costType === 'SERVICO') {
                    servicosOrcamento.push({
                        costName: cost.costName,
                        totalValue: cost.totalValue,
                        quantity: cost.quantity
                    });
                } else if (cost.costType === 'SERV_TERCEIROS') {
                    servicoTerceirosOrcamento.push({
                        costName: cost.costName,
                        unitValue: cost.unitValue,
                        totalValue: cost.totalValue,
                        quantity: cost.quantity
                    });
                }});    
                
            console.log('Produtos:', produtosOrcamento);
            console.log('Serviços:', servicosOrcamento);
            console.log('Serviços Terceiros:', servicoTerceirosOrcamento);

            return responseData;
        })
        .catch(error => {
            console.error('There was a problem with the fetch operation:', error);
            throw error;
        });
};

const calculateBudget = () => {
    if (budget.budgetId > 0) {
        putBudget() 
            .then(data => {
                let partsCost = data.partsCost;
                let servicesCost = data.servicesCost;
                let thirdPartyCost = data.thirdPartyCost
                let totalBudgetCost = data.totalBudgetCost;
                let totalDiscountedBudgetCost = data.totalDiscountedBudgetCost;
                let discountValue = totalBudgetCost - totalDiscountedBudgetCost;

                document.getElementById('valor_servicos').textContent = servicesCost;
                document.getElementById('valor_pecas').textContent = partsCost;
                document.getElementById('serv_terceiros').textContent = thirdPartyCost;
                document.getElementById('valor_total').textContent = totalBudgetCost;
                document.getElementById('valor_desconto').textContent = discountValue;
                document.getElementById('total_com_desconto').textContent = totalDiscountedBudgetCost;

                console.log("FUNCAO CALCULATE BUDGET ----------- " + data);
                splitShippingValue();
            })
            .catch(error => {
                console.log(error);
            });
    } else {
       postBudget()
            .then(data => {
                let thirdPartyCost = data.thirdPartyCost
                let partsCost = data.partsCost;
                let servicesCost = data.servicesCost;
                let totalBudgetCost = data.totalBudgetCost;
                let totalDiscountedBudgetCost = data.totalDiscountedBudgetCost;
                let discountValue = totalBudgetCost - totalDiscountedBudgetCost;

                document.getElementById('valor_servicos').textContent = servicesCost;
                document.getElementById('valor_pecas').textContent = partsCost;
                document.getElementById('serv_terceiros').textContent = thirdPartyCost;
                document.getElementById('valor_total').textContent = totalBudgetCost;
                document.getElementById('valor_desconto').textContent = discountValue;
                document.getElementById('total_com_desconto').textContent = totalDiscountedBudgetCost;

                console.log("FUNCAO CALCULATE BUDGET ----------- " + data);
                splitShippingValue();
            })
            .catch(error => {
                console.log(error);
            });
    }
};

function extractCostData(rows, costType) {
    const costDataList = [];
    if (costType === 'SERV_TERCEIROS') {
        rows.forEach(row => {
            const cells = row.querySelectorAll('td');
            const costData = {
                id: null,
                costName: cells[1]?.innerText || '',
                quantity: parseFloat(cells[0]?.innerText || 0),
                purchaseValue: parseFloat(cells[4]?.innerText || 0),
                supplier: 'SERVICE',
                margin: parseFloat(cells[2]?.innerText || 0),
                percentageDiscount: 0,
                realDiscount: 0,
                unitValue: 0,
                totalValue: 0,
                discountedValue: 0,
                costType: 'SERV_TERCEIROS'
            };
            costDataList.push(costData);
        });
    }
    else if (costType === 'SERVICO') {
        rows.forEach(row => {
            const cells = row.querySelectorAll('td');
            const costData = {
                id: null,
                costName: cells[1]?.innerText || '',
                quantity: parseFloat(cells[0]?.innerText || 0),
                purchaseValue: parseFloat(cells[3]?.innerText || 0),
                supplier: 'SERVICE',
                margin: 0,
                percentageDiscount: parseFloat(cells[2]?.innerText || 0),
                realDiscount: 0,
                unitValue: 0,
                totalValue: 0,
                discountedValue: 0,
                costType: 'SERVICO'
            };
            costDataList.push(costData);
        });
    } else {
        rows.forEach(row => {
            const cells = row.querySelectorAll('td');
            const costData = {
                id: null,
                costName: cells[1]?.innerText || '',
                quantity: parseFloat(cells[0]?.innerText || 0),
                purchaseValue: parseFloat(cells[2]?.innerText || 0),
                supplier: cells[3]?.innerText || '',
                margin: parseFloat(cells[4]?.innerText || 0),
                percentageDiscount: parseFloat(cells[5]?.innerText || 0),
                realDiscount: 0,
                unitValue: 0,
                totalValue: 0,
                discountedValue: 0,
                costType: 'PECA'
            };
            costDataList.push(costData);
        });
    }
    return costDataList;
}

const calculateDiscountThirdParty = () => {
    let discount = parseFloat(document.getElementById("descontoServicoTerceiro").value);
    calculateBudget();
    if (isNaN(discount) || discount < 0 || discount > 100) {
        console.error("Desconto inválido. Deve ser um número entre 0 e 100.");
        document.getElementById("serv_terceiros").textContent = budget.thirdPartyCost;
        return;
    }
   calculateBudget();
   
}

const calculateDiscountParts = () => {
    let discount = parseFloat(document.getElementById("descontoPeca").value);
    calculateBudget();
    if (isNaN(discount) || discount < 0 || discount > 100) {
        console.error("Desconto inválido. Deve ser um número entre 0 e 100.");
        document.getElementById("valor_pecas").textContent = budget.partsCost;
        return;
    }
    calculateBudget();
}

const calculateDiscountService = () => {
    let discount = parseFloat(document.getElementById("descontoServico").value);
    calculateBudget();
    if (isNaN(discount) || discount < 0 || discount > 100) {
        console.error("Desconto inválido. Deve ser um número entre 0 e 100.");
        document.getElementById("valor_servicos").textContent = budget.servicesCost;
        return;
    }
    calculateBudget();
}


const splitShippingValue = () => {
    let totalValueCosts = [];

    let unitValueCosts = [];

    budget.costList.forEach((cost , index) => {
        cost.id = `row-${index + 1}`;
        unitValueCosts.push({ unitValue: cost.unitValue, id: cost.id });
    })

    budget.costList.forEach((cost, index) => { // Adicionando 'index' como segundo parâmetro
        cost.id = `row-${index + 1}`;
        totalValueCosts.push({ totalValue: cost.totalValue, id: cost.id });
    });

    uniqueIds.forEach((uniqueId, index) => { // Adicionando 'index' como segundo parâmetro
        const totalValueElement = document.getElementById(`totalValue-${uniqueId}`);
        const unitValueElement = document.getElementById(`unitValue-${uniqueId}`)
        const discountValueElement = parseFloat(document.getElementById(`unitDiscount-${uniqueId}`).value);
        if (!totalValueElement) {
            console.error(`Elemento com ID totalValue-${uniqueId} não encontrado.`);
            return; // Pula para o próximo uniqueId se o elemento não for encontrado
        }
        if (discountValueElement.isNaN) {
            console.error("Sem valor de desconto " + discountValueElement);
        }
        if (index < totalValueCosts.length) {
            const { totalValue } = totalValueCosts[index];
            const { unitValue } = unitValueCosts[index]
            if (discountValueElement > 0) {
                totalValueElement.textContent = totalValue - (totalValue * discountValueElement / 100).toFixed(2);
                unitValueElement.textContent = unitValue - (unitValue * discountValueElement / 100).toFixed(2);
                return
            }
            totalValueElement.textContent = totalValue.toFixed(2);
            unitValueElement.textContent = unitValue.toFixed(2);
            
        }
    });

};

document.getElementById("descontoServicoTerceiro").addEventListener('input', calculateDiscountThirdParty);
document.getElementById("descontoPeca").addEventListener('input', calculateDiscountParts);
document.getElementById("descontoServico").addEventListener('input', calculateDiscountService);


document.getElementById('imprimirPdf').addEventListener('click', function () {
    const { jsPDF } = window.jspdf;
    const doc = new jsPDF();

    let date = new Date();
    date = date.toLocaleDateString('pt-BR');
    let name = document.getElementById('inpCliente').value;
    let plate = document.getElementById('inpPlaca').value;
    let vehicle = document.getElementById('inpVeiculo').value;
    let phone = document.getElementById('inpTelefone').value;
    let year = document.getElementById('inpAno').value;

    let servicesValue = document.getElementById('valor_servicos').textContent;
    let partsValue = document.getElementById('valor_pecas').textContent;
    let thirdPartyValue = document.getElementById('serv_terceiros').textContent;

    let total = parseFloat(document.getElementById('valor_total').textContent);
    let discountValue = document.getElementById('valor_desconto').textContent;
    let totalDiscounted = document.getElementById('total_com_desconto').textContent;

    // Adicionando título e cabeçalho
    doc.setFontSize(18);
    doc.text("Jony Tec OFICINA MECÂNICA LTDA", 10, 10);
    doc.setFontSize(11);
    doc.text("48 3242 4030 / 8421 2005", 10, 20);
    doc.text("jony.tec2009@gmail.com", 10, 25);
    doc.text(`DATA: ${date}`, 150, 10);
    doc.text(`NOME: ${name}`, 10, 35);
    doc.text(`PLACA: ${plate}`, 150, 35);
    doc.text(`VEÍCULO: ${vehicle}`, 10, 40);
    doc.text(`TELEFONE: ${phone}`, 150, 40);
    doc.text(`ANO: ${year}`, 10, 45);

    console.log("Produtos Orçamento " + produtosOrcamento)

    doc.autoTable({
        head: [['Produto', 'Unidade $', 'Total $']],
        body: produtosOrcamento.map(produto => [produto.costName, produto.unitValue, produto.totalValue]),
        startY: 50,
        theme: 'grid',
    });
    
    doc.autoTable({
        head: [['Serviços', 'Valor $']],
        body: servicosOrcamento.map(servico => [servico.costName, servico.totalValue]),
        startY: doc.lastAutoTable.finalY + 10,
        theme: 'grid',
    });
    
    doc.autoTable({
        head: [['Serviços Terceiros', 'Unidade $', 'Total $']],
        body: servicoTerceirosOrcamento.map(servico => [servico.costName, servico.unitValue, servico.totalValue]),
        startY: doc.lastAutoTable.finalY + 10,
        theme: 'grid',
    });

    doc.setFontSize(11);
    doc.text("Total Orçamento ", 10, doc.lastAutoTable.finalY + 20);
    doc.text("Serviço", 10, doc.lastAutoTable.finalY + 30);
    doc.text(`R$ ${servicesValue}`, 50, doc.lastAutoTable.finalY + 30);
    doc.text("Peças", 10, doc.lastAutoTable.finalY + 35);
    doc.text(`R$ ${partsValue}`, 50, doc.lastAutoTable.finalY + 35);
    doc.text("Terceiro", 10, doc.lastAutoTable.finalY + 40);
    doc.text(`R$ ${thirdPartyValue}`, 50, doc.lastAutoTable.finalY + 40);
    doc.setFontSize(12);
    doc.setTextColor(255, 0, 0);
    doc.text("Total", 10, doc.lastAutoTable.finalY + 45);
    doc.text(`R$ ${total}`, 50, doc.lastAutoTable.finalY + 45);

    const condicoesPagamento = [
        ["Valor", "Parcelas", "T Cartão", "Total"],
        [`R$ ${totalDiscounted}`, "À vista", "R$ 0,00", `R$ ${totalDiscounted}`],
        [`R$ ${(total / 2).toFixed(2)}`, "2x", "R$ 0,00", `R$ ${total.toFixed(2)}`],
        [`R$ ${(total / 3).toFixed(2)}`, "3x", "R$ 0,00", `R$ ${total.toFixed(2)}`],
        [`R$ ${(total / 4).toFixed(2)}`, "4x", "R$ 0,00", `R$ ${total.toFixed(2)}`],
        [`R$ ${(total / 5).toFixed(2)}`, "5x", "R$ 0,00", `R$ ${total.toFixed(2)}`],
        [`R$ ${(total / 6).toFixed(2)}`, "6x", "R$ 0,00", `R$ ${total.toFixed(2)}`],
        [`R$ ${(total / 7).toFixed(2)}`, "7x", "R$ 0,00", `R$ ${total.toFixed(2)}`],
        [`R$ ${(total / 8).toFixed(2)}`, "8x", "R$ 0,00", `R$ ${total.toFixed(2)}`],
        [`R$ ${(total / 9).toFixed(2)}`, "9x", "R$ 0,00", `R$ ${total.toFixed(2)}`],
        [`R$ ${(total / 10).toFixed(2)}`, "10x", "R$ 0,00", `R$ ${total.toFixed(2)}`],
        [`R$ ${(total / 11).toFixed(2)}`, "11x", "R$ 0,00", `R$ ${total.toFixed(2)}`],
        [`R$ ${(total / 12).toFixed(2)}`, "12x", "R$ 0,00", `R$ ${total.toFixed(2)}`]
    ];

    doc.autoTable({
        head: [condicoesPagamento[0]],
        body: condicoesPagamento.slice(1),
        startY: doc.lastAutoTable.finalY + 55,
        theme: 'grid',
    });

    doc.save('orcamento.pdf');
});


