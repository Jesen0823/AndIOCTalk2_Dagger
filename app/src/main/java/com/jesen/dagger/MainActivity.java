package com.jesen.dagger;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.jesen.dagger.annotation.ContentView;
import com.jesen.dagger.annotation.InjectTool;
import com.jesen.dagger.demo.DaggerStudentComponent;
import com.jesen.dagger.demo.Student;

import javax.inject.Inject;


@ContentView(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    @Inject
    public Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        InjectTool.inject(this);

        // 把Activity给Dagger2
        DaggerStudentComponent.create().injectMainActivity(this);

        Log.d("Main---"," MainActivity student:" + student.hashCode());


    }
}