package com.example.yizu;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yizu.bean.User;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class UserMessageActivity extends AppCompatActivity {

    User user;
    TextView NAME;
    TextView GRADE;
    TextView GENDER;
    ImageView USERIMAGE;
    ImageButton GoName;
    ImageButton GoGender;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_message);
        final Toolbar toolbar = (Toolbar)findViewById(R.id.userToolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        NAME=(TextView)findViewById(R.id.name);
        GRADE=(TextView)findViewById(R.id.grade);
        GENDER=(TextView)findViewById(R.id.gender);
        USERIMAGE=(ImageView)findViewById(R.id.userimage);
        GoGender = (ImageButton)findViewById(R.id.goGender);
        GoName = (ImageButton)findViewById(R.id.goName);
        button= (Button)findViewById(R.id.exit_user_message);
        final Intent intent = getIntent();
        user = (User)intent.getSerializableExtra("user");
        show();
        GoName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(UserMessageActivity.this,ChangeNameActivity.class);
                startActivityForResult(intent1, 1);
            }
        });
        GoGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(UserMessageActivity.this)
                        .setTitle("请选择")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setSingleChoiceItems(new String[] {"男","女"}, -1,
                                new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int which) {
                                        if(which==0){
                                            GENDER.setText("男");
                                            user.setGender("男");
                                        }else{
                                            GENDER.setText("女");
                                            user.setGender("女");
                                        }
                                        SaveUserMessage();
                                        dialog.dismiss();
                                    }
                                }
                        )
                        .show();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(UserMessageActivity.this,RegisterActivity.class);
                startActivity(intent3);
                finish();
            }
        });
    }
    void show(){
        NAME.setText(user.getName());
        GRADE.setText(user.getGrade().toString());
        GENDER.setText(user.getGender());
        // Glide.with(this).load(user.getTouXiang()).into(USERIMAGE);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if(resultCode==RESULT_OK){
                    String newName = data.getStringExtra("ReName");
                    NAME.setText(newName);
                    user.setName(newName);
                    SaveUserMessage();
                }
                break;
            default:;
        }
    }
    void SaveUserMessage(){
        user.update(user.getObjectId(), new UpdateListener() {

            @Override
            public void done(BmobException e) {
                if(e==null){
                    Toast.makeText(UserMessageActivity.this,"更新成功",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(UserMessageActivity.this,"更新失败",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    void BuildDialog(){

    }
}

