function atualizarLivro() {
    const id = document.getElementById("inputIdLivro").value;
    const tituloDoLivro = document.getElementById("inputTituloLivro").value;
    const categoria = document.getElementById("inputCategoriaLivro").value;
    const quantidade = document.getElementById("inputQuantidadeLivro").value;
    const emprestados = document.getElementById("inputEmprestadosLivro").value;
  
    const dadosAtualizados = {
      id: id,
      tituloDoLivro: tituloDoLivro,
      categoria: categoria,
      quantidade: quantidade,
      emprestados: emprestados
    };
  
    const jsonDados = JSON.stringify(dadosAtualizados);
  
    enviarJSONAtualizacaoLivro(jsonDados);
    clearFormAtualizarLivro();
  }
  
  async function enviarJSONAtualizacaoLivro(jsonDados) {
    try {
      const token = localStorage.getItem("token");
      const response = await fetch('http://localhost:8081/usuarios/livros/atualizar_livro', {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ` + token
        },
        body: jsonDados
      });
  
      if (response.ok) {
        alert("Livro atualizado com sucesso!");
        console.log("Status:", response.status);
      } else {
        alert("Erro ao atualizar livro.");
        console.log("Status do erro:", response.status);
      }
    } catch (error) {
      console.error("Erro ao conectar com o servidor:", error);
      alert("Erro de conexão com o servidor.");
    }
  }
  
  function clearFormAtualizarLivro() {
    document.getElementById("inputIdLivro").value = '';
    document.getElementById("inputTituloLivro").value = '';
    document.getElementById("inputCategoriaLivro").value = '';
    document.getElementById("inputQuantidadeLivro").value = '';
    document.getElementById("inputEmprestadosLivro").value = '';
  }
  