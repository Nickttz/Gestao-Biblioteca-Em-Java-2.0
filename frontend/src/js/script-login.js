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
    fetch('http://localhost:8081/usuarios/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: jsonDados
    })
    .then(res => {
      if (res.ok) {
        return res.json();
      } else {
        throw new Error("Login inválido");
      }
    })
    .then(data => {
      console.log("Token:", data.token);
      window.location.href = "pages/menu.html";
    })
    .catch(error => {
      console.error("Erro:", error);
      alert("Erro ao logar: " + error.message);
    });
}

function clear() {
    document.getElementById("floatingInput").value = '';
    document.getElementById("floatingPassword").value = ''; 
}
