function criarJSONCadastroLivro() {
    const tituloDoLivro = document.getElementById("livro-titulo").value;
    const categoria = document.getElementById("livro-categoria").value;
    const quantidade = document.getElementById("livro-quantidade").value;

    if (!tituloDoLivro || !categoria || !quantidade) {
        alert("Preencha todos os campos.");
        return;
    }

    const dados = {
        tituloDoLivro: tituloDoLivro,
        categoria: categoria,
        quantidade: quantidade
    };

    const jsonDados = JSON.stringify(dados);

    enviarJSONLivro(jsonDados);
    clearCamposLivro();
}

async function enviarJSONLivro (jsonDados) {
    try {
      const token = localStorage.getItem("token");
      const response = await fetch('http://localhost:8081/usuarios/livros/cadastrar_livro', {
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

function clearCamposLivro() {
    document.getElementById("livro-titulo").value = '';
    document.getElementById("livro-categoria").value = '';
    document.getElementById("livro-quantidade").value = '';
}
