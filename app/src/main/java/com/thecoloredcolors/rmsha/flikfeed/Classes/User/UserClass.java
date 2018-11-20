package com.thecoloredcolors.rmsha.flikfeed.Classes.User;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;
import com.thecoloredcolors.rmsha.flikfeed.Helpers.FireBaseHelper;
import com.thecoloredcolors.rmsha.flikfeed.Classes.User.UserStates.UserState;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by rmsha on 10/22/2017.
 */

public class UserClass extends SugarRecord implements Serializable {
    @Unique
    private String userid;
    private String username;
    private String profilepic;
    private String password;
    private String name;
    private String email;
    private int gender;
    private String coverpic;
    private String phonenumber;
    private Date dateofbirth;
    private String address;
    private UserState state;
    private int privacy;
    private boolean hasstories;
    /*private List<StoryActivity> stories;
    private List<PostInterface> homescreenposts;
    private List<PostInterface> discoverposts;
    private List<UserProxyClass> followers;
    private List<UserProxyClass> following;
    private List<PostInterface> saved;
    private List<PostInterface> tagged;
    private List<Integer> linkedaccounts;
    private List<SearchInterface> searchhistory;
    private List<notificationInterface> notifications;*/
    private Date lastonline;
    private Date lastviewnotifications;

    public UserClass(String userID, String username, String password, String name, String email, int gender) {
        this.userid = userID;
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.gender = gender;
        hasstories = false;
    }

    public UserClass(String userID, String username, String password, String name, String email, int gender, String profilepic) {
        this.userid = userID;
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.profilepic = profilepic;
        hasstories = false;
    }

    public UserProxyClass UserProxy() {
        return new UserProxyClass(userid,username,name,profilepic,hasstories);
    }

    public UserClass() {

    }

    public void RegisterNewUser() {
        FireBaseHelper.GetInstance().AddNewUser(this);
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public Date getDateofbirth() {
        return dateofbirth;
    }

    public void setDateofbirth(Date dateofbirth) {
        this.dateofbirth = dateofbirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCoverpic() {
        return coverpic;
    }

    public void setCoverpic(String coverpic) {
        this.coverpic = coverpic;
    }

    public UserState getState() {
        return state;
    }

    public void setState(UserState state) {
        this.state = state;
    }

    public Date getLastonline() {
        return lastonline;
    }

    public void setLastonline(Date lastonline) {
        this.lastonline = lastonline;
    }

    public Date getLastviewnotifications() {
        return lastviewnotifications;
    }

    public void setLastviewnotifications(Date lastviewnotifications) {
        this.lastviewnotifications = lastviewnotifications;
    }

    public int getPrivacy() {
        return privacy;
    }

    public void setPrivacy(int privacy) {
        this.privacy = privacy;
    }

    public boolean isHasstories() {
        return hasstories;
    }

    public void setHasstories(boolean hasstories) {
        this.hasstories = hasstories;
    }

}
