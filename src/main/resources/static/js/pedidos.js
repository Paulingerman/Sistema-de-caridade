let pedidosCache = [];
let beneficiariosCache = [];
let itensCache = [];
let usuarioLogadoPedidos = null;

document.addEventListener("DOMContentLoaded", async () => {
    usuarioLogadoPedidos = getUsuarioLogado();
    await carregarBasesAuxiliares();
    configurarPedidosPorPerfil();
    await carregarPedidos();
});

async function carregarBasesAuxiliares() {
    try {
        const [beneficiariosResponse, itensResponse] = await Promise.all([
            fetch("/beneficiarios"),
            fetch("/itens")
        ]);

        beneficiariosCache = await beneficiariosResponse.json();
        itensCache = await itensResponse.json();
    } catch (error) {
        mostrarMensagemPedidos("Erro ao carregar dados auxiliares: " + error.message, "error");
    }
}

function configurarPedidosPorPerfil() {
    if (!usuarioLogadoPedidos) {
        return;
    }

    if (usuarioLogadoPedidos.perfil === "BENEFICIARIO") {
        const filtro = document.getElementById("filtroBeneficiario");
        filtro.value = usuarioLogadoPedidos.id;
        filtro.disabled = true;
    }
}

async function carregarPedidos(beneficiarioId = "", status = "") {
    try {
        const params = new URLSearchParams();

        const filtroBeneficiarioReal =
                usuarioLogadoPedidos && usuarioLogadoPedidos.perfil === "BENEFICIARIO"
                    ? usuarioLogadoPedidos.id
                    : beneficiarioId;

        if (filtroBeneficiarioReal) {
            params.append("beneficiarioId", filtroBeneficiarioReal);
        }

        if (status) {
            params.append("status", status);
        }

        const url = params.toString()
            ? `/solicitacoes?${params.toString()}`
            : "/solicitacoes";

        const response = await fetch(url);
        pedidosCache = await response.json();

        renderizarPedidos(pedidosCache);
    } catch (error) {
        mostrarMensagemPedidos("Erro ao carregar pedidos: " + error.message, "error");
    }
}

function aplicarFiltrosPedidos() {
    const beneficiarioId = document.getElementById("filtroBeneficiario").value.trim();
    const status = document.getElementById("filtroStatus").value;

    carregarPedidos(beneficiarioId, status);
}

function podeGerenciarPedidos() {
    if (!usuarioLogadoPedidos) {
        return false;
    }

    return ["ADMIN", "OPERADOR"].includes(usuarioLogadoPedidos.perfil);
}

function renderizarPedidos(pedidos) {
    const grid = document.getElementById("pedidos-grid");
    grid.innerHTML = "";

    if (!pedidos.length) {
        grid.innerHTML = `<div class="empty-state">Nenhum pedido encontrado.</div>`;
        return;
    }

    pedidos.forEach((pedido, index) => {
        const beneficiario = buscarBeneficiarioPorId(pedido.beneficiarioId);
        const item = buscarItemPorId(pedido.itemId);

        const nomeBeneficiario = beneficiario ? beneficiario.nome : "Beneficiário não encontrado";
        const nomeItem = item ? item.nomeItem : "Item não encontrado";

        const card = document.createElement("article");
        card.className = "pedido-card";

        card.innerHTML = `
            <div class="pedido-topo">
                <div>
                    <h2 class="pedido-title">Pedido ${index + 1}</h2>
                    <div class="pedido-id">ID: ${pedido.id}</div>
                </div>
                <div>
                    <span class="badge-status status-${pedido.status}">${pedido.status}</span>
                </div>
            </div>

            <div class="pedido-grid">
                <div class="pedido-info">
                    <strong>Beneficiário</strong>
                    <span>${nomeBeneficiario}</span>
                    <small class="pedido-meta">ID: ${pedido.beneficiarioId}</small>
                </div>

                <div class="pedido-info">
                    <strong>Item</strong>
                    <span>${nomeItem}</span>
                    <small class="pedido-meta">ID: ${pedido.itemId}</small>
                </div>

                <div class="pedido-info">
                    <strong>Quantidade</strong>
                    <span>${pedido.quantidadeSolicitada}</span>
                </div>

                <div class="pedido-info">
                    <strong>Data</strong>
                    <span>${formatarDataPedido(pedido.dataSolicitacao)}</span>
                </div>

                <div class="pedido-info full">
                    <strong>Justificativa</strong>
                    <span>${pedido.justificativa}</span>
                </div>

                <div class="pedido-info full">
                    <strong>Observação</strong>
                    <span>${pedido.observacao || "Sem observações"}</span>
                </div>
            </div>

            <div class="pedido-actions">
                ${podeGerenciarPedidos() && pedido.status === "APROVADA"
                    ? `<button class="button-primary" onclick="concluirPedido('${pedido.id}')">Concluir</button>`
                    : ""
                }

                ${podeGerenciarPedidos() && pedido.status !== "CONCLUIDA" && pedido.status !== "CANCELADA"
                    ? `<button class="button-danger" onclick="cancelarPedido('${pedido.id}')">Cancelar</button>`
                    : ""
                }
            </div>
        `;

        grid.appendChild(card);
    });
}

function buscarBeneficiarioPorId(id) {
    return beneficiariosCache.find(beneficiario => beneficiario.id === id);
}

function buscarItemPorId(id) {
    return itensCache.find(item => item.id === id);
}

async function concluirPedido(id) {
    try {
        const response = await fetch(`/solicitacoes/${id}/concluir`, {
            method: "PUT"
        });

        const data = await response.json();

        if (response.ok) {
            mostrarMensagemPedidos("Pedido concluído com sucesso.", "success");
            await carregarBasesAuxiliares();
            await carregarPedidos();
            return;
        }

        mostrarMensagemPedidos(JSON.stringify(data, null, 2), "error");
    } catch (error) {
        mostrarMensagemPedidos("Erro ao concluir pedido: " + error.message, "error");
    }
}

async function cancelarPedido(id) {
    try {
        const response = await fetch(`/solicitacoes/${id}/cancelar`, {
            method: "PUT"
        });

        const data = await response.json();

        if (response.ok) {
            mostrarMensagemPedidos("Pedido cancelado com sucesso.", "success");
            await carregarBasesAuxiliares();
            await carregarPedidos();
            return;
        }

        mostrarMensagemPedidos(JSON.stringify(data, null, 2), "error");
    } catch (error) {
        mostrarMensagemPedidos("Erro ao cancelar pedido: " + error.message, "error");
    }
}

function mostrarMensagemPedidos(texto, tipo) {
    const box = document.getElementById("mensagem-pedidos");
    box.className = `message-box ${tipo}`;
    box.textContent = texto;
}

function formatarDataPedido(dataIso) {
    if (!dataIso) {
        return "-";
    }

    const data = new Date(dataIso);
    return data.toLocaleString("pt-BR");
}