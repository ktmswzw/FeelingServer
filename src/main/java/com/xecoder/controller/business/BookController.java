package com.xecoder.controller.business;

import com.xecoder.common.exception.ReturnMessage;
import com.xecoder.controller.core.BaseController;
import com.xecoder.model.business.Book;
import com.xecoder.service.dao.BookDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
        ReturnMessage returnMessage = new ReturnMessage("path delete is ok",HttpStatus.OK);
        return new ResponseEntity<>(returnMessage, HttpStatus.OK);
    }

    @SuppressWarnings(value = "unchecked")
    @RequestMapping(value = "jwtDelete", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<?> jwtDelete() {
        Map<String, Object> claims = (Map<String, Object>) request.getAttribute("claims");//获取解密内容
        String id = String.valueOf(claims.get("id"));
        if(id==null){
            ReturnMessage returnMessage = new ReturnMessage("id is null",HttpStatus.BAD_REQUEST);
            new ResponseEntity<>(returnMessage, HttpStatus.OK);
        }
        dao.delete(id);
        ReturnMessage returnMessage = new ReturnMessage("jwt delete is ok",HttpStatus.OK);
        return new ResponseEntity<>(returnMessage, HttpStatus.OK);
    }

    @RequestMapping(value = "all", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> findAll() {
        List<Book> list = dao.findAll();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

}
