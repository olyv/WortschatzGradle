package com.olyv.wortschatz.lesson.items;

import android.os.Parcel;
import android.os.Parcelable;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Adjektive")
public class Adjektive implements LessonItemI, Parcelable
{
    public final static String ADJEKTIVE_ID = "id";
    public final static String ADJEKTIVE_WORD = "word";
    public final static String ADJEKTIVE_TRANSLATION = "translation";

    @DatabaseField(generatedId = true, unique = true, columnName = ADJEKTIVE_ID)
    private Integer id;

    @DatabaseField(canBeNull = false, columnName = ADJEKTIVE_WORD)
    private String adjektive;

    @DatabaseField(canBeNull = false, columnName = ADJEKTIVE_TRANSLATION)
    private String translation;

    public Adjektive() { /* ORMLite needs a no-arg constructor */ }

    public Adjektive(Integer id, String adjektive, String translation)
    {
        this.id = id;
        this.adjektive = adjektive;
        this.translation = translation;
    }

    public Adjektive(Parcel in)
    {
        this.id = in.readInt();
        this.adjektive = in.readString();
        this.translation = in.readString();
    }

    public int getId()
    {
        return id;
    }

    public LessonItemI setId(int id)
    {
        this.id = id;
        return this;
    }

    public String getWord()
    {
        return adjektive;
    }

    public Adjektive setWord(String adjektive)
    {
        this.adjektive = adjektive;
        return this;
    }

    public String getTranslation()
    {
        return translation;
    }

    public Adjektive setTranslation(String translation)
    {
        this.translation = translation;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(id);
        dest.writeString(adjektive);
        dest.writeString(translation);
    }

    public static final Parcelable.Creator<Adjektive> CREATOR = new Parcelable.Creator<Adjektive>()
    {
        public Adjektive createFromParcel(Parcel in)
        {
            return new Adjektive(in);
        }
        public Adjektive[] newArray(int size)
        {
            return new Adjektive[size];
        }
    };
}
