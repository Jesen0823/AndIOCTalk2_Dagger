package com.jesen.dagger.demo;

import com.jesen.dagger.MainActivity;

import dagger.Component;

// Component拿到module包裹
@Component(modules = StudentModule.class)
public interface StudentComponent {

    void injectMainActivity(MainActivity mainActivity);
}
