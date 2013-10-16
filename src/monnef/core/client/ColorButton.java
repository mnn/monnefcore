/*
 * Automatic Assembly Table
 * author: monnef
 */

package monnef.core.client;

import monnef.core.utils.ColorEnum;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class ColorButton extends GuiButton {
    private final int defaultStateNumber;

    private State currentState;
    private State[] states;

    private static class State {
        public int color;
        public String text;
        public boolean isColor;

        public State(int color) {
            this.color = color;
            isColor = true;
        }

        public State(String text) {
            this.text = text;
            isColor = false;
        }

        public boolean isColor() {
            return isColor;
        }
    }

    public ColorButton(int x, int y, int width, int height, Integer[] colorStates, String[] stringStates, int defaultStateNumber, boolean stringFirst) {
        super(x, y, width, height, ColorEnum.WHITE.getInt(), "");
        this.defaultStateNumber = defaultStateNumber;
        setState(defaultStateNumber);
        states = new State[colorStates.length + stringStates.length];
        if (stringFirst) {
            for (int i = 0; i < stringStates.length; i++) {
                states[i] = new State(stringStates[i]);
            }
            for (int i = 0; i < colorStates.length; i++) {
                states[i + stringStates.length] = new State(colorStates[i]);
            }
        } else {
            for (int i = 0; i < colorStates.length; i++) {
                states[i] = new State(colorStates[i]);
            }
            for (int i = 0; i < stringStates.length; i++) {
                states[i + colorStates.length] = new State(stringStates[i]);
            }
        }
    }

    public void setState(int stateNumber) {
        currentState = states[stateNumber];
        if (currentState.isColor) displayString = "";
        else displayString = currentState.text;
    }

    private static final int borderSize = 3;

    @Override
    public void drawButton(Minecraft par1Minecraft, int par2, int par3) {
        super.drawButton(par1Minecraft, par2, par3);
        if (currentState.isColor) {
            drawRect(xPosition + borderSize, yPosition + borderSize, xPosition + width - borderSize, yPosition + height - borderSize, currentState.color);
        }
    }
}
