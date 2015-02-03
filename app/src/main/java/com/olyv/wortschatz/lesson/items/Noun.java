package com.olyv.wortschatz.lesson.items;

import android.os.Parcel;
import android.os.Parcelable;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Noun")
public class Noun implements LessonItemI
{
    public final static String NOUN_ID = "id";
    public final static String NOUN_SINGULAR = "singular";
    public final static String NOUN_PLURAL = "plural";
    public final static String NOUN_ARTICLE = "article";
    public final static String NOUN_TRANSLATION = "translation";

    public final static String ARTICLE_DER = "der";
    public final static String ARTICLE_DIE = "die";
    public final static String ARTICLE_DAS = "das";

    @DatabaseField(generatedId = true, unique = true, columnName = NOUN_ID)
    private Integer id;

    @DatabaseField(canBeNull = false, columnName = NOUN_SINGULAR)
    private String noun;

    @DatabaseField(canBeNull = false, columnName = NOUN_TRANSLATION)
    private String translation;

    @DatabaseField(canBeNull = false, columnName = NOUN_PLURAL)
    private String plural;

    @DatabaseField(canBeNull = false, columnName = NOUN_ARTICLE)
    private String article;

    public Noun() { /* ORMLite needs a no-arg constructor */ }

    public Noun(Integer id, String noun, String translation, String plural, String article)
    {
        this.id = id;
        this.noun = noun;
        this.translation = translation;
        this.plural = plural;
        this.article = article;
    }

    public Noun(Parcel in)
    {
        this.id = in.readInt();
        this.noun = in.readString();
        this.translation = in.readString();
        this.plural = in.readString();
        this.article = in.readString();
    }

    public int getId()
    {
        return id;
    }

    public Noun setId(int id)
    {
        this.id = id;
        return this;
    }

    public String getWord()
    {
        return noun;
    }

    public Noun setWord(String noun)
    {
        this.noun = noun;
        return this;
    }

    public String getTranslation()
    {
        return translation;
    }

    public Noun setTranslation(String translation)
    {
        this.translation = translation;
        return this;
    }

    public String getPlural()
    {
        return plural;
    }

    public Noun setPlural(String plural)
    {
        this.plural = plural;
        return this;
    }

    public String getArticle()
    {
        return article;
    }

    public Noun setArticle(String article)
    {
        this.article = article;
        return this;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(id);
        dest.writeString(noun);
        dest.writeString(translation);
        dest.writeString(plural);
        dest.writeString(article);
    }

    public static final Parcelable.Creator<Noun> CREATOR = new Parcelable.Creator<Noun>()
    {
        public Noun createFromParcel(Parcel in)
        {
            return new Noun(in);
        }
        public Noun[] newArray(int size)
        {
            return new Noun[size];
        }
    };
}
