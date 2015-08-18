/*
 * Software License Agreement (BSD License)
 *
 * Copyright (c) 2010, Samuel Coleman
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
import java.util.List;

public class LineParser
{
	/**
	 * Retrieves a character's print/save page from Open Source Shakespeare and parses lines and cues out of it.
	 * @param url URL of the print/save page to parse.
	 * @return All the character's lines with cue lines attached.
	 * @throws IOException
	 */
	public static List<Line> getLines(URL url) throws IOException
	{
		String page = OSSClient.getPage(url);

		List<Line> retval = new ArrayList<Line>();

		int i = 0;
		// This HTML opening tag is used on the print/save page to denote the beginning of a line. Thankfully, when normalsans paragraphs are used elsewhere, some other attribute is always present as well.
		while ((i = page.indexOf("<p class='normalsans'>", i) + 22) != 21)
		{
			// Grab the line &ndash; it'll end at the next paragraph closing tag &ndash; and make it into a Line.
			Line cue = parseLine(page.substring(i, page.indexOf("</p>", i)));

			// Now try to grab the immediately proceeding line - if the last line really was a cue (which it always should be), this will be the line of the focal character.
			i = page.indexOf("<p class='normalsans'>", i) + 22;
			Line line = parseLine(page.substring(i, page.indexOf("</p>", i)));

			// Attach the cue to the main line.
			line.setCue(cue);

			// Now that we have both lines, we can add them to the return value.
			retval.add(line);
		}

		return retval;
	}

	/**
	 * Parse a line in HTML form into a {@link Line}.
	 * @param line The line to parse.
	 * @return A parsed {@link Line}.
	 */
	private static Line parseLine(String line)
	{
		String lineBreak = System.getProperty("line.separator");

		// Make all HTML breaks into linebreaks as platform-appropriate.
		line = line.replace("<br>", lineBreak);
		// Now that break tags are out of the way, we can safely remove all other HTML tags.
		line = line.replaceAll("\\<.*?>", "");
		// Get rid of any extra whitespace -- stage directions are often prefaced by a number of spaces.
		while (line.contains("  "))
		{
			line = line.replace("  ", " ");
		}
		// Make sure there's no whitespace around the line.
		line = line.trim();

		// Split the line on ". " (delimeter between character name and line) and return it.
		int split = line.indexOf(". ");
		return new Line(new PlayCharacter(line.substring(0, split)), line.substring(split + 2));
	}
}
