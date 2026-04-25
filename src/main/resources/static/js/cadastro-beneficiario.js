async function cadastrarBeneficiario() {
    const nome = document.getElementById("nome").value;
    const telefone = document.getElementById("telefone").value;
    const email = document.getElementById("email").value;
    const endereco = document.getElementById("endereco").value;
    const senha = document.getElementById("senha").value;
    const tipoBeneficiario = document.getElementById("tipoBeneficiario").value;
    const nivelPrioridade = document.getElementById("nivelPrioridade").value;
    const resultado = document.getElementById("resultado");

    const beneficiario = {
        nome,
        telefone,
        email,
        endereco,
        senha,
        tipoBeneficiario,
        nivelPrioridade
    };

    try {
        const response = await fetch("/beneficiarios", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(beneficiario)
        });

        const data = await response.json();

        resultado.style.display = "block";

        if (response.ok) {
            resultado.className = "resultado sucesso";
            resultado.textContent =
                "Beneficiário cadastrado com sucesso!\n\n" +
                JSON.stringify(data, null, 2);

            limparFormularioBeneficiario();
        } else {
            resultado.className = "resultado erro";
            resultado.textContent =
                "Erro ao cadastrar beneficiário:\n\n" +
                JSON.stringify(data, null, 2);
        }
    } catch (error) {
        resultado.style.display = "block";
        resultado.className = "resultado erro";
        resultado.textContent = "Erro na requisição: " + error.message;
    }
}

function limparFormularioBeneficiario() {
    document.getElementById("nome").value = "";
    document.getElementById("telefone").value = "";
    document.getElementById("email").value = "";
    document.getElementById("endereco").value = "";
    document.getElementById("senha").value = "";
    document.getElementById("tipoBeneficiario").value = "FAMILIA";
    document.getElementById("nivelPrioridade").value = "BAIXA";
}