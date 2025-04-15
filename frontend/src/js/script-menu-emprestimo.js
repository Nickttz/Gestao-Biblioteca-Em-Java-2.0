function criarJSONemprestimo() {
    const idcliente = document.getElementById("cliente-id-emprestimo").value;
    const idlivro = document.getElementById("livro-id-emprestimo").value;
    const data_devolucao = document.getElementById("devolucao-data").value;

    if (!idcliente || !idlivro || !data_devolucao) {
        alert("Preencha todos os campos.");
        return;
    }

    const dados = {
        idcliente: idcliente,
        idlivro: idlivro,
        data_devolucao: data_devolucao
    };

    const jsonDados = JSON.stringify(dados);

    enviarJSONemprestimo(jsonDados);
    clearCamposEmprestimo();
}

async function enviarJSONemprestimo (jsonDados) {
    try {
      const token = localStorage.getItem("token");
      const response = await fetch('http://localhost:8081/usuarios/emprestimos/realizar_emprestimo', {
        method: 'POST',
        headers: {
          'Authorization': 'Bearer ' + token,
          'Content-Type': 'application/json'
        },
        body: jsonDados,
      });
  
      if (response.ok) {
            alert("Empréstimo realizado com sucesso!");
      } else {
            alert("Erro ao realizar empréstimo. Verifique os dados e tente novamente.");
            console.warn("Resposta com erro:", response.status);
      }
    } catch (err) {
        console.error('Erro ao enviar os dados:', err);
        alert("Erro de conexão com o servidor.");
    }
  }

function clearCamposEmprestimo() {
    document.getElementById("livro-id-emprestimo").value = '';
    document.getElementById("cliente-id-emprestimo").value = '';
    document.getElementById("devolucao-data").value = '';
}