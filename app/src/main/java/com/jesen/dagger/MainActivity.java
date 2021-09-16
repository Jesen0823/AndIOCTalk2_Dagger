package com.jesen.dagger;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.jesen.dagger.annotation.MeBindView;
import com.jesen.dagger.annotation.MeContentView;
import com.jesen.dagger.annotation.InjectTool;
import com.jesen.dagger.demo.DaggerStudentComponent;
import com.jesen.dagger.demo.Student;

import javax.inject.Inject;


@MeContentView(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    @MeBindView(R.id.btn_test)
    Button button;

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