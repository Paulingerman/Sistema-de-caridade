async function cadastrarUsuario() {
    const nome = document.getElementById("nome").value;
    const telefone = document.getElementById("telefone").value;
    const email = document.getElementById("email").value;
    const endereco = document.getElementById("endereco").value;
    const senha = document.getElementById("senha").value;
    const prioridade = document.getElementById("prioridade").value;
    const perfil = document.getElementById("perfil").value;
    const resultado = document.getElementById("resultado");

    const usuario = {
        nome,
        telefone,
        email,
        endereco,
        senha,
        prioridade,
        perfil
    };

    try {
        const response = await fetch("/usuarios", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(usuario)
        });

        const data = await response.json();

        resultado.style.display = "block";

        if (response.ok) {
            resultado.className = "resultado sucesso";
            resultado.textContent =
                "Usuário cadastrado com sucesso!\n\n" +
                JSON.stringify(data, null, 2);

            limparFormulario();
        } else {
            resultado.className = "resultado erro";
            resultado.textContent =
                "Erro ao cadastrar usuário:\n\n" +
                JSON.stringify(data, null, 2);
        }
    } catch (error) {
        resultado.style.display = "block";
        resultado.className = "resultado erro";
        resultado.textContent = "Erro na requisição: " + error.message;
    }
}

function limparFormulario() {
    document.getElementById("nome").value = "";
    document.getElementById("telefone").value = "";
    document.getElementById("email").value = "";
    document.getElementById("endereco").value = "";
    document.getElementById("senha").value = "";
    document.getElementById("prioridade").value = "BAIXA";
    document.getElementById("perfil").value = "ADMIN";
}