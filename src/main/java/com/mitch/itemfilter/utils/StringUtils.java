package com.Mitch.itemfilter.utils;

import org.bukkit.ChatColor;

import java.util.List;
import java.util.stream.Collectors;

public class StringUtils {

    public static String colour(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static List<String> colourList(List<String> string) {
        return string.stream().map(StringUtils::colour).collect(Collectors.toList());
    }
}
