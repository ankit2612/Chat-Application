package com.plusconnect.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.LinearLayout;


import com.plusconnect.Beans.ChatUserBean;
import com.plusconnect.PlusConnectUtils.ApiConstantsAndKeys;
import com.plusconnect.Views.FontAwesomeTextView;
import com.plusconnect.Views.RobotoMediumTextView;
import com.plusconnect.Views.RobotoRegularTextView;
import com.plusconnect.Views.RoundedImageView;
import com.plusconnect.chat.R;
import com.plusconnect.chat.UserChatsActivity;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;



/**
 * Created by ambesh on 01-06-2015.
 */
public class ChatUserAdapter extends RecyclerView.Adapter<ChatUserAdapter.ChatUserViewHolder> {


    private HashMap<String,ArrayList<ChatUserBean>> chatUserBeans;
    private Activity activity;
    private ArrayList<String> keySet=new ArrayList<>();
    private String chatTrue,chatFalse;
    private CharSequence tick;

    public ChatUserAdapter(HashMap<String,ArrayList<ChatUserBean>> chatUserBeans,Activity activity) {
        this.activity=activity;
        this.chatUserBeans = chatUserBeans;
        keySet.addAll(chatUserBeans.keySet());
        chatTrue=activity.getString(R.string.textTrue);
        chatFalse=activity.getString(R.string.textFalse);
        tick=activity.getString(R.string.textTick);
    }

    @Override
    public ChatUserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_chat_list,null);
        ChatUserViewHolder chatUserViewHolder=new ChatUserViewHolder(view);
        return chatUserViewHolder;
    }

    @Override
    public void onBindViewHolder(ChatUserViewHolder holder, int position) {

        ArrayList<ChatUserBean> chatUserBeanArrayList= chatUserBeans.get(keySet.get(position));
//        Picasso.with(activity).load(chatUserBean.getChat_profile_image()).into(holder.chat_list_profile_image);


//        if (chatUserBean.getChatBeans()!=null&&!chatUserBean.getChatBeans().isEmpty()){
//            holder.chat_list_message.setText(chatUserBean.getChatBeans().get(0).getChat_text());
//        }

        ChatUserBean chatUserBean=chatUserBeanArrayList.get(chatUserBeanArrayList.size()-1);
            if (!TextUtils.isEmpty(chatUserBean.getMessage())){
                holder.chat_list_message.setText(chatUserBean.getMessage());
            }

        holder.chat_list_message_count.setText(chatUserBeanArrayList.size()+"");


        if (!TextUtils.isEmpty(chatUserBean.getTimeInMilliSecs())){

            holder.chat_list_time_indicator.setText(DateUtils.formatSameDayTime(Long.parseLong(chatUserBean.getTimeInMilliSecs()),System.currentTimeMillis(), DateFormat.DEFAULT,DateFormat.DEFAULT));

        }


        if (!TextUtils.isEmpty(chatUserBean.getMessageSenToType())&&chatUserBean.getMessageSenToType().equals(ApiConstantsAndKeys.CHAT_TYPE_GROUP)){

            holder.chat_list_user_name.setText(chatUserBean.getSentBy());
        }
        else{
            if (!TextUtils.isEmpty(chatUserBean.getSentBy())){
                holder.chat_list_user_name.setText(chatUserBean.getSentBy());
            }

        }


        if (!TextUtils.isEmpty(chatUserBean.isMsgSentToServerFromUserAcknl())
                &&chatUserBean.isMsgSentToServerFromUserAcknl().equals(chatTrue)){
           holder.chat_list_message_indicator.setImageResource(R.drawable.sent_message_single_tick);
        }

        if (!TextUtils.isEmpty(chatUserBean.isMsgSentToServerFromUserAcknl())
                &&chatUserBean.isMsgSentToUserFromServerAcknl().equals(chatTrue)){
            holder.chat_list_message_indicator.setImageResource(R.drawable.sent_message_double_tick);
        }

        if (!TextUtils.isEmpty(chatUserBean.isMsgSentToServerFromUserAcknl())
                &&chatUserBean.isMsgReadAcknlByClient().equals(chatTrue)){
            holder.chat_list_message_indicator.setImageResource(R.drawable.sent_message_double_tick_blue);

        }

        if (!TextUtils.isEmpty(chatUserBean.isMsgSentToServerFromUserAcknl())
                &&chatUserBean.isMsgReadAcknlByServerToClient().equals(chatTrue)){


            holder.chat_list_message_indicator.setImageResource(R.drawable.sent_message_double_tick_blue);


        }
        holder.view.setTag(chatUserBean);
        holder.view.setOnClickListener(onClickListener);

//        Picasso.with(activity).load(R.drawable.ic_action_profile_img).transform(new CircleTransform()).into(holder.chat_list_profile_image);
//

    }


    public class CircleTransform implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }

            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap,
                    BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);

            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
        }
    }


    private View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ChatUserBean chatUserBean= (ChatUserBean) v.getTag();
            Intent intent=new Intent(activity, UserChatsActivity.class);
            intent.putExtra(ChatUserBean.SENT_TO,chatUserBean.getSentTo());
            intent.putExtra(ChatUserBean.SENTBY,chatUserBean.getSentBy());

            activity.startActivity(intent);


        }
    };
    @Override
    public int getItemCount() {
        return chatUserBeans.size();
    }

    public class ChatUserViewHolder extends RecyclerView.ViewHolder{

        public View view;
        public ImageView chat_list_profile_image;
        public LinearLayout text_container;
        public RobotoMediumTextView chat_list_user_name;
        public FontAwesomeTextView tickIndicator1,tickIndicator2;
        public RobotoRegularTextView chat_list_time_indicator,chat_list_message,chat_list_message_count;
        public ImageView chat_list_message_indicator;






        public ChatUserViewHolder(View itemView) {
            super(itemView);
            view=itemView;
            chat_list_profile_image= (ImageView) view.findViewById(R.id.chat_list_profile_image);
            text_container= (LinearLayout) view.findViewById(R.id.text_container);
            chat_list_user_name= (RobotoMediumTextView) view.findViewById(R.id.chat_list_user_name);
            chat_list_time_indicator= (RobotoRegularTextView) view.findViewById(R.id.chat_list_time_indicator);
            chat_list_message= (RobotoRegularTextView) view.findViewById(R.id.chat_list_message);
            chat_list_message_count= (RobotoRegularTextView) view.findViewById(R.id.chat_list_message_count);
            chat_list_message_indicator= (ImageView) view.findViewById(R.id.chat_list_message_indicator);


        }
    }

}
