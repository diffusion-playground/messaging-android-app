package com.example.rtmessagingapptutorial;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pushtechnology.diffusion.client.Diffusion;
import com.pushtechnology.diffusion.client.callbacks.ErrorReason;
import com.pushtechnology.diffusion.client.features.TopicUpdate;
import com.pushtechnology.diffusion.client.features.Topics;
import com.pushtechnology.diffusion.client.features.control.topics.TopicControl;
import com.pushtechnology.diffusion.client.session.Session;
import com.pushtechnology.diffusion.client.session.SessionFactory;
import com.pushtechnology.diffusion.client.topics.details.TopicSpecification;
import com.pushtechnology.diffusion.client.topics.details.TopicType;
import com.pushtechnology.diffusion.datatype.json.JSON;
import com.pushtechnology.diffusion.datatype.json.JSONDataType;
import com.pushtechnology.repackaged.jackson.core.JsonProcessingException;
import com.pushtechnology.repackaged.jackson.databind.ObjectMapper;

import java.util.ArrayList;

import java8.util.concurrent.CompletableFuture;

public class ChatActivity extends AppCompatActivity {
    private SessionHandler sessionHandler = null;
    RecyclerView rvChat;
    ArrayList<Message> mMessages;
    ChatAdapter mAdapter;
    String diffusionService = "";
    String chatRoom = "";
    String userName = "";
    String password = "";

    protected String getStringExtraDefault(String name, String defaultString) {
        String value = getIntent().getStringExtra(name);
        if (value == null) {
            return defaultString;
        }
        return value;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        this.diffusionService = getStringExtraDefault("diffusionService", "diffusionchatapp.eu.diffusion.cloud");
        this.chatRoom = "Chat/" + getStringExtraDefault("chatRoom", "Default room");
        this.userName = getStringExtraDefault("userName", "aUser");
        this.password = getStringExtraDefault("password", "");

        // Setup and add Message posting listener
        setupMessagePosting();

        if (this.sessionHandler == null) {
            // Here's where we instantiate the SessionHandler class, where we will initialize it
            this.sessionHandler = new SessionHandler();

            //Pass important data to the session handler to interact with the Activity
            this.sessionHandler.setChatRoomName(this.chatRoom);
            this.sessionHandler.setMessagesView(this.rvChat, this.mMessages, this.mAdapter);

            // Starting Difussion Session
            Diffusion.sessions()
                    .principal(this.userName)
                    .password(this.password)
                    // And we pass the sessionHandler instance to the session.
                    .open("ws://" + this.diffusionService, this.sessionHandler);
        }
    }

    @Override
    protected void onDestroy() {
        // Destroy the Session Handler
        if ( this.sessionHandler != null ) {
            this.sessionHandler.close();
            this.sessionHandler = null;
        }
        super.onDestroy();
    }

    // Setup button event handler which posts the entered message to Parse
    void setupMessagePosting() {
        // Get references to the text field and button
        EditText etMessage = (EditText) findViewById(R.id.etMessage);
        Button btSend = (Button) findViewById(R.id.btSend);

        // More definitions and references needed to implement the chat messages section
        this.rvChat = (RecyclerView) findViewById(R.id.rvChat);
        this.mMessages = new ArrayList<>();
        final String userId = this.userName;
        this.mAdapter = new ChatAdapter(ChatActivity.this, userId, this.mMessages);
        this.rvChat.setAdapter(this.mAdapter);

        // associate the LayoutManager with the RecylcerView
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChatActivity.this);
        this.rvChat.setLayoutManager(linearLayoutManager);

        // When send button is clicked, create message object
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = etMessage.getText().toString();
                sendMessage(data);
                etMessage.setText(null);
            }
        });
    }

    /**
     * We send message using our sessionHandler and Topic
     * @param data
     */
    protected void sendMessage(String data) {
        final JSONDataType jsonDataType = Diffusion.dataTypes().json();

        // Concatenate it into a JSON string
        StringBuilder builder = new StringBuilder();
        builder.append("{\"name\" : \"" + this.userName + "\",  \"text\" : \"");
        builder.append(data);
        builder.append("\"}");

        // Convert it to a JSON value
        final JSON value = jsonDataType.fromJsonString(builder.toString());

        // Now we send the message
        final CompletableFuture<?> result = this.sessionHandler.
                getSession().feature(TopicUpdate.class).
                set(this.sessionHandler.getSessionTopicName(), JSON.class, value);
    }

    /**
     * This class responsibility is to handle the session we created in the constructor of this Activity
     */
    private class SessionHandler implements SessionFactory.OpenCallback {
        private Session session = null;
        /* We set the name of the TOPIC */
        private String chatRoomName = "chat/room1";

        RecyclerView rvChat;
        ArrayList<Message> mMessages;
        ChatAdapter mAdapter;

        public void setChatRoomName(String chatRoomName) {
            this.chatRoomName = chatRoomName;
        }

        public void setMessagesView(RecyclerView rvChat, ArrayList<Message> mMessages, ChatAdapter mAdapter) {
            this.rvChat = rvChat;
            this.mMessages = mMessages;
            this.mAdapter = mAdapter;
        }

        /**
         * This function is called when the session is Opened
         * In this function we create the topic, and subscribe to it.
         * Subscribing to it will allow us to listen to everything streamed into the Topic's channel
         * @param session This is the session we created in the Activity's constructor
         */
        @Override
        public void onOpened(Session session) {
            this.session = session;

            // Here is where we add the topic to the session
            this.session.feature(TopicControl.class).addTopic(
                    this.chatRoomName,
                    TopicType.JSON);

            sendMessage("Welcome to the App");

            // Attach a Stream to listen for updates
            this.session.feature(Topics.class).addStream(this.chatRoomName, JSON.class, new Topics.ValueStream.Default<JSON>() {
                /**
                 * Function to listen to the result of the Subscription to the topic
                 * @param topicPath
                 * @param specification
                 */
                @Override
                public void onSubscription(String topicPath, TopicSpecification specification) {
                    Log.i("DIFUSSION", "Subscribed to: " + topicPath);
                }

                /**
                 * This function gets called when new data is read from the Topic's channel
                 * @param topicPath
                 * @param topicSpec
                 * @param oldValue
                 * @param newValue
                 */
                @Override
                public void onValue(String topicPath, TopicSpecification topicSpec, JSON oldValue, JSON newValue) {
                    System.out.println("New value for" + topicPath + ": " + newValue.toJsonString());
                    try {
                        receiveMessage(newValue);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                }
            });

            // And we finally subscribe to the topic
            this.session.feature(Topics.class).subscribe(this.chatRoomName);

        }

        @Override
        public void onError(ErrorReason errorReason) {
            System.out.println("*************Error in Session: " + errorReason.toString());
        }

        public Session getSession() {
            return this.session;
        }

        public String getSessionTopicName() {
            return this.chatRoomName;
        }

        public void close() {
            if ( this.session != null ) {
                this.session.close();
            }
        }

        /**
         * Function to handle message reception and display in the messages list
         * @param data
         * @throws JsonProcessingException
         */
        public void receiveMessage(JSON data) throws JsonProcessingException {
            ObjectMapper mapper = new ObjectMapper();
            Message newMessage = mapper.readValue(data.toJsonString(), Message.class);
            this.mMessages.add(newMessage);
            this.mAdapter.notifyDataSetChanged(); // update adapter
            this.rvChat.scrollToPosition(0);
            this.rvChat.scrollBy(0,1);
        }

    }


}