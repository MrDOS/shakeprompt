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
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import javax.swing.UIManager;

public class Main
{
    private static final String APP_NAME = "ShakePrompt";
    private static ImageIcon DLG_ICON;

    static {
        try
        {
            DLG_ICON = new ImageIcon(ImageIO.read(ClassLoader.getSystemResourceAsStream("icon-48.png")));
        }
        catch (IOException e)
        {
            DLG_ICON = null;
        }
    }

    public static void main(String[] args)
    {
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception e)
        {
        }

        List<Play> plays = null;
        List<PlayCharacter> characters = null;
        List<Line> lines = null;

        try
        {
            plays = OSSClient.getPlays();
        }
        catch (IOException e)
        {
            fail("Could not retrieve a list of plays!", 100);
        }

        Play play = promptOption("From which play do you want to select a character?", plays);
        if (play == null)
            System.exit(0);

        try
        {
            characters = OSSClient.getPlayCharacters(play);
        }
        catch (IOException e)
        {
            fail("Could not retrieve a list of characters for the play!", 101);
        }

        PlayCharacter character = promptOption("For which character do you want to view lines?", characters);
        if (character == null)
            System.exit(0);

        try
        {
            lines = OSSClient.getCharacterLines(play, character);
        }
        catch (IOException e)
        {
            fail("Could not retrieve lines for the character!", 102);
        }

        PromptFrame frame = new PromptFrame(lines);
        frame.setVisible(true);
    }

    private static <T> T promptOption(String message, List<T> options)
    {
        Object[] optarr = options.toArray(new Object[options.size()]);
        return (T) JOptionPane.showInputDialog(null, message, APP_NAME,
                                               JOptionPane.QUESTION_MESSAGE,
                                               DLG_ICON, optarr, optarr[0]);
    }

    private static void fail(String message, int errorCode)
    {
        JOptionPane.showMessageDialog(null, message, APP_NAME,
                                      JOptionPane.ERROR_MESSAGE, DLG_ICON);
        System.exit(errorCode);
    }
}
