package ru.kravchenko.servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.kravchenko.model.Tag;
import ru.kravchenko.service.TagService;
import ru.kravchenko.service.impl.TagServiceImpl;
import ru.kravchenko.servlet.adapter.LocalDateTimeAdapter;
import ru.kravchenko.servlet.dto.OutGoingTagDto;
import ru.kravchenko.servlet.mapper.TagMapper;
import ru.kravchenko.servlet.mapper.impl.TagMapperImpl;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

@WebServlet(value = "/tag")
public class TagServlet extends HttpServlet {

    private TagService tagService;
    private TagMapper tagMapper = new TagMapperImpl();

    public TagServlet() {
        this.tagService = new TagServiceImpl();
    }

    public TagServlet(TagService tagService) {
        this.tagService = tagService;
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("action").equals("get")) {
            Long id = Long.parseLong(req.getParameter("id"));
            Tag tag = tagService.findById(id);
            OutGoingTagDto tagDto = tagMapper.map(tag);
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                    .create();
            String json = gson.toJson(tagDto);
            try (PrintWriter out = resp.getWriter()) {
                out.print(json);
                out.flush();
            } catch (IOException exception) {
                throw new RuntimeException(exception);
            }
        } else if (
                !req.getParameter("action").equals("get")
                        && !req.getParameter("action").equals("put")
                        && !req.getParameter("action").equals("delete")
        ) {
            try {
                resp.sendRedirect("/");
            } catch (IOException exception) {
                throw new RuntimeException(exception);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("action").equals("delete")) {
            Long id = Long.parseLong(req.getParameter("id"));
            tagService.removeTag(id);
        }
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}
