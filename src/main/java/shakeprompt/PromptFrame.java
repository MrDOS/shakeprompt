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
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class PromptFrame extends JFrame implements ActionListener
{

	private JEditorPane cuePane;
	private JEditorPane linePane;
	private JPanel rangePanel;
	private JTextField rangeMinimum;
	private JTextField rangeMaximum;
	private JPanel orderPanel;
	private JRadioButton sequential;
	private JRadioButton random;
	private ButtonGroup orderGroup;
	private JButton nextLine;
	private JButton showWord;
	private JButton showLine;
	private JButton quit;
	// Even though there is no "-1" element in the lines List, this is incremented right off the bat so it's always >= 0 by the time it's first used.
	private int lastLine = -1;
	private Line line = new Line(new PlayCharacter(""), "");
	private String lineChunk;

	public PromptFrame()
	{
		setTitle("ShakePrompt");

		List<BufferedImage> icons = new ArrayList<BufferedImage>();
		try
		{
			icons.add(ImageIO.read(ClassLoader.getSystemResourceAsStream("icon-16.png")));
			icons.add(ImageIO.read(ClassLoader.getSystemResourceAsStream("icon-32.png")));
			icons.add(ImageIO.read(ClassLoader.getSystemResourceAsStream("icon-48.png")));
		}
		catch (IOException e)
		{
			System.out.println("Couldn't load icon!");
		}
		setIconImages(icons);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationByPlatform(true);

		Dimension size = new Dimension(480, 576);
		setPreferredSize(size);
		setMinimumSize(size);

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
		rangePanel = new JPanel();
		rangePanel.setLayout(new GridBagLayout());
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.insets = new Insets(0, 4, 0, 4);
		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 4;
		add(rangePanel, c);

		c = new GridBagConstraints();
		c.insets = new Insets(4, 4, 4, 4);
		c.gridx = 0;
		c.gridy = 0;
		rangePanel.add(new JLabel("Line range:"), c);

		c = new GridBagConstraints();
		rangeMinimum = new JTextField("1");
		rangeMinimum.setColumns(2);
		rangeMinimum.setHorizontalAlignment(JTextField.RIGHT);
		c.insets = new Insets(4, 4, 4, 4);
		c.gridx = 1;
		c.gridy = 0;
		rangePanel.add(rangeMinimum, c);

		c = new GridBagConstraints();
		JLabel rangeDivider = new JLabel("/");
		rangeDivider.setEnabled(false);
		c.insets = new Insets(4, 4, 4, 4);
		c.gridx = 2;
		c.gridy = 0;
		rangePanel.add(rangeDivider, c);

		c = new GridBagConstraints();
		rangeMaximum = new JTextField(new NumericDocument(), Integer.valueOf(Main.lines.size()).toString(), 2);
		rangeMaximum.setHorizontalAlignment(JTextField.RIGHT);
		c.insets = new Insets(4, 4, 4, 4);
		c.gridx = 3;
		c.gridy = 0;
		rangePanel.add(rangeMaximum, c);

		c = new GridBagConstraints();
		JLabel range = new JLabel("(Total: " + Integer.valueOf(Main.lines.size()).toString() + ")");
		range.setEnabled(false);
		c.insets = new Insets(4, 4, 4, 4);
		c.gridx = 4;
		c.gridy = 0;
		rangePanel.add(range, c);

		c = new GridBagConstraints();
		orderPanel = new JPanel();
		orderPanel.setLayout(new GridBagLayout());
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.insets = new Insets(0, 4, 0, 4);
		c.gridx = 0;
		c.gridy = 5;
		c.gridwidth = 4;
		add(orderPanel, c);

		c = new GridBagConstraints();
		c.insets = new Insets(4, 4, 4, 4);
		c.gridx = 0;
		c.gridy = 0;
		orderPanel.add(new JLabel("Order:"), c);

		c = new GridBagConstraints();
		sequential = new JRadioButton("Sequential");
		sequential.setSelected(true);
		c.insets = new Insets(4, 4, 4, 4);
		c.gridx = 1;
		c.gridy = 0;
		orderPanel.add(sequential, c);

		c = new GridBagConstraints();
		random = new JRadioButton("Random");
		c.insets = new Insets(4, 4, 4, 4);
		c.gridx = 2;
		c.gridy = 0;
		orderPanel.add(random, c);

		orderGroup = new ButtonGroup();
		orderGroup.add(sequential);
		orderGroup.add(random);

		c = new GridBagConstraints();
		nextLine = new JButton("Next Line");
		nextLine.setMnemonic(KeyEvent.VK_N);
		nextLine.setToolTipText("Show the next line.");
		nextLine.addActionListener(this);
		c.anchor = GridBagConstraints.EAST;
		c.gridx = 0;
		c.gridy = 6;
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
		c.gridy = 6;
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
		c.gridy = 6;
		c.insets = new Insets(4, 0, 4, 4);
		add(showLine, c);

		c = new GridBagConstraints();
		quit = new JButton("Quit");
		quit.setMnemonic(KeyEvent.VK_Q);
		quit.setToolTipText("Exit the program.");
		quit.addActionListener(this);
		c.anchor = GridBagConstraints.EAST;
		c.gridx = 3;
		c.gridy = 6;
		c.insets = new Insets(4, 4, 4, 4);
		add(quit, c);

		pack();

		nextLine();
	}

	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource().equals(nextLine))
		{
			nextLine();
		}
		else if (e.getSource().equals(showWord))
		{
			showWord();
		}
		else if (e.getSource().equals(showLine))
		{
			showLine();
		}
		else if (e.getSource().equals(quit))
		{
			System.exit(0);
		}
	}

	private void nextLine()
	{
		// Get the minimum/maximum range values from the GUI.
		int rangeMinimum = Integer.valueOf(this.rangeMinimum.getText()) - 1;
		int rangeMaximum = Integer.valueOf(this.rangeMaximum.getText()) - 1;

		// Make sure the minimum/maximum range values are acceptable.
		if (rangeMinimum < 0)
		{
			this.rangeMinimum.setText("1");
			rangeMinimum = 0;
		}
		if (rangeMinimum > rangeMaximum)
		{
			this.rangeMaximum.setText(Integer.valueOf(rangeMinimum + 1).toString());
			rangeMaximum = rangeMinimum;
		}

		// For sequential.
		int lineId = ++lastLine;
		System.out.println(lineId);
		if (lineId < rangeMinimum)
		{
			lineId = rangeMinimum;
			lastLine = rangeMinimum;
		}
		if (lineId > rangeMaximum)
		{
			lineId = rangeMinimum;
			lastLine = rangeMinimum;
		}

		// For random.
		if (random.isSelected())
		{
			lineId = new Random().nextInt(rangeMaximum - rangeMinimum) + rangeMinimum;
			lastLine = lineId;
		}

		// Try to get the line.
		try
		{
			line = Main.lines.get(lineId);
		}
		catch (IllegalArgumentException ex)
		{
			JOptionPane.showMessageDialog(null, "No lines found!", "ShakePrompt", JOptionPane.ERROR_MESSAGE);
		}

		// We've got to reset the line's word prompt, or else we'll recieve words from part-way through the line if the line's already been shown and the user's requested a prompt.
		line.reset();
		// Reset the chunk of the line we use to build word prompts.
		lineChunk = "<strong>" + line.getCharacter().getName() + ":</strong>";
		// Put the cue onscreen.
		cuePane.setText(line.getCue().toHTMLString());
		// Clear the previous line.
		linePane.setText("");
		// Enable the prompt buttons.
		showWord.setEnabled(true);
		showLine.setEnabled(true);
	}

	private void showWord()
	{
		lineChunk += " " + line.getNextWord().replace(System.getProperty("line.separator"), "<br>" + System.getProperty("line.separator"));
		linePane.setText(lineChunk);

		if (lineChunk.equals(line.toHTMLString()))
		{
			showWord.setEnabled(false);
			showLine.setEnabled(false);
		}
	}

	private void showLine()
	{
		linePane.setText(line.toHTMLString());
		showWord.setEnabled(false);
		showLine.setEnabled(false);
	}

	static class NumericDocument extends PlainDocument
	{

		@Override
		public void insertString(int offset, String string, AttributeSet a) throws BadLocationException
		{
			if (string == null)
			{
				return;
			}

			super.insertString(offset, string.replaceAll("[^0-9]", ""), a);
		}
	}
}
