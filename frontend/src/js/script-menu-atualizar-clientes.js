function atualizarCliente() {
  const id = document.getElementById("inputIdCliente").value;
  const nome = document.getElementById("inputNomeCliente").value;
  const sobrenome = document.getElementById("inputSobrenomeCliente").value;
  const dataNascimento = document.getElementById("inputDataNascimentoCliente").value;
  const cpf_format = document.getElementById("inputCpfCliente").value;
  const cpf = cpf_format.replace(/\D/g, '');
  const endereco = document.getElementById("inputEnderecoCliente").value;
  const telefone_format = document.getElementById("inputTelefoneCliente").value;
  const telefone = telefone_format.replace(/\D/g, '');
  
  const dadosAtualizados = {
    id: id,
    nome: nome,
    sobrenome: sobrenome,
    dataNascimento: dataNascimento,
    cpf: cpf,
    endereco: endereco,
    telefone: telefone
  };

  const jsonDados = JSON.stringify(dadosAtualizados);

  enviarJSONAtualizacaoCliente(jsonDados);
  clearFormAtualizarCliente();
}

async function enviarJSONAtualizacaoCliente(jsonDados) {
  try {
    const token = localStorage.getItem("token");
    const response = await fetch('http://localhost:8081/usuarios/clientes/atualizar_dados', {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + token
      },
      body: jsonDados
    });

    if (response.ok) {
      alert("Cliente atualizado com sucesso!");
      console.log("Status:", response.status);
    } else {
      alert("Erro ao atualizar cliente.");
      console.log("Status do erro:", response.status);
    }
  } catch (error) {
    console.error("Erro ao conectar com o servidor:", error);
    alert("Erro de conexão com o servidor.");
  }
}

function clearFormAtualizarCliente() {
  document.getElementById("inputIdCliente").value = '';
  document.getElementById("inputNomeCliente").value = '';
  document.getElementById("inputSobrenomeCliente").value = '';
  document.getElementById("inputDataNascimentoCliente").value = '';
  document.getElementById("inputCpfCliente").value = '';
  document.getElementById("inputEnderecoCliente").value = '';
  document.getElementById("inputTelefoneCliente").value = '';
}
