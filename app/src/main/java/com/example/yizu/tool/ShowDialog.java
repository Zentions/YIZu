package com.example.yizu.tool;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yizu.CreateMessageActivity;
import com.example.yizu.R;

/**
 * Created by 10591 on 2017/7/27.
 */

public class ShowDialog {
        public static void  showCustomizeDialog(final Activity activity, String title, String message) {
    /* @setView 装入自定义View ==> R.layout.dialog_customize
     * 由于dialog_customize.xml只放置了一个EditView，因此和图8一样
     * dialog_customize.xml可自定义更复杂的View
     */
        AlertDialog.Builder customizeDialog =
                new AlertDialog.Builder(activity);
        final View dialogView = LayoutInflater.from(activity)
                .inflate(R.layout.create_dialog,null);
        TextView textView = (TextView)dialogView.findViewById(R.id.title_message);
        textView.setText(title);
        Button button = (Button)dialogView.findViewById(R.id.confirm);
        button.setText(message);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
        customizeDialog.setView(dialogView);
        customizeDialog.show();

    }
    public static void  showZhuceDialog(final Activity activity, String title, String rules) {
    /* @setView 装入自定义View ==> R.layout.dialog_customize
     * 由于dialog_customize.xml只放置了一个EditView，因此和图8一样
     * dialog_customize.xml可自定义更复杂的View
     */
        final AlertDialog.Builder customizeDialog =
                new AlertDialog.Builder(activity);
        final View dialogView = LayoutInflater.from(activity)
                .inflate(R.layout.zhuce_dialog,null);
        TextView textView = (TextView)dialogView.findViewById(R.id.title_message_zhuce);
        textView.setText(title);
        TextView textView1=(TextView)dialogView.findViewById(R.id.rules);
        textView1.setText(rules);
        new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                arg0.dismiss();

            }
        };
        customizeDialog.setView(dialogView);
        customizeDialog.show();

    }
    public static void showLoseDialog(final Activity activity, String title, String message) {
    /* @setView 装入自定义View ==> R.layout.dialog_customize
     * 由于dialog_customize.xml只放置了一个EditView，因此和图8一样
     * dialog_customize.xml可自定义更复杂的View
     */
        final AlertDialog.Builder customizeDialog =
                new AlertDialog.Builder(activity);
        final View dialogView = LayoutInflater.from(activity)
                .inflate(R.layout.lose_dialog,null);
        TextView textView = (TextView)dialogView.findViewById(R.id.title_message_lose);
        textView.setText(title);
        Button button = (Button)dialogView.findViewById(R.id.confirm_lose);
        button.setText(message);
        customizeDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 获取EditView中的输入内容
                        EditText edit_text =
                                (EditText) dialogView.findViewById(R.id.edit_message_lose);
                    }
                });
        customizeDialog.show();
    }
}
