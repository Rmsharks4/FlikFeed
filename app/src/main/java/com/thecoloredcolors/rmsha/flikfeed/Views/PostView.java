package com.thecoloredcolors.rmsha.flikfeed.Views;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Priority;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.firestore.WriteBatch;
import com.nightonke.boommenu.BoomButtons.BoomButton;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.TextOutsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.OnBoomListener;
import com.thecoloredcolors.rmsha.flikfeed.Classes.CommentClass;
import com.thecoloredcolors.rmsha.flikfeed.Classes.CommentLikeClass;
import com.thecoloredcolors.rmsha.flikfeed.Classes.NotificationClass;
import com.thecoloredcolors.rmsha.flikfeed.Classes.PostClass;
import com.thecoloredcolors.rmsha.flikfeed.Classes.PostLikeClass;
import com.thecoloredcolors.rmsha.flikfeed.Classes.PostProxyClass;
import com.thecoloredcolors.rmsha.flikfeed.Classes.PostResourceClass;
import com.thecoloredcolors.rmsha.flikfeed.Classes.User.UserClass;
import com.thecoloredcolors.rmsha.flikfeed.Classes.User.UserProxyClass;
import com.thecoloredcolors.rmsha.flikfeed.Fragments.LikesPageFragment;
import com.thecoloredcolors.rmsha.flikfeed.Fragments.NotificationsFragment;
import com.thecoloredcolors.rmsha.flikfeed.Fragments.UserProfileFragment;
import com.thecoloredcolors.rmsha.flikfeed.GlideApp;
import com.thecoloredcolors.rmsha.flikfeed.Helpers.FireBaseHelper;
import com.thecoloredcolors.rmsha.flikfeed.R;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.glide.transformations.BlurTransformation;
import me.itangqi.waveloadingview.WaveLoadingView;

import static com.thecoloredcolors.rmsha.flikfeed.GlideOptions.bitmapTransform;

/**
 * Created by rmsha on 11/22/2017.
 */

public class PostView extends RelativeLayout {

    private FragmentManager fragmentManager;
    private UserProxyClass currentUser;
    private FireBaseHelper fireBaseHelper = FireBaseHelper.GetInstance();
    private PostClass post;
    private PostProxyClass currentpost;

    private boolean LIKE_STATUS = false;
    private boolean SAVE_STATUS = false;
    private boolean FOLLOW_STATUS = false;
    private boolean SET = false;

    public PostView(Context context) {
        super(context);
        Initialize(context);
    }

