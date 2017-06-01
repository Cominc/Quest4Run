package com.comincini_micheli.quest4run.other;

    import android.content.ContentValues;
    import android.content.Context;
    import android.database.Cursor;
    import android.database.sqlite.SQLiteDatabase;
    import android.database.sqlite.SQLiteOpenHelper;
    import android.util.Log;

    import com.comincini_micheli.quest4run.objects.Task;
    import com.comincini_micheli.quest4run.objects.Character;
    import com.comincini_micheli.quest4run.objects.Equipment;

    import java.util.ArrayList;
    import java.util.List;

/**
 * Created by Daniele on 17/05/2017.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 3;

    // Database Name
    private static final String DATABASE_NAME = "Quest4Run";

    // List table name
    private static final String TABLE_EQUIPMENT = "equipment";
    private static final String TABLE_TASK = "task";
    private static final String TABLE_CHARACTER = "character";

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

    private static final String KEY_GENDER = "gender";
    private static final String KEY_AVATAR = "avatar";
    private static final String KEY_EXP = "exp";

    private static final String KEY_ID_EQUIPMENT_TYPE = "idEquipmentType";
    private static final String KEY_BOUGHT = "bought";
    private static final String KEY_EQUIPPED = "equipped";

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
            + KEY_ACTIVE + " INTEGER" +
             ")";
    private static final String CREATE_CHARACTER_TABLE = "CREATE TABLE " + TABLE_CHARACTER + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NAME + " TEXT," + KEY_GENDER + " INTEGER," + KEY_AVATAR + " INTEGER,"
            + KEY_EXP + " INTEGER, " + KEY_ATK + " INTEGER," + KEY_DEF + " INTEGER," + KEY_MGC + " INTEGER"
            + ")";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
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
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EQUIPMENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHARACTER);

        // Create tables again
        onCreate(db);
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

        // Inserting Row
        int id = (int) db.insert(TABLE_TASK, null, values);
        db.close(); // Closing database connection
        return id;
    }

    //Getting single Task
    Task getTask(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_TASK, new String[] { KEY_ID,
                        KEY_NAME, KEY_REWARD, KEY_ID_TASK_TYPE, KEY_GOAL, KEY_COMPLETED, KEY_ACTIVE }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Task task = new Task(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), Integer.parseInt(cursor.getString(2)),
                Integer.parseInt(cursor.getString(3)),cursor.getString(4),
                castStringToBoolean(cursor.getString(5)),castStringToBoolean(cursor.getString(6)));
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

        String selectQuery = "SELECT  * FROM " + TABLE_TASK + " WHERE " + KEY_COMPLETED + " == " + _completed;

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
                // Adding Task to list
                taskList.add(task);
            } while (cursor.moveToNext());
        }
        cursor.close();
        Log.w("N row",taskList.size()+"");
        // return Task list
        return taskList;
    }

    private boolean castStringToBoolean(String s) {
        return s.equals("1");
    }

    // Updating single Task
    public int updateTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, task.getName());
        values.put(KEY_REWARD, task.getReward());
        values.put(KEY_ID_TASK_TYPE, task.getIdTaskType());
        values.put(KEY_GOAL, task.getGoal());

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
        values.put(KEY_DEF, character.getDefence());
        values.put(KEY_MGC, character.getMagic());

        // Inserting Row
        int addedCharacterId = (int) db.insert(TABLE_CHARACTER, null, values);
        db.close(); // Closing database connection
        return addedCharacterId;
    }

    //Getting single Character
    public Character getCharacter(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CHARACTER, new String[] { KEY_ID,
                        KEY_NAME, KEY_GENDER, KEY_AVATAR, KEY_EXP, KEY_ATK, KEY_DEF, KEY_MGC }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Character character = new Character(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), Integer.parseInt(cursor.getString(2)),
                Integer.parseInt(cursor.getString(3)),Integer.parseInt(cursor.getString(4)),
                Integer.parseInt(cursor.getString(5)),Integer.parseInt(cursor.getString(6)),
                Integer.parseInt(cursor.getString(7)));
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
        values.put(KEY_DEF, character.getDefence());
        values.put(KEY_MGC, character.getMagic());

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


    //EQUIPMENT METHODS


    public void addEquipment(Equipment equipment) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, equipment.getName());
        values.put(KEY_ID_EQUIPMENT_TYPE, equipment.getName());
        values.put(KEY_ATK, equipment.getAtk());
        values.put(KEY_DEF, equipment.getDef());
        values.put(KEY_MGC, equipment.getMgc());
        values.put(KEY_PRICE, equipment.getPrice());
        values.put(KEY_ICON, equipment.getIcon());
        values.put(KEY_BOUGHT, equipment.getIcon());
        values.put(KEY_EQUIPPED, equipment.getIcon());

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
}
