package com.comincini_micheli.quest4run.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.comincini_micheli.quest4run.R;
import com.comincini_micheli.quest4run.activity.EquipmentActivity;
import com.comincini_micheli.quest4run.other.Constants;
import com.comincini_micheli.quest4run.other.DatabaseHandler;
import com.comincini_micheli.quest4run.objects.Character;

import static android.app.Activity.RESULT_OK;
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
    public void onResume() {
        super.onResume();
        DatabaseHandler db = new DatabaseHandler(getContext());
        SharedPreferences settings = getActivity().getSharedPreferences(Constants.NAME_PREFS, Context.MODE_PRIVATE);
        Character myCharacter = db.getCharacter(settings.getInt(Constants.CHAR_ID_PREFERENCE,-1));

        TextView attack = (TextView) getActivity().findViewById(R.id.character_attack_value);
        attack.setText(String.valueOf(myCharacter.getAttack()));

        TextView defense = (TextView) getActivity().findViewById(R.id.character_defense_value);
        defense.setText(String.valueOf(myCharacter.getDefense()));

        TextView magic = (TextView) getActivity().findViewById(R.id.character_magic_value);
        magic.setText(String.valueOf(myCharacter.getMagic()));
    }

    /*
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Constants.OPEN_ATTACK_EQUIPMENTS||requestCode == Constants.OPEN_DEFENSE_EQUIPMENTS||requestCode == Constants.OPEN_MAGIC_EQUIPMENTS)
        {
            if(resultCode == RESULT_OK)
            {
                DatabaseHandler db = new DatabaseHandler(getContext());
                SharedPreferences settings = getActivity().getSharedPreferences(Constants.NAME_PREFS, Context.MODE_PRIVATE);
                Character myCharacter = db.getCharacter(settings.getInt(Constants.CHAR_ID_PREFERENCE,-1));

                TextView attack = (TextView) getActivity().findViewById(R.id.character_attack_value);
                attack.setText(String.valueOf(myCharacter.getAttack()));

                TextView defense = (TextView) getActivity().findViewById(R.id.character_defense_value);
                defense.setText(String.valueOf(myCharacter.getDefense()));

                TextView magic = (TextView) getActivity().findViewById(R.id.character_magic_value);
                magic.setText(String.valueOf(myCharacter.getMagic()));
            }
        }
    }
    */

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DatabaseHandler db = new DatabaseHandler(getContext());
        SharedPreferences settings = getActivity().getSharedPreferences(Constants.NAME_PREFS, Context.MODE_PRIVATE);
        Character myCharacter = db.getCharacter(settings.getInt(Constants.CHAR_ID_PREFERENCE,-1));

        TextView name = (TextView) getActivity().findViewById(R.id.character_name);
        name.setText(myCharacter.getName());

        ImageView gender = (ImageView) getActivity().findViewById(R.id.character_gender_icon);

        ImageView avatar = (ImageView) getActivity().findViewById(R.id.character_avatar);
        int[] femaleAvatar = {
                R.drawable.f_0,
                R.drawable.f_1,
                R.drawable.f_2,
        };

        int[] maleAvatar = {
                R.drawable.m_0,
                R.drawable.m_1,
                R.drawable.m_2,
        };
        int resIdAvatar,resIdGender;
        if(myCharacter.getGender()==0)
        {
            resIdAvatar = maleAvatar[myCharacter.getAvatar()];
            resIdGender = R.drawable.ic_gender_male_black_24dp;
        }
        else
        {
            resIdAvatar = femaleAvatar[myCharacter.getAvatar()];
            resIdGender = R.drawable.ic_gender_female_black_24dp;
        }
        avatar.setImageResource(resIdAvatar);
        gender.setImageResource(resIdGender);


        ProgressBar exp_bar = (ProgressBar) getActivity().findViewById(R.id.character_exp_bar);
        exp_bar.setMax(EXP_FOR_NEXT_LEVEL);
        exp_bar.setProgress(myCharacter.getExp()%EXP_FOR_NEXT_LEVEL);

        TextView wallet = (TextView) getActivity().findViewById(R.id.character_wallet_value);
        wallet.setText(String.valueOf(myCharacter.getWallet())+ getResources().getString(R.string.cents_symbol_label));

        TextView level = (TextView) getActivity().findViewById(R.id.character_level_value);
        level.setText(String.valueOf(myCharacter.getLevel()));

        TextView attack = (TextView) getActivity().findViewById(R.id.character_attack_value);
        attack.setText(String.valueOf(myCharacter.getAttack()));

        TextView defense = (TextView) getActivity().findViewById(R.id.character_defense_value);
        defense.setText(String.valueOf(myCharacter.getDefense()));

        TextView magic = (TextView) getActivity().findViewById(R.id.character_magic_value);
        magic.setText(String.valueOf(myCharacter.getMagic()));

        ImageButton attackEquipment = (ImageButton) getActivity().findViewById(R.id.image_btn_attack);
        attackEquipment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EquipmentActivity.class);
                Bundle b = new Bundle();
                b.putInt(Constants.ID_EQUIPMENT_TYPE, Constants.ID_TYPE_ATTACK);
                intent.putExtras(b);
                startActivity(intent);
            }
        });

        ImageButton defenceEquipment = (ImageButton) getActivity().findViewById(R.id.image_btn_defense);
        defenceEquipment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EquipmentActivity.class);
                Bundle b = new Bundle();
                b.putInt(Constants.ID_EQUIPMENT_TYPE, Constants.ID_TYPE_DEFENSE);
                intent.putExtras(b);
                startActivity(intent);
            }
        });

        ImageButton magicEquipment = (ImageButton) getActivity().findViewById(R.id.image_btn_magic);
        magicEquipment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EquipmentActivity.class);
                Bundle b = new Bundle();
                b.putInt(Constants.ID_EQUIPMENT_TYPE, Constants.ID_TYPE_MAGIC);
                intent.putExtras(b);
                startActivity(intent);
            }
        });


    }
}
