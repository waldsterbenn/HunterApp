package dk.ls.hunter.maps;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "AnimalsDb";

    // Animal table name
    private static final String TABLE_ANIMAL = "animal";

    // Animal Table Columns names
    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "name";
    private static final String KEY_LAIN_NAME = "latin_name";
    private static final String KEY_AMMO_ID = "ammoID";
    private static final String KEY_ANIMAL_CAT_ID = "animal_catID";
    private static final String KEY_SHOT_DIST_ID = "shot_distID";
    private static final String KEY_SEASON_PERIOD_ID = "season_periodID";
    private static final String KEY_DESC = "desc";

    private String[] allAnimalColumns = new String[]{KEY_ID, KEY_NAME, KEY_LAIN_NAME, KEY_DESC, KEY_AMMO_ID, KEY_ANIMAL_CAT_ID,
            KEY_SHOT_DIST_ID, KEY_SEASON_PERIOD_ID};

    /**
     * Season
     */
    private static final String TABLE_SEASONS = "season_period";

    private static final String KEY_SEASON_START_MONTH = "start_month";
    private static final String KEY_SEASON_START_DAY = "start_day";
    private static final String KEY_SEASON_END_MONTH = "end_month";
    private static final String KEY_SEASON_END_DAY = "end_day";

    private String[] allSeasonColumns = new String[]{KEY_ID, KEY_SEASON_START_MONTH, KEY_SEASON_START_DAY,
            KEY_SEASON_END_MONTH, KEY_SEASON_END_DAY};

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = String
                .format(
                        "CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT, %s TEXT, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER)",
                        TABLE_ANIMAL,
                        KEY_ID,
                        KEY_NAME,
                        KEY_LAIN_NAME,
                        KEY_DESC,
                        KEY_AMMO_ID,
                        KEY_ANIMAL_CAT_ID,
                        KEY_SHOT_DIST_ID,
                        KEY_SEASON_PERIOD_ID
                );
        db.execSQL(CREATE_CONTACTS_TABLE);
        String CREATE_SEASONS_TABLE = String
                .format(
                        "CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER)",
                        TABLE_SEASONS,
                        KEY_ID,
                        KEY_SEASON_START_MONTH,
                        KEY_SEASON_START_DAY,
                        KEY_SEASON_END_MONTH,
                        KEY_SEASON_END_DAY);
        db.execSQL(CREATE_SEASONS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ANIMAL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SEASONS);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new contact
    public void addAnimal(AnimalMap a) {
        int sid = 0;
        if (a.season != null) {
            sid = getOrCreateSeason(a.season).id;
        }
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, a.name); // Contact Name
        values.put(KEY_LAIN_NAME, a.latinName); // Contact Name
        values.put(KEY_DESC, a.Description); // Contact Name
        values.put(KEY_AMMO_ID, a.ammo == null ? 0 : a.ammo.id); // Contact Name
        values.put(KEY_ANIMAL_CAT_ID, a.animalCatagory == null ? 0 : a.animalCatagory.id); // Contact Name
        values.put(KEY_SHOT_DIST_ID, a.shotDistance == null ? 0 : a.shotDistance.id); // Contact Name
        values.put(KEY_SEASON_PERIOD_ID, sid); // Contact Name

        // Inserting Row
        if (db != null) {
            db.insert(TABLE_ANIMAL, null, values);
            db.close(); // Closing database connection
        }
    }


    // Getting single contact
    public AnimalMap getAnimal(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.query(
                    TABLE_ANIMAL,
                    allAnimalColumns,
                    KEY_ID + "=?",
                    new String[]{String.valueOf(id)},
                    null,
                    null,
                    null,
                    null);
        }
        AnimalMap animal = null;
        if (cursor != null) {
            cursor.moveToFirst();

            //        int animalId = Integer.parseInt(cursor.getString(0));
            int animalId = cursor.getInt(0);


            //		ShotDistanceMap shotDistance, SeasonMap season, AnimalCatagoryMap animalCatagory)
            String name = cursor.getString(1);
            String latinName = cursor.getString(2);
            String desc = cursor.getString(3);
            //season ID is in col 7
            int seasonId = cursor.getInt(7);

            cursor.close();
            db.close();

            SeasonMap season = getSeason(seasonId);

            //AnimalMap(String id, String name, String latinName, String Description, AmmoMap ammo,
            animal = new AnimalMap(animalId,
                    name,
                    latinName,
                    desc,
                    null,//ammo cursor.getString(4),
                    null,//shotDist cursor.getString(5),
                    season,//season cursor.getString(6),
                    null//an cat cursor.getString(7)
            );
        }
