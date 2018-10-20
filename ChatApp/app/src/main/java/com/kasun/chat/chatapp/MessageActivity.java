package com.kasun.chat.chatapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.github.nkzawa.emitter.Emitter;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;


public class MessageActivity extends AppCompatActivity {

    private EditText textField;
    public static final String TAG = "MessageActivity";
    public static String Username;

    private ListView messageListView;
    private MessageAdapter messageAdapter;

    //initialize a new instance of Socket.IO
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("https://socket-io-chat.now.sh/");
        } catch (URISyntaxException e) {
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Username = getIntent().getStringExtra("username");

        //make a connection
        mSocket.connect();
        //make the socket to listen 'new message' event
        mSocket.on("new message", onNewMessage);

        JSONObject userId = new JSONObject();
        try {
            userId.put("username", Username + " Connected");
            //Sending data(username) to register new user
            mSocket.emit("add user", Username);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i(TAG, "onCreate: " + Username + " " + "Connected");

        textField = findViewById(R.id.textField);
        messageListView = findViewById(R.id.messageListView);

        List<MessageFormat> messageFormatList = new ArrayList<>();
        messageAdapter = new MessageAdapter(this, R.layout.item_message, messageFormatList);
        messageListView.setAdapter(messageAdapter);

    }

    // listen 'new message' event to get other's messages
    Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    JSONObject data = (JSONObject) args[0];
                    String username, message;

                    try {
                        //extracting username and message form JSON object
                        username = data.getString("username");
                        message = data.getString("message");

                        MessageFormat format = new MessageFormat(username, message);
                        Log.i(TAG, "run: new message : " + username);
                        messageAdapter.add(format);

                    } catch (Exception e) {
                        return;
                    }
                }
            });
        }
    };

    //send our new message to server
    public void sendMessage(View view) {

        String message = textField.getText().toString().trim();
        if (TextUtils.isEmpty(message)) {
            return;
        }
        textField.setText("");
        //send message
        mSocket.emit("new message", message);
        Log.i(TAG, "sendMessage: " + message);

        MessageFormat format = new MessageFormat(Username, message);
        messageAdapter.add(format);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //when activity destroy, disconnect the socket connection
        if (isFinishing()) {
            Log.i(TAG, "onDestroy: ");

            JSONObject userId = new JSONObject();
            try {
                userId.put("username", Username);
                //disconnect the user
                mSocket.emit("disconnect", userId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //disconnect the socket connection
            mSocket.disconnect();
            //unregister listener event
            mSocket.off("new message", onNewMessage);

            Username = "";
            messageAdapter.clear();
        } else {
            Log.i(TAG, "onDestroy: is rotating.....");
        }

    }

}
