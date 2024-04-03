package ru.kravchenko.servlet;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.testcontainers.shaded.org.apache.commons.io.output.ByteArrayOutputStream;
import ru.kravchenko.service.CommentService;
import ru.kravchenko.service.impl.CommentServiceImpl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CommentServletTest {

    @Mock
    CommentService commentService = mock(CommentServiceImpl.class);

    @Mock
    CommentServlet commentServlet = new CommentServlet(commentService);

    @Mock
    HttpServletRequest request = mock(HttpServletRequest.class);

    @Mock
    HttpServletResponse response = mock(HttpServletResponse.class);

    @Test
    void doGetTest() throws IOException {
        when(request.getParameter("action")).thenReturn("get");
        when(request.getParameter("id")).thenReturn("1");
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);
        commentServlet.doGet(request, response);
        verify(commentService, times(1)).findById(Long.valueOf(request.getParameter("id")));
    }

    @Test
    void DoPostTest() throws IOException {
        when(request.getParameter("action")).thenReturn("put");
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream(50);
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        when(request.getInputStream()).thenReturn(new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            @Override
            public int read() throws IOException {
                return inputStream.read();
            }
        });
        commentServlet.doPost(request, response);
        verify(commentService, times(1)).saveComment(null);
    }

    @Test
    void doDeleteTest() {
        when(request.getParameter("action")).thenReturn("delete");
        when(request.getParameter("id")).thenReturn("100");
        commentServlet.doDelete(request, response);
        verify(commentService, times(1)).removeComment(Long.valueOf(request.getParameter("id")));
    }
}