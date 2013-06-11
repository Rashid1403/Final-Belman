package gui;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Graphical User Interface NumbersOnlyKeyListener klassen.
 *
 * @author Daniel, Klaus, Mak, Rashid
 */
public class NumbersOnlyKeyListener extends KeyAdapter
{
    @Override
    public void keyTyped(KeyEvent ke)
    {
        if (!Character.isDigit(ke.getKeyChar()))
        {
            ke.consume();
        }
    }
}
