package com.olyv.wortschatz.lesson.items;

import android.os.Parcel;
import android.os.Parcelable;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Verb")
public class Verb implements LessonItemI, Parcelable
{
    public final static String VERB_ID = "id";
    public final static String VERB_INFINITIV = "verb";
    public final static String VERB_AUXVERB = "auxverb";
    public final static String VERB_PARTIZIP = "partizip";
    public final static String VERB_TRANSLATION = "translation";

    public final static String AUXVERB_HAT = "hat";
    public final static String AUXVERB_IST = "ist";

    @DatabaseField(generatedId = true, unique = true, columnName = VERB_ID)
    private Integer id;

    @DatabaseField(canBeNull = false, columnName = VERB_INFINITIV)
    private String verb;

    @DatabaseField(canBeNull = false, columnName = VERB_AUXVERB)
    private String auxverb;

    @DatabaseField(canBeNull = false, columnName = VERB_PARTIZIP)
    private String partizip;

    @DatabaseField(canBeNull = false, columnName = VERB_TRANSLATION)
    private String translation;

    public Verb() { /* ORMLite needs a no-arg constructor */ }

    public Verb(Integer id, String verb, String auxverb, String partizip, String translation)
    {
        this.id = id;
        this.verb = verb;
        this.auxverb = auxverb;
        this.partizip = partizip;
        this.translation = translation;
    }

    public Verb(Parcel in)
    {
        this.id = in.readInt();
        this.verb = in.readString();
        this.auxverb = in.readString();
        this.partizip = in.readString();
        this.translation = in.readString();
    }

    public int getId()
    {
        return id;
    }

    public Verb setId(int id)
    {
        this.id = id;
        return this;
    }

    public String getWord()
    {
        return verb;
    }

    public Verb setWord(String verb)
    {
        this.verb = verb;
        return this;
    }

    public String getAuxverb()
    {
        return auxverb;
    }

    public Verb setAuxverb(String auxverb)
    {
        this.auxverb = auxverb;
        return this;
    }

    public String getPartizip()
    {
        return partizip;
    }

    public Verb setPartizip(String partizip)
    {
        this.partizip = partizip;
        return this;
    }

    public String getTranslation()
    {
        return translation;
    }

    public Verb setTranslation(String translation)
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
        dest.writeString(verb);
        dest.writeString(auxverb);
        dest.writeString(partizip);
        dest.writeString(translation);
    }

    public static final Parcelable.Creator<Verb> CREATOR = new Parcelable.Creator<Verb>()
    {
        public Verb createFromParcel(Parcel in)
        {
            return new Verb(in);
        }
        public Verb[] newArray(int size)
        {
            return new Verb[size];
        }
    };
}
