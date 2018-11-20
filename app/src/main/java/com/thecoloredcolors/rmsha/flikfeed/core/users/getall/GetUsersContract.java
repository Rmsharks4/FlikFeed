package com.thecoloredcolors.rmsha.flikfeed.core.users.getall;



import com.thecoloredcolors.rmsha.flikfeed.UserProxy;
import com.thecoloredcolors.rmsha.flikfeed.models.User;

import java.util.List;



public interface GetUsersContract {
    interface View {
        void onGetAllUsersSuccess(List<User> users);

        void onGetAllUsersFailure(String message);

        void onGetChatUsersSuccess(List<UserProxy> users);

        void onGetChatUsersFailure(String message);
    }

    interface Presenter {
        void getAllUsers();

        void getChatUsers();

        void OnGetChatUsersListener(List<UserProxy> users);
    }

    interface Interactor {
        void getAllUsersFromFirebase();

        void getChatUsersFromFirebase();
    }

    interface OnGetAllUsersListener {
        void onGetAllUsersSuccess(List<User> users);

        void onGetAllUsersFailure(String message);
    }

    interface OnGetChatUsersListener {
        void onGetChatUsersSuccess(List<UserProxy> users);

        void onGetChatUsersFailure(String message);
    }
}
