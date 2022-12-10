package com.xzit.bookmanager.controller.user;

import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.xzit.bookmanager.ResponseData.Response;
import com.xzit.bookmanager.entity.AuthUser;
import com.xzit.bookmanager.entity.Book;
import com.xzit.bookmanager.entity.Borrow;
import com.xzit.bookmanager.service.BookService;
import com.xzit.bookmanager.service.BorrowService;
import com.xzit.bookmanager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    BorrowService borrowService;
    @Autowired
    BookService bookService;
    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    Gson gson;
    @GetMapping("/index")
    String index(HttpSession session, Model model, @RequestParam(value = "page",defaultValue = "1") int pageNum, @RequestParam(value = "size",defaultValue = "5") int pageSize){
        AuthUser user=userService.findUser(session);
        PageInfo<Book> bookList=bookService.getAvailableBook(pageNum,pageSize);
        model.addAttribute("bookList",bookList);
        model.addAttribute("user",user);
        return "user/index";
    }
    @PostMapping("/borrowBook")
    @ResponseBody
        Response borrowBook(HttpSession session,String ISBN){
        AuthUser user = userService.findUser(session);
        ValueOperations<String,String> ops=redisTemplate.opsForValue();
        Book book=gson.fromJson(ops.get(ISBN),Book.class);
        borrowService.borrowBook(user,book);
        return Response.ok("借阅成功");

    }
    @GetMapping("/book")
    String borrowInfo(HttpSession session,Model model){
        AuthUser user = userService.findUser(session);
        List<Borrow> borrowList=borrowService.getBookList(user);
        model.addAttribute("user",user);
        model.addAttribute("borrowList",borrowList);
        return "user/book";


    }
}
