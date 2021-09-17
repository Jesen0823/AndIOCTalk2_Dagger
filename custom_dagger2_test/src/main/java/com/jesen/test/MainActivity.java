package com.jesen.test;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.jesen.custom_dagger2.annotation.Inject;
import com.jesen.test.aptout.DaggerStudentComponent;

public class MainActivity extends AppCompatActivity {

    @Inject
    public Student student;

    @Inject
    public Student student2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DaggerStudentComponent.create().inject(this);

        Log.d("Main---", "onCreate: " + student.hashCode());
    }
}
