package com.xecoder.controller.business;

import com.xecoder.controller.core.BaseController;
import com.xecoder.model.business.Book;
import com.xecoder.service.dao.BookDao;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by  moxz
 * User: 224911261@qq.com
 * 2016/2/4-16:09
 * Feeling.com.xecoder.controller.business
 */
@RestController

@RequestMapping("/book")
public class BookController extends BaseController {

    @Autowired
    BookDao dao;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> add(@RequestParam String title,
                                 @RequestParam String content) {
        String id = String.valueOf((dao.count() + 1));
        Book book = new Book(id, title, content);
        book = dao.save(book);
        return new ResponseEntity<>(book, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<?> modify(@RequestParam String id,
                                    @RequestParam String title,
                                    @RequestParam String content) {
        Book book = new Book(id, title, content);
        book = dao.save(book);
        return new ResponseEntity<>(book, HttpStatus.OK);
    }

    @RequestMapping(value = "pathDelete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<?> pathDelete(@PathVariable String id) {
        dao.delete(id);
        return new ResponseEntity<>("path delete is ok", HttpStatus.OK);
    }

    @RequestMapping(value = "jwtDelete", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<?> jwtDelete() {
        Claims claims = (Claims) request.getAttribute("claims");//获取解密内容
        String id = String.valueOf(claims.get("id"));
        if(id==null){
            new ResponseEntity<>("id is null", HttpStatus.OK);
        }
        dao.delete(id);
        return new ResponseEntity<>("jwt delete ok", HttpStatus.OK);
    }

    @RequestMapping(value = "all", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> findAll() {
        List<Book> list = dao.findAll();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

}
