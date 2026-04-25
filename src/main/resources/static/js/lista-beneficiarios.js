let beneficiariosCache = [];

document.addEventListener("DOMContentLoaded", () => {
    carregarBeneficiarios();
});

async function carregarBeneficiarios() {
    const tabela = document.getElementById("tabela-beneficiarios");

    esconderMensagem();

    try {
        const response = await fetch("/beneficiarios");
        beneficiariosCache = await response.json();

        beneficiariosCache.sort((a, b) => prioridadePeso(b.nivelPrioridade) - prioridadePeso(a.nivelPrioridade));

        renderizarTabelaBeneficiarios(beneficiariosCache);
    } catch (error) {
        tabela.innerHTML = `
            <tr>
                <td colspan="7" class="estado-vazio">Erro ao carregar beneficiários.</td>
            </tr>
        `;
        mostrarMensagem("Erro ao carregar beneficiários: " + error.message, "erro");
    }
}

function prioridadePeso(prioridade) {
    switch (prioridade) {
        case "CRITICA": return 5;
        case "URGENTE": return 4;
        case "ALTA": return 3;
        case "MEDIA": return 2;
        case "BAIXA": return 1;
        default: return 0;
    }
}

function renderizarTabelaBeneficiarios(beneficiarios) {
    const tabela = document.getElementById("tabela-beneficiarios");
    tabela.innerHTML = "";

    if (!beneficiarios.length) {
        tabela.innerHTML = `
            <tr>
                <td colspan="7" class="estado-vazio">Nenhum beneficiário cadastrado até o momento.</td>
            </tr>
        `;
        return;
    }

    beneficiarios.forEach(beneficiario => {
        const linha = document.createElement("tr");

        linha.innerHTML = `
            <td>${beneficiario.nome}</td>
            <td>${beneficiario.telefone}</td>
            <td>${beneficiario.email}</td>
            <td>${beneficiario.endereco}</td>
            <td>${beneficiario.tipoBeneficiario}</td>
            <td>
                <span class="badge prioridade-${beneficiario.nivelPrioridade}">
                    ${beneficiario.nivelPrioridade}
                </span>
            </td>
            <td>
                <button class="botao-solicitar" type="button" onclick="irParaSolicitacao('${beneficiario.id}')">
                    Solicitar Item
                </button>
            </td>
        `;

        tabela.appendChild(linha);
    });
}

function filtrarBeneficiarios() {
    const termo = document.getElementById("campo-busca").value.toLowerCase().trim();

    const filtrados = beneficiariosCache.filter(beneficiario =>
        beneficiario.nome.toLowerCase().includes(termo) ||
        beneficiario.telefone.toLowerCase().includes(termo) ||
        beneficiario.email.toLowerCase().includes(termo) ||
        beneficiario.endereco.toLowerCase().includes(termo) ||
        beneficiario.tipoBeneficiario.toLowerCase().includes(termo) ||
        beneficiario.nivelPrioridade.toLowerCase().includes(termo)
    );

    renderizarTabelaBeneficiarios(filtrados);
}

function irParaSolicitacao(beneficiarioId) {
    window.location.href = `/solicitacao.html?beneficiarioId=${beneficiarioId}`;
}

function mostrarMensagem(texto, tipo) {
    const mensagem = document.getElementById("mensagem");
    mensagem.style.display = "block";
    mensagem.className = `mensagem ${tipo}`;
    mensagem.textContent = texto;
}

function esconderMensagem() {
    const mensagem = document.getElementById("mensagem");
    mensagem.style.display = "none";
    mensagem.textContent = "";
    mensagem.className = "mensagem";
}