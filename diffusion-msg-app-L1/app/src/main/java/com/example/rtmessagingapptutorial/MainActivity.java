package com.example.rtmessagingapptutorial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /** Called when the user taps the Send button */
    public void startApp(View view) {
        Intent intent = new Intent(this, ChatActivity.class);

        EditText userName = (EditText)findViewById(R.id.userName);
        intent.putExtra("userName", userName.getText().toString());

        EditText userPassword = (EditText)findViewById(R.id.userPassword);
        intent.putExtra("password", userPassword.getText().toString());

        EditText diffusionService = (EditText)findViewById(R.id.diffusionService);
        intent.putExtra("diffusionService", diffusionService.getText().toString());

        EditText chatRoom = (EditText)findViewById(R.id.chatRoom);
        intent.putExtra("chatRoom", chatRoom.getText().toString());

        startActivity(intent);
    }

}