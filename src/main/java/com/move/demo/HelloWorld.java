package com.move.demo;


import com.move.model.GirlInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorld {

    @Autowired
    private GirlInfo girl;


    @RequestMapping(value = "/hello",method = RequestMethod.GET)
    public String say(){
        return  girl.getCupSize();
    }
}
