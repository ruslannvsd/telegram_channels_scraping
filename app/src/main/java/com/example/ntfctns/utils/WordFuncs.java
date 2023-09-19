package com.example.ntfctns.utils;

import static java.lang.String.join;

import android.content.Context;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WordFuncs {
    public static List<String> handlePunctuation(String text, Context ctx) {
        List<String> words = new ArrayList<>();
        // text passed is a list of words received from the user.
        // Each word to be checked for presence of prohibited punctuation
        // and a list as a list to be made
        if (text.contains(" ")) { // if there's a space as a divider it means more than one word
            String[] text_split = text.split(" ");
            Collections.addAll(words, text_split);
        } else words.add(text); // if a single word was entered
        for (String item : words) {
            if (item.length() <= 3) {
                Toast.makeText(ctx, "Too short word has been entered", Toast.LENGTH_LONG).show();
            } else {
                if (item.matches(".*[" + "!\"#$%&'()*+,./:;<=>?@\\[\\]^`{|}~" + "].*")) {
                    Toast.makeText(ctx, "Prohibited punctuation has been used", Toast.LENGTH_LONG).show();
                    return null; // return new ArrayList<>();
                }
            }
        }
        return words;
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
        }
        return "IMG";
    }
}


