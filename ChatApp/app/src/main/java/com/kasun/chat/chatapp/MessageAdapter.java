package com.kasun.chat.chatapp;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class MessageAdapter extends ArrayAdapter<MessageFormat> {
    public MessageAdapter(Context context, int resource, List<MessageFormat> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MessageFormat message = getItem(position);

        if (message.getUsername().equals(MessageActivity.Username)) {
            //use 'my_message' layout if that message is mine
            Log.i(MessageActivity.TAG, "getView: my message");
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.my_message, parent, false);
            TextView messageText = convertView.findViewById(R.id.message_body);
            messageText.setText(message.getMessage());

        } else {
            //use 'their_message' layout if that message is not mine
            Log.i(MessageActivity.TAG, "getView: other message");
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.their_message, parent, false);

            TextView messageText = convertView.findViewById(R.id.message_body);
            TextView usernameText = convertView.findViewById(R.id.name);

            messageText.setText(message.getMessage());
            usernameText.setText(message.getUsername());
        }

        return convertView;
    }
}
