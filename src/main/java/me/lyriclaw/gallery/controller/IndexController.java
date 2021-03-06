package me.lyriclaw.gallery.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("")
@Slf4j
public class IndexController {

    @RequestMapping(value = "", method = RequestMethod.GET)
    void index(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/gallery/").forward(request, response);
    }

    @RequestMapping(value = "/gallery/**", method = RequestMethod.GET)
    void gallery(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/static/dist/index.html").forward(request, response);
    }

}
