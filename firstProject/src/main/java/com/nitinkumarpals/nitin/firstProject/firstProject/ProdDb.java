package com.nitinkumarpals.nitin.firstProject.firstProject;

import org.springframework.stereotype.Component;

@Component
public class ProdDb implements DB{
    public String getData(){
        return "Production DB";
    }
}
