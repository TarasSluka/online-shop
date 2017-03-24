package com.sluka.taras.web.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = {"/", "/home", "/index"})
public class IndexController {
    public Logger logger = LogManager.getLogger(IndexController.class);

    @RequestMapping(value = "/connection", method = RequestMethod.GET)
    public
    @ResponseBody
    String connection() {
        logger.info("");
        return "OK";
    }

    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return "index";
    }
}