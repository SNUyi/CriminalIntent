package com.sunny.criminalintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.sunny.criminalintent.database.CrimeBaseHelper;
import com.sunny.criminalintent.database.CrimeCursorWrapper;
import com.sunny.criminalintent.database.CrimeDbSchema;
import com.sunny.criminalintent.database.CrimeDbSchema.CrimeTable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by SUNny on 2016/9/12 0012.
 */
public class CrimeLab {
    private static CrimeLab sCrimeLab;

    //private List<Crime> mCrimes;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static CrimeLab get(Context context){
        if(sCrimeLab == null){
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    private CrimeLab(Context context){
        mContext =context.getApplicationContext();
        mDatabase = new CrimeBaseHelper(mContext)
                .getWritableDatabase();

        //mCrimes = new ArrayList<>();
        //for (int i = 0;i < 100; i++){
          //  Crime crime = new Crime();
          //  crime.setTitle("Crime #"+i);
           // crime.setSolved(i % 2 == 0);//每隔一个设置为选中
          //  mCrimes.add(crime);
        //}
    }

    public List<Crime> getCrimes(){
        //return mCrimes;
        //return new ArrayList<>();
        List<Crime> crimes = new ArrayList<>();

        CrimeCursorWrapper cursor = queryCrimes(null,null);

        try {
            cursor.moveToFirst();
            while (! cursor.isAfterLast()){
                crimes.add(cursor.getCrime());
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
        }

        return crimes;
    }

    public Crime getCrime(UUID id){
        //for (Crime crime : mCrimes){
          //  if (crime.getId().equals(id)){
            //    return crime;
            //}
        //}
        //return null;
        CrimeCursorWrapper cursor = queryCrimes(CrimeTable.Cols.UUID + " = ?",
                new String[] { id.toString() });

        try {
            if (cursor.getCount() == 0){
                return null;
            }

            cursor.moveToFirst();
            return cursor.getCrime();
        }finally {
            cursor.close();
        }
    }

    public File getPhotoFile(Crime crime){
        File externalFilesDir = mContext
                .getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        if (externalFilesDir == null){
            return null;
        }

        return new File(externalFilesDir, crime.getPhotoFilename());
    }

    public void updateCrime(Crime crime){
        String uuidString = crime.getId().toString();
        ContentValues values = getContentValues(crime);

        mDatabase.update(CrimeTable.NAME,values,
                CrimeTable.Cols.UUID + " = ?",
                new String[] {uuidString });
    }

    private static ContentValues getContentValues(Crime crime){
        ContentValues values = new ContentValues();
        values.put(CrimeTable.Cols.UUID,crime.getId().toString());
        values.put(CrimeTable.Cols.TITLE,crime.getTitle());
        values.put(CrimeTable.Cols.DATE,crime.getDate().getTime());
        values.put(CrimeTable.Cols.SOLVED,crime.isSolved() ? 1 : 0);
        values.put(CrimeTable.Cols.SUSPECT,crime.getSuspect());

        return values;
    }

    private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(
                CrimeTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new CrimeCursorWrapper(cursor);
    }

    public void addCrime(Crime c){
        ContentValues values = getContentValues(c);

        mDatabase.insert(CrimeTable.NAME,null,values);
        //mCrimes.add(c);
    }
}