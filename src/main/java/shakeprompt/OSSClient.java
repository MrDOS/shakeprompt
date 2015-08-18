/*
 * Software License Agreement (BSD License)
 *
 * Copyright (c) 2010, 2015, Samuel Coleman
 * All rights reserved.
 *
 * Redistribution and use of this software in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 *   Redistributions of source code must retain the above
 *   copyright notice, this list of conditions and the
 *   following disclaimer.
 *
 *   Redistributions in binary form must reproduce the above
 *   copyright notice, this list of conditions and the
 *   following disclaimer in the documentation and/or other
 *   materials provided with the distribution.
 *
 *   Neither the name of the author nor the names of its
 *   contributors may be used to endorse or promote products
 *   derived from this software without specific prior
 *   written permission of the author.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
 * TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package shakeprompt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * General access to the Open Source Shakespear website.
 */
public class OSSClient
{
    private static final String PLAYS_URL_FORMAT = "http://opensourceshakespeare.org/views/plays/plays.php";
    private static final String CHARS_URL_FORMAT = "http://opensourceshakespeare.org/views/plays/playmenu.php?WorkID=%s";
    private static final String LINES_URL_FORMAT = "http://opensourceshakespeare.org/views/plays/characters/charlines.php?WorkID=%s&CharID=%s&cues=1&longspeeches=0&displaytype=print";

    private static final Pattern PLAY_PATTERN = Pattern.compile("<a href='playmenu\\.php\\?WorkID=([a-z0-9]+)'>(.+?)<br></a>");
    private static final Pattern CHAR_PATTERN = Pattern.compile("<li class='charnames'><a href='characters/charlines\\.php\\?CharID=([A-Za-z0-9-]+)&WorkID=([a-z0-9]+)'><strong>(.+?)</strong></a>(, (.+?))?</li>");

    /**
     * @return a list of all plays
     */
    public static ArrayList<Play> getPlays() throws IOException
    {
        URL playsUrl = new URL(OSSClient.PLAYS_URL_FORMAT);
        String page = OSSClient.getPage(playsUrl);

        ArrayList<Play> plays = new ArrayList<Play>();

        Matcher playMatcher = OSSClient.PLAY_PATTERN.matcher(page);
        while (playMatcher.find())
            plays.add(new Play(playMatcher.group(2).replaceAll("</?strong>", ""),
                               playMatcher.group(1)));

        Collections.sort(plays);
        return plays;
    }

    /**
     * @param play a play
     * @return a list of all characters in the play
     */
    public static ArrayList<PlayCharacter> getPlayCharacters(Play play) throws IOException
    {
        URL charactersUrl = new URL(String.format(OSSClient.CHARS_URL_FORMAT, play.getId()));
        String page = OSSClient.getPage(charactersUrl);

        ArrayList<PlayCharacter> chars = new ArrayList<PlayCharacter>();

        Matcher charMatcher = OSSClient.CHAR_PATTERN.matcher(page);
        while (charMatcher.find())
            chars.add(new PlayCharacter(charMatcher.group(3),
                                        charMatcher.group(1)));

        Collections.sort(chars);
        return chars;
    }

    /**
     * @param play a play
     * @param character a character within the play
     * @return all lines for the character in the play
     */
    public static List<Line> getCharacterLines(Play play, PlayCharacter character) throws IOException
    {
        URL linesUrl = new URL(String.format(OSSClient.LINES_URL_FORMAT,
                                             play.getId(),
                                             character.getId()));
        return LineParser.getLines(linesUrl);
    }

    /**
     * @param url an HTTP URL
     * @return the contents of a page
     */
    protected static String getPage(URL url) throws IOException
    {
        URLConnection connection = url.openConnection();
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder page = new StringBuilder();
        String htmlLine;
        while ((htmlLine = reader.readLine()) != null)
        {
            page.append(htmlLine);
        }

        return page.toString();
    }
}
