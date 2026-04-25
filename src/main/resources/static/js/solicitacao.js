let beneficiariosSolicitacao = [];
let itensDisponiveisSolicitacao = [];
let usuarioLogadoSolicitacao = null;

document.addEventListener("DOMContentLoaded", async () => {
    usuarioLogadoSolicitacao = getUsuarioLogado();

    await carregarBeneficiariosSolicitacao();
    await carregarItensDisponiveisSolicitacao();
    configurarSolicitacaoPorPerfil();
    preencherBeneficiarioPorParametro();
});

async function carregarBeneficiariosSolicitacao() {
    const select = document.getElementById("beneficiario");

    try {
        const response = await fetch("/beneficiarios");
        beneficiariosSolicitacao = await response.json();

        select.innerHTML = `<option value="">Selecione um beneficiário</option>`;

        beneficiariosSolicitacao.forEach(beneficiario => {
            const option = document.createElement("option");
            option.value = beneficiario.id;
            option.textContent = `${beneficiario.nome} - ${beneficiario.tipoBeneficiario} - ${beneficiario.nivelPrioridade}`;
            select.appendChild(option);
        });

        select.addEventListener("change", atualizarInfoBeneficiario);
    } catch (error) {
        mostrarMensagemSolicitacao("Erro ao carregar beneficiários: " + error.message, "error");
    }
}

async function carregarItensDisponiveisSolicitacao() {
    const select = document.getElementById("item");

    try {
        const response = await fetch("/itens/disponiveis");
        itensDisponiveisSolicitacao = await response.json();

        select.innerHTML = `<option value="">Selecione um item</option>`;

        itensDisponiveisSolicitacao.forEach(item => {
            const option = document.createElement("option");
            option.value = item.id;
            option.textContent = `${item.nomeItem} - ${item.categoria} - qtd: ${item.quantidade}`;
            select.appendChild(option);
        });

        select.addEventListener("change", atualizarInfoItem);
    } catch (error) {
        mostrarMensagemSolicitacao("Erro ao carregar itens disponíveis: " + error.message, "error");
    }
}

function configurarSolicitacaoPorPerfil() {
    if (!usuarioLogadoSolicitacao) {
        mostrarMensagemSolicitacao("Faça login para registrar solicitações.", "error");
        return;
    }

    if (usuarioLogadoSolicitacao.perfil === "BENEFICIARIO") {
        const select = document.getElementById("beneficiario");
        select.value = usuarioLogadoSolicitacao.id;
        select.disabled = true;
        atualizarInfoBeneficiario();

        const existeNoCadastro = beneficiariosSolicitacao.some(item => item.id === usuarioLogadoSolicitacao.id);

        if (!existeNoCadastro) {
            mostrarMensagemSolicitacao(
                "Seu usuário beneficiário ainda não foi correlacionado à base de beneficiários. Entre novamente após atualizar o cadastro.",
                "error"
            );
        }
    }
}

function preencherBeneficiarioPorParametro() {
    const params = new URLSearchParams(window.location.search);
    const beneficiarioId = params.get("beneficiarioId");

    if (usuarioLogadoSolicitacao && usuarioLogadoSolicitacao.perfil === "BENEFICIARIO") {
        return;
    }

    if (beneficiarioId) {
        document.getElementById("beneficiario").value = beneficiarioId;
        atualizarInfoBeneficiario();
    }
}

function atualizarInfoBeneficiario() {
    const beneficiarioId = document.getElementById("beneficiario").value;
    const info = document.getElementById("beneficiario-info");

    const beneficiario = beneficiariosSolicitacao.find(b => b.id === beneficiarioId);

    if (!beneficiario) {
        info.classList.add("hidden");
        info.innerHTML = "";
        return;
    }

    info.classList.remove("hidden");
    info.innerHTML = `
        <strong>${beneficiario.nome}</strong><br>
        Tipo: ${beneficiario.tipoBeneficiario}<br>
        Prioridade: <span class="badge prioridade-${beneficiario.nivelPrioridade}">${beneficiario.nivelPrioridade}</span><br>
        Email: ${beneficiario.email}<br>
        Endereço: ${beneficiario.endereco}
    `;
}

function atualizarInfoItem() {
    const itemId = document.getElementById("item").value;
    const info = document.getElementById("item-info");

    const item = itensDisponiveisSolicitacao.find(i => i.id === itemId);

    if (!item) {
        info.classList.add("hidden");
        info.innerHTML = "";
        return;
    }

    info.classList.remove("hidden");
    info.innerHTML = `
        <strong>${item.nomeItem}</strong><br>
        Categoria: ${item.categoria}<br>
        Quantidade disponível: ${item.quantidade}<br>
        Estado: ${item.estadoConservacao}<br>
        Status: <span class="badge badge-soft">${item.status}</span><br>
        Descrição: ${item.descricao}
    `;
}

async function criarSolicitacao() {
    const beneficiarioId = document.getElementById("beneficiario").value;
    const itemId = document.getElementById("item").value;
    const quantidadeSolicitada = Number(document.getElementById("quantidadeSolicitada").value);
    const justificativa = document.getElementById("justificativa").value.trim();

    const payload = {
        beneficiarioId,
        itemId,
        quantidadeSolicitada,
        justificativa
    };

    try {
        const response = await fetch("/solicitacoes", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(payload)
        });

        const data = await response.json();

        if (response.ok) {
            mostrarMensagemSolicitacao(
                "Solicitação registrada com sucesso!\n\n" + JSON.stringify(data, null, 2),
                "success"
            );
            limparFormularioSolicitacao();
            await carregarItensDisponiveisSolicitacao();
            return;
        }

        mostrarMensagemSolicitacao(
            "Erro ao registrar solicitação:\n\n" + JSON.stringify(data, null, 2),
            "error"
        );
    } catch (error) {
        mostrarMensagemSolicitacao("Erro na requisição: " + error.message, "error");
    }
}

function limparFormularioSolicitacao() {
    document.getElementById("item").value = "";
    document.getElementById("quantidadeSolicitada").value = "";
    document.getElementById("justificativa").value = "";
    document.getElementById("item-info").classList.add("hidden");
    document.getElementById("item-info").innerHTML = "";
}

function mostrarMensagemSolicitacao(texto, tipo) {
    const box = document.getElementById("mensagem-solicitacao");
    box.className = `message-box ${tipo}`;
    box.textContent = texto;
}