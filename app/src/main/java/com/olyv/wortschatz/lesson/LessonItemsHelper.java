package com.olyv.wortschatz.lesson;

import android.util.Log;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.olyv.wortschatz.lesson.items.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LessonItemsHelper
{
    public static final String LOG_TAG = "LessonClassLog";

    private Dao<Verb, Integer> verbDao;
    private Dao<Noun, Integer> nounDao;
    private Dao<Adjektive, Integer> adjektiveDao;
    private QueryBuilder<Verb, Integer> verbQueryBuilder;
    private QueryBuilder<Noun, Integer> nounQueryBuilder;
    private QueryBuilder<Adjektive, Integer> adjektiveQueryBuilder;
    private UpdateBuilder<Verb, Integer> verbUpdateBuilder;
    private UpdateBuilder<Noun, Integer> nounUpdateBuilder;
    private UpdateBuilder<Adjektive, Integer> adjektiveUpdateBuilder;
    private DeleteBuilder<Verb, Integer> verbDeleteBuilder;
    private DeleteBuilder<Noun, Integer> nounDeleteBuilder;
    private DeleteBuilder<Adjektive, Integer> adjektiveDeleteBuilder;

    public ArrayList<LessonItemI> getLessonItems(DatabaseHelper databaseHelper, int...numberOfItems)
    {
        ArrayList<LessonItemI> selectedItems = new ArrayList<LessonItemI>();
        try
        {
            selectedItems.addAll(getVerbs(databaseHelper, numberOfItems[0]));
            selectedItems.addAll(getNouns(databaseHelper, numberOfItems[1]));
            selectedItems.addAll(getAdjektives(databaseHelper, numberOfItems[2]));
        }
        catch (java.sql.SQLException e)
        {
            Log.e(LOG_TAG, "Database exception", e);
            return null;
        }
        return selectedItems;
    }

    public ArrayList<LessonItemI> searchItems(DatabaseHelper databaseHelper, String keyword)
    {
        ArrayList<LessonItemI> foundItems = new ArrayList<LessonItemI>();

        try
        {
            foundItems.addAll(getVerbsByKeyword(databaseHelper, keyword));
            foundItems.addAll(getNounsByKeyword(databaseHelper, keyword));
            foundItems.addAll(getAdjektivesByKeyword(databaseHelper, keyword));
        }
        catch (SQLException e)
        {
            Log.e(LOG_TAG, "Database exception", e);
        }
        return foundItems;
    }

    private List<Verb> getVerbsByKeyword(DatabaseHelper databaseHelper, String keyword) throws SQLException
    {
        verbDao = databaseHelper.getVerbDao();
        verbQueryBuilder = verbDao.queryBuilder();
        verbQueryBuilder.where().like(Verb.VERB_INFINITIV, "%" + keyword + "%");
        PreparedQuery<Verb> preparedQuery = verbQueryBuilder.prepare();

        Log.i(LOG_TAG, "selected verbs by keyword " + keyword);

        return verbDao.query(preparedQuery);
    }

    private List<Noun> getNounsByKeyword(DatabaseHelper databaseHelper, String keyword) throws SQLException
    {
        nounDao = databaseHelper.getNounDao();
        nounQueryBuilder = nounDao.queryBuilder();
        nounQueryBuilder.where().like(Noun.NOUN_SINGULAR, "%" + keyword + "%");
        PreparedQuery<Noun> preparedQuery = nounQueryBuilder.prepare();

        Log.i(LOG_TAG, "selected nouns by keyword " + keyword);

        return nounDao.query(preparedQuery);
    }

    private List<Adjektive> getAdjektivesByKeyword(DatabaseHelper databaseHelper, String keyword) throws SQLException
    {
        adjektiveDao = databaseHelper.getAdjektiveDao();
        adjektiveQueryBuilder = adjektiveDao.queryBuilder();
        adjektiveQueryBuilder.where().like(Adjektive.ADJEKTIVE_WORD, "%" + keyword + "%");
        PreparedQuery<Adjektive> preparedQuery = adjektiveQueryBuilder.prepare();

        Log.i(LOG_TAG, "selected adjektives by keyword " + keyword);

        return adjektiveDao.query(preparedQuery);
    }

    private List<Verb> getVerbs(DatabaseHelper databaseHelper, int verbsNumber) throws java.sql.SQLException
    {
        verbDao = databaseHelper.getVerbDao();
        verbQueryBuilder = verbDao.queryBuilder();
        verbQueryBuilder.orderByRaw("RANDOM()").limit(verbsNumber);
        PreparedQuery<Verb> preparedQuery = verbQueryBuilder.prepare();

        Log.i(LOG_TAG, "selected verbs for lesson");

        return verbDao.query(preparedQuery);
    }

    private List<Noun> getNouns(DatabaseHelper databaseHelper, int nounsNumber) throws java.sql.SQLException
    {
        nounDao = databaseHelper.getNounDao();
        nounQueryBuilder = nounDao.queryBuilder();
        nounQueryBuilder.orderByRaw("RANDOM()").limit(nounsNumber);
        PreparedQuery<Noun> preparedQuery = nounQueryBuilder.prepare();

        Log.i(LOG_TAG, "selected nouns for lesson");

        return nounDao.query(preparedQuery);
    }

    private List<Adjektive> getAdjektives(DatabaseHelper databaseHelper, int adjektivesNumber) throws java.sql.SQLException
    {
        adjektiveDao = databaseHelper.getAdjektiveDao();
        adjektiveQueryBuilder = adjektiveDao.queryBuilder();
        adjektiveQueryBuilder.orderByRaw("RANDOM()").limit(adjektivesNumber);
        PreparedQuery<Adjektive> preparedQuery = adjektiveQueryBuilder.prepare();

        Log.i(LOG_TAG, "selected adjektives for lesson");

        return adjektiveDao.query(preparedQuery);
    }

    public void updateVerb(DatabaseHelper databaseHelper, Verb verb)
    {
        try
        {
            verbDao = databaseHelper.getVerbDao();
            verbUpdateBuilder = verbDao.updateBuilder();
            verbUpdateBuilder.where().eq(Verb.VERB_ID, verb.getId());
            verbUpdateBuilder.updateColumnValue(Verb.VERB_INFINITIV, verb.getWord());
            verbUpdateBuilder.updateColumnValue(Verb.VERB_AUXVERB, verb.getAuxverb());
            verbUpdateBuilder.updateColumnValue(Verb.VERB_PARTIZIP, verb.getPartizip());
            verbUpdateBuilder.updateColumnValue(Verb.VERB_TRANSLATION, verb.getTranslation());
            verbUpdateBuilder.update();
        }
        catch (SQLException e)
        {
            Log.e(LOG_TAG, "Can't update verb " + verb.getWord());
            e.printStackTrace();
        }
    }

    public void updateNoun(DatabaseHelper databaseHelper, Noun noun)
    {
        try
        {
            nounDao = databaseHelper.getNounDao();
            nounUpdateBuilder = nounDao.updateBuilder();
            nounUpdateBuilder.where().eq(Noun.NOUN_ID, noun.getId());
            nounUpdateBuilder.updateColumnValue(Noun.NOUN_SINGULAR, noun.getWord());
            nounUpdateBuilder.updateColumnValue(Noun.NOUN_ARTICLE, noun.getArticle());
            nounUpdateBuilder.updateColumnValue(Noun.NOUN_PLURAL, noun.getPlural());
            nounUpdateBuilder.updateColumnValue(Noun.NOUN_TRANSLATION, noun.getTranslation());
            nounUpdateBuilder.update();
        }
        catch (SQLException e)
        {
            Log.e(LOG_TAG, "Can't update noun " + noun.getWord());
            e.printStackTrace();
        }
    }

    public void updateAdjektive(DatabaseHelper databaseHelper, Adjektive adjektive)
    {
        try
        {
            adjektiveDao = databaseHelper.getAdjektiveDao();
            adjektiveUpdateBuilder = adjektiveDao.updateBuilder();
            adjektiveUpdateBuilder.where().eq(Adjektive.ADJEKTIVE_ID, adjektive.getId());
            adjektiveUpdateBuilder.updateColumnValue(Adjektive.ADJEKTIVE_WORD, adjektive.getWord());
            adjektiveUpdateBuilder.updateColumnValue(Adjektive.ADJEKTIVE_TRANSLATION, adjektive.getTranslation());
            adjektiveUpdateBuilder.update();
        }
        catch (SQLException e)
        {
            Log.e(LOG_TAG, "Can't update adjektive " + adjektive.getWord());
            e.printStackTrace();
        }
    }

    public void deleteVerb(DatabaseHelper databaseHelper, Verb verb)
    {
        try
        {
            verbDao = databaseHelper.getVerbDao();
            verbDeleteBuilder = verbDao.deleteBuilder();
            verbDeleteBuilder.where().eq(Verb.VERB_ID, verb.getId());
            verbDeleteBuilder.delete();
        }
        catch (SQLException e)
        {
            Log.e(LOG_TAG, "Failed to delete verb " + verb.getWord());
            e.printStackTrace();
        }
    }

    public void deleteNoun(DatabaseHelper databaseHelper, Noun noun)
    {
        try
        {
            nounDao = databaseHelper.getNounDao();
            nounDeleteBuilder = nounDao.deleteBuilder();
            nounDeleteBuilder.where().eq(Noun.NOUN_ID, noun.getId());
            nounDeleteBuilder.delete();
        }
        catch (SQLException e)
        {
            Log.e(LOG_TAG, "Failed to delete noun " + noun.getWord());
            e.printStackTrace();
        }
    }

    public void deleteAdjektive(DatabaseHelper databaseHelper, Adjektive adjektive)
    {
        try
        {
            adjektiveDao = databaseHelper.getAdjektiveDao();
            adjektiveDeleteBuilder = adjektiveDao.deleteBuilder();
            adjektiveDeleteBuilder.where().eq(Adjektive.ADJEKTIVE_ID, adjektive.getId());
            adjektiveDeleteBuilder.delete();
        }
        catch (SQLException e)
        {
            Log.e(LOG_TAG, "Failed to delete adjektive " + adjektive.getWord());
            e.printStackTrace();
        }
    }

    public void insertNewVerb(DatabaseHelper databaseHelper, Verb verb)
    {
        try
        {
            verbDao = databaseHelper.getVerbDao();
            verbDao.create(verb);
        }
        catch (SQLException e)
        {
            Log.e(LOG_TAG, "Failed to insert verb " + verb.getWord());
            e.printStackTrace();
        }
    }

    public void insertNewNoun(DatabaseHelper databaseHelper, Noun noun)
    {
        try
        {
            nounDao = databaseHelper.getNounDao();
            nounDao.create(noun);
        }
        catch (SQLException e)
        {
            Log.e(LOG_TAG, "Failed to insert noun " + noun.getWord());
            e.printStackTrace();
        }
    }

    public void insertNewAdjektive(DatabaseHelper databaseHelper, Adjektive adjektive)
    {
        try
        {
            adjektiveDao = databaseHelper.getAdjektiveDao();
            adjektiveDao.create(adjektive);
        }
        catch (SQLException e)
        {
            Log.e(LOG_TAG, "Failed to insert adjektive " + adjektive.getWord());
            e.printStackTrace();
        }
    }
}