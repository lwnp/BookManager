package com.xzit.bookmanager.controller.admin;

import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.xzit.bookmanager.ResponseData.Response;
import com.xzit.bookmanager.entity.*;
import com.xzit.bookmanager.service.*;
import com.xzit.bookmanager.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
   StatisticService statisticService;
    @Autowired
    UserService userService;
    @Autowired
    BookService bookService;
    @Autowired
    HistoryService historyService;
    @Autowired
    Gson gson;
    @Resource
    RedisUtils redisUtils;
    @Autowired
    BorrowInfoService borrowInfoService;
    @Autowired
    BorrowService borrowService;
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    String ISBN;
    String query;

    @GetMapping("/index")
    String index(Model model, HttpSession session){
        redisUtils.ReadStatisticToRedis();
        AuthUser user=userService.findUser(session);
        model.addAttribute("user",user);
        model.addAttribute("stat",statisticService.getStatisticNum());
        return "admin/index";

    }
    @GetMapping("/book")
    String bookManage(Model model,HttpSession session,@RequestParam(value = "page",defaultValue = "1") int pageNum,@RequestParam(value = "pageSize",defaultValue = "5")int pageSize){
        AuthUser user=userService.findUser(session);
        PageInfo<Book> booklist=bookService.getBookList(pageNum,pageSize);
        model.addAttribute("bookList",booklist);
        model.addAttribute("user",user);
        return "admin/book";

    }
    @PostMapping("/addBook")
    @ResponseBody
    Response addBook(Book book){
        if(bookService.isExist(book)){
            return Response.fail("该图书已存在，添加失败！");
        }
        else {
            bookService.addBook(book);
            return Response.ok("添加成功");
        }


    }
    @PostMapping( "/bookData")
    @ResponseBody
    void getInfo(@RequestParam(value = "ISBN") String Isbn){
        ISBN=Isbn;

    }
    @PostMapping("/deleteBook")
    @ResponseBody
    Response deleteBook(){
        bookService.deleteBookByIsbn(ISBN);
        return Response.ok("删除成功");
    }
    @PostMapping("/updateBook")
    @ResponseBody
    Response updateBook(Book book){
        book.setISBN(ISBN);
        bookService.updateBook(book);
        return Response.ok("更新成功");
    }
    @GetMapping("/user")
    String userManager(Model model,HttpSession session,@RequestParam(value = "upagenum",defaultValue = "1") int upageNum,@RequestParam(value = "upagesize",defaultValue = "5")int upageSize){
        AuthUser user=userService.findUser(session);
        PageInfo<AuthUser> userList=userService.getUserList(upageNum,upageSize);
        model.addAttribute("userList",userList);
        model.addAttribute("user",user);
        return "admin/user";

    }
    @PostMapping("/resetPassword")
    @ResponseBody
    Response resetPassword(AuthUser user){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword("{bcrypt}"+encoder.encode(user.getPassword()));
        userService.resetPassword(user);
        return Response.ok("修改成功");

    }
    @GetMapping("/borrow")
    String borrowInfo(HttpSession session,Model model,@RequestParam(value = "page",defaultValue = "1",required = false)int pageNum,@RequestParam(value = "size",defaultValue = "5",required = false)int pageSize){
        AuthUser user=userService.findUser(session);
        PageInfo<BorrowInfo> borrowList=borrowInfoService.getBorrowInfo(pageNum,pageSize);
        model.addAttribute("user",user);
        model.addAttribute("borrowList",borrowList);
        return "admin/borrow";
    }
    @GetMapping("/borrow/{username}")
    String getBorrowList(@PathVariable("username")String username,Model model,HttpSession session){
        ValueOperations<String,String> ops=stringRedisTemplate.opsForValue();
        AuthUser user=gson.fromJson(ops.get(username),AuthUser.class);
        AuthUser admin=userService.findUser(session);
        List<Borrow> borrowList=borrowService.getBookList(user);
        model.addAttribute("user",admin);
        model.addAttribute("borrowList",borrowList);
        model.addAttribute("username",username);
        return "admin/detail";

    }
    @PostMapping("/returnBook")
    @ResponseBody
    Response returnBook(String username){
        bookService.returnBook(username);
        return Response.ok("归还成功");

    }
    @PostMapping("/searchBook")
    @ResponseBody
    Response searchBook(@RequestParam("content")String content){
        query=content;
        return Response.ok("ok");

    }
    @GetMapping("/searchResult")
    String getSearchBooks(Model model,HttpSession session,@RequestParam(value = "page",defaultValue = "1",required = false)int pageNum,@RequestParam(value = "size",defaultValue = "5",required = false)int pageSize){
       AuthUser user=userService.findUser(session);
       PageInfo<Book> bookList=bookService.searchBook(query,pageNum,pageSize);
       model.addAttribute("user",user);
       model.addAttribute("bookList",bookList);
       return "admin/book";
    }
    @PostMapping("/searchUser")
    @ResponseBody
    Response searchUser(@RequestParam("username")String username){
        query=username;
        return Response.ok("ok");
    }
    @GetMapping("/searchUserResult")
    String searchUserResult(Model model,HttpSession session,@RequestParam(value = "upage",defaultValue = "1",required = false)int pageNum,@RequestParam(value = "usize",defaultValue = "5",required = false)int pageSize){
        AuthUser user=userService.findUser(session);
        PageInfo<AuthUser> userList=userService.searchUserByQuery(query,pageNum,pageSize);
        model.addAttribute("userList",userList);
        model.addAttribute("user",user);
        return "admin/user";
    }
    @PostMapping("/searchBorrow")
    @ResponseBody
    Response getBorrowInfo(@RequestParam("username")String username){
        query=username;
        return Response.ok("ok");
    }
    @GetMapping("/searchBorrowResults")
    String searchBorrowResult(Model model,HttpSession session,@RequestParam(value = "page",defaultValue = "1",required = false)int pageNum,@RequestParam(value = "size",defaultValue = "5",required = false)int pageSize){
        AuthUser user=userService.findUser(session);
        PageInfo<BorrowInfo> borrowList=borrowInfoService.getBorrowInfoByQuery(query,pageNum,pageSize);
        model.addAttribute("user",user);
        model.addAttribute("borrowList",borrowList);
        return "admin/borrow";
    }
    @GetMapping("/history")
    String history(Model model,HttpSession session,@RequestParam(value = "page",defaultValue = "1",required = false)int pageNum,@RequestParam(value = "size",defaultValue = "5",required = false)int pageSize){
        AuthUser user=userService.findUser(session);
        PageInfo<History> historyList=historyService.getHistoryList(pageNum,pageSize);
        model.addAttribute("user",user);
        model.addAttribute("historyList",historyList);
        return "admin/history";
    }
    @PostMapping("/searchHistory")
    @ResponseBody
    Response getHistoryData(@RequestParam("content")String content){
        query=content;
        return Response.ok("ok");

    }
    @GetMapping("/historyResults")
    String getHistoryResults(Model model,HttpSession session,@RequestParam(value = "page",defaultValue = "1",required = false)int pageNum,@RequestParam(value = "size",defaultValue = "5",required = false)int pageSize){
        AuthUser user=userService.findUser(session);
        PageInfo<History> list=historyService.getHistoryListByQuery(query,pageNum,pageSize);
        model.addAttribute("user",user);
        model.addAttribute("historyList",list);
        return "admin/history";
    }

}
