package com.thecoloredcolors.rmsha.flikfeed.Helpers;

import com.google.firebase.firestore.FirebaseFirestore;
import com.thecoloredcolors.rmsha.flikfeed.Classes.FollowingClass;
import com.thecoloredcolors.rmsha.flikfeed.Classes.PostSaveClass;
import com.thecoloredcolors.rmsha.flikfeed.Classes.User.UserClass;
import java.sql.Timestamp;

/**
 * Created by rmsha on 11/9/2017.
 */

public class FireBaseHelper {

    private static FireBaseHelper firebase;

    private FirebaseFirestore DataBase;

    private FireBaseHelper() {
        DataBase = FirebaseFirestore.getInstance();
    }

    public FirebaseFirestore getDataBase() {
        return DataBase;
    }

    public static FireBaseHelper GetInstance() {
        if(firebase==null) {
            firebase = new FireBaseHelper();
        }
        return firebase;
    }

    public void AddNewUser(final UserClass user) {
        DataBase.collection("users")
                .document(user.getUserid())
                .set(user);
    }

    public void RegisterPOSTSave(String userid, String postid) {
        PostSaveClass map = new PostSaveClass(userid,postid,new Timestamp(System.currentTimeMillis() / 1000).toString());
        DataBase.collection("saved-posts")
                .document(userid+postid)
                .set(map);
    }

    public void RegisterPOSTUnsave(String userid, String postid) {
        DataBase.collection("saved-posts")
                .document(userid+postid)
                .delete();
    }

    public void RegisterFollow(String user1id, String user2id) {
        FollowingClass map = new FollowingClass(user1id,user2id);
        DataBase.collection("following")
                .document(user1id+user2id)
                .set(map);
    }

    public void RegisterUnfollow(String user1id, String user2id) {
        DataBase.collection("following")
                .document(user1id+user2id)
                .delete();
    }

}
