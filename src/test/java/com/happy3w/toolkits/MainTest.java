package com.happy3w.toolkits;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class MainTest {
    @Test
    public void test() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(new File("/Users/ysgao/Projects/me/happy3w/wp_comments-insert.sql")));
        PrintWriter writer = new PrintWriter(new FileOutputStream(new File("/Users/ysgao/Projects/me/happy3w/last-insert.sql")));
        String preLine = reader.readLine();
        int lineCount = 0;
        for (String line = reader.readLine(); line != null; line = reader.readLine()) {
            if (line.startsWith("('")) {
                lineCount++;
                if (lineCount >= 100) {
                    preLine = preLine.substring(0, preLine.length() - 1) + ';';
                    writer.println(preLine);
                    preLine = "insert into `wp_posts`(`ID`,`post_author`,`post_date`,`post_date_gmt`,`post_content`,`post_title`,`post_excerpt`,`post_status`,`comment_status`,`ping_status`,`post_password`,`post_name`,`to_ping`,`pinged`,`post_modified`,`post_modified_gmt`,`post_content_filtered`,`post_parent`,`guid`,`menu_order`,`post_type`,`post_mime_type`,`comment_count`) values";
                    lineCount = 0;
                }
            }
            writer.println(preLine);
            preLine = line;
        }
        writer.println(preLine);
        reader.close();
        writer.close();
    }
}
