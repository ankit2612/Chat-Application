package com.plusconnect.chat;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.plusconnect.Beans.ChatUserBean;
import com.plusconnect.Utilities.ApiKeysAndConstants;
import com.plusconnect.Utilities.PlusConnectUtils;
import com.plusconnect.Views.OfferLinearLayoutManager;
import com.rockerhieu.emojicon.EmojiconGridFragment;
import com.rockerhieu.emojicon.EmojiconsFragment;
import com.rockerhieu.emojicon.emoji.Emojicon;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import io.codetail.animation.SupportAnimator;

/**
 * Created by ambesh on 14-09-2015.
 */
public class UserChatsActivity extends ActionBarActivity implements EmojiconGridFragment.OnEmojiconClickedListener, EmojiconsFragment.OnEmojiconBackspaceClickedListener
{



    private Toolbar toolbar;


    private ImageView attachmentIv;
    private RecyclerView chatRv;
    private FrameLayout emojicons_FrameLayout;
    private EditText cc_message_box_EditText;
        private LinearLayout cc_multimedia_slider_LinearLayout;
    private ImageButton cc_speaker_ImageButton,cc_send_ImageButton;
    private ImageButton hc_back_ImageButton;
    private ImageButton cc_emoticon_ImageButton;
    private  Uri imageUri,contactUri,videoUri;
    private ImageView cc_contact_ImageButton,cc_location_ImageButton,
            cc_audio_ImageButton,cc_gallery_ImageButton,cc_camera_ImageButton,cc_video_ImageButton;

