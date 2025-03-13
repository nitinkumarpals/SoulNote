package com.nitinkumarpals.nitin.firstProject.firstProject;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

public class Apple {
    void eatApple(){
        System.out.println("Eating Apple");
    }
    @PostConstruct
    void callBeforeCreating(){
        System.out.println("Calling before Creating");
    }

    @PreDestroy
    void destroy(){
        System.out.println("Destroying bean");
    }
}