//		db.close();
        // return contact
        return animal;
    }

    // Getting All Contacts
    public List<AnimalShortMap> getAllAnimalsList() {
        List<AnimalShortMap> animalList = new ArrayList<AnimalShortMap>();
        // Select All Query
        String selectQuery = String.format("SELECT %s, %s FROM %s", KEY_ID, KEY_NAME, TABLE_ANIMAL);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(selectQuery, null);
        }

        // looping through all rows and adding to list
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    AnimalShortMap contact = new AnimalShortMap(cursor.getInt(0), cursor.getString(1));
                    //                contact.setID(Integer.parseInt(cursor.getString(0)));
                    //                contact.setName(cursor.getString(1));
                    //                contact.setPhoneNumber(cursor.getString(2));
                    // Adding contact to list
                    animalList.add(contact);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        if (db != null) {
            db.close();
        }
        // return contact list
        return animalList;
    }

    // Updating single contact
    //    public int updateAnimal(AnimalMap contact) {
    //
    //    	SQLiteDatabase db = this.getWritableDatabase();
    //
    //        ContentValues values = new ContentValues();
    //        values.put(KEY_NAME, contact.getName());
    //        values.put(KEY_PH_NO, contact.getPhoneNumber());
    //
    //        // updating row
    //        return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
    //                new String[] { String.valueOf(contact.id)) });
    //    }

    // Deleting single contact
    public void deleteAnimal(AnimalMap animal) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (db != null) {
            db.delete(TABLE_ANIMAL, KEY_ID + " = ?", new String[]{String.valueOf(animal.id)});
            db.close();
        }
    }

    // Getting contacts Count
    public int getAnimalCount() {
        String countQuery = String.format("SELECT Count(1) FROM %s", TABLE_ANIMAL);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        int count = 0;
        if (db != null) {
            cursor = db.rawQuery(countQuery, null);
            count = 0;
            if (cursor != null) {
                count = cursor.getCount();
                cursor.close();
            }
            db.close();

        }
        // return count
        return count;
    }

    public Cursor getAllAnimalsCursor() {
        String selectQuery = String.format("SELECT %s, %s FROM %s", KEY_ID, KEY_NAME, TABLE_ANIMAL);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(selectQuery, null);
            db.close();
        }
        return cursor;
    }


    SeasonMap getOrCreateSeason(SeasonMap sm) {
        SeasonMap season = null;

        if (sm.id == 0) {

            SQLiteDatabase db = this.getReadableDatabase();

            //
            Cursor seasonCursor = null;
            if (db != null) {
                seasonCursor = db.query(
                        TABLE_SEASONS,
                        allSeasonColumns,
                        String.format("%s=? and %s=? and %s=? and %s=?", KEY_SEASON_START_MONTH, KEY_SEASON_START_DAY, KEY_SEASON_END_MONTH, KEY_SEASON_END_DAY),
                        new String[]{String.valueOf(sm.StartMonth), String.valueOf(sm.StartDay), String.valueOf(sm.EndMonth), String.valueOf(sm.EndDay)},
                        null,
                        null,
                        null,
                        null);

                if (seasonCursor != null){
                    if (seasonCursor.getCount() > 0) {
                        seasonCursor.moveToFirst();
                        season = new SeasonMap(
                                seasonCursor.getInt(0),
                                seasonCursor.getInt(1),
                                seasonCursor.getInt(2),
                                seasonCursor.getInt(3),
                                seasonCursor.getInt(4)
                        );
                        seasonCursor.close();
                        db.close();
                    } else {
                        seasonCursor.close();
                    }

                }
                db.close();
                //Does not exicst create it
                newSeason(sm);
            }

        }
        // return contact
        return season;
    }

    /**
     * @return
     */
    public SeasonMap newSeason(SeasonMap a) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
