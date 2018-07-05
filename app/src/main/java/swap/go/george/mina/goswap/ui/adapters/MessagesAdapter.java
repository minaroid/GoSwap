package swap.go.george.mina.goswap.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import swap.go.george.mina.goswap.R;
import swap.go.george.mina.goswap.models.ChatFragmentMessages;
import swap.go.george.mina.goswap.ui.activities.conversationActivity.ConversationActivity;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MyViewHolder> implements View.OnClickListener {

    private ArrayList<ChatFragmentMessages> messages;
    private Context context;

    public MessagesAdapter(Context context, ArrayList<ChatFragmentMessages> messages) {
        this.messages = messages;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_messages, parent, false);
        return new MessagesAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.itemName.setText(messages.get(position).getItemName());
        holder.reciverName.setText(messages.get(position).getReciverName());
        try {
            Glide.with(context)
                    .setDefaultRequestOptions(new RequestOptions()
                            .error(R.drawable.camera_error))
                    .load(context.getString(R.string.base_url) + messages.get(position).getItemPic())
                    .into(holder.imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public void onClick(View v) {
        int pos = (int) v.getTag();
        Intent i = new Intent(context, ConversationActivity.class);
        i.putExtra("senderId", messages.get(pos).getSenderId());
        i.putExtra("recieverId", messages.get(pos).getReciverId());
        i.putExtra("senderName", messages.get(pos).getSenderName());
        i.putExtra("reciverName", messages.get(pos).getReciverName());
        i.putExtra("itemId", String.valueOf(messages.get(pos).getItemId()));
        i.putExtra("itemName", messages.get(pos).getItemName());
        context.startActivity(i);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_item_name_msg)
        TextView itemName;
        @BindView(R.id.tv_reciever_name)
        TextView reciverName;
        @BindView(R.id.msg_image)
        CircleImageView imageView;

        private MyViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}
