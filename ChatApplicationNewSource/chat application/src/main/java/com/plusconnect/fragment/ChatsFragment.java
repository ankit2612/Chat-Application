package com.plusconnect.fragment;

/**
 * Created by asuss on 8/22/2015.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.plusconnect.Beans.ChatUserBean;
import com.plusconnect.PlusConnectApplication;
import com.plusconnect.Utilities.PlusConnectUtils;
import com.plusconnect.Utilities.PlusNetworkCallerRequest;
import com.plusconnect.Views.OfferLinearLayoutManager;
import com.plusconnect.Views.RobotoMediumTextView;
import com.plusconnect.adapter.ChatUserAdapter;
import com.plusconnect.chat.R;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class ChatsFragment extends Fragment {

    private RecyclerView recyclerView;
    private ChatUserAdapter chatUserAdapter;
    private RequestQueue requestQueue;
    private ProgressBar progressBar;
    private RobotoMediumTextView errorTv;
    private View noNetworkContainer,chatContainer;
    private ImageView refreshIcon;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_chat,container,false);
        recyclerView= (RecyclerView) v.findViewById(R.id.chatRv);
        progressBar= (ProgressBar) v.findViewById(R.id.progressBar);
        errorTv= (RobotoMediumTextView) v.findViewById(R.id.errorTv);

        requestQueue=((PlusConnectApplication)getActivity().getApplication()).getQueue();

        noNetworkContainer=v.findViewById(R.id.noNetworkContainer);
        chatContainer=v.findViewById(R.id.chatContainer);

        refreshIcon= (ImageView) v.findViewById(R.id.refreshIcon);
        refreshIcon.setOnClickListener(onClickListener);
        if (PlusConnectUtils.isNetworkAvailable(getActivity(),false)){

            fetchChatsUserFromServer();

        }
        else {
            noNetworkContainer.setVisibility(View.VISIBLE);
            chatContainer.setVisibility(View.GONE);
        }
        return v;
    }


    private View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (PlusConnectUtils.isNetworkAvailable(getActivity(),false)){

                noNetworkContainer.setVisibility(View.GONE);
                chatContainer.setVisibility(View.VISIBLE);
                fetchChatsUserFromServer();
            }
            else {
                noNetworkContainer.setVisibility(View.VISIBLE);
                chatContainer.setVisibility(View.GONE);
            }
        }
    };

    private PlusNetworkCallerRequest plusNetworkCallerRequest;
private void fetchChatsUserFromServer(){


    String stringRequest=getString(R.string.baseUrl)+getString(R.string.chatServices)+ File.separator
            +getString(R.string.textTokenKey)+File.separator+getString(R.string.textTokenValue)
            +File.separator+getString(R.string.textUser)+File.separator+"jatin@abc.com"+File.separator+
            getString(R.string.textTime)+File.separator+getString(R.string.timeStampContant);
    if (PlusConnectUtils.isNetworkAvailable(getActivity(),false)){
plusNetworkCallerRequest=new
        PlusNetworkCallerRequest<ChatUserBean>
        (ChatUserBean[].class,stringRequest,listener,
                errorListener,((PlusConnectApplication)getActivity().getApplication()).getGson()
        );
        requestQueue.add(plusNetworkCallerRequest);
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);


    }


}

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (plusNetworkCallerRequest!=null)
        requestQueue.cancelAll(plusNetworkCallerRequest);
        listener=null;
        errorListener=null;
    }

    private Response.Listener<ChatUserBean[]> listener=new Response.Listener<ChatUserBean[]>() {
     @Override
     public void onResponse(ChatUserBean[] response) {

         progressBar.setVisibility(View.GONE);

            if (response!=null&&response.length!=0){

                HashMap<String,ArrayList<ChatUserBean>> stringArrayListHashMap=new HashMap<>();

                for (ChatUserBean chatUserBean:response){
                    if (stringArrayListHashMap.containsKey(chatUserBean.getSentBy())){
                        stringArrayListHashMap.get(chatUserBean.getSentBy()).add(chatUserBean);

                    }
                    else {
                        ArrayList<ChatUserBean> chatUserBeans=new ArrayList<>();
                        stringArrayListHashMap.put(chatUserBean.getSentBy(),chatUserBeans);
                        stringArrayListHashMap.get(chatUserBean.getSentBy()).add(chatUserBean);
                    }

                }


                recyclerView.setVisibility(View.VISIBLE);
                chatUserAdapter=new ChatUserAdapter(stringArrayListHashMap,getActivity());

                recyclerView.setAdapter(chatUserAdapter);
                OfferLinearLayoutManager linearLayoutManager=new OfferLinearLayoutManager(getActivity());

                recyclerView.setLayoutManager(linearLayoutManager);
            }
         else {
                errorTv.setVisibility(View.VISIBLE);
                errorTv.setText(getString(R.string.textNoChatsAvaliable));
            }


     }
 };

    private Response.ErrorListener errorListener=new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            PlusConnectUtils.logException(error);
            progressBar.setVisibility(View.GONE);
            errorTv.setText(getString(R.string.textSorry)+"\n"+getString(R.string.textErrorOccured));

        }
    };

}

