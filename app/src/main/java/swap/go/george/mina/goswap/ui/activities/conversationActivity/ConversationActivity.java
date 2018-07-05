package swap.go.george.mina.goswap.ui.activities.conversationActivity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import swap.go.george.mina.goswap.R;
import swap.go.george.mina.goswap.models.ChatMessage;

public class ConversationActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.fab_send)
    FloatingActionButton fabSend;
    @BindView(R.id.ed_message)
    EditText messageText;
    @BindView(R.id.list_of_messages)
    ListView listView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.empty_view)
    TextView emptyView;

    private FirebaseListAdapter<ChatMessage> adapter;
    private String senderId, recieverId,
            senderName, reciverName, itemId, itemName, itemPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        ButterKnife.bind(this);

        senderId = getIntent().getStringExtra("senderId");
        recieverId = getIntent().getStringExtra("recieverId");
        senderName = getIntent().getStringExtra("senderName");
        reciverName = getIntent().getStringExtra("reciverName");
        itemId = getIntent().getStringExtra("itemId");
        itemName = getIntent().getStringExtra("itemName");
        if (getIntent().getStringExtra("itemPic") != null) {
            itemPic = getIntent().getStringExtra("itemPic");
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Conversation");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        displayChatMessages();

    }

    private void displayChatMessages() {

        adapter = new FirebaseListAdapter<ChatMessage>(this, ChatMessage.class,
                R.layout.item_message, FirebaseDatabase.getInstance().getReference()
                .child("private").child(itemId).child(senderId + "-" + recieverId)) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {

                TextView messageText = v.findViewById(R.id.message_text);
                TextView messageUser = v.findViewById(R.id.message_user);
                TextView messageTime = v.findViewById(R.id.message_time);
                RelativeLayout layout = v.findViewById(R.id.layout);


                if (model.getMessageSender().equals(senderName)) {
                    layout.setBackgroundResource(R.drawable.rectangle);
                    messageText.setText(model.getMessageText());
                    messageUser.setText("you");

                } else {
                    layout.setBackgroundResource(R.drawable.rectangle2);
                    messageText.setText(model.getMessageText());
                    messageUser.setText(model.getMessageSender());
                }

                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                        model.getMessageTime()));
            }
        };

        listView.post(new Runnable() {
            @Override
            public void run() {
                listView.smoothScrollToPosition(listView.getCount() - 1);
            }
        });
        if (adapter.getCount() == 0) {
            emptyView.setVisibility(View.VISIBLE);
        } else {
            emptyView.setVisibility(View.GONE);
        }
        listView.setEmptyView(emptyView);
        listView.setAdapter(adapter);
    }

    @OnClick({R.id.fab_send})
    @Override
    public void onClick(View v) {

        switch ((v.getId())) {

            case R.id.fab_send:
                FirebaseDatabase.getInstance().getReference().child("private").child(itemId).child(senderId + "-" + recieverId).
                        push().setValue(new ChatMessage(messageText.getText().toString(),
                        senderName, senderName));

                FirebaseDatabase.getInstance().getReference().child("private").child(itemId).child(recieverId + "-" + senderId).
                        push().setValue(new ChatMessage(messageText.getText().toString(),
                        senderName, reciverName));

                FirebaseDatabase.getInstance().getReference()
                        .child("private")
                        .child(itemId)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                if (!dataSnapshot.hasChild("itemName")) {
                                    FirebaseDatabase.getInstance().getReference().child("private").child(itemId)
                                            .child("itemName").push().setValue(itemName);
                                    FirebaseDatabase.getInstance().getReference().child("private").child(itemId)
                                            .child("itemPic").push().setValue(itemPic);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                messageText.setText("");
                break;
        }
    }

}
