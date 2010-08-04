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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class Main
{
	public static List<Line> lines;

	public static void main(String[] args)
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e)
		{
		}

		String url = JOptionPane.showInputDialog(null, "Enter URL for the Open Source Shakespeare print/save page with lines and cues for your character:", "ShakePrompt", JOptionPane.QUESTION_MESSAGE);
		if (url == null || url.equals(""))
		{
			JOptionPane.showMessageDialog(null, "No URL given!", "ShakePrompt", JOptionPane.ERROR_MESSAGE);
			System.exit(100);
		}

		try
		{
			lines = LineParser.getLines(new URL(url));
		}
		catch (MalformedURLException e)
		{
			JOptionPane.showMessageDialog(null, "Could not parse URL!", "ShakePrompt", JOptionPane.ERROR_MESSAGE);
			System.exit(101);
		}
		catch (IOException e)
		{
			JOptionPane.showMessageDialog(null, "Could not retrieve page!", "ShakePrompt", JOptionPane.ERROR_MESSAGE);
			System.exit(102);
		}

		PromptFrame frame = new PromptFrame();
		frame.setVisible(true);
	}
}
