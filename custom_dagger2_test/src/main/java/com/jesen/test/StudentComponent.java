package com.jesen.test;

import com.jesen.custom_dagger2.annotation.Component;

@Component(modules = {StudentModule.class})
public interface StudentComponent {

    void inject(MainActivity mainActivity);
}
