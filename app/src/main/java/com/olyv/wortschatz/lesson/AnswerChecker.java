package com.olyv.wortschatz.lesson;

import com.olyv.wortschatz.lesson.items.Adjektive;
import com.olyv.wortschatz.lesson.items.Noun;
import com.olyv.wortschatz.lesson.items.Verb;

public class AnswerChecker
{
    public static boolean isVerbCorrect(Verb taskVerb, Verb answerVerb)
    {
        return answerVerb.getPartizip().length() > 0
                & taskVerb.getPartizip().equals(answerVerb.getPartizip())
                & taskVerb.getAuxverb().equals(answerVerb.getAuxverb());
    }

    public static String getStringAnswerFromVerb(Verb verb)
    {
        return verb.getAuxverb() + " " + verb.getPartizip();
    }

    public static boolean isNounCorrect(Noun taskNoun, Noun answerNoun)
    {
        return answerNoun.getPlural().length() > 0
                & taskNoun.getPlural().equals(answerNoun.getPlural())
                & taskNoun.getArticle().equals(answerNoun.getArticle());
    }

    public static String getStringAnswerFromNoun(Noun noun)
    {
        return noun.getArticle() + " " + noun.getPlural();
    }

    public static boolean isAdjektiveCorrect(Adjektive taskAdjektive, Adjektive answerAdjektive)
    {
        return taskAdjektive.getTranslation().equals(answerAdjektive.getTranslation());
    }

    public static String getStringAnswerFromAdjektive(Adjektive adjektive)
    {
        return adjektive.getTranslation();
    }
}
