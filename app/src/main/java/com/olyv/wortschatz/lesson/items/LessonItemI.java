package com.olyv.wortschatz.lesson.items;

import android.os.Parcel;
import android.os.Parcelable;

public interface LessonItemI extends Parcelable
{
    int getId();

    LessonItemI setId(int id);

    String getWord();

    LessonItemI setWord(String word);

    String getTranslation();

    LessonItemI setTranslation(String translation);

    @Override
    int describeContents();

    @Override
    void writeToParcel(Parcel dest, int flags);
}
