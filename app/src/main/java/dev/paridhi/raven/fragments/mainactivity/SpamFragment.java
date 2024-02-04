package dev.paridhi.raven.fragments.mainactivity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dev.paridhi.raven.R;
import dev.paridhi.raven.databinding.FragmentSpamBinding;

public class SpamFragment extends Fragment {

    FragmentSpamBinding binding;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding=FragmentSpamBinding.inflate(getLayoutInflater(),container,false);
        View view=binding.getRoot();
        return view;
    }
}