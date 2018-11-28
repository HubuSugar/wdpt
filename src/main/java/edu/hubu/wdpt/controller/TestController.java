package edu.hubu.wdpt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * created by Sugar  2018/11/12 22:02
 */
@Controller
public class TestController {

    @GetMapping(path="/header")
    public String head(){

        return "header";
    }


    @RequestMapping(path = "/queryStudentInfo",method = {RequestMethod.GET})
    public String  queryStudentInfo(Model model){

        List<Map<String,Object>> resultList = new ArrayList<>();
        Map<String,Object> student = new HashMap<>();

        student.put("sid","101");
        student.put("sname","张三");
        student.put("sage",20);

        Map<String,String> course = new HashMap<>();
        course.put("cname","数学，语文，英语");
        course.put("score","80,87,93");

        student.put("course",course);
        resultList.add(student);



        student.put("sid","102");
        student.put("sname","李四");
        student.put("sage",18);

        course.put("cname","物理，化学，生物");
        course.put("score","88.78,90");
        student.put("course",course);

        resultList.add(student);

        model.addAttribute("resultList",resultList);
        return "studentInfo";
    }


}
