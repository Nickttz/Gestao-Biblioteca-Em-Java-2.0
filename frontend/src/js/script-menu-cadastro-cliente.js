// Máscara CPF
const inputCPF = document.getElementById('cliente-cpf');
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
const inputTelefone = document.getElementById('cliente-telefone');
inputTelefone.addEventListener('input', function () {
    let tel = inputTelefone.value.replace(/\D/g, '');

    if (tel.length <= 10) {
        tel = tel.replace(/^(\d{2})(\d{4})(\d{0,4})$/, '($1) $2-$3');
    } else {
        tel = tel.replace(/^(\d{2})(\d{5})(\d{0,4})$/, '($1) $2-$3');
    }

    inputTelefone.value = tel;
});

function criarJSONCadastroCliente() {
    const nome = document.getElementById("cliente-nome").value;
    const sobrenome = document.getElementById("cliente-sobrenome").value;
    const dataNascimento = document.getElementById("cliente-data-nascimento").value;
    const cpf_format = document.getElementById("cliente-cpf").value;
    const cpf = cpf_format.replace(/\D/g, '');
    const endereco = document.getElementById("cliente-endereco").value;
    const telefone_format = document.getElementById("cliente-telefone").value;
    const telefone = telefone_format.replace(/\D/g, '');

    if (!nome || !sobrenome || !dataNascimento || !cpf || !endereco || !telefone) {
        alert("Preencha todos os campos.");
        return;
    }

    const dados = {
        nome: nome,
        sobrenome: sobrenome,
        dataNascimento: dataNascimento,
        cpf: cpf,
        endereco: endereco,
        telefone: telefone
    };

    const jsonDados = JSON.stringify(dados);

    enviarJSONCliente(jsonDados);
    clearCamposCliente();
}

async function enviarJSONCliente (jsonDados) {
    try {
      const token = localStorage.getItem("token");
      const response = await fetch('http://localhost:8081/usuarios/clientes/cadastrar_cliente', {
        method: 'POST',
        headers: {
          'Authorization': 'Bearer ' + token,
          'Content-Type': 'application/json'
        },
        body: jsonDados,
      });
  
      if (response.ok) {
            alert("Cadastro realizado com sucesso!");
      } else {
            alert("Erro ao cadastrar. Verifique os dados e tente novamente.");
            console.warn("Resposta com erro:", response.status);
      }
    } catch (err) {
        console.error('Erro ao enviar os dados:', err);
        alert("Erro de conexão com o servidor.");
    }
  }

function clearCamposCliente() {
  document.getElementById("cliente-nome").value = '';
  document.getElementById("cliente-sobrenome").value = '';
  document.getElementById("cliente-data-nascimento").value = '';
  document.getElementById("cliente-cpf").value = '';
  document.getElementById("cliente-endereco").value = '';
  document.getElementById("cliente-telefone").value = '';
}
