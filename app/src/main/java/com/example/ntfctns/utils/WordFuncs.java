package com.example.ntfctns.utils;

import static java.lang.String.join;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordFuncs {
    public static List<String> handlePunctuation(String text, Context ctx) {
        List<String> words = new ArrayList<>();
        if (text.contains(" ")) {
            String[] text_split = text.split(" ");
            for (String item : text_split) {
                words.add(item);
            }
        } else words.add(text);
        for (String item : words) {
            if (item.matches(".*[" + "!\"#$%&'()*+,./:;<=>?@\\[\\]^`{|}~" + "].*")) {
                Toast.makeText(ctx, "Prohibited punctuation used", Toast.LENGTH_LONG).show();
                return null; // or return new ArrayList<>(); if you prefer an empty list
            }
        }
        return words;
    }

    public static String replaceBR(Element element) {
        String[] articleBodyStr = element.html().split("<br>");// WordFuncs.replaceBR(articleBody);
        String artBody = Jsoup.parse(join("$$$$$", articleBodyStr)).text().replace("$$$$$", "\n");
        return artBody;
    }

    public static String getLink(Element section) {
        Element photoElement = section.select("a.tgme_widget_message_photo_wrap").first();
        if (photoElement != null) {
            String styleValue = photoElement.attr("style");
            String imgLink = styleValue.substring(styleValue.indexOf("url('") + 5, styleValue.lastIndexOf("')"));
            return imgLink;
        }
        return "IMG";
    }
}


