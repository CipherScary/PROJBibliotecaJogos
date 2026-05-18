package br.com.biblioteca.servlet;

import java.io.IOException;

import br.com.biblioteca.model.Jogo;
import br.com.biblioteca.service.JogoService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/jogos")
public class JogoServlet extends HttpServlet {

    private JogoService service = new JogoService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String acao = req.getParameter("acao");

        if (acao == null) {

            req.setAttribute("lista", service.listar());
            req.getRequestDispatcher("jogos.jsp").forward(req, resp);

        } else if (acao.equals("editar")) {

            int id = Integer.parseInt(req.getParameter("id"));

            req.setAttribute("jogo", service.buscarPorId(id));
            req.getRequestDispatcher("editar-jogo.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String acao = req.getParameter("acao");

        // DELETE
        if ("deletar".equals(acao)) {

            int id = Integer.parseInt(req.getParameter("id"));
            service.deletar(id);

            resp.sendRedirect("jogos");
            return;
        }

        // CREATE / UPDATE
        String id = req.getParameter("id");

        Jogo jogo = new Jogo();

        jogo.setNome(req.getParameter("nome"));
        jogo.setGenero(req.getParameter("genero"));
        jogo.setPlataforma(req.getParameter("plataforma"));
        jogo.setPreco(Double.parseDouble(req.getParameter("preco")));

        if (id == null || id.isEmpty()) {

            service.salvar(jogo);

        } else {

            jogo.setId(Integer.parseInt(id));
            service.atualizar(jogo);
        }

        resp.sendRedirect("jogos");
    }
}