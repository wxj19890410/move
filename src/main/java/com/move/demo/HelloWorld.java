package com.move.demo;


import com.move.model.GirlInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/hello")
public class HelloWorld {


    @Autowired
    public GirlInfo girl;


    @GetMapping(value = "/say" )
    public String say(@RequestParam(value = "id",required = false,defaultValue = "0") Integer id){
        return  "id:"+id;
    }

}