    private String chatTo,chatFrom;
    private TextView hc_user_name_TextView;
    private View chatOptionsVw,chatHeaderVw;
    private LinearLayout container_image;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_chat_new);
        
        initData();
        initLayout();

        intiaChatLayout();


    }

    private void initData() {
        Intent intent=getIntent();

        Bundle bundle=intent.getExtras();
        if (bundle!=null&&!bundle.isEmpty()){
            chatTo=bundle.getString(ChatUserBean.SENT_TO,"");
            chatFrom=bundle.getString(ChatUserBean.SENTBY,"");

        }

    }


    private void initLayout(){
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.textColorPrimary));
        cc_multimedia_slider_LinearLayout= (LinearLayout) findViewById(R.id.cc_multimedia_slider_LinearLayout);
        cc_multimedia_slider_LinearLayout.setVisibility(View.GONE);
        setSupportActionBar(toolbar);
        chatRv= (RecyclerView) findViewById(R.id.chatRv);
        container_image= (LinearLayout) findViewById(R.id.container_image);
        chatRv.setLayoutManager(new OfferLinearLayoutManager(this));
        container_image= (LinearLayout) findViewById(R.id.container_image);
        emojicons_FrameLayout= (FrameLayout) findViewById(R.id.emojicons_FrameLayout);
        cc_message_box_EditText= (EditText) findViewById(R.id.cc_message_box_EditText);
        cc_send_ImageButton= (ImageButton) findViewById(R.id.cc_send_ImageButton);
        cc_speaker_ImageButton= (ImageButton) findViewById(R.id.cc_speaker_ImageButton);
        cc_speaker_ImageButton.setOnClickListener(onClickListener);
        cc_emoticon_ImageButton= (ImageButton) findViewById(R.id.cc_emoticon_ImageButton);
        cc_emoticon_ImageButton.setOnClickListener(onClickListener);



        cc_video_ImageButton= (ImageView) findViewById(R.id.cc_video_ImageButton);
        cc_contact_ImageButton= (ImageView) findViewById(R.id.cc_contact_ImageButton);
        cc_location_ImageButton= (ImageView) findViewById(R.id.cc_location_ImageButton);
        cc_audio_ImageButton= (ImageView) findViewById(R.id.cc_audio_ImageButton);
        cc_camera_ImageButton= (ImageView) findViewById(R.id.cc_camera_ImageButton);
        cc_gallery_ImageButton= (ImageView) findViewById(R.id.cc_gallery_ImageButton);
        cc_message_box_EditText.addTextChangedListener(textWatcher);


        cc_video_ImageButton.setOnClickListener(onClickListener);
        cc_contact_ImageButton.setOnClickListener(onClickListener);
        cc_location_ImageButton.setOnClickListener(onClickListener);
        cc_audio_ImageButton.setOnClickListener(onClickListener);
        cc_camera_ImageButton.setOnClickListener(onClickListener);
        cc_gallery_ImageButton.setOnClickListener(onClickListener);

        chatHeaderVw=getLayoutInflater().inflate(R.layout.header_chat,null);
        chatOptionsVw=getLayoutInflater().inflate(R.layout.layout_header_chat_user,null);
        hc_user_name_TextView = (TextView) chatHeaderVw.findViewById(R.id.hc_user_name_TextView);
        hc_user_name_TextView.setText(chatFrom);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        attachmentIv= (ImageView) chatHeaderVw.findViewById(R.id.attachmentIv);
        attachmentIv.setOnClickListener(onClickListener);

        container_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.addView(chatHeaderVw,layoutParams);



        initEmojiFragment();


    }




    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, ApiKeysAndConstants.RESPONSE_CODE_OPEN_VOICE);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }





    private void initEmojiFragment() {

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.emojicons_FrameLayout, EmojiconsFragment.newInstance(false))
                .commit();


    }

    private void intiaChatLayout() {


    }

    private View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {


            switch (v.getId()){
                case R.id.attachmentIv:

                    if (cc_multimedia_slider_LinearLayout.getVisibility()==View.GONE){

                        int cx = (cc_multimedia_slider_LinearLayout.getLeft() + cc_multimedia_slider_LinearLayout.getRight());
                        int cy = cc_multimedia_slider_LinearLayout.getTop();

                        int finalRadius = Math.max(cc_multimedia_slider_LinearLayout.getWidth()
                                , cc_multimedia_slider_LinearLayout.getHeight());





                        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
                            Animator anim =
                                    ViewAnimationUtils.createCircularReveal(cc_multimedia_slider_LinearLayout, cx, cy, 0, finalRadius);
                            cc_multimedia_slider_LinearLayout.setVisibility(View.VISIBLE);
                            anim.setInterpolator(new AccelerateDecelerateInterpolator());
                            anim.setDuration(400);
                            anim.start();
                        }
                        else
                        {




                            int cy1 = toolbar.getHeight();
                            int cx1 = (int) attachmentIv.getX()+(attachmentIv.getWidth()/2);

                            int finalRadius1 = Math.max(cc_multimedia_slider_LinearLayout.getWidth()
                                    , cc_multimedia_slider_LinearLayout.getHeight());

                            SupportAnimator animator =
                                    io.codetail.animation.ViewAnimationUtils.createCircularReveal(cc_multimedia_slider_LinearLayout, cx1, cy1, 0, finalRadius1);
                            animator.setInterpolator(new AccelerateDecelerateInterpolator());
                            animator.setDuration(600);
                            animator.addListener(new SupportAnimator.AnimatorListener() {
                                @Override
                                public void onAnimationStart() {
                                    cc_multimedia_slider_LinearLayout.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onAnimationEnd() {

                                }

                                @Override
                                public void onAnimationCancel() {

                                }

                                @Override
                                public void onAnimationRepeat() {

                                }
                            });
                            animator.start();

                        }



                    }
                    else {
                        int cx = (cc_multimedia_slider_LinearLayout.getLeft() + cc_multimedia_slider_LinearLayout.getRight());
                        int cy = cc_multimedia_slider_LinearLayout.getTop();

                        int initialRadius = Math.max(cc_multimedia_slider_LinearLayout.getWidth()
                                , cc_multimedia_slider_LinearLayout.getHeight());
// create the animation (the final radius is zero)

                        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
                            Animator anim =
                                    ViewAnimationUtils.createCircularReveal(cc_multimedia_slider_LinearLayout, cx, cy, initialRadius, 0);

// make the view invisible when the animation is done
                            anim.addListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    cc_multimedia_slider_LinearLayout.setVisibility(View.GONE);
                                }
                            });

// start the animation
                            anim.start();
                        }
                        else {



                            int cx1 = (cc_multimedia_slider_LinearLayout.getLeft() + cc_multimedia_slider_LinearLayout.getRight());
                            int cy1 = cc_multimedia_slider_LinearLayout.getTop();

                            int finalRadius1 = Math.max(cc_multimedia_slider_LinearLayout.getWidth()
                                    , cc_multimedia_slider_LinearLayout.getHeight());

                            SupportAnimator animator =
                                    io.codetail.animation.ViewAnimationUtils.createCircularReveal(cc_multimedia_slider_LinearLayout, cx1, cy1, 0, finalRadius1);
                            animator.setInterpolator(new AccelerateDecelerateInterpolator());
                            animator.setDuration(400);
                            SupportAnimator animator_reverse = animator.reverse();

                            animator_reverse.addListener(new SupportAnimator.AnimatorListener() {
                                @Override
                                public void onAnimationStart() {

                                }

                                @Override
                                public void onAnimationEnd() {
                                    cc_multimedia_slider_LinearLayout.setVisibility(View.GONE);

                                }

                                @Override
                                public void onAnimationCancel() {

                                }

                                @Override
                                public void onAnimationRepeat() {

                                }
                            });
                            animator_reverse.start();
                        }

                    }

                    break;



                case R.id.cc_speaker_ImageButton:
                    promptSpeechInput();
                    break;
                case R.id.cc_send_ImageButton:
                    break;

                case R.id.cc_audio_ImageButton:

