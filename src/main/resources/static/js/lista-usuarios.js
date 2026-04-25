let usuariosCache = [];
let modoModal = "visualizar";

document.addEventListener("DOMContentLoaded", () => {
    carregarUsuarios();
});

async function carregarUsuarios() {
    const tabela = document.getElementById("tabela-usuarios");

    esconderMensagem();

    try {
        const response = await fetch("/usuarios");
        usuariosCache = await response.json();

        renderizarTabela(usuariosCache);
    } catch (error) {
        tabela.innerHTML = `
            <tr>
                <td colspan="8" class="estado-vazio">Erro ao carregar usuários.</td>
            </tr>
        `;

        mostrarMensagem("Erro ao carregar usuários: " + error.message, "erro");
    }
}

function renderizarTabela(usuarios) {
    const tabela = document.getElementById("tabela-usuarios");
    tabela.innerHTML = "";

    if (!usuarios.length) {
        tabela.innerHTML = `
            <tr>
                <td colspan="8" class="estado-vazio">Nenhum usuário cadastrado até o momento.</td>
            </tr>
        `;
        return;
    }

    usuarios.forEach(usuario => {
        const linha = document.createElement("tr");

        linha.innerHTML = `
            <td>${usuario.id}</td>
            <td>${usuario.nome}</td>
            <td>${usuario.telefone}</td>
            <td>${usuario.email}</td>
            <td>${usuario.endereco}</td>
            <td>
                <span class="badge prioridade-${usuario.prioridade}">
                    ${usuario.prioridade}
                </span>
            </td>
            <td>${usuario.perfil}</td>
            <td>
                <div class="acoes-linha">
                    <button class="botao-visualizar" type="button" onclick="abrirVisualizacao('${usuario.id}')">
                        Visualizar
                    </button>
                    <button class="botao-editar" type="button" onclick="abrirEdicao('${usuario.id}')">
                        Editar
                    </button>
                    <button class="botao-excluir" type="button" onclick="excluirUsuario('${usuario.id}')">
                        Excluir
                    </button>
                </div>
            </td>
        `;

        tabela.appendChild(linha);
    });
}

function filtrarUsuarios() {
    const termo = document.getElementById("campo-busca").value.toLowerCase().trim();

    const filtrados = usuariosCache.filter(usuario =>
        usuario.nome.toLowerCase().includes(termo) ||
        usuario.telefone.toLowerCase().includes(termo) ||
        usuario.email.toLowerCase().includes(termo) ||
        usuario.endereco.toLowerCase().includes(termo) ||
        usuario.id.toLowerCase().includes(termo) ||
        usuario.prioridade.toLowerCase().includes(termo) ||
        usuario.perfil.toLowerCase().includes(termo)
    );

    renderizarTabela(filtrados);
}

function abrirVisualizacao(id) {
    const usuario = usuariosCache.find(usuario => usuario.id === id);

    if (!usuario) {
        mostrarMensagem("Usuário não encontrado para visualização.", "erro");
        return;
    }

    modoModal = "visualizar";

    document.getElementById("modal-titulo").textContent = "Visualizar Usuário";
    document.getElementById("modal-usuario-id").value = usuario.id;
    document.getElementById("modal-nome").value = usuario.nome;
    document.getElementById("modal-telefone").value = usuario.telefone;
    document.getElementById("modal-email").value = usuario.email;
    document.getElementById("modal-endereco").value = usuario.endereco;
    document.getElementById("modal-prioridade").value = usuario.prioridade;
    document.getElementById("modal-perfil").value = usuario.perfil;
    document.getElementById("modal-senha").value = "";
    document.getElementById("campo-senha-container").style.display = "none";
    document.getElementById("botao-salvar-edicao").style.display = "none";

    definirCamposSomenteLeitura(true);
    abrirModal();
}

function abrirEdicao(id) {
    const usuario = usuariosCache.find(usuario => usuario.id === id);

    if (!usuario) {
        mostrarMensagem("Usuário não encontrado para edição.", "erro");
        return;
    }

    modoModal = "editar";

    document.getElementById("modal-titulo").textContent = "Editar Usuário";
    document.getElementById("modal-usuario-id").value = usuario.id;
    document.getElementById("modal-nome").value = usuario.nome;
    document.getElementById("modal-telefone").value = usuario.telefone;
    document.getElementById("modal-email").value = usuario.email;
    document.getElementById("modal-endereco").value = usuario.endereco;
    document.getElementById("modal-prioridade").value = usuario.prioridade;
    document.getElementById("modal-perfil").value = usuario.perfil;
    document.getElementById("modal-senha").value = "";
    document.getElementById("campo-senha-container").style.display = "block";
    document.getElementById("botao-salvar-edicao").style.display = "inline-block";

    definirCamposSomenteLeitura(false);
    abrirModal();
}

function definirCamposSomenteLeitura(somenteLeitura) {
    document.getElementById("modal-nome").readOnly = somenteLeitura;
    document.getElementById("modal-telefone").readOnly = somenteLeitura;
    document.getElementById("modal-email").readOnly = somenteLeitura;
    document.getElementById("modal-endereco").readOnly = somenteLeitura;
    document.getElementById("modal-prioridade").disabled = somenteLeitura;
    document.getElementById("modal-perfil").disabled = somenteLeitura;
    document.getElementById("modal-senha").readOnly = somenteLeitura;
}

function abrirModal() {
    document.getElementById("modal-overlay").classList.remove("oculto");
}

function fecharModal() {
    document.getElementById("modal-overlay").classList.add("oculto");
}

async function salvarEdicao() {
    if (modoModal !== "editar") {
        return;
    }

    const id = document.getElementById("modal-usuario-id").value;

    const usuarioAtualizado = {
        nome: document.getElementById("modal-nome").value,
        telefone: document.getElementById("modal-telefone").value,
        email: document.getElementById("modal-email").value,
        endereco: document.getElementById("modal-endereco").value,
        senha: document.getElementById("modal-senha").value,
        prioridade: document.getElementById("modal-prioridade").value,
        perfil: document.getElementById("modal-perfil").value
    };

    try {
        const response = await fetch(`/usuarios/${id}`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(usuarioAtualizado)
        });

        if (response.ok) {
            fecharModal();
            mostrarMensagem("Usuário atualizado com sucesso.", "sucesso");
            await carregarUsuarios();
            return;
        }

        const erro = await response.json();

        if (erro.mensagem) {
            mostrarMensagem(erro.mensagem, "erro");
            return;
        }

        mostrarMensagem(JSON.stringify(erro), "erro");
    } catch (error) {
        mostrarMensagem("Erro na requisição: " + error.message, "erro");
    }
}

async function excluirUsuario(id) {
    const confirmar = confirm("Tem certeza que deseja excluir este usuário?");

    if (!confirmar) {
        return;
    }

    try {
        const response = await fetch(`/usuarios/${id}`, {
            method: "DELETE"
        });

        if (response.status === 204) {
            mostrarMensagem("Usuário excluído com sucesso.", "sucesso");
            await carregarUsuarios();
            return;
        }

        const erro = await response.json();
        mostrarMensagem(erro.mensagem || "Erro ao excluir usuário.", "erro");
    } catch (error) {
        mostrarMensagem("Erro na requisição: " + error.message, "erro");
    }
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