//		values.put(KEY_ID, a.Description); // Contact Name
        values.put(KEY_SEASON_START_MONTH, a.StartMonth); // Contact Name
        values.put(KEY_SEASON_START_DAY, a.StartDay); // Contact Name
        values.put(KEY_SEASON_END_MONTH, a.EndMonth); // Contact Name
        values.put(KEY_SEASON_END_DAY, a.EndDay); // Contact Name


        // Inserting Row
        if (db != null) {
            db.insert(TABLE_SEASONS, null, values);
            db.close(); // Closing database connection
        }


        db = this.getReadableDatabase();
        String selectQuery = String.format("SELECT %s FROM %s ORDER BY _id DESC  LIMIT 1", KEY_ID, TABLE_SEASONS);

        SeasonMap season = null;
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            cursor.moveToFirst();
            //int1 = cursor.getInt(0);
            season = new SeasonMap(
                    cursor.getInt(0),
                    cursor.getInt(1),
                    cursor.getInt(2),
                    cursor.getInt(3),
                    cursor.getInt(4));
            Log.d("DB", "Found season: " + season);
            cursor.close();
        }

        selectQuery = String.format("SELECT * FROM %s ", TABLE_SEASONS);

        cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            cursor.moveToFirst();
            Log.d("DB", "Found season: " + season);
            do {
                Log.d("DB", String.format("Found season %d: %d/%d - %d/%d", cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getInt(3), cursor.getInt(4)));
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close(); // Closing database connection
        }
        return season;
    }

    public SeasonMap getSeason(int id) {
        SQLiteDatabase db = this.getReadableDatabase();


        Cursor seasonCursor;
        SeasonMap season = null;
        if (db != null) {
            seasonCursor = db.query(
                    TABLE_SEASONS,
                    allSeasonColumns,
                    KEY_ID + "=?",
                    new String[]{String.valueOf(id)},
                    null,
                    null,
                    null,
                    null);

            if (seasonCursor != null) {
                if (seasonCursor.getCount()>0){
                seasonCursor.moveToFirst();
                season = new SeasonMap(
                        seasonCursor.getInt(0),
                        seasonCursor.getInt(1),
                        seasonCursor.getInt(2),
                        seasonCursor.getInt(3),
                        seasonCursor.getInt(4)
                );
                }
                seasonCursor.close();
            }
            db.close();
        }
        // return contact
        return season;
    }


    public String makeDummyData() {
        Random r = new Random();
        SeasonMap se = getOrCreateSeason(new SeasonMap(1, r.nextInt(11), r.nextInt(30), r.nextInt(11), r.nextInt(29)));
        //SeasonMap s = getSeason(se);
        String[] a = new String[]{"R�v", "Hest", "M�r", "Ged", "B�ver", "Ko", "Delfin", "Egern", "Velociraptor", "Tukan"};
        String[] d = new String[]{"Har store �re og d�rlig �konomi", "Edderfuglen er vores st�rste dykand. Den kan blive op til 60 cm og vejer 2-2,5 kg. Hannen er den eneste and, der har sort bug og hvid ryg.", "Gr��nder vejer fra 900-1.400 gram. Anden l�gger fra midt i marts 9-13 �g, som den ruger p� i 27-28 dage.", "Haren h�rer ikke til gnaverne, som mange fejlagtigt tror. Sammen med kaninerne h�rer de til ordenen de st�ttetandede.", "Havlit (Clangula Hyemalis) - Sortand (Melanitta Nigra) - Fl�jlsand (Melinitta Fusca). De fleste dyk�nder bliver nedlagt fra motorb�d, og det er langt overvejende edderfugle. Men der skydes ogs� andre havdyk�nder, nemlig havlit, sortand og fl�jlsand.", "Hvinand (Bucephala Clangula) og Taffeland (Ayathya Ferina) er et par af de sm� hurtige arter.", "Krikand (Anas Crecca) og Atlingand (Anas Querquedula). Det er n�rliggende at placere de to arter p� samme opslag, da de er vore to mindste sv�mme�nder.", "Fascinationen af dette mellemstore rovdyr er stor for mange j�gere, og den kan v�kke kraftige f�lelser hos s�vel j�gere som jagthunde. Adskillige j�gere har haft mere �feber� over at have r�v p� kornet, end n�r sommerbukken kommer for.", "Situationen for Danmarks mest graci�se hjorteart i dag er en ren solskinshistorie, for dyrene har bredt sig til hele landet, og mange steder er der s� mange, at st�r der blot to tr�er ved siden af hinanden, ja, s� er der ogs� r�vildt i �skoven�.", "Toppet skallesluger (Mergus Serrator) og stor skallesluger Mergus Merganser. Med en skallesluger i h�nden kan man godt blive i tvivl. Er det en and eller hvad? Faktum er, at de begge h�rer til familien af �nder."};
        String[] l = new String[]{"Vulpes Vulpes", "Streptopelia Decaoto", "Capreolus Capreolus", "Annos", "Mongolos", "Sympatico volsomos", "Lepus Capensis", "Hestos maximus", "Roligo", "Markapos mongolos"};
        String name = a[r.nextInt(9)];
        String description = d[r.nextInt(9)];
        String latin = l[r.nextInt(9)];
        addAnimal(new AnimalMap(1, name, latin, description, null, null, se, null));
        return name;
    }


}