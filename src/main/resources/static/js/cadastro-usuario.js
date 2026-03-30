async function cadastrarUsuario() {
    const nome = document.getElementById("nome").value;
    const email = document.getElementById("email").value;
    const senha = document.getElementById("senha").value;
    const prioridade = document.getElementById("prioridade").value;
    const resultado = document.getElementById("resultado");

    const usuario = {
        nome,
        email,
        senha,
        prioridade
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
    document.getElementById("email").value = "";
    document.getElementById("senha").value = "";
    document.getElementById("prioridade").value = "BAIXA";
}