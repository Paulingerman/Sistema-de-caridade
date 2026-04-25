let itensCache = [];

document.addEventListener("DOMContentLoaded", () => {
    carregarItens();
});

async function carregarItens(categoria = "", status = "") {
    try {
        const params = new URLSearchParams();

        if (categoria) {
            params.append("categoria", categoria);
        }

        if (status) {
            params.append("status", status);
        }

        const url = params.toString()
            ? `/itens?${params.toString()}`
            : "/itens";

        const response = await fetch(url);
        itensCache = await response.json();

        renderizarItens(itensCache);
    } catch (error) {
        mostrarMensagemItens("Erro ao carregar itens: " + error.message, "error");
    }
}

function aplicarFiltrosItens() {
    const categoria = document.getElementById("filtroCategoria").value;
    const status = document.getElementById("filtroStatus").value;

    carregarItens(categoria, status);
}

function renderizarItens(itens) {
    const grid = document.getElementById("itens-grid");
    grid.innerHTML = "";

    if (!itens.length) {
        grid.innerHTML = `<div class="empty-state">Nenhum item encontrado.</div>`;
        return;
    }

    itens.forEach((item, index) => {
        const disponivel = item.status === "DISPONIVEL" && item.quantidade > 0;

        const card = document.createElement("article");
        card.className = "item-card";

        card.innerHTML = `
            <div class="item-topo">
                <div>
                    <h2 class="item-title">Item ${index + 1} - ${item.nomeItem}</h2>
                    <div class="item-subtitle">ID: ${item.id}</div>
                </div>
                <div>
                    <span class="badge-status status-${item.status}">${item.status}</span>
                </div>
            </div>

            <div class="item-grid">
                <div class="item-info">
                    <strong>Categoria</strong>
                    <span>${item.categoria}</span>
                </div>

                <div class="item-info">
                    <strong>Estado de conservação</strong>
                    <span>${item.estadoConservacao}</span>
                </div>

                <div class="item-info">
                    <strong>Quantidade em estoque</strong>
                    <span>${item.quantidade}</span>
                </div>

                <div class="item-info">
                    <strong>Situação</strong>
                    <span class="${disponivel ? "stock-ok" : "stock-off"}">
                        ${disponivel ? "Disponível para solicitação" : "Indisponível no momento"}
                    </span>
                </div>

                <div class="item-info">
                    <strong>Data de cadastro</strong>
                    <span>${item.dataCadastro}</span>
                </div>

                <div class="item-info">
                    <strong>Status atual</strong>
                    <span>${item.status}</span>
                </div>

                <div class="item-info full">
                    <strong>Descrição</strong>
                    <span>${item.descricao}</span>
                </div>
            </div>

            <div class="item-actions">
                <button class="button-secondary" onclick="atualizarStatusItem('${item.id}', 'DISPONIVEL')">Disponível</button>
                <button class="button-secondary" onclick="atualizarStatusItem('${item.id}', 'RESERVADO')">Reservado</button>
                <button class="button-secondary" onclick="atualizarStatusItem('${item.id}', 'ENTREGUE')">Entregue</button>
                <button class="button-danger" onclick="atualizarStatusItem('${item.id}', 'CANCELADO')">Cancelar</button>
            </div>
        `;

        grid.appendChild(card);
    });
}

async function atualizarStatusItem(id, status) {
    try {
        const response = await fetch(`/itens/${id}/status`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ status })
        });

        const data = await response.json();

        if (response.ok) {
            mostrarMensagemItens("Status do item atualizado com sucesso.", "success");
            await carregarItens();
            return;
        }

        mostrarMensagemItens(JSON.stringify(data, null, 2), "error");
    } catch (error) {
        mostrarMensagemItens("Erro ao atualizar status do item: " + error.message, "error");
    }
}

function mostrarMensagemItens(texto, tipo) {
    const box = document.getElementById("mensagem-itens");
    box.className = `message-box ${tipo}`;
    box.textContent = texto;
}