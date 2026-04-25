function getUsuarioLogado() {
    try {
        return JSON.parse(localStorage.getItem("usuarioLogado"));
    } catch (error) {
        return null;
    }
}

function logoutSistema() {
    localStorage.removeItem("usuarioLogado");
    window.location.href = "/login.html";
}

function hasPerfil(usuario, perfisPermitidos) {
    if (!usuario) {
        return false;
    }

    return perfisPermitidos.includes(usuario.perfil);
}

function buildLink(activePage, key, iconHtml, label, href, perfisPermitidos, usuarioLogado) {
    if (!hasPerfil(usuarioLogado, perfisPermitidos)) {
        return "";
    }

    return `
        <a href="${href}" class="sidebar-link ${activePage === key ? "active" : ""}">
            <span class="sidebar-link-icon">${iconHtml}</span>
            <span>${label}</span>
        </a>
    `;
}

function renderNavbar(activePage = "") {
    const navbar = document.getElementById("app-navbar");

    if (!navbar) {
        return;
    }

    const usuarioLogado = getUsuarioLogado();

    const linksPublicos = `
        <a href="/index.html" class="sidebar-link ${activePage === "painel" ? "active" : ""}">
            <span class="sidebar-link-icon"><i class="bi bi-house-door"></i></span>
            <span>Painel</span>
        </a>
        <a href="/login.html" class="sidebar-link ${activePage === "login" ? "active" : ""}">
            <span class="sidebar-link-icon"><i class="bi bi-box-arrow-in-right"></i></span>
            <span>Login</span>
        </a>
    `;

    const linksProtegidos = usuarioLogado ? `
        ${buildLink(activePage, "usuarios", "<i class='bi bi-person-gear'></i>", "Usuários", "/cadastro-usuario.html", ["ADMIN"], usuarioLogado)}
        ${buildLink(activePage, "usuarios-lista", "<i class='bi bi-people'></i>", "Lista de Usuários", "/lista-usuarios.html", ["ADMIN"], usuarioLogado)}
        ${buildLink(activePage, "beneficiarios", "<i class='bi bi-person-plus'></i>", "Beneficiários", "/cadastro-beneficiario.html", ["ADMIN", "OPERADOR"], usuarioLogado)}
        ${buildLink(activePage, "beneficiarios-lista", "<i class='bi bi-people'></i>", "Lista de Beneficiários", "/lista-beneficiarios.html", ["ADMIN", "OPERADOR"], usuarioLogado)}
        ${buildLink(activePage, "itens", "<i class='bi bi-gift'></i>", "Cadastrar Item", "/cadastro-item.html", ["ADMIN", "OPERADOR"], usuarioLogado)}
        ${buildLink(activePage, "estoque", "<i class='bi bi-boxes'></i>", "Estoque de Itens", "/estoque-itens.html", ["ADMIN", "OPERADOR"], usuarioLogado)}
        ${buildLink(activePage, "solicitacao", "<i class='bi bi-journal-check'></i>", "Solicitações", "/solicitacao.html", ["ADMIN", "OPERADOR", "BENEFICIARIO"], usuarioLogado)}
        ${buildLink(activePage, "pedidos", "<i class='bi bi-clipboard2-check'></i>", "Pedidos", "/pedidos.html", ["ADMIN", "OPERADOR", "BENEFICIARIO"], usuarioLogado)}
    ` : "";

    navbar.innerHTML = `
        <aside class="sidebar">
            <a href="/index.html" class="sidebar-brand">
                <div class="sidebar-brand-badge">RS</div>
                <div class="sidebar-brand-text">
                    <h1>Rede Solidária</h1>
                    <p>Doação e Reaproveitamento</p>
                </div>
            </a>

            <div class="sidebar-section-title">Navegação</div>

            <nav class="sidebar-nav">
                ${linksPublicos}
                ${linksProtegidos}
            </nav>

            <div class="sidebar-footer">
                ${
                    usuarioLogado
                        ? `
                            <div><strong>${usuarioLogado.nome}</strong></div>
                            <div>${usuarioLogado.email}</div>
                            <div style="margin-top:4px;">Perfil: <strong>${usuarioLogado.perfil}</strong></div>
                            <div style="margin-top:10px;">
                                <button type="button" class="button-danger" style="width:100%;" onclick="logoutSistema()">Sair</button>
                            </div>
                        `
                        : `
                            Faça login para acessar os módulos internos.
                        `
                }
            </div>
        </aside>
    `;
}