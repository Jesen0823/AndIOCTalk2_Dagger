package com.jesen.dagger.demo;

import dagger.Module;
import dagger.Provides;

/**
 * Module 用来包装对象Student
 * */

@Module()
public class StudentModule {

    @Provides
    public Student getStudent(){
        return new Student();
    }
}
