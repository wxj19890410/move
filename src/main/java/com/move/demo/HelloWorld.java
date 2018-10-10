package com.move.demo;


import com.move.model.GirlInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/hello")
public class HelloWorld {


    @Autowired
    public GirlInfo girl;


    @RequestMapping(value = "/say" , method = RequestMethod.GET)
    public String say(@RequestParam(value = "id",required = false,defaultValue = "0") Integer id){
        return  "id:"+id;
    }

}