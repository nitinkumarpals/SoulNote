package com.nitinkumarpals.nitin.firstProject.firstProject;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class ProdDb implements DB{
    public String getData(){
        return "Production DB";
    }
}
