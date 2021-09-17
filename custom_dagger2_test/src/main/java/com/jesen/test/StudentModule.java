package com.jesen.test;

import com.jesen.custom_dagger2.annotation.Module;
import com.jesen.custom_dagger2.annotation.Provider;

@Module
public class StudentModule {

    @Provider
    public Student providerStudent(){
        return new Student();
    }
}
