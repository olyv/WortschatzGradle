package com.olyv.wortschatz.lesson;

import com.olyv.wortschatz.lesson.items.Adjektive;
import com.olyv.wortschatz.lesson.items.Noun;
import com.olyv.wortschatz.lesson.items.Verb;

public class AnswerChecker
{
    private static String yourAnswer = "Deine Antwort";
    private static String correctAnswer = "Richtige Answer ist";
    private static String assertCorrectAnswer = "Richtige Antwort!";

    public static boolean isVerbCorrect(Verb taskVerb, Verb answerVerb)
    {
        return taskVerb.getPartizip().equals(answerVerb.getPartizip()) || taskVerb.getAuxverb().equals(answerVerb.getAuxverb());
    }

    public static String getVerbComment(boolean correct, Verb taskVerb, Verb answerVerb)
    {
        String comment = "";
        if (correct)
        {
            comment += answerVerb.getAuxverb() + " " + answerVerb.getPartizip() + "\n\n" + assertCorrectAnswer;
        }
        else
        {
            comment += yourAnswer + " '" + answerVerb.getAuxverb() + " " + answerVerb.getPartizip() + "'\n\n" +
                    correctAnswer + " \n\n" + taskVerb.getAuxverb() + " " + taskVerb.getPartizip();
        }
        return comment;
    }

    public static boolean isNounCorrect(Noun taskNoun, Noun answerNoun)
    {
        return taskNoun.getPlural().equals(answerNoun.getPlural()) || taskNoun.getArticle().equals(answerNoun.getArticle());
    }

    public static String getNounComment(boolean correct, Noun taskNoun, Noun answerNoun)
    {
        String comment = "";
        if (correct)
        {
            comment += answerNoun.getArticle() + " " + answerNoun.getPlural() + "\n\n" + assertCorrectAnswer;
        }
        else
        {
            comment += yourAnswer + " '" + answerNoun.getArticle() + " " + answerNoun.getPlural() + "'\n\n" +
                    correctAnswer + " \n\n" + taskNoun.getArticle() + " " + taskNoun.getPlural();
        }
        return comment;
    }

    public static boolean isAdjektiveCorrect(Adjektive taskAdjektive, Adjektive answerAdjektive)
    {
        return taskAdjektive.getTranslation().equals(answerAdjektive.getTranslation());
    }

    public static String getAdjektiveComment(boolean correct, Adjektive taskAdjektive, Adjektive answerAdjektive)
    {
        String comment = "";
        if (correct)
        {
            comment += answerAdjektive.getTranslation() + "\n\n" + assertCorrectAnswer;
        }
        else
        {
            comment += yourAnswer + " '" + answerAdjektive.getTranslation() + "' \n\n" +
                    correctAnswer + " \n\n" + taskAdjektive.getTranslation();
        }
        return comment;
    }
}
