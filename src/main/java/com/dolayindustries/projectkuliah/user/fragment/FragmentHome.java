package com.dolayindustries.projectkuliah.user.fragment;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dolayindustries.projectkuliah.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentHome extends Fragment {
    private AnimationDrawable animationDrawable;

    public FragmentHome() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home_user, container, false);
        CardView cardViewGradient = view.findViewById(R.id.card_view_untuk_gradient);
        cardViewGradient.setBackgroundResource(R.drawable.gradient_animation_sambutan_user);
        animationDrawable = (AnimationDrawable) cardViewGradient.getBackground();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (animationDrawable != null && !animationDrawable.isRunning()){
            animationDrawable.start();
        }
    }

    @Override
    public void onPause() {

        super.onPause();
        if (animationDrawable != null && animationDrawable.isRunning()){
            animationDrawable.stop();
        }
    }
}