    public PostView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Initialize(context);
    }

    public PostView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Initialize(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PostView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        Initialize(context);
    }

    public void SetArguments(String postID, UserProxyClass currentUser, FragmentManager fragmentManager) {
        if(!SET) {
            this.currentUser = currentUser;
            this.fragmentManager = fragmentManager;
            LoadPost(postID, currentUser.getUserid());
        } else {
            Initialize(getContext());
        }
    }

    public void SetArguments(PostClass post, UserProxyClass currentUser, FragmentManager fragmentManager) {
        if(!SET) {
            this.currentUser = currentUser;
            this.fragmentManager = fragmentManager;
            this.post = post;
            LoadPost(currentUser.getUserid());
        } else {
            Initialize(getContext());
        }
    }

    private void LoadPost(final String userid) {
        fireBaseHelper.getDataBase().runTransaction(new Transaction.Function<Boolean>() {
            @Nullable
            @Override
            public Boolean apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                DocumentReference u = fireBaseHelper
                        .getDataBase()
                        .collection("users")
                        .document(post.getUserid());
                post.loadPosteduser(transaction.get(u).toObject(UserClass.class).UserProxy());
                DocumentReference l = fireBaseHelper
                        .getDataBase()
                        .collection("post-likes")
                        .document(userid+post.getPostid());
                LIKE_STATUS = transaction.get(l).exists();
                DocumentReference s = fireBaseHelper
                        .getDataBase()
                        .collection("saved-posts")
                        .document(userid+post.getPostid());
                SAVE_STATUS = transaction.get(s).exists();
                DocumentReference f = fireBaseHelper
                        .getDataBase()
                        .collection("following")
                        .document(userid+post.getUserid());
                FOLLOW_STATUS = transaction.get(f).exists();
                return true;
            }
        }).addOnCompleteListener(new OnCompleteListener<Boolean>() {
            @Override
            public void onComplete(@NonNull Task<Boolean> task) {
                if(post!=null){
                    currentpost = post.PostProxy();
                    SET = true;
                    Initialize(getContext());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if(post==null)
                    Toast.makeText(getContext(),"No internet connection!",Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void LoadPost(final String postID, final String userID){
        fireBaseHelper.getDataBase().runTransaction(new Transaction.Function<Boolean>() {
            @Nullable
            @Override
            public Boolean apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                DocumentReference d = fireBaseHelper
                        .getDataBase()
                        .collection("posts")
                        .document(postID);
                post = transaction.get(d).toObject(PostClass.class);
                DocumentReference u = fireBaseHelper
                        .getDataBase()
                        .collection("users")
                        .document(post.getUserid());
                post.loadPosteduser(transaction.get(u).toObject(UserClass.class).UserProxy());
                DocumentReference l = fireBaseHelper
                        .getDataBase()
                        .collection("post-likes")
                        .document(userID+post.getPostid());
                LIKE_STATUS = transaction.get(l).exists();
                DocumentReference s = fireBaseHelper
                        .getDataBase()
                        .collection("saved-posts")
                        .document(userID+post.getPostid());
                SAVE_STATUS = transaction.get(s).exists();
                DocumentReference f = fireBaseHelper
                        .getDataBase()
                        .collection("following")
                        .document(userID+post.getUserid());
                FOLLOW_STATUS = transaction.get(f).exists();
                return true;
            }
        }).addOnCompleteListener(new OnCompleteListener<Boolean>() {
            @Override
            public void onComplete(@NonNull Task<Boolean> task) {
                if(post!=null){
                    currentpost = post.PostProxy();
                    SET = true;
                    Initialize(getContext());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if(post==null)
                    Toast.makeText(getContext(),"No internet connection!",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void Initialize(Context context) {
        if(!SET) {
            final View view = inflate(context, R.layout.loadpost,this);
            WaveLoadingView wave = view.findViewById(R.id.waveLoadingView);
            wave.startAnimation();
            wave.setAnimDuration(3000);
        }
        else {

            final View view = inflate(context, R.layout.layout_post,this);

            //1. VIEW PAGER - DONE!!

            final ViewPager postpicpager = (ViewPager) view.findViewById(R.id.postviewpager);
            PostViewPagerAdapter postViewPagerAdapter = new PostViewPagerAdapter();
            postViewPagerAdapter.setPostResources(post.getResources());
            postpicpager.setAdapter(postViewPagerAdapter);

            //2. DOTS - DONE!!

            LinearLayout sliderdots = (LinearLayout) view.findViewById(R.id.sliderdots);
            final int dotscount = post.getResources().size();
            if (dotscount > 1) {
                final ImageView[] dots = new ImageView[dotscount];
                for (int i = 0; i < dotscount; i++) {
                    dots[i] = new ImageView(getContext());
                    dots[i].setImageResource(R.drawable.nonactive_dot);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(8, 0, 8, 0);
                    sliderdots.addView(dots[i], params);
                }
                dots[0].setImageResource(R.drawable.active_dot);
                postpicpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        for (int i = 0; i < dotscount; i++) {
                            dots[i].setImageResource(R.drawable.nonactive_dot);
                        }
                        dots[position].setImageResource(R.drawable.active_dot);
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
            }

            //3. STORY - DONE!!

            final ImageView story = (ImageView) view.findViewById(R.id.storyview);

            RotateAnimation rotateAnimation = new RotateAnimation(0, 360f,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);

            if (post.Posteduser().isHasstories()) {
                story.setVisibility(VISIBLE);

                rotateAnimation.setInterpolator(new LinearInterpolator());
                rotateAnimation.setDuration(2000);
                rotateAnimation.setRepeatCount(Animation.INFINITE);

                story.startAnimation(rotateAnimation);
            } else {
                story.setVisibility(GONE);
            }

            //4. PROFILE PICTURE - DONE!!...

            ImageView profilePic = (ImageView) view.findViewById(R.id.profilepic);

            if(post.Posteduser().getProfilepic()!=null) {
                GlideApp
                        .with(getContext())
                        .asBitmap()
                        .load(post.Posteduser().getProfilepic())
                        .circleCrop()
                        .placeholder(R.drawable.user_pink)
                        .into(profilePic);
            } else profilePic.setImageResource(R.drawable.user_pink);

            profilePic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!post.Posteduser().isHasstories())
                        UserProfileClick(post.Posteduser().getUserid());
                    else {
                        story.clearAnimation();

                        //LEFTTTTT....

                        //Code for stories here ...
                    }
                }
            });

            //6. BOOM BUTTON

            final BoomMenuButton bmb = (BoomMenuButton) view.findViewById(R.id.bmb);
            InitializeBMB(bmb, view);

            //8. COMMENT BUTTON

            final ImageButton commentButton = (ImageButton) view.findViewById(R.id.comment);
            commentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommentButtonClick(view);
                }
            });

            //9. TAG BUTTON

            final ImageButton tagButton = (ImageButton) view.findViewById(R.id.tag);
            tagButton.setOnClickListener(new View.OnClickListener() {

                private boolean tagClicked = true;

                @Override
                public void onClick(View v) {
                    SetVisibleTags(view, v, tagClicked);
                    tagClicked = !tagClicked;
                }

            });

            //10. USERNAME - DONE!!...

            TextView username = (TextView) view.findViewById(R.id.postusername);

            username.setText(post.Posteduser().getUsername());

            username.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserProfileClick(post.Posteduser().getUserid());
                }
            });

            //11. CAPTION

            TextView caption = (TextView) view.findViewById(R.id.postcaption);

            caption.setText(post.getCaption());

            //12. TIME - DONE!!....

            TextView time = (TextView) view.findViewById(R.id.posttime);

            time.setText(post.getTime());

            //7. LIKE BUTTON - DONE!!....

            final ImageButton like = (ImageButton) view.findViewById(R.id.like);

            if (LIKE_STATUS) {
                like.setImageResource(R.drawable.like_pink);
            } else {
                like.setImageResource(R.drawable.like);
            }

            like.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    DoubleTapFunction(view);
                }
            });

            final TextView numoflikes = (TextView) view.findViewById(R.id.numoflikes);

            numoflikes.setText(post.getNumoflikes() + "");

            numoflikes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OpenPostLikesPage();
                }
            });

            //14. NUM OF COMMENTS

            TextView numofcomments = (TextView) view.findViewById(R.id.numofcomments);

            numofcomments.setText(post.getNumofcomments() + "");

            numofcomments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommentButtonClick(view);
                }
            });
        }
    }

    private void CommentButtonClick(final View view) {

        final Dialog dialog = new Dialog(view.getContext());
        dialog.setContentView(R.layout.comment_drawer);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        lp.windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setAttributes(lp);

        final EditText e = (EditText) dialog.findViewById(R.id.writecomment);
        e.setEnabled(false);
        final ListView l = (ListView) dialog.findViewById(R.id.commentsonpost);

        final List<CommentClass> commentClassList = new ArrayList<>();
        final List<Boolean> likestats = new ArrayList<>();
        final List<Boolean> followstats = new ArrayList<>();
        final CommentAdapter commentAdapter = new CommentAdapter(getContext(),commentClassList,likestats,followstats,e);
        l.setAdapter(commentAdapter);

        ImageButton close = (ImageButton) dialog.findViewById(R.id.closedialogbutton);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onCancel(DialogInterface dialog) {
                TextView textView = (TextView) view.findViewById(R.id.numofcomments);
                textView.setText(post.getNumofcomments()+"");
            }
        });

        ImageView postComment = (ImageView) dialog.findViewById(R.id.postcomment);
        postComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentClass c = new CommentClass(post.getPostid(), currentUser.getUserid(),e.getText().toString(),new Timestamp(System.currentTimeMillis()).toString(),0);
                c.loadUserProxy(currentUser);
                commentClassList.add(c);
                likestats.add(false);
                commentAdapter.notifyDataSetChanged();
                RegisterComment(c);
                e.setText("");
            }
        });

        fireBaseHelper
                .getDataBase()
                .collection("post-comments")
                .orderBy("time", Query.Direction.DESCENDING)
                .whereEqualTo("postid",post.getPostid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                        commentClassList.clear();
                        commentClassList.addAll(documentSnapshots.toObjects(CommentClass.class));
                        if(commentClassList.size()>0) {
                            fireBaseHelper
                                    .getDataBase()
                                    .runTransaction(new Transaction.Function<Boolean>() {
                                @Override
                                public Boolean apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                                    likestats.clear();
                                    for (int i = 0; i < commentClassList.size(); i++) {
                                        DocumentReference u = fireBaseHelper
                                                .getDataBase()
                                                .collection("users")
                                                .document(commentClassList.get(i).getUserid());
                                        commentClassList.get(i).loadUserProxy(transaction.get(u).toObject(UserClass.class).UserProxy());
                                        DocumentReference d = fireBaseHelper
                                                .getDataBase()
                                                .collection("comment-likes")
                                                .document(currentUser.getUserid()+commentClassList.get(i).getcommentid());
                                        likestats.add(transaction.get(d).exists());
                                        DocumentReference f = fireBaseHelper
                                                .getDataBase()
                                                .collection("following")
                                                .document(currentUser.getUserid()+commentClassList.get(i).getUserid());
                                        followstats.add(transaction.get(f).exists());
                                            }

                                    return true;
                                }
                            }).addOnCompleteListener(new OnCompleteListener<Boolean>() {
                                @Override
                                public void onComplete(@NonNull Task<Boolean> task) {
                                    dialog.findViewById(R.id.progresscoms).setVisibility(View.GONE);
                                    e.setEnabled(true);
                                    commentAdapter.notifyDataSetChanged();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("fail","comms");
                                }
                            });
                        } else {
                            Toast.makeText(getContext(),"Comment First!",Toast.LENGTH_SHORT).show();
                            dialog.findViewById(R.id.progresscoms).setVisibility(View.GONE);
                            e.setEnabled(true);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("here","what");
            }
        });
        dialog.show();
    }

    private void RegisterComment(CommentClass c) {
        WriteBatch batch = fireBaseHelper.getDataBase().batch();
        DocumentReference postLikes = fireBaseHelper.getDataBase()
                .collection("post-comments")
                .document();
        c.setcommentid(postLikes.getId());
        batch.set(postLikes,c);
        if(!currentUser.getUserid().equals(post.getUserid())) {
            NotificationClass n = new NotificationClass(currentUser.getUserid(), post.getPostid(), NotificationClass.COMMENT, post.getUserid(), c.getTime());
            DocumentReference notification = fireBaseHelper.getDataBase()
                    .collection("notifications")
                    .document();
            batch.set(notification, n);
        }
        DocumentReference cpost = fireBaseHelper.getDataBase()
                .collection("posts")
                .document(currentpost.getPostid());
        batch.update(cpost,"numofcomments",post.getNumofcomments()+1);
        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                    post.setNumofcomments(post.getNumofcomments() + 1);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),"Could Not Post Comment!",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void UserProfileClick(String userid)
    {
        Bundle b = new Bundle();
        b.putString("userid",userid);
        b.putBoolean("followstat",FOLLOW_STATUS);
        final UserProfileFragment userProfileFragment = new UserProfileFragment();
        userProfileFragment.setArguments(b);
        fragmentManager.beginTransaction().replace(android.R.id.content, userProfileFragment).addToBackStack(null).commit();
    }

    private void InitializeBMB(final BoomMenuButton bmb, final View rootView)
    {
        bmb.setButtonPlaceEnum(ButtonPlaceEnum.SC_9_1);
        bmb.setDimColor(R.color.black);

        bmb.addBuilder(new TextOutsideCircleButton.Builder()
                .normalImageRes(R.mipmap.save)
                .normalColorRes(R.color.black)
                .textTopMargin(5)
                .textSize(15)
                .highlightedColorRes(R.color.black));

        TextOutsideCircleButton.Builder save = (TextOutsideCircleButton.Builder) bmb.getBuilder(0);

        if(SAVE_STATUS) {
            save.normalText("Unsave Post");
        } else {
            save.normalText("Save Post");
        }

        bmb.addBuilder(new TextOutsideCircleButton.Builder()
                .normalImageRes(R.mipmap.copy)
                .normalColorRes(R.color.yellow)
                .normalText("Copy")
                .textTopMargin(5)
                .textSize(15)
                .highlightedColorRes(R.color.yellow));

        bmb.addBuilder(new TextOutsideCircleButton.Builder()
                .normalImageRes(R.mipmap.report)
                .normalColorRes(R.color.red)
                .normalText("Report")
                .textTopMargin(5)
                .textSize(15)
                .highlightedColorRes(R.color.red));

        bmb.addBuilder(new TextOutsideCircleButton.Builder()
                .normalImageRes(R.mipmap.facebook)
                .normalColorRes(R.color.facebook_blue)
                .normalText("Facebook")
                .textTopMargin(5)
                .textSize(15)
                .highlightedColorRes(R.color.facebook_blue));

        bmb.addBuilder(new TextOutsideCircleButton.Builder()
                .normalImageRes(R.mipmap.twitter)
                .normalColorRes(R.color.twitter_blue)
                .normalText("Twitter")
                .textTopMargin(5)
                .textSize(15)
                .highlightedColorRes(R.color.twitter_blue)
        );

        bmb.addBuilder(new TextOutsideCircleButton.Builder()
                .normalImageRes(R.mipmap.instagram)
                .normalColorRes(R.color.instagram_pink)
                .normalText("Instagram")
                .textTopMargin(5)
                .textSize(15)
                .highlightedColorRes(R.color.instagram_pink));

        bmb.addBuilder(new TextOutsideCircleButton.Builder()
                .normalImageRes(R.mipmap.pinterest)
                .normalColorRes(R.color.white)
                .normalText("Pinterest")
                .textTopMargin(5)
                .textSize(15)
                .highlightedColorRes(R.color.white));

        bmb.addBuilder(new TextOutsideCircleButton.Builder()
                .normalImageRes(R.mipmap.whatsapp)
                .normalColorRes(R.color.whatsapp_green)
                .normalText("Whatsapp")
                .textTopMargin(5)
                .textSize(15)
                .highlightedColorRes(R.color.whatsapp_green));

        bmb.addBuilder(new TextOutsideCircleButton.Builder()
                .normalImageRes(R.mipmap.googleplus)
                .normalColorRes(R.color.google_plus_red)
                .normalText("Google+")
                .textTopMargin(5)
                .textSize(15)
                .highlightedColorRes(R.color.google_plus_red));

        bmb.setOnBoomListener(new OnBoomListener() {
            @Override
            public void onClicked(int index, BoomButton boomButton) {
                if(index==0)
                {
                    if(!SAVE_STATUS) {
                        final ImageView imageView = (ImageView) rootView.findViewById(R.id.bookmark);
                        YoYo
                                .with(Techniques.Bounce)
                                .repeat(0)
                                .duration(1000)
                                .withListener(new Animator.AnimatorListener() {
                                    @Override
                                    public void onAnimationStart(Animator animator) {
                                        imageView.setVisibility(VISIBLE);
                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animator) {
                                        imageView.setVisibility(INVISIBLE);
                                    }

                                    @Override
                                    public void onAnimationCancel(Animator animator) {

                                    }

                                    @Override
                                    public void onAnimationRepeat(Animator animator) {

                                    }
                                })
                                .playOn(imageView);
                        fireBaseHelper.RegisterPOSTSave(currentUser.getUserid(),currentpost.getPostid());
                        boomButton.getTextView().setText("Unsave Post");
                    } else {
                        fireBaseHelper.RegisterPOSTUnsave(currentUser.getUserid(),currentpost.getPostid());
                        boomButton.getTextView().setText("Save Post");
                    }
                    SAVE_STATUS = !SAVE_STATUS;
                } else if(index==2) {
                    Toast.makeText(getContext(),"Report Noted!",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onBackgroundClick() {

            }

            @Override
            public void onBoomWillHide() {

            }

            @Override
            public void onBoomDidHide() {

            }

            @Override
            public void onBoomWillShow() {

            }

            @Override
            public void onBoomDidShow() {

            }
        });
    }

    private void OpenPostLikesPage() {
        Bundle b = new Bundle();
        b.putString("postid",currentpost.getPostid());
        final LikesPageFragment likesPageFragment = new LikesPageFragment();
        likesPageFragment.setArguments(b);
        fragmentManager.beginTransaction().replace(android.R.id.content, likesPageFragment).addToBackStack(null).commit();
    }

    private void OpenLikesPage(String commentid) {
        Bundle b = new Bundle();
        b.putString("postid",currentpost.getPostid());
        b.putString("commentid",commentid);
        final LikesPageFragment likesPageFragment = new LikesPageFragment();
        likesPageFragment.setArguments(b);
        fragmentManager.beginTransaction().replace(android.R.id.content, likesPageFragment).addToBackStack(null).commit();
    }

    void DoubleTapFunction(final View view) {
        final ImageButton imageButton = (ImageButton) view.findViewById(R.id.like);

        final TextView numoflikes = (TextView) view.findViewById(R.id.numoflikes);

        if (!LIKE_STATUS) {
            imageButton.setImageResource(R.drawable.like_pink);
            final ImageView imageView = (ImageView) view.findViewById(R.id.onLikeDoubleTap);
            YoYo
                    .with(Techniques.RotateIn)
                    .repeat(0)
                    .duration(700)
                    .withListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {
                            imageView.setVisibility(VISIBLE);
                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            imageView.setVisibility(INVISIBLE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {
                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {
                        }
                    })
                    .playOn(imageView);
            RegisterPostLike();
            numoflikes.setText(Integer.valueOf(numoflikes.getText().toString()) + 1 + "");
        } else {
            imageButton.setImageResource(R.drawable.like);
            RegisterPostUnlike();
            numoflikes.setText(Integer.valueOf(numoflikes.getText().toString()) - 1 + "");
        }
        LIKE_STATUS = !LIKE_STATUS;
    }

    public void RegisterPostLike() {
        WriteBatch batch = fireBaseHelper.getDataBase().batch();
        DocumentReference postLikes = fireBaseHelper.getDataBase()
                .collection("post-likes")
                .document(currentUser.getUserid()+currentpost.getPostid());
        PostLikeClass map = new PostLikeClass(post.getPostid(),currentUser.getUserid(),new Timestamp(System.currentTimeMillis() / 1000).toString());
        batch.set(postLikes,map);
        if(!currentUser.getUserid().equals(post.getUserid())) {
            NotificationClass n = new NotificationClass(currentUser.getUserid(), post.getPostid(), NotificationClass.POST_LIKE, post.getUserid(), map.getTime());
            DocumentReference notification = fireBaseHelper.getDataBase()
                    .collection("notifications")
                    .document();
            batch.set(notification, n);
        }
        DocumentReference cpost = fireBaseHelper.getDataBase()
                .collection("posts")
                .document(currentpost.getPostid());
        batch.update(cpost,"numoflikes",post.getNumoflikes()+1);
        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                    post.setNumoflikes(post.getNumoflikes()+1);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    public void RegisterPostUnlike() {
        final WriteBatch batch = fireBaseHelper.getDataBase().batch();
        DocumentReference d = fireBaseHelper.getDataBase().collection("post-likes")
                .document(currentUser.getUserid()+post.getPostid());
        batch.delete(d);
        DocumentReference posts = fireBaseHelper.getDataBase()
                .collection("posts")
                .document(currentpost.getPostid());
        batch.update(posts,"numoflikes",post.getNumoflikes()-1);
        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                    post.setNumoflikes(post.getNumoflikes()-1);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
    }

    void SetVisibleTags(View view, View buttonview, boolean b) {
        PostPictureView postPictureView = (PostPictureView) view.findViewById(R.id.postpicture);
        ImageButton imageButton = (ImageButton) buttonview;
        if (b) {
            imageButton.setImageResource(R.drawable.tag_pink);
            Toast.makeText(getContext(),"No tags to show!", Toast.LENGTH_SHORT).show();
        } else {
            imageButton.setImageResource(R.drawable.tag);
        }
    }

    private class PostViewPagerAdapter extends android.support.v4.view.PagerAdapter {

        private List<PostResourceClass> PostResources = new ArrayList<>();

        @Override
        public Object instantiateItem(final ViewGroup container, int position) {

            final PostResourceClass currentres = PostResources.get(position);

            //15. NUM OF TAGS

            TextView numoftags = (TextView) container.getRootView().findViewById(R.id.numoftags);

            numoftags.setText("0");

            numoftags.setOnClickListener(new View.OnClickListener() {
                private boolean tagClicked = true;
                @Override
                public void onClick(View v) {
                    SetVisibleTags(container.getRootView(),v,tagClicked);
                    tagClicked = !tagClicked;
                }
            });

            if(currentres.getResourcetype()==PostResourceClass.IMAGE) {

                LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                View view = layoutInflater.inflate(R.layout.post_picture_layout, container, false);

                final ImageView pictureCover = (ImageView) view.findViewById(R.id.picturecover);

                final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progress);

                GlideApp
                        .with(getContext())
                        .load(currentres.getResource())
                        .thumbnail(0.1f)
                        .priority(Priority.NORMAL)
                        .apply(bitmapTransform(new BlurTransformation(25)))
                        .into(pictureCover);

                final PostPictureView postPictureView = (PostPictureView) view.findViewById(R.id.postpicture);
                postPictureView.SetArguments(PostView.this);

                GlideApp
                        .with(getContext())
                        .asBitmap()
                        .load(currentres.getResource())
                        .priority(Priority.HIGH)
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                progressBar.setVisibility(View.GONE);
                                postPictureView.setImage(ImageSource.bitmap(resource));
                            }

                            @Override
                            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getContext(), "Please Check your Internet Connection and Try Again!", Toast.LENGTH_SHORT).show();
                            }
                        });

                container.addView(view,0);
                return view;
            }
            else if (currentres.getResourcetype()==PostResourceClass.VIDEO) {

                LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                View view = layoutInflater.inflate(R.layout.post_video_layout, container, false);

                ImageView videoCover = (ImageView) view.findViewById(R.id.videocover);

                GlideApp
                        .with(getContext())
                        .asBitmap()
                        .load(currentres.getResource())
                        .priority(Priority.HIGH)
                        .apply(bitmapTransform(new BlurTransformation(25)))
                        .into(videoCover);

                final PostVideoView postVideoView = view.findViewById(R.id.postvideo);
                postVideoView.setVideoURI(Uri.parse(currentres.getResource()));
                postVideoView.SetArguments(PostView.this);
                container.addView(view, 0);
                return view;
            }
            else
                return container;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((RelativeLayout)object);
        }

        @Override
        public int getCount() {
            return PostResources.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==(RelativeLayout)object;
        }

        public List<PostResourceClass> getPostResources() {
            return PostResources;
        }

        public void setPostResources(List<PostResourceClass> postResources) {
            PostResources = postResources;
            notifyDataSetChanged();
        }
    }

    public class CommentAdapter extends ArrayAdapter<CommentClass> {

        private List<Boolean> likestats;
        private List<Boolean> followstats;
        private EditText e;

        public CommentAdapter(@NonNull Context context, @NonNull List<CommentClass> objects, List<Boolean> likestats, List<Boolean> followstats, EditText e) {
            super(context, R.layout.comment_list_item, objects);
            this.e = e;
            this.likestats = likestats;
            this.followstats = followstats;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater theinflater = LayoutInflater.from(getContext());

            @SuppressLint("ViewHolder")
            final View view = theinflater.inflate(R.layout.comment_list_item,parent,false);

            final CommentClass comment = getItem(position);

            ImageView profilePic = (ImageView) view.findViewById(R.id.profilepic);

            if(comment.UserProxy().getProfilepic()!=null) {
                GlideApp
                        .with(getContext())
                        .asBitmap()
                        .load(comment.UserProxy().getProfilepic())
                        .circleCrop()
                        .into(profilePic);
            } else profilePic.setImageResource(R.drawable.user_pink);

            profilePic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserProfileClick(comment.UserProxy().getUserid(),followstats.get(position));
                }
            });

            TextView username = (TextView) view.findViewById(R.id.commentusername);

            username.setText(comment.UserProxy().getUsername());

            username.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserProfileClick(comment.UserProxy().getUserid(),followstats.get(position));
                }
            });

            TextView listedtext = (TextView) view.findViewById(R.id.listedcommenttext);

            listedtext.setText(comment.getText());

            LikeComment(view,position);

            TextView reply = (TextView) view.findViewById(R.id.commentreply);
            reply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ReplyToUser(comment.UserProxy().getUsername());
                }
            });

            TextView time = (TextView) view.findViewById(R.id.commenttime);
            time.setText(comment.getTime());

            return view;
        }

        @SuppressLint("SetTextI18n")
        private void LikeComment(final View view, final int position)
        {
            final ImageView imageButton = (ImageView) view.findViewById(R.id.commentlove);
            final TextView numoflikes = (TextView) view.findViewById(R.id.commentlikes);

            final CommentClass c = getItem(position);

            numoflikes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OpenLikesPage(c.getcommentid());
                }
            });

            numoflikes.setText(c.getNumoflikes()+" likes");
            if(likestats.get(position)) {
                imageButton.setImageResource(R.drawable.reply_love_pink);
            } else {
                imageButton.setImageResource(R.drawable.reply_love);
            }
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!likestats.get(position))
                    {
                        imageButton.setImageResource(R.drawable.reply_love_pink);
                        numoflikes.setText(Integer.valueOf(numoflikes.getText().toString())+1+"");
                        RegisterCOMMENTLike(c);
                    } else {
                        imageButton.setImageResource(R.drawable.reply_love);
                        numoflikes.setText(Integer.valueOf(numoflikes.getText().toString())-1+"");
                        RegisterCOMMENTUnlike(c);
                    }
                    likestats.set(position,!likestats.get(position));
                }
            });
        }

        private void ReplyToUser(String username) {
            e.setText(e.getText().append("@").append(username));
            e.moveCursorToVisibleOffset();
        }

    }

    private void UserProfileClick(String userid, Boolean aBoolean) {
        Bundle b = new Bundle();
        b.putString("userid",userid);
        b.putBoolean("followstat",aBoolean);
        final UserProfileFragment userProfileFragment = new UserProfileFragment();
        userProfileFragment.setArguments(b);
        fragmentManager.beginTransaction().replace(android.R.id.content, userProfileFragment).addToBackStack(null).commit();
    }

    private void RegisterCOMMENTUnlike(final CommentClass currentcomment) {
        final WriteBatch batch = fireBaseHelper.getDataBase().batch();
        DocumentReference d = fireBaseHelper.getDataBase()
                .collection("comment-likes")
                .document(currentUser.getUserid()+currentcomment.getcommentid());
        batch.delete(d);
        DocumentReference cpost = fireBaseHelper.getDataBase()
                .collection("post-comments")
                .document(currentcomment.getcommentid());
        batch.update(cpost,"numoflikes",currentcomment.getNumoflikes()-1);
        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                            currentcomment.setNumoflikes(currentcomment.getNumoflikes()-1);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("no","no");
                    }
                });
    }

    private void RegisterCOMMENTLike(final CommentClass currentcomment) {
        WriteBatch batch = fireBaseHelper.getDataBase().batch();
        DocumentReference postLikes = fireBaseHelper.getDataBase()
                .collection("comment-likes")
                .document(currentUser.getUserid()+currentcomment.getcommentid());
        CommentLikeClass map = new CommentLikeClass(currentcomment.getcommentid(),currentUser.getUserid(),new Timestamp(System.currentTimeMillis() / 1000).toString());
        batch.set(postLikes,map);
        if(!currentUser.getUserid().equals(currentcomment.getUserid())) {
            NotificationClass n = new NotificationClass(currentUser.getUserid(), currentcomment.getPostid(), NotificationClass.COMMENT_LIKE, currentcomment.getUserid(), map.getTime());
            DocumentReference notification = fireBaseHelper.getDataBase()
                    .collection("notifications")
                    .document();
            batch.set(notification, n);
        }
        DocumentReference cpost = fireBaseHelper.getDataBase()
                .collection("post-comments")
                .document(currentcomment.getcommentid());
        batch.update(cpost,"numoflikes",currentcomment.getNumoflikes()+1);
        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                    currentcomment.setNumoflikes(currentcomment.getNumoflikes()+1);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("no","no");
            }
        });
    }

}

