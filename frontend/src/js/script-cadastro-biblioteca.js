document.addEventListener('DOMContentLoaded', function () {
    verificarBiblioteca();

    const btnSalvar = document.getElementById('salvarBiblioteca');
    if (btnSalvar) {
        btnSalvar.addEventListener('click', aoClicarCadastrarBiblioteca);
    }
});

let modalExibido = false;

function verificarBiblioteca() {
    const token = localStorage.getItem("token");

    if (!token) {
        alert("Token não encontrado. Faça login novamente.");
        return;
    }

    fetch(`http://localhost:8081/usuarios/perfil/biblioteca`, {
        headers: {
            "Authorization": `Bearer ` + token
        }
    })
    .then(res => {
        if (!res.ok) throw new Error("Erro na resposta do servidor.");
        return res.json();
    })
    .then(gestor => {
        document.getElementById("header-nome-usuario").textContent = gestor.nome;
        document.getElementById("header-sobrenome-usuario").textContent = gestor.sobrenome;

        if (!gestor.biblioteca && !modalExibido) {
            modalExibido = true;
            const modalElement = document.getElementById('modalCadastroBiblioteca');
            const modalInstance = new bootstrap.Modal(modalElement);

            modalElement.addEventListener('shown.bs.modal', () => {
                setTimeout(() => {
                    const input = document.getElementById("nome-biblioteca");
                    if (input) input.focus();
                }, 10); 
            });

            modalInstance.show();
        } else {
            document.getElementById("header-nome-biblioteca").textContent =
                gestor.biblioteca?.nomeBiblioteca || "Biblioteca não configurada";
        }
    })
    .catch(err => {
        console.error("Erro ao buscar perfil do gestor:", err);
        alert("Erro ao carregar perfil do usuário. Faça login novamente.");
    });
}

function aoClicarCadastrarBiblioteca() {
    const dados = montarJSONBiblioteca();
    if (dados) {
        enviarJSONBiblioteca(dados);
    }
}

function montarJSONBiblioteca() {
    const nomeBiblioteca = document.getElementById("nome-biblioteca")?.value.trim();
    const maxLivros = document.getElementById("max-emprestimos")?.value.trim();
    const diasTolerancia = document.getElementById("dias-tolerancia")?.value.trim();

    if (!nomeBiblioteca || !maxLivros || !diasTolerancia) {
        alert("Preencha todos os campos da biblioteca.");
        return null;
    }

    return {
        nomeBiblioteca: nomeBiblioteca,
        maxEmprestimos: parseInt(maxLivros),
        diasTolerancia: parseInt(diasTolerancia)
    };
}

async function enviarJSONBiblioteca(dados) {
    const token = localStorage.getItem("token");

    try {
        const response = await fetch(`http://localhost:8081/usuarios/perfil/biblioteca`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ` + token
            },
            body: JSON.stringify(dados)
        });

        if (response.ok) {
            alert("Biblioteca cadastrada com sucesso!");
            document.getElementById("header-nome-biblioteca").textContent = dados.nomeBiblioteca;

            const modal = bootstrap.Modal.getInstance(document.getElementById('modalCadastroBiblioteca'));
            if (modal) {
                modal.hide();
            }
        } else {
            const erro = await response.text();
            alert("Erro ao cadastrar biblioteca: " + erro);
        }
    } catch (error) {
        console.error("Erro ao enviar os dados:", error);
        alert("Erro de conexão com o servidor.");
    }
}
