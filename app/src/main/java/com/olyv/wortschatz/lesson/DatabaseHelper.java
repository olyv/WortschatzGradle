package com.olyv.wortschatz.lesson;

import java.io.*;
import java.sql.SQLException;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.j256.ormlite.android.AndroidConnectionSource;
import com.j256.ormlite.android.AndroidDatabaseConnection;
import com.j256.ormlite.android.DatabaseTableConfigUtil;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.DatabaseConnection;
import com.j256.ormlite.table.DatabaseTableConfig;
import com.j256.ormlite.table.TableUtils;
import com.olyv.wortschatz.lesson.items.Adjektive;
import com.olyv.wortschatz.lesson.items.Noun;
import com.olyv.wortschatz.lesson.items.Verb;

/**
 * Database helper class used to manage the creation and upgrading of the database. This class also provides
 * the DAOs used by the other classes.
 */
public class DatabaseHelper extends SQLiteOpenHelper
{
    public static final String LOG_TAG = "DatabaseHelperLog";
    protected AndroidConnectionSource connectionSource = new AndroidConnectionSource(this);

    private static final String DATABASE_NAME = "wortschatz.db";
    private static final int DATABASE_VERSION = 1;

    // the DAO object to access the tables
    private Dao<Verb, Integer> verbDao = null;
    private Dao<Noun, Integer> nounDao = null;
    private Dao<Adjektive, Integer> adjektiveDao = null;

    public DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        DatabaseConnection conn = connectionSource.getSpecialConnection();
        boolean clearSpecial = false;
        if (conn == null)
        {
            conn = new AndroidDatabaseConnection(db, true);
            try
            {
                connectionSource.saveSpecialConnection(conn);
                clearSpecial = true;
            }
            catch (SQLException e)
            {
                throw new IllegalStateException("Could not save special connection", e);
            }
        }
        try
        {
            onCreate();
        }
        finally
        {
            if (clearSpecial)
            {
                connectionSource.clearSpecialConnection(conn);
            }
        }
    }
    @Override
    public final void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        DatabaseConnection conn = connectionSource.getSpecialConnection();
        boolean clearSpecial = false;
        if (conn == null)
        {
            conn = new AndroidDatabaseConnection(db, true);
            try
            {
                connectionSource.saveSpecialConnection(conn);
                clearSpecial = true;
            } catch (SQLException e)
            {
                throw new IllegalStateException("Could not save special connection", e);
            }
        }
        try
        {
            onUpgrade(oldVersion, newVersion);
        } finally
        {
            if (clearSpecial)
            {
                connectionSource.clearSpecialConnection(conn);
            }
        }
    }
    /**
     * Close the database connections and clear any cached DAOs.
     */
    @Override
    public void close()
    {
        super.close();
        verbDao = null;
        nounDao = null;
        adjektiveDao = null;
    }
    /**
     * Returns the Database Access Object (DAO) for our SimpleData class. It will create it or just give the cached
     * value.
     */
    public Dao<Verb, Integer> getVerbDao() throws SQLException
    {
        if (verbDao == null)
        {
            verbDao = getDao(Verb.class);
        }
        return verbDao;
    }

    public Dao<Noun, Integer> getNounDao() throws SQLException
    {
        if (nounDao == null)
        {
            nounDao = getDao(Noun.class);
        }
        return nounDao;
    }

    public Dao<Adjektive, Integer> getAdjektiveDao() throws SQLException
    {
        if (adjektiveDao == null)
        {
            adjektiveDao = getDao(Adjektive.class);
        }
        return  adjektiveDao;
    }

    public static void releaseDatabaseHelper(DatabaseHelper databaseHelper)
    {
        if (databaseHelper != null)
        {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }

    private void onCreate()
    {
        try
        {
            TableUtils.createTable(connectionSource, Verb.class);
            TableUtils.createTable(connectionSource, Adjektive.class);
            TableUtils.createTable(connectionSource, Noun.class);

            String nouns = "res/raw/insert_nouns.sql";
            String verbs = "res/raw/insert_verbs.sql";
            String adjektives = "res/raw/insert_adjektives.sql";

            Dao<Verb, Integer> verbDao = getVerbDao();
            Dao<Noun, Integer> nounDao = getNounDao();
            Dao<Adjektive, Integer> adjektiveDao = getAdjektiveDao();

            InputStream in;

            in = this.getClass().getClassLoader().getResourceAsStream(verbs);
            verbDao.queryRaw(getStringFromInputStream(in));

            in = this.getClass().getClassLoader().getResourceAsStream(nouns);
            nounDao.queryRaw(getStringFromInputStream(in));

            in = this.getClass().getClassLoader().getResourceAsStream(adjektives);
            adjektiveDao.queryRaw(getStringFromInputStream(in));

            Log.i(LOG_TAG, "created new entries in onCreate");
        }
        catch (SQLException e)
        {
            Log.e(LOG_TAG, "Can't create database", e);
            throw new RuntimeException(e);
        }
    }

    private static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try
        {
            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null)
            {
                sb.append(line);
            }

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (br != null)
            {
                try
                {
                    br.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    /**
     * This is called when your application is upgraded and it has a higher version number. This allows you to adjust
     * the various data to match the new version number.
     */
    private void onUpgrade(int oldVersion, int newVersion)
    {
        try
        {
            Log.i(DatabaseHelper.class.getName(), "onUpgrade");
            TableUtils.dropTable(connectionSource, Noun.class, true);
            TableUtils.dropTable(connectionSource, Verb.class, true);
            TableUtils.dropTable(connectionSource, Adjektive.class, true);
            // after we drop the old databases, we create the new ones
            onCreate();
        }
        catch (SQLException e)
        {
            Log.e(LOG_TAG, "Can't drop databases", e);
            throw new RuntimeException(e);
        }
    }

    private <D extends Dao<T, ?>, T> D getDao(Class<T> clazz) throws SQLException
    {
        // lookup the dao, possibly invoking the cached database config
        Dao<T, ?> dao = DaoManager.lookupDao(connectionSource, clazz);
        if (dao == null)
        {
            // try to use our new reflection magic
            DatabaseTableConfig<T> tableConfig = DatabaseTableConfigUtil.fromClass(connectionSource, clazz);
            if (tableConfig == null)
            {
                /**
                 * TODO: we have to do this to get to see if they are using the deprecated annotations like
                 * {@link DatabaseFieldSimple}.
                 */
                dao = (Dao<T, ?>) DaoManager.createDao(connectionSource, clazz);
            }
            else
            {
                dao = (Dao<T, ?>) DaoManager.createDao(connectionSource, tableConfig);
            }
        }
        @SuppressWarnings("unchecked")
        D castDao = (D) dao;
        return castDao;
    }
}