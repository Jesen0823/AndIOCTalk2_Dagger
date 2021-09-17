package com.jesen.custom_dagger2;

public interface MembersInjctor<T> {

    void injectMembers(T instance);
}
