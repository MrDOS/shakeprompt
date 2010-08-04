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

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

public class PromptFrame extends JFrame implements ActionListener
{

	JEditorPane cuePane;
	JEditorPane linePane;
	JButton context;
	JButton nextLine;
	JButton showWord;
	JButton showLine;
	JButton quit;
	Line line = new Line(new PlayCharacter(""), "");

	public PromptFrame()
	{
		setTitle("ShakePrompt");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationByPlatform(true);

		setPreferredSize(new Dimension(480, 576));

		setLayout(new GridBagLayout());
		GridBagConstraints c;

		c = new GridBagConstraints();
		c.anchor = GridBagConstraints.NORTHWEST;
		c.insets = new Insets(4, 4, 0, 4);
		c.gridx = 0;
		c.gridy = 0;
		add(new JLabel("Cue:"), c);

		c = new GridBagConstraints();
		cuePane = new JEditorPane("text/html", "");
		cuePane.setEditable(false);
		c.anchor = GridBagConstraints.NORTHWEST;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 1;
		c.insets = new Insets(0, 4, 4, 4);
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 4;
		add(new JScrollPane(cuePane), c);

		c = new GridBagConstraints();
		c.anchor = GridBagConstraints.NORTHWEST;
		c.insets = new Insets(4, 4, 0, 4);
		c.gridx = 0;
		c.gridy = 2;
		add(new JLabel("Line:"), c);

		c = new GridBagConstraints();
		linePane = new JEditorPane("text/html", "");
		linePane.setEditable(false);
		c.anchor = GridBagConstraints.NORTHWEST;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 1;
		c.insets = new Insets(0, 4, 4, 4);
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 4;
		add(new JScrollPane(linePane), c);

		c = new GridBagConstraints();
		nextLine = new JButton("Next Line");
		nextLine.setMnemonic(KeyEvent.VK_N);
		nextLine.setToolTipText("Show another random line.");
		nextLine.addActionListener(this);
		c.anchor = GridBagConstraints.EAST;
		c.gridx = 0;
		c.gridy = 4;
		c.insets = new Insets(4, 4, 4, 4);
		add(nextLine, c);

		c = new GridBagConstraints();
		showWord = new JButton("Show Word");
		showWord.setMnemonic(KeyEvent.VK_W);
		showWord.setToolTipText("Show the next word in the line.");
		showWord.setEnabled(false);
		showWord.addActionListener(this);
		c.anchor = GridBagConstraints.WEST;
		c.gridx = 1;
		c.gridy = 4;
		c.insets = new Insets(4, 4, 4, 0);
		add(showWord, c);

		c = new GridBagConstraints();
		showLine = new JButton("Show Line");
		showLine.setMnemonic(KeyEvent.VK_L);
		showLine.setToolTipText("Show the entire line.");
		showLine.setEnabled(false);
		showLine.addActionListener(this);
		c.anchor = GridBagConstraints.WEST;
		c.gridx = 2;
		c.gridy = 4;
		c.insets = new Insets(4, 0, 4, 4);
		add(showLine, c);

		c = new GridBagConstraints();
		quit = new JButton("Quit");
		quit.setMnemonic(KeyEvent.VK_Q);
		quit.setToolTipText("Quit the program.");
		quit.addActionListener(this);
		c.anchor = GridBagConstraints.EAST;
		c.gridx = 3;
		c.gridy = 4;
		c.insets = new Insets(4, 4, 4, 4);
		add(quit, c);

		pack();
	}

	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource().equals(nextLine))
		{
			try
			{
				line = Main.lines.get(new Random().nextInt(Main.lines.size()));
				cuePane.setText(line.getCue().toHTMLString());
				linePane.setText("");
				showWord.setEnabled(true);
				showLine.setEnabled(true);
			}
			catch (NullPointerException ex)
			{
				JOptionPane.showMessageDialog(null, "No lines found!", "ShakePrompt", JOptionPane.ERROR_MESSAGE);
			}
		}
		else if (e.getSource().equals(showWord))
		{
			String words = line.getNextHTMLWord();
			linePane.setText(words);

			if (words.equals(line.toHTMLString()))
			{
				showWord.setEnabled(false);
				showLine.setEnabled(false);
			}
		}
		else if (e.getSource().equals(showLine))
		{
			linePane.setText(line.toHTMLString());
			showWord.setEnabled(false);
			showLine.setEnabled(false);
		}
		else if (e.getSource().equals(quit))
		{
			System.exit(0);
		}
	}
}
