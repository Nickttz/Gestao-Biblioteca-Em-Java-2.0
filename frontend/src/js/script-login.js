document.addEventListener('DOMContentLoaded', function () {
  document.getElementById('submit').addEventListener('click', criarJSON);
});

function criarJSON() {
    const email = document.getElementById("floatingInput").value;
    const senha = document.getElementById("floatingPassword").value;

    const dados = {
      email: email,
      senha: senha
    };

    const jsonDados = JSON.stringify(dados);

    enviarJSON(jsonDados);
    clear();
}

function enviarJSON(jsonDados) {
    fetch('http://localhost:8081', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: jsonDados
    })
    .then(function(res) {
      console.log('Resposta do servidor:', res); 
    })
    .catch(function(err) {
      console.log('Erro ao enviar os dados:', err); 
    });
}

function clear() {
    document.getElementById("floatingInput").value = '';
    document.getElementById("floatingPassword").value = ''; 
}
