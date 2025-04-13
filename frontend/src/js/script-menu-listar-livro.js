async function carregarLivros() {
    try {
        const token = localStorage.getItem("token");
        const response = await fetch("http://localhost:8081/usuarios/livros/listar_livro", {
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
        const tbody = document.getElementById("tabela_livro");
        tbody.innerHTML = "";
        clientes.forEach((livro, index) => {
            const tr = document.createElement("tr");

            tr.innerHTML = `
                <th>${index + 1}</th>
                <td>${livro.id}</td>
                <td>${livro.tituloDoLivro}</td>
                <td>${livro.categoria}</td>
                <td>${(livro.quantidade)}</td>
                <td>${(livro.emprestados)}</td>
            `;
            tbody.appendChild(tr);
        });

    } catch (err) {
        console.error("Erro ao buscar livros:", err);
        alert("Erro ao carregar lista de livros.");
    }
}

document.addEventListener("DOMContentLoaded", function () {
    const input = document.getElementById("input-pesquisa-livro");
  
    input.addEventListener("input", function () {
      const termo = input.value.toLowerCase();
      const linhas = document.querySelectorAll("#tabela_livro tr");
  
      linhas.forEach((linha) => {
        const texto = linha.textContent.toLowerCase();
        const visivel = texto.includes(termo);
        linha.style.display = visivel ? "" : "none";
      });
    });
  });
  