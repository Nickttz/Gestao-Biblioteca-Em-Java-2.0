document.addEventListener('DOMContentLoaded', function () {
  document.getElementById('login').addEventListener('click', logar);
});

function logar() {

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

async function enviarJSON(jsonDados) {
  try {  
      const response = await fetch('http://localhost:8081/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: jsonDados
      });
      if (response.ok) {
        const data = await response.json(); // capturar o token
        console.log("Resposta do servidor:", response.status);
        localStorage.setItem("token", data.token); // armazenar o token
        
        window.location.href = "pages/menu.html";
      } else {
        alert("Email e/ou senha inválidos");
        console.log("Resposta do servidor: ", response.status);
      }  
  } catch (err) {
      console.error("Erro ao enviar os dados:", err);
      alert("Erro de conexão com o servidor.");
  }
}

function clear() {
    document.getElementById("floatingInput").value = '';
    document.getElementById("floatingPassword").value = ''; 
}