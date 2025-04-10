async function carregarClientes() {
    try {
        const token = localStorage.getItem("token");
        const response = await fetch("http://localhost:8081/usuarios/lista_cliente", {
            method: "GET",
            headers: {
                "Authorization": "Bearer " + token
            }
        });

        if (!response.ok) {
            throw new Error("Erro ao carregar clientes.");
        }

        const clientes = await response.json();
        console.log("Clientes recebidos:", clientes);
        const tbody = document.getElementById("tabela_cliente");
        tbody.innerHTML = "";
        clientes.forEach((cliente, index) => {
            const tr = document.createElement("tr");

            tr.innerHTML = `
                <th scope="row">${index + 1}</th>
                <td>${cliente.nome}</td>
                <td>${cliente.sobrenome}</td>
                <td>${formatarData(cliente.dataNascimento)}</td>
                <td>${cliente.cpf}</td>
                <td>${cliente.endereco}</td>
            `;

            tbody.appendChild(tr);
        });

    } catch (err) {
        console.error("Erro ao buscar clientes:", err);
        alert("Erro ao carregar lista de clientes.");
    }
}

function formatarData(dataISO) {
    if (dataISO) {
        const [ano, mes, dia] = dataISO.split("-");
        return `${dia}/${mes}/${ano}`;
    } 
    return "";
}

document.addEventListener("DOMContentLoaded", function () {
    const input = document.getElementById("input-pesquisa-cliente");
  
    input.addEventListener("input", function () {
      const termo = input.value.toLowerCase();
      const linhas = document.querySelectorAll("#tabela_cliente tr");
  
      linhas.forEach((linha) => {
        const texto = linha.textContent.toLowerCase();
        const visivel = texto.includes(termo);
        linha.style.display = visivel ? "" : "none";
      });
    });
  });
  