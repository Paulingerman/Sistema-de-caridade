document.addEventListener("DOMContentLoaded", () => {
    carregarUsuarios();
});

async function carregarUsuarios() {
    const tabela = document.getElementById("tabela-usuarios");
    const mensagem = document.getElementById("mensagem");

    esconderMensagem();

    try {
        const response = await fetch("/usuarios");
        const usuarios = await response.json();

        tabela.innerHTML = "";

        if (!usuarios.length) {
            tabela.innerHTML = `
                <tr>
                    <td colspan="5" class="estado-vazio">Nenhum usuário cadastrado até o momento.</td>
                </tr>
            `;
            return;
        }

        usuarios.forEach(usuario => {
            const linha = document.createElement("tr");

            linha.innerHTML = `
                <td>${usuario.id}</td>
                <td>${usuario.nome}</td>
                <td>${usuario.email}</td>
                <td>
                    <span class="badge prioridade-${usuario.prioridade}">
                        ${usuario.prioridade}
                    </span>
                </td>
                <td>
                    <div class="acoes-linha">
                        <button class="botao-excluir" onclick="excluirUsuario('${usuario.id}')">
                            Excluir
                        </button>
                    </div>
                </td>
            `;

            tabela.appendChild(linha);
        });
    } catch (error) {
        tabela.innerHTML = `
            <tr>
                <td colspan="5" class="estado-vazio">Erro ao carregar usuários.</td>
            </tr>
        `;

        mostrarMensagem("Erro ao carregar usuários: " + error.message, "erro");
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