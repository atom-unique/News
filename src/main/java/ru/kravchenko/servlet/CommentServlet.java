package ru.kravchenko.servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.kravchenko.model.Comment;
import ru.kravchenko.service.CommentService;
import ru.kravchenko.service.impl.CommentServiceImpl;
import ru.kravchenko.servlet.adapter.LocalDateTimeAdapter;
import ru.kravchenko.servlet.dto.IncomingCommentDto;
import ru.kravchenko.servlet.dto.OutGoingCommentDto;
import ru.kravchenko.servlet.mapper.CommentMapper;
import ru.kravchenko.servlet.mapper.impl.CommentMapperImpl;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.Scanner;

@WebServlet(value = "/comment")
public class CommentServlet extends HttpServlet {

    private CommentService commentService;
    private CommentMapper commentMapper = new CommentMapperImpl();

    public CommentServlet() {
        this.commentService = new CommentServiceImpl();
    }

    public CommentServlet(CommentService commentService) {
        this.commentService = commentService;
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        if (req.getParameter("action").equals("get")) {
            Long id = Long.parseLong(req.getParameter("id"));
            Comment comment = commentService.findById(id);
            OutGoingCommentDto commentDto = commentMapper.map(comment);
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                    .create();
            String json = gson.toJson(commentDto);
            try (PrintWriter out = resp.getWriter()) {
                out.print(json);
                out.flush();
            } catch (IOException exception) {
                throw new RuntimeException(exception);
            }
        } else {
            try {
                resp.sendRedirect("/");
            } catch (IOException exception) {
                throw new RuntimeException(exception);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String json = "";
        if (req.getParameter("action").equals("put")) {
            Scanner scanner = new Scanner(req.getInputStream(), "UTF-8");
            if (scanner.useDelimiter("\\A").hasNext()) {
                json = scanner.useDelimiter("\\A").next();
            }
            scanner.close();
        } else {
            try {
                resp.sendRedirect("/");
            } catch (IOException exception) {
                throw new RuntimeException(exception);
            }
        }
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
        IncomingCommentDto commentDto = gson.fromJson(json, IncomingCommentDto.class);
        Comment comment = commentMapper.map(commentDto);
        commentService.saveComment(comment);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        if (req.getParameter("action").equals("delete")) {
            Long id = Long.parseLong(req.getParameter("id"));
            commentService.removeComment(id);
        }
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}