//                    Intent audioIntent = new Intent(Intent.ACTION_GET_CONTENT);
//                    audioIntent.setType("audio/*");
//                    Uri data = Uri.fromFile(getTempFile());
//                    audioIntent.setData(data);
//                    startActivityForResult(Intent.createChooser(audioIntent,UserChatsActivity.this.getString(R.string.textChooseAnAction)), ApiKeysAndConstants.RESPONSE_CODE_OPEN_SOUND);


                    break;
                case R.id.cc_contact_ImageButton:
                    Intent contactIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);


                    startActivityForResult
                            (Intent.createChooser
                                    (contactIntent,UserChatsActivity.this.
                                            getString(R.string.textChooseAnAction)),
                                    ApiKeysAndConstants.RESPONSE_CODE_OPEN_CONTACT);




                    break;
                case R.id.cc_location_ImageButton:
                    Intent locationIntent=new Intent();
                    startActivityForResult(locationIntent,ApiKeysAndConstants.RESPONSE_CODE_OPEN_LOCATION);


                    break;
                case R.id.cc_gallery_ImageButton:

                    Intent gallaryintent = new Intent();
                    gallaryintent.setType("image/*");
                    gallaryintent.setAction(Intent.ACTION_GET_CONTENT);//
                    startActivityForResult(Intent.createChooser(gallaryintent,UserChatsActivity.this.getString(R.string.textChooseAnAction)), ApiKeysAndConstants.RESPONSE_CODE_OPEN_GALLARY);

                    break;
                case R.id.cc_camera_ImageButton:
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    String fileName = "Camera_Example.jpg";

                    // Create parameters for Intent with filename

                    ContentValues values = new ContentValues();

                    values.put(MediaStore.Images.Media.TITLE, fileName);
                    imageUri = getContentResolver().insert(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(cameraIntent, ApiKeysAndConstants.RESPONSE_CODE_OPEN_CAMERA);
                    break;
                case R.id.cc_video_ImageButton:
                    Intent videoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

                    // create a file to save the video
                    videoUri = getOutputMediaFileUri(ApiKeysAndConstants.RESPONSE_CODE_OPEN_VIDEO);

                    // set the image file name
                    videoIntent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);

                    // set the video image quality to high
                    videoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

                    // start the Video Capture Intent
                    startActivityForResult(videoIntent, ApiKeysAndConstants.RESPONSE_CODE_OPEN_VIDEO);

                    break;

                case R.id.cc_emoticon_ImageButton:
                    if (emojicons_FrameLayout.getVisibility()==View.VISIBLE){
                        emojicons_FrameLayout.setVisibility(View.GONE);
                    }
                    else {
                        emojicons_FrameLayout.setVisibility(View.VISIBLE);

                    }

                    break;

            }

        }
    };






    private File dir;

    private File getTempFile(){
        dir =new File(this.getExternalFilesDir(null).getAbsolutePath()+"/Music");
        return dir;


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case ApiKeysAndConstants.RESPONSE_CODE_OPEN_GALLARY:
                fetchGallaryAndUploadToServer(resultCode,data);
                break;
            case ApiKeysAndConstants.RESPONSE_CODE_OPEN_SOUND:
                fetchAudioFromGallary(resultCode,data);
                break;
            case ApiKeysAndConstants.RESPONSE_CODE_OPEN_CAMERA:
                fetchImageFromCamera(resultCode,data);
                break;
            case ApiKeysAndConstants.RESPONSE_CODE_OPEN_CONTACT:

                fetchContacts(resultCode,data);
                break;
            case ApiKeysAndConstants.RESPONSE_CODE_OPEN_VIDEO:
                fetchVideo(resultCode,data);
                break;
            case ApiKeysAndConstants.RESPONSE_CODE_OPEN_VOICE:
                fetchTheVoice(resultCode,data);
                break;
            case ApiKeysAndConstants.RESPONSE_CODE_OPEN_LOCATION:
                fetchLocation(resultCode,data);
                break;




        }



    }

    private void fetchLocation(int resultCode, Intent data) {

        if (resultCode==RESULT_OK&&data!=null){

            Bundle bundle=data.getExtras();
            if (bundle!=null){
                String lat=bundle.getString(LocationActiivty.LATITUDE,"");
                String lon=bundle.getString(LocationActiivty.LONGITUDE,"");


            }

        }
    }


    private void fetchTheVoice(int resultCode,Intent data){
        if (resultCode == RESULT_OK && null != data) {

            ArrayList<String> result = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            cc_message_box_EditText.setText(result.get(0));
        }

    }

        private void fetchVideo(int resultCode,Intent data){

            if (resultCode==RESULT_OK){

                Uri videoUri=data.getData();
                if (videoUri!=null){
                    Toast.makeText(this,videoUri.getPath(),Toast.LENGTH_LONG).show();
                }

            }


        }


    private String PHONE_NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
    private String DISPLAY_NAME = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ? ContactsContract.Contacts.DISPLAY_NAME_PRIMARY
            : ContactsContract.Contacts.DISPLAY_NAME;

    private void fetchContacts(int resultCode, Intent data) {

    if (resultCode==RESULT_OK){
        Uri contactUri=data.getData();

        if (contactUri!=null){
            String[] projection = { PHONE_NUMBER, DISPLAY_NAME };
            Cursor cursor=managedQuery(contactUri,projection,null,null,null);
            String phoneNumber=cursor.getString(cursor.getColumnIndex(PHONE_NUMBER));
            String contactName=cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));
        }

    }



    }







    private  Uri getOutputMediaFileUri(int type){

        return Uri.fromFile(getOutputMediaFile(type));
    }

    /** Create a File for saving an image or video */
    private  File getOutputMediaFile(int type){

        // Check that the SDCard is mounted
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraVideo");


        // Create the storage directory(MyCameraVideo) if it does not exist
        if (! mediaStorageDir.exists()){

            if (! mediaStorageDir.mkdirs()){

                return null;
            }
        }


        // Create a media file name

        // For unique file name appending current timeStamp with file name
        java.util.Date date= new java.util.Date();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(date.getTime());

        File mediaFile;

        if(type == ApiKeysAndConstants.RESPONSE_CODE_OPEN_VIDEO) {

            // For unique video file name appending current timeStamp with file name
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");

        } else {
            return null;
        }

        return mediaFile;
    }



    private void fetchImageFromCamera(int resultCode,Intent data){


        if (resultCode==RESULT_OK){
            int id= PlusConnectUtils.convertImageUriToFile(imageUri,this);
        }
        else {
            Toast.makeText(this, getString(R.string.textCamerayNotAvalaible), Toast.LENGTH_SHORT).show();
        }

    }
    private void fetchAudioFromGallary(int resultCode,Intent data){


        if (resultCode==this.RESULT_OK){
            String[] proj = { MediaStore.Images.Media.DATA };
            Cursor musiccursor = managedQuery(data.getData(), proj, null, null, null);
        int  music_column_index = musiccursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
            musiccursor.moveToFirst();
            String filename = musiccursor.getString(music_column_index);
            musiccursor.close();
        }
        else {
            Toast.makeText(this, getString(R.string.textSoundyNotAvalaible), Toast.LENGTH_SHORT).show();
        }


    }


    TextWatcher textWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {


        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

            if (TextUtils.isEmpty(s.toString())){
                        cc_send_ImageButton.setVisibility(View.GONE);
                        cc_speaker_ImageButton.setVisibility(View.VISIBLE);
            }
            else {
                cc_send_ImageButton.setVisibility(View.VISIBLE);
                cc_speaker_ImageButton.setVisibility(View.GONE);
            }

        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chat_detail, menu);

        return true;
    }

    @Override
    public void onEmojiconBackspaceClicked(View v) {
        EmojiconsFragment.backspace(cc_message_box_EditText);
    }

    @Override
    public void onEmojiconClicked(Emojicon emojicon) {
        EmojiconsFragment.input(cc_message_box_EditText, emojicon);
    }



    private void fetchGallaryAndUploadToServer(int resultCode,Intent data) {

        if (resultCode == this.RESULT_OK) {
            if (data != null) {
                try {

                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());

                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else if (resultCode == this.RESULT_CANCELED) {
                Toast.makeText(this, getString(R.string.textGallaryNotAvalaible), Toast.LENGTH_SHORT).show();
            }

        }

    }
}
