package com.comincini_micheli.quest4run.other;

    import android.content.ContentValues;
    import android.content.Context;
    import android.database.Cursor;
    import android.database.sqlite.SQLiteDatabase;
    import android.database.sqlite.SQLiteOpenHelper;
    import android.util.Log;

    import com.comincini_micheli.quest4run.objects.Equipment;
    import com.comincini_micheli.quest4run.objects.Task;

    import java.util.ArrayList;
    import java.util.List;

/**
 * Created by Daniele on 17/05/2017.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 4;

    // Database Name
    private static final String DATABASE_NAME = "Quest4Run";

    // List table name
    private static final String TABLE_EQUIPMENT = "equipment";
    private static final String TABLE_TASK = "task";

    // EQUIPMENT Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_ATK = "attack";
    private static final String KEY_DEF = "defense";
    private static final String KEY_MGC = "magic";
    private static final String KEY_PRICE = "price";
    private static final String KEY_ICON = "icon";
    private static final String KEY_OBJECTIVE = "objective";
    private static final String KEY_REWARD = "reward";
    private static final String KEY_IDTASKTYPE = "idTaskType";
    private static final String KEY_COMPLETED = "completed";
    private static final String KEY_ACTIVE = "active";

    //Create QUERIES
    private static final String CREATE_EQUIPMENT_TABLE = "CREATE TABLE " + TABLE_EQUIPMENT + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NAME + " TEXT,"
            + KEY_ATK + " INTEGER," + KEY_DEF + " INTEGER," + KEY_MGC + " INTEGER," + KEY_PRICE + " INTEGER," + KEY_ICON + " TEXT"
            + ")";
    private static final String CREATE_TASK_TABLE = "CREATE TABLE " + TABLE_TASK + "("
            + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
            + KEY_REWARD + " INTEGER," + KEY_IDTASKTYPE + " INTEGER,"
            + KEY_OBJECTIVE + " NUMERIC," + KEY_COMPLETED + " INTEGER,"
            + KEY_ACTIVE + " INTEGER" +
             ")";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_EQUIPMENT_TABLE);
        db.execSQL(CREATE_TASK_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EQUIPMENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASK);

        // Create tables again
        onCreate(db);
    }


    //TASK METHODS

    //Add Task
    public void addTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, task.getName());
        values.put(KEY_REWARD, task.getReward());
        values.put(KEY_IDTASKTYPE, task.getIdTaskType());
        values.put(KEY_OBJECTIVE, task.getGoal());
        values.put(KEY_COMPLETED, task.isCompleted());
        values.put(KEY_ACTIVE, task.isActive());

        // Inserting Row
        db.insert(TABLE_TASK, null, values);
        db.close(); // Closing database connection
    }

    //Getting single Task
    Task getTask(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_TASK, new String[] { KEY_ID,
                        KEY_NAME, KEY_REWARD, KEY_IDTASKTYPE, KEY_OBJECTIVE, KEY_COMPLETED, KEY_ACTIVE }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Task task = new Task(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), Integer.parseInt(cursor.getString(2)),
                Integer.parseInt(cursor.getString(3)),cursor.getString(4),
                Boolean.parseBoolean(cursor.getString(5)),castStringToBoolean(cursor.getString(6)));
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
                task.setCompleted(Boolean.parseBoolean(cursor.getString(5)));
                task.setActive(castStringToBoolean(cursor.getString(6)));
                // Adding Task to list
                taskList.add(task);
            } while (cursor.moveToNext());
        }

        // return Task list
        return taskList;
    }

    private boolean castStringToBoolean(String s)
    {
        if(s.equals("1"))
            return true;
        else
            return false;
    }

    // Updating single Task
    public int updateTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, task.getName());
        values.put(KEY_REWARD, task.getReward());
        values.put(KEY_IDTASKTYPE, task.getIdTaskType());
        values.put(KEY_OBJECTIVE, task.getGoal());
        values.put(KEY_COMPLETED, task.isCompleted());

        //TODO forse non funziona
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


    // Getting Task Count
    public int getTaskCount() {
        String countQuery = "SELECT  * FROM " + TABLE_TASK;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }



    //EQUIPMENT METHODS


    public void addEquipment(Equipment equipment) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, equipment.getName());
        values.put(KEY_ATK, equipment.getAtk());
        values.put(KEY_DEF, equipment.getDef());
        values.put(KEY_MGC, equipment.getMgc());
        values.put(KEY_PRICE, equipment.getPrice());
        values.put(KEY_ICON, equipment.getIcon());

        // Inserting Row
        db.insert(TABLE_EQUIPMENT, null, values);
        db.close(); // Closing database connection
    }

    //Getting single Equipment
    Equipment getEquipment(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_EQUIPMENT, new String[] { KEY_ID,
                        KEY_NAME, KEY_ATK, KEY_DEF, KEY_MGC, KEY_PRICE, KEY_ICON }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Equipment equipment = new Equipment(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), Integer.parseInt(cursor.getString(2)),
                Integer.parseInt(cursor.getString(3)),Integer.parseInt(cursor.getString(4)),
                Integer.parseInt(cursor.getString(5)),cursor.getString(6));
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
                equipment.setAtk(Integer.parseInt(cursor.getString(2)));
                equipment.setDef(Integer.parseInt(cursor.getString(3)));
                equipment.setMgc(Integer.parseInt(cursor.getString(4)));
                equipment.setPrice(Integer.parseInt(cursor.getString(5)));
                equipment.setIcon(cursor.getString(6));
                // Adding Equipment to list
                equipmentList.add(equipment);
            } while (cursor.moveToNext());
        }

        // return Equipment list
        return equipmentList;
    }

    // Updating single Equipment
    public int updateEquipment(Equipment equipment) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, equipment.getName());
        values.put(KEY_ATK, equipment.getAtk());
        values.put(KEY_DEF, equipment.getDef());
        values.put(KEY_MGC, equipment.getMgc());
        values.put(KEY_PRICE, equipment.getPrice());
        values.put(KEY_ICON, equipment.getIcon());

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
