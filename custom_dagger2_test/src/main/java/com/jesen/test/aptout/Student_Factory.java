package com.jesen.test.aptout;


import com.jesen.custom_dagger2.Factory;
import com.jesen.test.Student;

// TODO 这个是编译期 APT 自动生成的  // 第一个注解
public enum  Student_Factory implements Factory<Student> {

    INSTANCE;

    @Override
    public Student get() {
        return new Student();
    }

    public static Factory create() {
        return INSTANCE;
    }
}
