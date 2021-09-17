package com.jesen.dagger;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.jesen.dagger.annotation.Click;
import com.jesen.dagger.annotation.MeBindView;
import com.jesen.dagger.annotation.MeContentView;
import com.jesen.dagger.annotation.InjectTool;
import com.jesen.dagger.annotation.jianrong.OnClickCommon;
import com.jesen.dagger.annotation.jianrong.OnClickLongCommon;
import com.jesen.dagger.demo.DaggerStudentComponent;
import com.jesen.dagger.demo.Student;

import javax.inject.Inject;


@MeContentView(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    @MeBindView(R.id.btn_test1)
    Button btnTest1;

    @MeBindView(R.id.btn_test2)
    Button btnTest2;

    @MeBindView(R.id.btn_test3)
    Button btnTest3;

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

        btnTest1.setText("test1 注入成功");

    }

    @Click(R.id.btn_test1)
    private void show() {
        Toast.makeText(this, "show is test1", Toast.LENGTH_SHORT).show();
    }

    @OnClickCommon(R.id.btn_test2)
    private  void test2(){
        Toast.makeText(this, "onClickCommon", Toast.LENGTH_SHORT).show();
    }

    @OnClickLongCommon(R.id.btn_test3)
    private void test3(){
        Toast.makeText(this, "onLongClickCommon", Toast.LENGTH_SHORT).show();
    }
}