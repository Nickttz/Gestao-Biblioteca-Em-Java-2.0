function criarJSONdevolucao() {
    const idcliente = document.getElementById("cliente-id-devolucao").value;
    const idlivro = document.getElementById("livro-id-devolucao").value;

    if (!idcliente || !idlivro) {
        alert("Preencha todos os campos.");
        return;
    }

    const dados = {
        idcliente: idcliente,
        idlivro: idlivro,
    };

    const jsonDados = JSON.stringify(dados);

    enviarJSONdevolucao(jsonDados);
    clearCamposdevolucao();
}

async function enviarJSONdevolucao (jsonDados) {
    try {
      const token = localStorage.getItem("token");
      console.log(token);
      const response = await fetch('http://localhost:8081/usuarios/emprestimos/realizar_devolucao', {
        method: 'POST',
        headers: {
          'Authorization': 'Bearer ' + token,
          'Content-Type': 'application/json'
        },
        body: jsonDados,
      });
  
      if (response.ok) {
            alert("Devolução realizada com sucesso!");
      } else {
            alert("Erro ao realizar devolução. Verifique os dados e tente novamente.");
            console.warn("Resposta com erro:", response.status);
      }
    } catch (err) {
        console.error('Erro ao enviar os dados:', err);
        alert("Erro de conexão com o servidor.");
    }
  }

function clearCamposdevolucao() {
    document.getElementById("livro-id-devolucao").value = '';
    document.getElementById("cliente-id-devolucao").value = '';
}