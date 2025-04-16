async function deletarEntidade(tipo, inputId) {
    try {
        const id = document.getElementById(inputId).value;

        if (!id) {
            alert(`Preencha o campo do ID para excluir o ${tipo}.`);
            return;
        }

        const token = localStorage.getItem("token");
        const response = await fetch(`http://localhost:8081/usuarios/${tipo}/deletar_${tipo}/${id}`, {
            method: 'DELETE',
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });

        if (response.ok) {
            alert(`${tipo.charAt(0).toUpperCase() + tipo.slice(1)} deletado.`);
        } else {
            alert(`Erro ao deletar ${tipo}. Verifique se há empréstimos pendentes.`);
        }
    } catch (error) {
        console.error("Erro ao conectar com o servidor:", error);
    }

    document.getElementById(inputId).value = '';
}
