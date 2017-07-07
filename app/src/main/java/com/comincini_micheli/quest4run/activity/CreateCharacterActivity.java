package com.comincini_micheli.quest4run.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.comincini_micheli.quest4run.R;
import com.comincini_micheli.quest4run.objects.Character;
import com.comincini_micheli.quest4run.other.Constants;
import com.comincini_micheli.quest4run.other.DatabaseHandler;

public class CreateCharacterActivity extends AppCompatActivity
{
    private int[] femaleAvatar = {
            R.drawable.f_0,
            R.drawable.f_1,
            R.drawable.f_2,
    };

    private int[] maleAvatar = {
            R.drawable.m_0,
            R.drawable.m_1,
            R.drawable.m_2,
    };

    private String[] femaleAvatarTag;
    private String[] maleAvatarTag;

    private int[] avatarRes;
    private String[] avatarTag;
    private int avatarIndex;

    TextView nameTextEdit;


    //ProgressDialog nDialog;
    AlertDialog dialog;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_character);
        femaleAvatarTag = getResources().getStringArray(R.array.female_avatar_tag);
        maleAvatarTag = getResources().getStringArray(R.array.male_avatar_tag);

        avatarRes = maleAvatar;
        avatarTag = maleAvatarTag;
        avatarIndex = 0;

        ImageButton btn_left = (ImageButton) findViewById(R.id.update_characher_left_btn);
        ImageButton btn_right = (ImageButton) findViewById(R.id.update_character_right_btn);

        btn_left.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ImageView avatar = (ImageView) findViewById(R.id.create_character_avatar_img);
                String nomeTag = String.valueOf(avatar.getTag());
                if(nomeTag.equals(avatarTag[0]))
                {
                    avatarIndex = 2;
                }
                else if(nomeTag.equals(avatarTag[1]))
                {
                    avatarIndex = 0;
                }
                else if(nomeTag.equals(avatarTag[2]))
                {
                    avatarIndex = 1;
                }
                avatar.setImageResource(avatarRes[avatarIndex]);
                avatar.setTag(avatarTag[avatarIndex]);
            }
        });

        btn_right.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ImageView avatar = (ImageView) findViewById(R.id.create_character_avatar_img);
                String nomeTag = String.valueOf(avatar.getTag());

                if(nomeTag.equals(avatarTag[0]))
                {
                    avatarIndex = 1;
                }
                else if(nomeTag.equals(avatarTag[1]))
                {
                    avatarIndex = 2;
                }
                else if(nomeTag.equals(avatarTag[2]))
                {
                    avatarIndex = 0;
                }
                avatar.setImageResource(avatarRes[avatarIndex]);
                avatar.setTag(avatarTag[avatarIndex]);
            }
        });

        final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radio_gender_selection);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId)
                {
                    case R.id.create_character_male_radiobtn:
                        avatarRes = maleAvatar;
                        avatarTag = maleAvatarTag;
                        break;

                    case R.id.create_character_female_radiobtn:
                        avatarRes = femaleAvatar;
                        avatarTag = femaleAvatarTag;
                        break;

                    default:
                        break;
                }
                avatarIndex = 0;
                ImageView avatar = (ImageView) findViewById(R.id.create_character_avatar_img);
                avatar.setImageResource(avatarRes[avatarIndex]);
                avatar.setTag(avatarTag[avatarIndex]);
            }
        });

        nameTextEdit = (TextView)findViewById(R.id.create_character_insert_name);

        Button createBtn = (Button) findViewById(R.id.btn_create_character);
        context = this;
        createBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog = new AlertDialog.Builder(context).create();
                dialog.setMessage(getResources().getString(R.string.loading));
                dialog.show();
                /*nDialog = new ProgressDialog(context);
                nDialog.setMessage(getResources().getString(R.string.loading));
                nDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                nDialog.setIndeterminate(true);
                nDialog.setCancelable(false);
                nDialog.show();*/

                String newCharacterName = nameTextEdit.getText().toString().trim();
                if(newCharacterName.length() != 0)
                {


                    int gender;
                    if(radioGroup.getCheckedRadioButtonId() == R.id.create_character_male_radiobtn)
                        gender = 0;
                    else
                        gender = 1;


                    Character newCharacter = new Character(newCharacterName, gender, avatarIndex);
                    Log.w("avatarId", avatarIndex+"");

                    DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                    int newCharacterID = db.addCharacter(newCharacter);
                    SharedPreferences.Editor firstLaunchSetting = getSharedPreferences(Constants.NAME_PREFS, MODE_PRIVATE).edit();
                    firstLaunchSetting.putInt(Constants.CHAR_ID_PREFERENCE, newCharacterID);
                    if(firstLaunchSetting.commit())
                    {
                        Intent i = new Intent(CreateCharacterActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                        //nDialog.dismiss();
                    }
                    else
                    {
                        Log.w("Errore","Salvataggio non riuscito");
                    }
                }
                else
                {
                    nameTextEdit.setError(getResources().getString(R.string.name_empty_character_error));
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.info_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.info_button:
                showInfo();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showInfo()
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Titolo");
        alert.setMessage("Prova");
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

            }
        });
        alert.show();
    }

}
