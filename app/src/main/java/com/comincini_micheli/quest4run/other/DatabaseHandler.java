package com.comincini_micheli.quest4run.other;

    import android.content.ContentValues;
    import android.content.Context;
    import android.database.Cursor;
    import android.database.sqlite.SQLiteDatabase;
    import android.database.sqlite.SQLiteOpenHelper;
    import android.util.Log;

    import com.comincini_micheli.quest4run.R;
    import com.comincini_micheli.quest4run.objects.Gps;
    import com.comincini_micheli.quest4run.objects.Quest;
    import com.comincini_micheli.quest4run.objects.Task;
    import com.comincini_micheli.quest4run.objects.Character;
    import com.comincini_micheli.quest4run.objects.Equipment;

    import org.w3c.dom.Document;
    import org.w3c.dom.Element;
    import org.w3c.dom.NodeList;
    import org.xmlpull.v1.XmlPullParser;
    import org.xmlpull.v1.XmlPullParserException;

    import java.io.ByteArrayOutputStream;
    import java.io.IOException;
    import java.io.InputStream;
    import java.io.UnsupportedEncodingException;
    import java.util.ArrayList;
    import java.util.List;

/**
 * Created by Daniele on 17/05/2017.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private Context context;

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 6;

    // Database Name
    private static final String DATABASE_NAME = "Quest4Run";

    // List table name
    private static final String TABLE_EQUIPMENT = "equipment";
    private static final String TABLE_TASK = "task";
    private static final String TABLE_CHARACTER = "character";
    private static final String TABLE_QUEST = "quest";

    // Temporary table
    private static final String TABLE_GPS = "gps";

    // EQUIPMENT Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_ATK = "attack";
    private static final String KEY_DEF = "defense";
    private static final String KEY_MGC = "magic";
    private static final String KEY_PRICE = "price";
    private static final String KEY_ICON = "icon";

    private static final String KEY_GOAL = "objective";
    private static final String KEY_REWARD = "reward";
    private static final String KEY_ID_TASK_TYPE = "idTaskType";
    private static final String KEY_COMPLETED = "completed";
    private static final String KEY_ACTIVE = "active";
    private static final String KEY_PROGRESS = "progress";
    private static final String KEY_EXEC_DATE = "execDate";

    private static final String KEY_GENDER = "gender";
    private static final String KEY_AVATAR = "avatar";
    private static final String KEY_EXP = "exp";
    private static final String KEY_WALLET = "wallet";

    private static final String KEY_ID_EQUIPMENT_TYPE = "idEquipmentType";
    private static final String KEY_BOUGHT = "bought";
    private static final String KEY_EQUIPPED = "equipped";

    private static final String KEY_TITLE = "title";
    private static final String KEY_DURATION = "duration";
    private static final String KEY_START_DATE = "startDate";
    private static final String KEY_FINISH_DATE = "finishDate";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_EXP_REWARD = "expReward";

    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";
    private static final String KEY_ALTITUDE = "altitude";
    private static final String KEY_TIME = "time";




    //Create QUERIES
    private static final String CREATE_EQUIPMENT_TABLE = "CREATE TABLE " + TABLE_EQUIPMENT + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NAME + " TEXT,"
            + KEY_ID_EQUIPMENT_TYPE + " INTEGER," + KEY_ATK + " INTEGER," + KEY_DEF + " INTEGER,"
            + KEY_MGC + " INTEGER," + KEY_PRICE + " INTEGER," + KEY_ICON + " TEXT,"
            + KEY_BOUGHT + " INTEGER," + KEY_EQUIPPED + " INTEGER"
            + ")";
    private static final String CREATE_TASK_TABLE = "CREATE TABLE " + TABLE_TASK + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NAME + " TEXT,"
            + KEY_REWARD + " INTEGER," + KEY_ID_TASK_TYPE + " INTEGER,"
            + KEY_GOAL + " NUMERIC," + KEY_COMPLETED + " INTEGER,"
            + KEY_ACTIVE + " INTEGER, " + KEY_PROGRESS + " REAL, " + KEY_EXEC_DATE + " TEXT" +
             ")";
    private static final String CREATE_CHARACTER_TABLE = "CREATE TABLE " + TABLE_CHARACTER + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NAME + " TEXT," + KEY_GENDER + " INTEGER," + KEY_AVATAR + " INTEGER,"
            + KEY_EXP + " INTEGER, " + KEY_ATK + " INTEGER," + KEY_DEF + " INTEGER," + KEY_MGC + " INTEGER," + KEY_WALLET + " INTEGER"
            + ")";

    private static final String CREATE_QUEST_TABLE = " CREATE TABLE " + TABLE_QUEST + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_TITLE + " TEXT," + KEY_DESCRIPTION + " TEXT," + KEY_COMPLETED + " INTEGER,"
            + KEY_ACTIVE + " INTEGER," + KEY_ATK + " INTEGER," + KEY_DEF + " INTEGER," + KEY_MGC + " INTEGER," + KEY_EXP_REWARD + " INTEGER,"
            + KEY_DURATION + " INTEGER," + KEY_START_DATE + " TEXT," + KEY_FINISH_DATE + " TEXT"
            + ")";

    private static final String CREATE_GPS_TABLE = " CREATE TABLE " + TABLE_GPS + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_LATITUDE + " TEXT," + KEY_LONGITUDE + " TEXT," + KEY_ALTITUDE + " INTEGER,"
            + KEY_TIME + " INTEGER"
            + ")";

    public DatabaseHandler(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.w("equipment: ", CREATE_EQUIPMENT_TABLE);
        db.execSQL(CREATE_EQUIPMENT_TABLE);
        Log.w("task: ", CREATE_TASK_TABLE);
        db.execSQL(CREATE_TASK_TABLE);
        Log.w("character: ", CREATE_CHARACTER_TABLE);
        db.execSQL(CREATE_CHARACTER_TABLE);
        Log.w("quest: ", CREATE_QUEST_TABLE);
        db.execSQL(CREATE_QUEST_TABLE);

        Log.w("gps: ", CREATE_GPS_TABLE);
        db.execSQL(CREATE_GPS_TABLE);
    }

    public void loadEquipmentfromXml()
    {
        XMLParser parser = new XMLParser();
        String equipmentXml = parser.getStringfromXml(context.getResources().openRawResource(R.raw.equipment));
        Document equipmentDoc = parser.getDomElement(equipmentXml);

        NodeList nl = equipmentDoc.getElementsByTagName("equipment");
        Element e;
        Equipment equipment;

        for(int i = 0; i < nl.getLength(); i++)
        {
            e = (Element) nl.item(i);
            equipment = new Equipment();
            equipment.setName(parser.getValue(e, KEY_NAME));
            equipment.setAtk(Integer.parseInt(parser.getValue(e, KEY_ATK)));
            equipment.setDef(Integer.parseInt(parser.getValue(e, KEY_DEF)));
            equipment.setMgc(Integer.parseInt(parser.getValue(e, KEY_MGC)));
            equipment.setPrice(Integer.parseInt(parser.getValue(e, KEY_PRICE)));
            equipment.setIdType(Integer.parseInt(parser.getValue(e, KEY_ID_EQUIPMENT_TYPE)));
            addEquipment(equipment);
        }
    }

    public void loadQuestfromXml()
    {
        XMLParser parser = new XMLParser();
        String questXml = parser.getStringfromXml(context.getResources().openRawResource(R.raw.quest));
        Document questDoc = parser.getDomElement(questXml);

        NodeList nl = questDoc.getElementsByTagName("quest");
        Element e;
        Quest quest;

        for(int i = 0; i < nl.getLength(); i++)
        {
            e = (Element) nl.item(i);
            quest = new Quest();
            quest.setTitle(parser.getValue(e, KEY_TITLE));
            quest.setDescription(parser.getValue(e, KEY_DESCRIPTION));
            quest.setMinAttack(Integer.parseInt(parser.getValue(e, KEY_ATK)));
            quest.setMinDefense(Integer.parseInt(parser.getValue(e, KEY_DEF)));
            quest.setMinMagic(Integer.parseInt(parser.getValue(e, KEY_MGC)));
            quest.setExpReward(Integer.parseInt(parser.getValue(e, KEY_EXP_REWARD)));
            quest.setDuration(Integer.parseInt(parser.getValue(e, KEY_DURATION)));
            addQuest(quest);
        }

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EQUIPMENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHARACTER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUEST);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GPS);
        // Create tables again
        onCreate(db);
    }

    private boolean castStringToBoolean(String s) {
        return s.equals("1");
    }


    //TASK METHODS

    //Add Task
    public int addTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, task.getName());
        values.put(KEY_REWARD, task.getReward());
        values.put(KEY_ID_TASK_TYPE, task.getIdTaskType());
        values.put(KEY_GOAL, task.getGoal());
        values.put(KEY_COMPLETED, task.isCompleted());
        values.put(KEY_ACTIVE, task.isActive());
        values.put(KEY_PROGRESS, task.getProgress());
        values.put(KEY_EXEC_DATE, task.getExecDate());

        // Inserting Row
        int id = (int) db.insert(TABLE_TASK, null, values);
        db.close(); // Closing database connection
        return id;
    }

    //Getting single Task
    Task getTask(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_TASK, new String[] { KEY_ID,
                        KEY_NAME, KEY_REWARD, KEY_ID_TASK_TYPE, KEY_GOAL, KEY_COMPLETED, KEY_ACTIVE,
                        KEY_PROGRESS, KEY_EXEC_DATE}, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Task task = new Task(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), Integer.parseInt(cursor.getString(2)),
                Integer.parseInt(cursor.getString(3)),cursor.getString(4),
                castStringToBoolean(cursor.getString(5)),castStringToBoolean(cursor.getString(6)),
                Double.parseDouble(cursor.getString(7)),Long.parseLong(cursor.getString(8)));
        cursor.close();
        // return Task
        return task;
    }

    // Getting All tasks
    public List<Task> getAllTasks() {
        List<Task> taskList = new ArrayList<Task>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_TASK;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Task task = new Task();
                task.setId(Integer.parseInt(cursor.getString(0)));
                task.setName(cursor.getString(1));
                task.setReward(Integer.parseInt(cursor.getString(2)));
                task.setIdTaskType(Integer.parseInt(cursor.getString(3)));
                task.setGoal(cursor.getString(4));
                task.setCompleted(castStringToBoolean(cursor.getString(5)));
                task.setActive(castStringToBoolean(cursor.getString(6)));
                task.setProgress(Double.parseDouble(cursor.getString(7)));
                task.setExecDate(Long.parseLong(cursor.getString(8)));
                // Adding Task to list
                taskList.add(task);
            } while (cursor.moveToNext());
        }
        cursor.close();

        // return Task list
        return taskList;
    }

    public List<Task> getTasks(boolean show_completed) {
        List<Task> taskList = new ArrayList<Task>();
        int _completed;
        if(show_completed)
            _completed = 1;
        else
            _completed = 0;

        String selectQuery = "SELECT  * FROM " + TABLE_TASK + " WHERE " + KEY_COMPLETED + " = " + _completed;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Task task = new Task();
                task.setId(Integer.parseInt(cursor.getString(0)));
                task.setName(cursor.getString(1));
                task.setReward(Integer.parseInt(cursor.getString(2)));
                task.setIdTaskType(Integer.parseInt(cursor.getString(3)));
                task.setGoal(cursor.getString(4));
                task.setCompleted(castStringToBoolean(cursor.getString(5)));
                task.setActive(castStringToBoolean(cursor.getString(6)));
                task.setProgress(Double.parseDouble(cursor.getString(7)));
                task.setExecDate(Long.parseLong(cursor.getString(8)));
                // Adding Task to list
                taskList.add(task);
            } while (cursor.moveToNext());
        }
        cursor.close();
        // return Task list
        return taskList;
    }

    public List<Task> getTasks(boolean show_completed, int taskType) {
        List<Task> taskList = new ArrayList<Task>();
        int _completed;
        if(show_completed)
            _completed = 1;
        else
            _completed = 0;

        String selectQuery = "SELECT  * FROM " + TABLE_TASK + " WHERE " + KEY_COMPLETED + " = " + _completed +
        " AND " + KEY_ID_TASK_TYPE + " = " + taskType;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Task task = new Task();
                task.setId(Integer.parseInt(cursor.getString(0)));
                task.setName(cursor.getString(1));
                task.setReward(Integer.parseInt(cursor.getString(2)));
                task.setIdTaskType(Integer.parseInt(cursor.getString(3)));
                task.setGoal(cursor.getString(4));
                task.setCompleted(castStringToBoolean(cursor.getString(5)));
                task.setActive(castStringToBoolean(cursor.getString(6)));
                task.setProgress(Double.parseDouble(cursor.getString(7)));
                task.setExecDate(Long.parseLong(cursor.getString(8)));
                // Adding Task to list
                taskList.add(task);
            } while (cursor.moveToNext());
        }
        cursor.close();
        // return Task list
        return taskList;
    }

    public List<Task> getTasks(boolean show_completed,boolean active, int taskType) {
        List<Task> taskList = new ArrayList<Task>();
        int _completed;
        if(show_completed)
            _completed = 1;
        else
            _completed = 0;

        int _active;
        if(active)
            _active = 1;
        else
            _active = 0;

        String selectQuery = "SELECT  * FROM " + TABLE_TASK + " WHERE " + KEY_COMPLETED + " = " + _completed + " AND " +
                 KEY_ACTIVE + " = " + _active + " AND " + KEY_ID_TASK_TYPE + " = " + taskType;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Task task = new Task();
                task.setId(Integer.parseInt(cursor.getString(0)));
                task.setName(cursor.getString(1));
                task.setReward(Integer.parseInt(cursor.getString(2)));
                task.setIdTaskType(Integer.parseInt(cursor.getString(3)));
                task.setGoal(cursor.getString(4));
                task.setCompleted(castStringToBoolean(cursor.getString(5)));
                task.setActive(castStringToBoolean(cursor.getString(6)));
                task.setProgress(Double.parseDouble(cursor.getString(7)));
                task.setExecDate(Long.parseLong(cursor.getString(8)));
                // Adding Task to list
                taskList.add(task);
            } while (cursor.moveToNext());
        }
        cursor.close();
        // return Task list
        return taskList;
    }

    // Updating single Task
    public int updateTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, task.getName());
        values.put(KEY_REWARD, task.getReward());
        values.put(KEY_ID_TASK_TYPE, task.getIdTaskType());
        values.put(KEY_GOAL, task.getGoal());
        values.put(KEY_PROGRESS, task.getProgress());
        values.put(KEY_EXEC_DATE, task.getExecDate());

        int completed_t = 0;
        if(task.isCompleted())
            completed_t = 1;
        values.put(KEY_COMPLETED, completed_t);

        int active_t = 0;
        if(task.isActive())
            active_t = 1;
        values.put(KEY_ACTIVE, active_t);

        // updating row
        return db.update(TABLE_TASK, values, KEY_ID + " = ?",
                new String[] { String.valueOf(task.getId()) });
    }

    // Deleting single Task
    public void deleteTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TASK, KEY_ID + " = ?",
                new String[]{String.valueOf(task.getId())});
        db.close();
    }

    public int deleteAllTasks() {
        SQLiteDatabase db = this.getWritableDatabase();
        int deleted = db.delete(TABLE_TASK, "1" , null);
        db.close();
        return deleted;
    }

    public int deleteAllTasks(boolean completed) {
        SQLiteDatabase db = this.getWritableDatabase();
        String _completed;
        if(completed)
            _completed = "1";
        else
            _completed = "0";
        String[] whereArgs = {_completed};
        int deleted = db.delete(TABLE_TASK, KEY_COMPLETED+" = ?" , whereArgs);
        db.close();
        return deleted;
    }

    // Getting Task Count
    public int getTaskCount() {
        String countQuery = "SELECT  * FROM " + TABLE_TASK;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    //CHARACTER METHODS

    public int addCharacter(Character character) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, character.getName());
        values.put(KEY_GENDER, character.getGender());
        values.put(KEY_AVATAR, character.getAvatar());
        values.put(KEY_EXP, character.getExp());
        values.put(KEY_ATK, character.getAttack());
        values.put(KEY_DEF, character.getDefense());
        values.put(KEY_MGC, character.getMagic());
        values.put(KEY_WALLET, character.getWallet());

        // Inserting Row
        int addedCharacterId = (int) db.insert(TABLE_CHARACTER, null, values);
        db.close(); // Closing database connection
        return addedCharacterId;
    }

    //Getting single Character
    public Character getCharacter(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CHARACTER, new String[] { KEY_ID,
                        KEY_NAME, KEY_GENDER, KEY_AVATAR, KEY_EXP, KEY_ATK, KEY_DEF, KEY_MGC, KEY_WALLET }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Character character = new Character(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), Integer.parseInt(cursor.getString(2)),
                Integer.parseInt(cursor.getString(3)),Integer.parseInt(cursor.getString(4)),
                Integer.parseInt(cursor.getString(5)),Integer.parseInt(cursor.getString(6)),
                Integer.parseInt(cursor.getString(7)),Integer.parseInt(cursor.getString(8)));
        cursor.close();
        // return Character
        return character;
    }

    // Updating single Character
    public int updateCharacter(Character character) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, character.getName());
        values.put(KEY_GENDER, character.getGender());
        values.put(KEY_AVATAR, character.getAvatar());
        values.put(KEY_EXP, character.getExp());
        values.put(KEY_ATK, character.getAttack());
        values.put(KEY_DEF, character.getDefense());
        values.put(KEY_MGC, character.getMagic());
        values.put(KEY_WALLET, character.getWallet());

        // updating row
        return db.update(TABLE_CHARACTER, values, KEY_ID + " = ?",
                new String[] { String.valueOf(character.getId()) });
    }

    // Deleting single Character
    public void deleteCharacter(Character character) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CHARACTER, KEY_ID + " = ?",
                new String[]{String.valueOf(character.getId())});
        db.close();
    }

    public void equipEquipment(Equipment equipment, int idCharacter)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        updateEquipment(equipment);
        Character me = getCharacter(idCharacter);
        me.setAttack(me.getAttack()+equipment.getAtk());
        me.setDefense(me.getDefense()+equipment.getDef());
        me.setMagic(me.getMagic()+equipment.getMgc());
        updateCharacter(me);
    }

    public void unequipEquipment(Equipment equipment, int idCharacter)
    {
        updateEquipment(equipment);
        Character me = getCharacter(idCharacter);
        me.setAttack(me.getAttack()-equipment.getAtk());
        me.setDefense(me.getDefense()-equipment.getDef());
        me.setMagic(me.getMagic()-equipment.getMgc());
        updateCharacter(me);
    }


    //EQUIPMENT METHODS


    public void addEquipment(Equipment equipment) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, equipment.getName());
        values.put(KEY_ID_EQUIPMENT_TYPE, equipment.getIdType());
        values.put(KEY_ATK, equipment.getAtk());
        values.put(KEY_DEF, equipment.getDef());
        values.put(KEY_MGC, equipment.getMgc());
        values.put(KEY_PRICE, equipment.getPrice());
        values.put(KEY_ICON, equipment.getIcon());
        values.put(KEY_BOUGHT, equipment.isBought());
        values.put(KEY_EQUIPPED, equipment.isEquipped());

        // Inserting Row
        db.insert(TABLE_EQUIPMENT, null, values);
        db.close(); // Closing database connection
    }

    //Getting single Equipment
    Equipment getEquipment(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_EQUIPMENT, new String[] { KEY_ID,
                        KEY_NAME, KEY_ID_EQUIPMENT_TYPE, KEY_ATK, KEY_DEF, KEY_MGC, KEY_PRICE, KEY_ICON, KEY_BOUGHT, KEY_EQUIPPED }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Equipment equipment = new Equipment(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), Integer.parseInt(cursor.getString(2)),
                Integer.parseInt(cursor.getString(3)),Integer.parseInt(cursor.getString(4)),
                Integer.parseInt(cursor.getString(5)),Integer.parseInt(cursor.getString(6)),cursor.getString(7),
                castStringToBoolean(cursor.getString(8)),castStringToBoolean(cursor.getString(9)));
        cursor.close();
        // return Equipment
        return equipment;
    }

    // Getting All EQUIPMENTS
    public List<Equipment> getAllEquipments() {
        List<Equipment> equipmentList = new ArrayList<Equipment>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_EQUIPMENT;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Equipment equipment = new Equipment();
                equipment.setId(Integer.parseInt(cursor.getString(0)));
                equipment.setName(cursor.getString(1));
                equipment.setIdType(Integer.parseInt(cursor.getString(2)));
                equipment.setAtk(Integer.parseInt(cursor.getString(3)));
                equipment.setDef(Integer.parseInt(cursor.getString(4)));
                equipment.setMgc(Integer.parseInt(cursor.getString(5)));
                equipment.setPrice(Integer.parseInt(cursor.getString(6)));
                equipment.setIcon(cursor.getString(7));
                equipment.setBought(castStringToBoolean(cursor.getString(8)));
                equipment.setEquipped(castStringToBoolean(cursor.getString(9)));
                // Adding Equipment to list
                equipmentList.add(equipment);
            } while (cursor.moveToNext());
        }

        cursor.close();
        // return Equipment list
        return equipmentList;
    }

    public List<Equipment> getAllEquipments(int idType, boolean bought) {
        List<Equipment> equipmentList = new ArrayList<Equipment>();
        int _bought;
        if(bought)
            _bought = 1;
        else
            _bought = 0;
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_EQUIPMENT
                + " WHERE " + KEY_ID_EQUIPMENT_TYPE + " = " + idType
                + " AND " + KEY_BOUGHT + " = " + _bought;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Equipment equipment = new Equipment();
                equipment.setId(Integer.parseInt(cursor.getString(0)));
                equipment.setName(cursor.getString(1));
                equipment.setIdType(Integer.parseInt(cursor.getString(2)));
                equipment.setAtk(Integer.parseInt(cursor.getString(3)));
                equipment.setDef(Integer.parseInt(cursor.getString(4)));
                equipment.setMgc(Integer.parseInt(cursor.getString(5)));
                equipment.setPrice(Integer.parseInt(cursor.getString(6)));
                equipment.setIcon(cursor.getString(7));
                equipment.setBought(castStringToBoolean(cursor.getString(8)));
                equipment.setEquipped(castStringToBoolean(cursor.getString(9)));
                // Adding Equipment to list
                equipmentList.add(equipment);
            } while (cursor.moveToNext());
        }
        cursor.close();
        // return Equipment list
        return equipmentList;
    }

    // Updating single Equipment
    public int updateEquipment(Equipment equipment) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, equipment.getName());
        values.put(KEY_ID_EQUIPMENT_TYPE, equipment.getIdType());
        values.put(KEY_ATK, equipment.getAtk());
        values.put(KEY_DEF, equipment.getDef());
        values.put(KEY_MGC, equipment.getMgc());
        values.put(KEY_PRICE, equipment.getPrice());
        values.put(KEY_ICON, equipment.getIcon());
        values.put(KEY_BOUGHT, equipment.isBought());
        values.put(KEY_EQUIPPED, equipment.isEquipped());
        // updating row
        return db.update(TABLE_EQUIPMENT, values, KEY_ID + " = ?",
                new String[] { String.valueOf(equipment.getId()) });
    }

    // Deleting single Equipment
    public void deleteEquipment(Equipment equipment) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EQUIPMENT, KEY_ID + " = ?",
                new String[]{String.valueOf(equipment.getId())});
        db.close();
    }


    // Getting Equipment Count
    public int getEquipmentCount() {
        String countQuery = "SELECT  * FROM " + TABLE_EQUIPMENT;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    //QUEST

    public int addQuest(Quest quest) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, quest.getTitle());
        values.put(KEY_DESCRIPTION, quest.getDescription());
        values.put(KEY_COMPLETED, quest.isCompleted());
        values.put(KEY_ACTIVE, quest.isActive());
        values.put(KEY_ATK, quest.getMinAttack());
        values.put(KEY_DEF, quest.getMinDefense());
        values.put(KEY_MGC, quest.getMinMagic());
        values.put(KEY_EXP_REWARD, quest.getExpReward());
        values.put(KEY_DURATION, quest.getDuration());
        values.put(KEY_START_DATE, quest.getDateStart());
        values.put(KEY_FINISH_DATE, quest.getDateFinish());

        // Inserting Row
        int id = (int) db.insert(TABLE_QUEST, null, values);
        db.close(); // Closing database connection
        return id;
    }

    //Getting single Task
    Quest getQuest(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_QUEST, new String[] { KEY_ID,
                        KEY_TITLE, KEY_DESCRIPTION, KEY_COMPLETED, KEY_ACTIVE, KEY_ATK, KEY_DEF, KEY_MGC, KEY_EXP_REWARD,
                        KEY_DURATION, KEY_START_DATE, KEY_FINISH_DATE}, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Quest quest = new Quest(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2),
                castStringToBoolean(cursor.getString(3)),castStringToBoolean(cursor.getString(4)),
                Integer.parseInt(cursor.getString(5)),Integer.parseInt(cursor.getString(6)),
                Integer.parseInt(cursor.getString(7)),Integer.parseInt(cursor.getString(8)),
                Integer.parseInt(cursor.getString(9)),Long.parseLong(cursor.getString(10)),
                Long.parseLong(cursor.getString(11)));
        cursor.close();
        return quest;
    }

    // Getting All tasks
    public List<Quest> getAllQuests() {
        List<Quest> questList = new ArrayList<Quest>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_QUEST;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Quest quest = new Quest();
                quest.setId(Integer.parseInt(cursor.getString(0)));
                quest.setTitle(cursor.getString(1));
                quest.setDescription(cursor.getString(2));
                quest.setCompleted(castStringToBoolean(cursor.getString(3)));
                quest.setActive(castStringToBoolean(cursor.getString(4)));
                quest.setMinAttack(Integer.parseInt(cursor.getString(5)));
                quest.setMinDefense(Integer.parseInt(cursor.getString(6)));
                quest.setMinMagic(Integer.parseInt(cursor.getString(7)));
                quest.setExpReward(Integer.parseInt(cursor.getString(8)));
                quest.setDuration(Integer.parseInt(cursor.getString(9)));
                quest.setDateStart(Long.parseLong(cursor.getString(10)));
                quest.setDateFinish(Long.parseLong(cursor.getString(11)));

                questList.add(quest);
            } while (cursor.moveToNext());
        }
        cursor.close();

        // return Task list
        return questList;
    }

    public Quest getActiveQuest()
    {
        String selectQuery = "SELECT  * FROM " + TABLE_QUEST + " WHERE " + KEY_ACTIVE + " == " + 1;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.moveToFirst())
        {
            Quest quest = new Quest();
            quest.setId(Integer.parseInt(cursor.getString(0)));
            quest.setTitle(cursor.getString(1));
            quest.setDescription(cursor.getString(2));
            quest.setCompleted(castStringToBoolean(cursor.getString(3)));
            quest.setActive(castStringToBoolean(cursor.getString(4)));
            quest.setMinAttack(Integer.parseInt(cursor.getString(5)));
            quest.setMinDefense(Integer.parseInt(cursor.getString(6)));
            quest.setMinMagic(Integer.parseInt(cursor.getString(7)));
            quest.setExpReward(Integer.parseInt(cursor.getString(8)));
            quest.setDuration(Integer.parseInt(cursor.getString(9)));
            quest.setDateStart(Long.parseLong(cursor.getString(10)));
            quest.setDateFinish(Long.parseLong(cursor.getString(11)));
            return quest;
        }
        else return null;
    }

    public List<Quest> getQuests(boolean show_completed) {
        List<Quest> questList = new ArrayList<Quest>();
        int _completed;
        if(show_completed)
            _completed = 1;
        else
            _completed = 0;

        String selectQuery = "SELECT  * FROM " + TABLE_QUEST + " WHERE " + KEY_COMPLETED + " == " + _completed;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Quest quest = new Quest();
                quest.setId(Integer.parseInt(cursor.getString(0)));
                quest.setTitle(cursor.getString(1));
                quest.setDescription(cursor.getString(2));
                quest.setCompleted(castStringToBoolean(cursor.getString(3)));
                quest.setActive(castStringToBoolean(cursor.getString(4)));
                quest.setMinAttack(Integer.parseInt(cursor.getString(5)));
                quest.setMinDefense(Integer.parseInt(cursor.getString(6)));
                quest.setMinMagic(Integer.parseInt(cursor.getString(7)));
                quest.setExpReward(Integer.parseInt(cursor.getString(8)));
                quest.setDuration(Integer.parseInt(cursor.getString(9)));
                quest.setDateStart(Long.parseLong(cursor.getString(10)));
                quest.setDateFinish(Long.parseLong(cursor.getString(11)));

                questList.add(quest);

            } while (cursor.moveToNext());
        }
        cursor.close();
        // return Task list
        return questList;
    }

    // Updating single Task
    public int updateQuest(Quest quest) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_TITLE, quest.getTitle());
        values.put(KEY_DESCRIPTION, quest.getDescription());
        values.put(KEY_COMPLETED, quest.isCompleted());
        values.put(KEY_ACTIVE, quest.isActive());
        values.put(KEY_ATK, quest.getMinAttack());
        values.put(KEY_DEF, quest.getMinDefense());
        values.put(KEY_MGC, quest.getMinMagic());
        values.put(KEY_EXP_REWARD, quest.getExpReward());
        values.put(KEY_DURATION, quest.getDuration());
        values.put(KEY_START_DATE, quest.getDateStart());
        values.put(KEY_FINISH_DATE, quest.getDateFinish());

        // updating row
        return db.update(TABLE_QUEST, values, KEY_ID + " = ?",
                new String[] { String.valueOf(quest.getId()) });
    }

    public void deleteQuest(Quest quest) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_QUEST, KEY_ID + " = ?",
                new String[]{String.valueOf(quest.getId())});
        db.close();
    }

    public int deleteAllQuests() {
        SQLiteDatabase db = this.getWritableDatabase();
        int deleted = db.delete(TABLE_QUEST, "1" , null);
        db.close();
        return deleted;
    }


    public int getQuestCount() {
        String countQuery = "SELECT  * FROM " + TABLE_QUEST;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    //GPS

    public int addGps(Gps gps) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_LATITUDE, gps.getLatitude());
        values.put(KEY_LONGITUDE, gps.getLongitude());
        values.put(KEY_ALTITUDE, gps.getAltitude());
        values.put(KEY_TIME, gps.getTime());

        // Inserting Row
        int id = (int) db.insert(TABLE_GPS, null, values);
        db.close(); // Closing database connection
        return id;
    }

    //Getting single Task
    Gps getGps(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_GPS, new String[] { KEY_ID,
                        KEY_LATITUDE, KEY_LONGITUDE, KEY_ALTITUDE, KEY_TIME}, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Gps gps = new Gps(Integer.parseInt(cursor.getString(0)), cursor.getString(1),
                cursor.getString(2), Integer.parseInt(cursor.getString(3)),
                Integer.parseInt(cursor.getString(4)));
        cursor.close();
        return gps;
    }

    // Getting All tasks
    public List<Gps> getAllGps() {
        List<Gps> gpsList = new ArrayList<Gps>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_GPS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Gps gps = new Gps();
                gps.setId(Integer.parseInt(cursor.getString(0)));
                gps.setLatitude(cursor.getString(1));
                gps.setLongitude(cursor.getString(2));
                gps.setAltitude(Integer.parseInt(cursor.getString(3)));
                gps.setTime(Integer.parseInt(cursor.getString(4)));

                gpsList.add(gps);
            } while (cursor.moveToNext());
        }
        cursor.close();

        // return Gps list
        return gpsList;
    }

    // Updating single Gps
    public int updateGps(Gps gps) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_LATITUDE, gps.getLatitude());
        values.put(KEY_LONGITUDE, gps.getLongitude());
        values.put(KEY_ALTITUDE, gps.getAltitude());
        values.put(KEY_TIME, gps.getTime());

        // updating row
        return db.update(TABLE_GPS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(gps.getId()) });
    }

    public void deleteGps(Gps gps) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_GPS, KEY_ID + " = ?",
                new String[]{String.valueOf(gps.getId())});
        db.close();
    }

    public int deleteAllGps() {
        SQLiteDatabase db = this.getWritableDatabase();
        int deleted = db.delete(TABLE_GPS, "1" , null);
        db.close();
        return deleted;
    }


    public int getGpsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_GPS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        //cursor.close();
        //TODO attenzione se chiudo il cursor e poi ci applico in metodo ottengo crash!!! (DA CORREGGERE ANCHE NEGLI ALTRI METODI)

        // return count
        return cursor.getCount();
    }
}
