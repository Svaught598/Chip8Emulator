package emulator;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Keyboard {

    public boolean[] keyPressed;
    public short lastKeyPressed;
    public short numberOfKeysPressed;

    public Keyboard(){
        keyPressed = new boolean[0x10];
        prepareKeyboard();
    }

    public boolean setKeys(int key, boolean isPressed){
        switch (key){

            case KeyEvent.VK_1:
                // key 1
                keyPressed[0x1] = isPressed;
                lastKeyPressed = 0x1;
                break;

            case KeyEvent.VK_2:
                // key 2
                keyPressed[0x2] = isPressed;
                lastKeyPressed = 0x2;
                break;

            case KeyEvent.VK_3:
                // key 3
                keyPressed[0x3] = isPressed;
                lastKeyPressed = 0x3;
                break;

            case KeyEvent.VK_4:
                // key C
                keyPressed[0xC] = isPressed;
                lastKeyPressed = 0xC;
                break;

            case KeyEvent.VK_Q:
                // key 4
                keyPressed[0x4] = isPressed;
                lastKeyPressed = 0x4;
                break;

            case KeyEvent.VK_W:
                //key 5
                keyPressed[0x5] = isPressed;
                lastKeyPressed = 0x5;
                break;

            case KeyEvent.VK_E:
                //key 6
                keyPressed[0x6] = isPressed;
                lastKeyPressed = 0x6;
                break;

            case KeyEvent.VK_R:
                // key D
                keyPressed[0xD] = isPressed;
                lastKeyPressed = 0xD;
                break;

            case KeyEvent.VK_A:
                // key 7
                keyPressed[0x7] = isPressed;
                lastKeyPressed = 0x7;
                break;

            case KeyEvent.VK_S:
                // key 8
                keyPressed[0x8] = isPressed;
                lastKeyPressed = 0x8;
                break;

            case KeyEvent.VK_D:
                // key 9
                keyPressed[0x9] = isPressed;
                lastKeyPressed = 0x9;
                break;

            case KeyEvent.VK_F:
                // key E
                keyPressed[0xE] = isPressed;
                lastKeyPressed = 0xE;
                break;

            case KeyEvent.VK_Z:
                // key A
                keyPressed[0xA] = isPressed;
                lastKeyPressed = 0xA;
                break;

            case KeyEvent.VK_X:
                // key 0
                keyPressed[0x0] = isPressed;
                lastKeyPressed = 0x0;
                break;

            case KeyEvent.VK_C:
                // key B
                keyPressed[0xB] = isPressed;
                lastKeyPressed = 0xB;
                break;

            case KeyEvent.VK_V:
                // key F
                keyPressed[0xF] = isPressed;
                lastKeyPressed = 0xF;
                break;

            default:
                return false;
        }
        return true;
    }

    public void prepareKeyboard(){
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher(){

            @Override
            public boolean dispatchKeyEvent(KeyEvent e){   

                synchronized (Keyboard.class){
                    switch (e.getID()){
                        case KeyEvent.KEY_PRESSED:
                            if (setKeys(e.getKeyCode(), true)){
                                numberOfKeysPressed += 1;
                                System.out.println(e.getKeyCode() + ": Pressed");
                            }
                            break;
                        case KeyEvent.KEY_RELEASED:
                            if (setKeys(e.getKeyCode(), false)){
                                numberOfKeysPressed -= 1;
                                System.out.println(e.getKeyCode() + ": Released");
                            }
                            break;
                   }
                   return false;
               }
            }

        });

    }

    public short waitForKeyPress(){
        while (numberOfKeysPressed == 0){
            try {
                Thread.sleep(1);
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        return lastKeyPressed;
    }
}