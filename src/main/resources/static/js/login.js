document.addEventListener("DOMContentLoaded", () => {
    const usuarioLogado = localStorage.getItem("usuarioLogado");

    if (usuarioLogado) {
        const message = document.getElementById("login-message");
        message.className = "message-box success";
        message.textContent = "Você já está logado. Faça logout se quiser entrar com outra conta.";
    }
});

async function realizarLogin() {
    const email = document.getElementById("email").value.trim();
    const senha = document.getElementById("senha").value.trim();
    const message = document.getElementById("login-message");

    if (!email || !senha) {
        message.className = "message-box error";
        message.textContent = "Preencha email e senha para continuar.";
        return;
    }

    try {
        const response = await fetch("/auth/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ email, senha })
        });

        const data = await response.json();

        if (response.ok) {
            localStorage.setItem("usuarioLogado", JSON.stringify(data));
            message.className = "message-box success";
            message.textContent = "Login realizado com sucesso. Redirecionando...";
            setTimeout(() => {
                window.location.href = "/index.html";
            }, 800);
            return;
        }

        message.className = "message-box error";
        message.textContent = data.mensagem || JSON.stringify(data, null, 2);
    } catch (error) {
        message.className = "message-box error";
        message.textContent = "Erro na requisição: " + error.message;
    }
}

function logoutSistema() {
    localStorage.removeItem("usuarioLogado");
    window.location.href = "/login.html";
}