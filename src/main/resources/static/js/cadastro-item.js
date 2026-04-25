async function cadastrarItem() {
    const nomeItem = document.getElementById("nomeItem").value.trim();
    const categoria = document.getElementById("categoria").value;
    const descricao = document.getElementById("descricao").value.trim();
    const quantidade = Number(document.getElementById("quantidade").value);
    const estadoConservacao = document.getElementById("estadoConservacao").value;

    const payload = {
        nomeItem,
        categoria,
        descricao,
        quantidade,
        estadoConservacao
    };

    try {
        const response = await fetch("/itens", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(payload)
        });

        const data = await response.json();

        if (response.ok) {
            mostrarMensagemItem(
                "Item cadastrado com sucesso!\n\n" + JSON.stringify(data, null, 2),
                "success"
            );
            limparFormularioItem();
            return;
        }

        mostrarMensagemItem(
            "Erro ao cadastrar item:\n\n" + JSON.stringify(data, null, 2),
            "error"
        );
    } catch (error) {
        mostrarMensagemItem("Erro na requisição: " + error.message, "error");
    }
}

function limparFormularioItem() {
    document.getElementById("nomeItem").value = "";
    document.getElementById("categoria").value = "ROUPA";
    document.getElementById("descricao").value = "";
    document.getElementById("quantidade").value = "";
    document.getElementById("estadoConservacao").value = "NOVO";
}

function mostrarMensagemItem(texto, tipo) {
    const box = document.getElementById("mensagem-item");
    box.className = `message-box ${tipo}`;
    box.textContent = texto;
}