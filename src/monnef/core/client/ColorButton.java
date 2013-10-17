/*
 * Automatic Assembly Table
 * author: monnef
 */

package monnef.core.client;

import monnef.core.utils.ColorEnum;
import net.minecraft.client.Minecraft;

public class ColorButton extends AdvancedButton {
    private final int defaultStateNumber;

    private int currentStateNumber;
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

    public ColorButton(int id, int x, int y, int width, int height, Integer[] colorStates, String[] stringStates, int defaultStateNumber, boolean stringFirst) {
        super(id, x, y, width, height, "");
        this.defaultStateNumber = defaultStateNumber;
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
        resetState();
    }

    public void setState(int stateNumber) {
        currentStateNumber = stateNumber;
        refreshState();
    }

    private void refreshState() {
        fixOverflow();
        currentState = states[currentStateNumber];
        if (currentState.isColor) displayString = "";
        else displayString = currentState.text;
    }

    public void resetState() {
        setState(defaultStateNumber);
    }

    public void nextState() {
        currentStateNumber++;
        refreshState();
    }

    public void prevState() {
        currentStateNumber--;
        refreshState();
    }

    private void fixOverflow() {
        if (currentStateNumber >= states.length) {
            currentStateNumber = 0;
        } else if (currentStateNumber < 0) {
            currentStateNumber = states.length - 1;
        }
    }

    private static final int borderSizeX = 5;
    private static final int borderSizeY = 6;
    private static final int blackBorderSize = 1;

    @Override
    public void drawButton(Minecraft par1Minecraft, int par2, int par3) {
        super.drawButton(par1Minecraft, par2, par3);
        if (currentState.isColor) {
            int x1 = xPosition + borderSizeX;
            int y1 = yPosition + borderSizeY;
            int x2 = xPosition + width - borderSizeX;
            int y2 = yPosition + height - borderSizeY;
            drawRect(x1 - blackBorderSize, y1 - blackBorderSize, x2 + blackBorderSize, y2 + blackBorderSize, ColorEnum.BLACK.getInt());
            drawRect(x1, y1, x2, y2, currentState.color);
        }
    }
}
