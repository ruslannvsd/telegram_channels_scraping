package com.example.ntfctns.utils;

import static java.lang.String.join;

import android.content.Context;
import android.widget.Toast;

import com.example.ntfctns.classes.Keyword;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

public class WordFuncs {
    public static List<Keyword> handlePunctuation(String text, Context ctx) {
        List<Keyword> keywords = new ArrayList<>();
        // text passed is a list of words received from the user.
        // Each word to be checked for the presence of prohibited punctuation
        // and added to the list as a Keyword object
        if (text.contains(" ")) { // if there's a space as a divider it means more than one word
            String[] text_split = text.split(" ");
            for (String word : text_split) {
                if (isValidWord(word, ctx)) {
                    keywords.add(new Keyword(word, 0));
                }
            }
        } else { // if a single word was entered
            if (isValidWord(text, ctx)) {
                keywords.add(new Keyword(text, 0));
            }
        }
        return keywords;
    }

    private static boolean isValidWord(String word, Context ctx) {
        if (word.length() <= 3) {
            Toast.makeText(ctx, "Too short word has been entered", Toast.LENGTH_LONG).show();
            return false;
        } else if (word.matches(".*[" + "!\"#$%&'()*+,./:;<=>?@\\[\\]^`{|}~" + "].*")) {
            Toast.makeText(ctx, "Prohibited punctuation has been used", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public static String replaceBR(Element element) {
        // getting text from html does not preserve new lines \n so here's the trick to do that
        String[] articleBodyStr = element.html().split("<br>");
        return Jsoup.parse(join("$$$$$", articleBodyStr)).text().replace("$$$$$", "\n");
    }

    public static String getLink(Element section) {
        // parsing an html element for getting required parts of it by html picture class to get its url
        Element photoElement = section.select("a.tgme_widget_message_photo_wrap").first();
        if (photoElement != null) {
            String styleValue = photoElement.attr("style");
            return styleValue.substring(styleValue.indexOf("url('") + 5, styleValue.lastIndexOf("')"));
        } else {
            return "IMG";
        }
    }
}


