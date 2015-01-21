package com.olyv.wortschatz.lesson;

import com.olyv.wortschatz.lesson.items.Adjektive;
import com.olyv.wortschatz.lesson.items.LessonItemI;
import com.olyv.wortschatz.lesson.items.Noun;
import com.olyv.wortschatz.lesson.items.Verb;

public class AnswerChecker
{
    public static boolean isVerbCorrect(Verb taskVerb, Verb answerVerb)
    {
        boolean result = false;

        if (!taskVerb.getPartizip().equals(answerVerb.getPartizip()))
        {
            return result;
        }
        if (!taskVerb.getAuxverb().equals(answerVerb.getAuxverb()))
        {
            return result;
        }
        else
        {
            result = true;
        }
        return result;
    }

    public static String getVerbComment(boolean correct, Verb taskVerb, Verb answerVerb)
    {
        String comment = "";
        if (correct)
        {
            comment += answerVerb.getAuxverb() + " " + answerVerb.getPartizip() + "\n\nCorrect answer!";
        }
        else
        {
            comment += "Your answer '" + answerVerb.getAuxverb() + " " + answerVerb.getPartizip() + "'\n\n" +
                    "Correct answer is: \n\n" + taskVerb.getAuxverb() + " " + taskVerb.getPartizip();
        }
        return comment;
    }

    public static boolean isNounCorrect(Noun taskNoun, Noun answerNoun)
    {
        boolean result = false;

        if (!taskNoun.getPlural().equals(answerNoun.getPlural()))
        {
            return result;
        }
        if (!taskNoun.getArticle().equals(answerNoun.getArticle()))
        {
            return result;
        }
        else
        {
            result = true;
        }
        return result;
    }

    public static String getNounComment(boolean correct, Noun taskNoun, Noun answerNoun)
    {
        String comment = "";
        if (correct)
        {
            comment += answerNoun.getArticle() + " " + answerNoun.getPlural() + "\n\nCorrect answer!";
        }
        else
        {
            comment += "Your answer '" + answerNoun.getArticle() + " " + answerNoun.getPlural() + "'\n\n" +
                    "Correct answer is: \n\n" + taskNoun.getArticle() + " " + taskNoun.getPlural();
        }
        return comment;
    }

    public static boolean isAdjektiveCorrect(Adjektive taskAdjektive, Adjektive answerAdjektive)
    {
        boolean result = false;

        if (!taskAdjektive.getTranslation().equals(answerAdjektive.getTranslation()))
        {
            return result;
        }
        else
        {
            result = true;
        }
        return result;
    }

    public static String getAdjektiveComment(boolean correct, Adjektive taskAdjektive, Adjektive answerAdjektive)
    {
        String comment = "";
        if (correct)
        {
            comment += answerAdjektive.getTranslation() + "\n\nCorrect answer!";
        }
        else
        {
            comment += "Your answer '" + answerAdjektive.getTranslation() + "' \n\n" +
                    "Correct answer is: \n\n" + taskAdjektive.getTranslation();
        }
        return comment;
    }
}
