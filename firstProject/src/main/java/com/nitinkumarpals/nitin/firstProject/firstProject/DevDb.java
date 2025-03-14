package com.nitinkumarpals.nitin.firstProject.firstProject;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
@ConditionalOnProperty(name = "deploy.env", havingValue = "dev")
public class DevDb implements DB{

    public String getData(){
        return "Dev Data";
    }

}
