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

public class Line
{

	private PlayCharacter character;
	private String line;
	private Line cue;
	private int wordIndex = 0;

	public Line(PlayCharacter character, String line)
	{
		this.character = character;
		this.line = line;
	}

	public Line(PlayCharacter character, String line, Line cue)
	{
		this.character = character;
		this.line = line;
		this.cue = cue;
	}

	@Override
	public String toString()
	{
		return character.getName() + ": " + line;
	}

	public String toHTMLString()
	{
		return "<strong>" + character.getName() + ":</strong> " + line.replace(System.getProperty("line.separator"), "<br>" + System.getProperty("line.separator"));
	}

	public PlayCharacter getCharacter()
	{
		return character;
	}

	public String getLine()
	{
		return line;
	}

	public Line getCue()
	{
		return cue;
	}

	public void setCue(Line cue)
	{
		this.cue = cue;
	}

	public String getNextWord()
	{
		return (wordIndex < line.split(" ").length) ? line.split(" ")[wordIndex++] : "";
	}

	public void reset()
	{
		wordIndex = 0;
	}
}
