package com.nitinkumarpals.nitin.firstProject.firstProject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DbService {
    @Autowired
    private DB db;
    String getData(){
        return db.getData();
    }
}
