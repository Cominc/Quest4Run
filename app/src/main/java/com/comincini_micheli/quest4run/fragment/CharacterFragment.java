package com.comincini_micheli.quest4run.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.comincini_micheli.quest4run.R;
import com.comincini_micheli.quest4run.other.DatabaseHandler;
import com.comincini_micheli.quest4run.objects.Character;

import static com.comincini_micheli.quest4run.other.Constants.EXP_FOR_NEXT_LEVEL;


public class CharacterFragment extends Fragment
{
    public CharacterFragment()
    {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_character, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DatabaseHandler db = new DatabaseHandler(getContext());
        Character myCharacter = new Character();

        TextView name = (TextView)getActivity().findViewById(R.id.character_name);
        name.setText(myCharacter.getName());

        TextView level = (TextView)getActivity().findViewById(R.id.character_level_value);
        level.setText(String.valueOf(myCharacter.getLevel()));

        TextView attack = (TextView)getActivity().findViewById(R.id.character_attack_value);
        attack.setText(String.valueOf(myCharacter.getAttack()));

        TextView defense = (TextView)getActivity().findViewById(R.id.character_defense_value);
        defense.setText(String.valueOf(myCharacter.getDefence()));

        TextView magic = (TextView)getActivity().findViewById(R.id.character_magic_value);
        magic.setText(String.valueOf(myCharacter.getMagic()));

        ProgressBar exp_bar = (ProgressBar)getActivity().findViewById(R.id.character_exp_bar);
        exp_bar.setMax(EXP_FOR_NEXT_LEVEL);
        exp_bar.setProgress(myCharacter.getExp()%EXP_FOR_NEXT_LEVEL);
    }
}
