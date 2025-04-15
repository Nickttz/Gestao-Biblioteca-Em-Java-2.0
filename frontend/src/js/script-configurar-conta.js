document.getElementById("v-pills-account-settings-tab").addEventListener("click", () => {
    carregarDadosConta();
});

async function carregarDadosConta() {
    const token = localStorage.getItem("token");

    try {
        const response = await fetch("http://localhost:8081/usuarios/perfil/biblioteca", {
            method: "GET",
            headers: {
                'Authorization': `Bearer ` + token
            }
        });

        if (response.ok) {
            const dados = await response.json();

            document.getElementById("nome-usuario-conta").textContent = `${dados.nome} ${dados.sobrenome}`;
            document.getElementById("email-usuario-conta").textContent = dados.email;
            document.getElementById("cpf-usuario-conta").textContent = dados.cpf;

            document.getElementById("nome-biblioteca-conta").textContent = dados.biblioteca.nomeBiblioteca;
            document.getElementById("max-dias-biblioteca").textContent = dados.biblioteca.diasTolerancia;
            document.getElementById("max-livros-biblioteca").textContent = dados.biblioteca.maxEmprestimos;

            document.getElementById("inputNome").value = dados.nome;
            document.getElementById("inputsobreNome").value = dados.sobrenome;
            document.getElementById("inputEmail").value = dados.email;
            document.getElementById("inputCpf").value = dados.cpf;
            document.getElementById("inputBibliotecaNome").value = dados.biblioteca.nomeBiblioteca;
            document.getElementById("inputMaxDias").value = dados.biblioteca.diasTolerancia;
            document.getElementById("inputMaxLivros").value = dados.biblioteca.maxEmprestimos;

        } else {
            console.error("Erro ao buscar dados do perfil:", response.status);
            alert("Erro ao carregar dados da conta.");
        }
    } catch (error) {
        console.error("Erro ao conectar com o servidor:", error);
        alert("Erro de conexão ao carregar dados da conta.");
    }
}

function atualizarConta() {
    const nome = document.getElementById("inputNome").value;
    const sobrenome = document.getElementById("inputsobreNome").value;
    const email = document.getElementById("inputEmail").value;
    const cpf = document.getElementById("inputCpf").value; 
    const senha = document.getElementById("inputSenha").value;
    const confirmarSenha = document.getElementById("inputConfirmarSenha").value;
    const nomeBiblioteca = document.getElementById("inputBibliotecaNome").value;
    const maxDias = document.getElementById("inputMaxDias").value;
    const maxLivros = document.getElementById("inputMaxLivros").value;

    if(senha !== confirmarSenha) {
        alert("As senhas não coincidem");
        return;
    }

    obterCpfDoToken().then(cpfToken => {
        const dadosAtualizados = { 
            nome: nome,
            sobrenome: sobrenome,
            email: email,
            cpf: cpfToken,
            senha: senha,
            biblioteca: {
                nomeBiblioteca: nomeBiblioteca,
                diasTolerancia: parseInt(maxDias),
                maxEmprestimos: parseInt(maxLivros)
            }
        };

        const jsonDados = JSON.stringify(dadosAtualizados);
        enviarJSONAtualizacaoConta(jsonDados, cpfToken);
        clearFormAtualizarConta();
    }).catch(error => {
        console.error("Erro ao obter CPF do token:", error);
        alert("Erro ao recuperar CPF.");
    });
}

async function obterCpfDoToken() {
    try {
        const token = localStorage.getItem("token");
        const response = await fetch('http://localhost:8081/usuarios/perfil/cpf', {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ` + token
            }
        });

        if (response.ok) {
            const cpf = await response.text();
            return cpf;
        } else {
            console.error("Erro ao recuperar CPF:", response.status);
            throw new Error("Erro ao recuperar CPF");
        }
    } catch (error) {
        console.error("Erro ao conectar com o servidor:", error);
        throw new Error("Erro de conexão com o servidor.");
    }
}

async function enviarJSONAtualizacaoConta(jsonDados, cpf) {
    try {
        const token = localStorage.getItem("token");
        const response = await fetch(`http://localhost:8081/usuarios/${cpf}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ` + token
            },
            body: jsonDados
        });

        if (response.ok) {
            alert("Conta atualizada com sucesso!");
            console.log("Status:", response.status);
        } else {
            alert("Erro ao atualizar conta.");
            console.log("Status do erro:", response.status);
        }
    } catch (error) {
        console.error("Erro ao conectar com o servidor:", error);
        alert("Erro de conexão com o servidor.");
    }
}

function clearFormAtualizarConta() {
    document.getElementById("inputNome").value = '';
    document.getElementById("inputsobreNome").value = '';
    document.getElementById("inputEmail").value = '';
    document.getElementById("inputCpf").value = '';
    document.getElementById("inputSenha").value = '';
    document.getElementById("inputConfirmarSenha").value = '';
    document.getElementById("inputBibliotecaNome").value = '';
    document.getElementById("inputMaxDias").value = '';
    document.getElementById("inputMaxLivros").value = '';
}
