package com.example.rtmessagingapptutorial;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MessageViewHolder> {
    private List<Message> mMessages;
    private Context mContext;
    private String mUserId;

    private static final int MESSAGE_OUTGOING = 123;
    private static final int MESSAGE_INCOMING = 321;

    public ChatAdapter(Context context, String userId, List<Message> messages) {
        this.mMessages = messages;
        this.mUserId = userId;
        this.mContext = context;
    }

    @Override
    public int getItemCount() {
        return this.mMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (isMe(position)) {
            return MESSAGE_OUTGOING;
        } else {
            return MESSAGE_INCOMING;
        }
    }

    private boolean isMe(int position) {
        Message message = this.mMessages.get(position);
        return message.name != null && message.name.equals(this.mUserId);
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        if (viewType == MESSAGE_INCOMING) {
            View contactView = inflater.inflate(R.layout.message_incoming, parent, false);
            return new IncomingMessageViewHolder(contactView);
        } else if (viewType == MESSAGE_OUTGOING) {
            View contactView = inflater.inflate(R.layout.message_outgoing, parent, false);
            return new OutgoingMessageViewHolder(contactView);
        } else {
            throw new IllegalArgumentException("Unknown view type");
        }
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        Message message = this.mMessages.get(position);
        holder.bindMessage(message);
    }

    public abstract class MessageViewHolder extends RecyclerView.ViewHolder {
        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
        }
        abstract void bindMessage(Message message);
    }

    public class IncomingMessageViewHolder extends MessageViewHolder {
        TextView body;
        TextView name;

        public IncomingMessageViewHolder(View itemView) {
            super(itemView);
            body = (TextView)itemView.findViewById(R.id.tvIncBody);
            name = (TextView)itemView.findViewById(R.id.tvIncName);
        }

        @Override
        public void bindMessage(Message message) {
            body.setText(message.text);
            name.setText(message.name);
        }
    }

    public class OutgoingMessageViewHolder extends MessageViewHolder {
        TextView body;
        TextView name;

        public OutgoingMessageViewHolder(View itemView) {
            super(itemView);
            body = (TextView)itemView.findViewById(R.id.tvBody);
            name = (TextView)itemView.findViewById(R.id.tvName);
        }

        @Override
        public void bindMessage(Message message) {
            body.setText(message.text);
            name.setText(message.name);
        }
    }
}
