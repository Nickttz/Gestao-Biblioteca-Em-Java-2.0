// Máscara CPF
const inputCPF = document.getElementById('cpfInput');
inputCPF.addEventListener('input', function () {
    let cpf = inputCPF.value.replace(/\D/g, '');

    if (cpf.length <= 3) {
        cpf = cpf.replace(/^(\d{1,3})$/, '$1');
    } else if (cpf.length <= 6) {
        cpf = cpf.replace(/^(\d{3})(\d{1,3})$/, '$1.$2');
    } else if (cpf.length <= 9) {
        cpf = cpf.replace(/^(\d{3})(\d{3})(\d{1,3})$/, '$1.$2.$3');
    } else {
        cpf = cpf.replace(/^(\d{3})(\d{3})(\d{3})(\d{1,2})$/, '$1.$2.$3-$4');
    }
    inputCPF.value = cpf;
});

// Máscara telefone
const inputTelefone = document.getElementById('telefoneInput');
inputTelefone.addEventListener('input', function () {
    let tel = inputTelefone.value.replace(/\D/g, '');

    if (tel.length <= 10) {
        tel = tel.replace(/^(\d{2})(\d{4})(\d{0,4})$/, '($1) $2-$3');
    } else {
        tel = tel.replace(/^(\d{2})(\d{5})(\d{0,4})$/, '($1) $2-$3');
    }

    inputTelefone.value = tel;
});

document.addEventListener('DOMContentLoaded', function () {
    document.getElementById('create').addEventListener('click', criarJSONCadastro);
});

function criarJSONCadastro() {
    const email = document.getElementById("emailInput").value;
    const senha = document.getElementById("senhaInput").value;
    const confirmarSenha = document.getElementById("confirmarSenhaInput").value;
    const nome = document.getElementById("nomeInput").value;
    const sobrenome = document.getElementById("sobrenomeInput").value;
    const cpf = document.getElementById("cpfInput").value;
    const telefone = document.getElementById("telefoneInput").value;

    if (!email || !senha || !nome || !sobrenome || !cpf || !telefone || !confirmarSenha) {
        alert("Preencha todos os campos.");
        return;
    }

    if (senha !== confirmarSenha) {
        alert("As senhas não coincidem. Por favor, verifique.");
        return;
    }

    const dados = {
        email: email,
        senha: senha,
        nome: nome,
        sobrenome: sobrenome,
        cpf: cpf,
        telefone: telefone,
    }

    const jsonDados = JSON.stringify(dados);

    enviarJSON(jsonDados);
    clear();
}

async function enviarJSON (jsonDados) {
    try {
      const response = await fetch('http://localhost:8081/cadastro', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: jsonDados,
      });
  
      if (response.ok) {
            alert("Cadastro realizado com sucesso!");
            console.log('Resposta do servidor:', await response.json());
            window.location.href = "pages/index.html";
      } else {
            alert("Erro ao cadastrar. Verifique os dados e tente novamente.");
            console.warn("Resposta com erro:", response.status);
      }
    } catch (err) {
        console.error('Erro ao enviar os dados:', err);
        alert("Erro de conexão com o servidor.");
    }
  }

function clear() {
    document.getElementById("emailInput").value = '';
    document.getElementById("senhaInput").value = '';
    document.getElementById("confirmarSenhaInput").value = '';
    document.getElementById("nomeInput").value = '';
    document.getElementById("sobrenomeInput").value = '';
    document.getElementById("cpfInput").value = '';
    document.getElementById("telefoneInput").value = '';
}