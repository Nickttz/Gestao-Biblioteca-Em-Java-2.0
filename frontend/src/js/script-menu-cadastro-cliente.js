function criarJSONCadastroCliente() {
    const nome = document.getElementById("cliente-nome").value;
    const sobrenome = document.getElementById("cliente-sobrenome").value;
    const dataNascimento = document.getElementById("cliente-data-nascimento").value;
    const cpf = document.getElementById("cliente-cpf").value;
    const endereco = document.getElementById("cliente-endereco").value;
    const telefone = document.getElementById("cliente-telefone").value;

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

    enviarJSON(jsonDados);
    clearCamposCliente();
}

async function enviarJSON (jsonDados) {
    try {
      const token = localStorage.getItem("token");
      console.log(token);
      const response = await fetch('http://localhost:8081/usuarios/cadastro_cliente', {
        method: 'POST',
        headers: {
          'Authorization': 'Bearer ' + token,
          'Content-Type': 'application/json'
        },
        body: jsonDados,
      });
  
      if (response.ok) {
            alert("Cadastro realizado com sucesso!");
            console.log('Resposta do servidor:', await response.json());
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
