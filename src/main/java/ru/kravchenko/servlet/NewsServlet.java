package ru.kravchenko.servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.kravchenko.model.News;
import ru.kravchenko.service.NewsService;
import ru.kravchenko.service.impl.NewsServiceImpl;
import ru.kravchenko.servlet.adapter.LocalDateTimeAdapter;
import ru.kravchenko.servlet.dto.IncomingNewsDto;
import ru.kravchenko.servlet.dto.OutGoingNewsDto;
import ru.kravchenko.servlet.mapper.NewsMapper;
import ru.kravchenko.servlet.mapper.impl.NewsMapperImpl;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.Scanner;

@WebServlet(value = "/news")
public class NewsServlet extends HttpServlet {

    private NewsService newsService;
    private NewsMapper newsMapper = new NewsMapperImpl();

    public NewsServlet() {
        this.newsService = new NewsServiceImpl();
    }

    public NewsServlet(NewsService newsService) {
        this.newsService = newsService;
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        if (req.getParameter("action").equals("get")) {
            Long id = Long.parseLong(req.getParameter("id"));
            News news = newsService.findById(id);
            OutGoingNewsDto newsDto = newsMapper.map(news);
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                    .create();
            String json = gson.toJson(newsDto);
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
        IncomingNewsDto newsDto = gson.fromJson(json, IncomingNewsDto.class);
        News news = newsMapper.map(newsDto);
        newsService.saveNews(news);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        if (req.getParameter("action").equals("delete")) {
            Long id = Long.parseLong(req.getParameter("id"));
            newsService.removeNews(id);
        }
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}