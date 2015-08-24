package com.plusconnect.fragment;

/**
 * Created by asuss on 8/22/2015.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import com.plusconnect.chat.R;
import android.view.ViewGroup;
public class ChatsFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_chat,container,false);
        return v;
    }
}
