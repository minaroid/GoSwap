package swap.go.george.mina.goswap.ui.fragments.chatFragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import swap.go.george.mina.goswap.R;
import swap.go.george.mina.goswap.models.ChatFragmentMessages;
import swap.go.george.mina.goswap.ui.adapters.MessagesAdapter;

import static android.content.Context.MODE_PRIVATE;

public class ChatFragment extends Fragment{

    @BindView(R.id.rv_chats)
    RecyclerView recyclerView;
    @BindView(R.id.tv_chat_empty)
    TextView emptyList;
    @BindView(R.id.progress_chat_loading)
    ProgressBar progressBar;
    String itemId;
    String itemName;
    String sender;
    String reciever;
    String senderName;
    String reciverName;

    private SharedPreferences userEditor;
    private ArrayList<ChatFragmentMessages> messages = new ArrayList<>();



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chat, container, false);
        ButterKnife.bind(this, v);


        userEditor = getActivity().getSharedPreferences("user", MODE_PRIVATE);
        if (userEditor.getString("id", null) != null) {
            progressBar.setVisibility(View.VISIBLE);
        FirebaseDatabase.getInstance().getReference()
                .child("private")

                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        messages.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            itemId = snapshot.getKey();
                            for (DataSnapshot snapshot1 : snapshot.getChildren()) {

                                if (snapshot1.getKey().toString().contains("-" + userEditor.getString("id", null))) {
                                    for (DataSnapshot snapshot3 : snapshot1.getChildren()) {
                                        reciverName = snapshot3.child("messageReciever").getValue().toString();
                                        reciever = snapshot1.getKey().toString().replace("-" + userEditor.getString("id", null), "");
                                        for (DataSnapshot snapshot2 : snapshot.child("itemName").getChildren()) {
                                            itemName = snapshot2.getValue().toString();
                                        }
                                        messages.add(new ChatFragmentMessages(userEditor.getString("id", null)
                                                , reciever, userEditor.getString("name", null)
                                                , reciverName, itemId, itemName));
                                        Log.d("dddddcdc", reciverName);
                                        break;
                                    }
                                }
                            }
                        }
                        progressBar.setVisibility(View.GONE);
                        if (messages.size() == 0) {
                            emptyList.setVisibility(View.VISIBLE);
                        } else {
                            emptyList.setVisibility(View.GONE);
                            recyclerView.setAdapter(new MessagesAdapter(getContext(), messages));
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        } else {
            Toast.makeText(getContext(), R.string.msg_must_login, Toast.LENGTH_SHORT).show();
            emptyList.setVisibility(View.VISIBLE);
        }

        return v;
    }
}
