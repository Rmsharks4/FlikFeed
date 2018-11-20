package com.thecoloredcolors.rmsha.flikfeed.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thecoloredcolors.rmsha.flikfeed.Helpers.FireBaseHelper;
import com.thecoloredcolors.rmsha.flikfeed.R;
import com.thecoloredcolors.rmsha.flikfeed.models.Chat;
import com.thecoloredcolors.rmsha.flikfeed.models.User;
import com.thecoloredcolors.rmsha.flikfeed.utils.Constants;

import java.util.List;



public class UserListingRecyclerAdapter extends RecyclerView.Adapter<UserListingRecyclerAdapter.ViewHolder> {
    private List<User> mUsers;
    FireBaseHelper fbh;

    public UserListingRecyclerAdapter(List<User> users) {
        this.mUsers = users;
    }

    public void add(User user) {
        mUsers.add(user);
        notifyItemInserted(mUsers.size() - 1);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_user_listing, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        User user = mUsers.get(position);

        if (user.email != null) {
            String alphabet = user.email.substring(0, 1);

            String username = "";

            //fbh.getDataBase().collection("users").whereEqualTo("email",user.email);

            holder.txtUsername.setText(user.email);
            holder.txtUserAlphabet.setText(alphabet);

            final String[] msg = {""};

            final String id = FirebaseAuth.getInstance().getCurrentUser().getUid() + user.uid;
            final String id2 = user.uid + FirebaseAuth.getInstance().getCurrentUser().getUid();

            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

            databaseReference.child(Constants.ARG_CHAT_ROOMS).child(id).orderByValue().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Chat chat = dataSnapshot.getValue(Chat.class);
                    if (chat!=null)
                    {
                        msg[0] = (chat.message);
                    }
                    else
                        msg[0] = "wfwf";
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            holder.lastmsg.setText(msg[0]);

        }
    }

    @Override
    public int getItemCount() {
        if (mUsers != null) {
            return mUsers.size();
        }
        return 0;
    }

    public User getUser(int position) {
        return mUsers.get(position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtUserAlphabet, txtUsername, lastmsg;

        ViewHolder(View itemView) {
            super(itemView);
            txtUserAlphabet = (TextView) itemView.findViewById(R.id.text_view_user_alphabet);
            txtUsername = (TextView) itemView.findViewById(R.id.text_view_username);
            lastmsg = (TextView) itemView.findViewById(R.id.text_view_lastmsg);
        }
    }
}
