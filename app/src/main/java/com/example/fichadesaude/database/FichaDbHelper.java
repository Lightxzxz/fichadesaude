package com.example.fichadesaude.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.fichadesaude.model.FichaSaude;
import java.util.ArrayList;
import java.util.List;

public class FichaDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "fichasaude.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_FICHAS = "fichas";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NOME = "nome";
    private static final String COLUMN_IDADE = "idade";
    private static final String COLUMN_PESO = "peso";
    private static final String COLUMN_ALTURA = "altura";
    private static final String COLUMN_PRESSAO = "pressao_arterial";

    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_FICHAS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NOME + " TEXT, " +
                    COLUMN_IDADE + " INTEGER, " +
                    COLUMN_PESO + " REAL, " +
                    COLUMN_ALTURA + " REAL, " +
                    COLUMN_PRESSAO + " TEXT)";

    public FichaDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FICHAS);
        onCreate(db);
    }

    public long addFicha(FichaSaude ficha) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_NOME, ficha.getNome());
        values.put(COLUMN_IDADE, ficha.getIdade());
        values.put(COLUMN_PESO, ficha.getPeso());
        values.put(COLUMN_ALTURA, ficha.getAltura());
        values.put(COLUMN_PRESSAO, ficha.getPressaoArterial());

        long id = db.insert(TABLE_FICHAS, null, values);
        db.close();
        return id;
    }

    public List<FichaSaude> getAllFichas() {
        List<FichaSaude> fichas = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_FICHAS + " ORDER BY " + COLUMN_ID + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                FichaSaude ficha = new FichaSaude();
                ficha.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                ficha.setNome(cursor.getString(cursor.getColumnIndex(COLUMN_NOME)));
                ficha.setIdade(cursor.getInt(cursor.getColumnIndex(COLUMN_IDADE)));
                ficha.setPeso(cursor.getDouble(cursor.getColumnIndex(COLUMN_PESO)));
                ficha.setAltura(cursor.getDouble(cursor.getColumnIndex(COLUMN_ALTURA)));
                ficha.setPressaoArterial(cursor.getString(cursor.getColumnIndex(COLUMN_PRESSAO)));

                fichas.add(ficha);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return fichas;
    }

    public FichaSaude getFicha(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_FICHAS,
                new String[]{COLUMN_ID, COLUMN_NOME, COLUMN_IDADE, COLUMN_PESO, COLUMN_ALTURA, COLUMN_PRESSAO},
                COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null) cursor.moveToFirst();

        FichaSaude ficha = new FichaSaude(
                cursor.getString(1),
                cursor.getInt(2),
                cursor.getDouble(3),
                cursor.getDouble(4),
                cursor.getString(5));
        ficha.setId(cursor.getInt(0));

        cursor.close();
        return ficha;
    }

    public int updateFicha(FichaSaude ficha) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_NOME, ficha.getNome());
        values.put(COLUMN_IDADE, ficha.getIdade());
        values.put(COLUMN_PESO, ficha.getPeso());
        values.put(COLUMN_ALTURA, ficha.getAltura());
        values.put(COLUMN_PRESSAO, ficha.getPressaoArterial());

        return db.update(TABLE_FICHAS, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(ficha.getId())});
    }

    public void deleteFicha(FichaSaude ficha) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FICHAS, COLUMN_ID + " = ?",
                new String[]{String.valueOf(ficha.getId())});
        db.close();
    }

    public int getFichasCount() {
        String countQuery = "SELECT * FROM " + TABLE_FICHAS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public double getMediaIdade() {
        String query = "SELECT AVG(" + COLUMN_IDADE + ") FROM " + TABLE_FICHAS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            return cursor.getDouble(0);
        }
        cursor.close();
        return 0;
    }

    public double getMediaIMC() {
        String query = "SELECT AVG(" + COLUMN_PESO + "/(" + COLUMN_ALTURA + "*" + COLUMN_ALTURA + ")) FROM " + TABLE_FICHAS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            return cursor.getDouble(0);
        }
        cursor.close();
        return 0;
    }
}