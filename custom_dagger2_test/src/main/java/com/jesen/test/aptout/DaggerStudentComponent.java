package com.jesen.test.aptout;

import com.jesen.custom_dagger2.DoubleCheck;
import com.jesen.custom_dagger2.MembersInjctor;
import com.jesen.custom_dagger2.Provider;
import com.jesen.test.MainActivity;
import com.jesen.test.Student;
import com.jesen.test.StudentComponent;
import com.jesen.test.StudentModule;

// TODO 这个是编译期 APT 自动生成的  // 第三个注解
public class DaggerStudentComponent implements StudentComponent {

    public DaggerStudentComponent(Builder builder) {
        initialize(builder); // 我们写第四个注解，才会生成
    }

    // 我们写第四个注解，才会生成
    private Provider<Student> studentProvider;
    private MembersInjctor<MainActivity> mainActivityMembersInjctor;

    private void initialize(Builder builder) {
        studentProvider =
                DoubleCheck.provider(StudentModule_ProviderStudentFactory.create(builder.studentModule));
        mainActivityMembersInjctor = MainActivity_MembersInjector.create(studentProvider);
    }

    public static Builder builder() {
        return new Builder();
    }

    private final static class Builder {

        StudentModule studentModule; // 定义了包裹{Student ---> MainActivity}

        private Builder() { }

        public StudentComponent build() {
            if (studentModule == null) {
                studentModule = new StudentModule();
            }
            return new DaggerStudentComponent(this);
        }

    }

    // 对外提供Builder
    public static StudentComponent create() {
        return builder().build();
    }

    // 往目标（MainActivity）中去注入
    @Override
    public void inject(MainActivity mainActivity) {
        mainActivityMembersInjctor.injectMembers(mainActivity);
    }
}
