package com.jesen.test.aptout;


import com.jesen.custom_dagger2.Factory;
import com.jesen.custom_dagger2.Preconditions;
import com.jesen.test.Student;
import com.jesen.test.StudentModule;

// TODO 这个是编译期 APT 自动生成的  // 第二个注解
public class StudentModule_ProviderStudentFactory implements Factory<Student> {

    private StudentModule studentModule;

    public StudentModule_ProviderStudentFactory(StudentModule studentModule) {
        this.studentModule = studentModule;
    }

    @Override
    public Student get() {
        //  studentModule.providerStudent() == new Student();
        return Preconditions.checkNotNull(studentModule.providerStudent(),
                "studentModule.providerStudent() is null exception...");
    }

    // 额外生成一个方法，为后续提供
    public static Factory<Student> create(StudentModule studentModule) {
        return new StudentModule_ProviderStudentFactory(studentModule);
    }
}
