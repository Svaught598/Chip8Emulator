package emulator;

import java.util.Random;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;

public class CPU extends Component{

    //TODO: Create Separate class for Memory
    public int PROGRAM_START_ADDRESS = 0x200;

    Random random = new Random();
    Panel panel;

    // registers, stack, memory, timers, etc...
    public short memory[] = new short[4096];
    public short V[] = new short[16];
    public int stack[] = new int[16];
    public int I;
    public short delayTimer;
    public short soundTimer;
    public int programCounter;
    public short stackPointer;
    public int opcode;
    public boolean carry;

    // some working variables for interpretting opcodes
    int nnn;
    int nn;
    short n;
    int vxi; 
    int vyi;
    int sum;

    // Hexadecimal Sprites
    public short[] fontSet = {
        0xF0, 0x90, 0x90, 0x90, 0xF0, // 0
        0x20, 0x60, 0x20, 0x20, 0x70, // 1
        0xF0, 0x10, 0xF0, 0x80, 0xF0, // 2
        0xF0, 0x10, 0xF0, 0x10, 0xF0, // 3
        0x90, 0x90, 0xF0, 0x10, 0x10, // 4
        0xF0, 0x80, 0xF0, 0x10, 0xF0, // 5
        0xF0, 0x80, 0xF0, 0x90, 0xF0, // 6
        0xF0, 0x10, 0x20, 0x40, 0x40, // 7
        0xF0, 0x90, 0xF0, 0x90, 0xF0, // 8
        0xF0, 0x90, 0xF0, 0x10, 0xF0, // 9
        0xF0, 0x90, 0xF0, 0x90, 0x90, // A
        0xE0, 0x90, 0xE0, 0x90, 0xE0, // B
        0xF0, 0x80, 0x80, 0x80, 0xF0, // C
        0xE0, 0x90, 0x90, 0x90, 0xE0, // D
        0xF0, 0x80, 0xF0, 0x80, 0xF0, // E
        0xF0, 0x80, 0xF0, 0x80, 0x80  // F
    };

    public CPU(){   
        //initialize the stack & registers
        stackPointer = 0;

        //most CHIP-8 programs start at byte 512
        programCounter = PROGRAM_START_ADDRESS;
        
        //load hex sprites into memory
        for (int i = 0; i < 80; i++){
            memory[i] = fontSet[i];
        }
    }

    protected void interpretOpcode(int opcode)
    {
        switch (opcode){
            case 0x0000:

                switch (opcode & 0x00FF){
                    case 0x00E0:
                        //Clears the screen. 
                        panel.clearScreen();
                        programCounter += 2;
                        break;

                    case 0x00EE:
                        //Returns from a subroutine. 
                        stackPointer -= 1;
                        programCounter = stack[stackPointer];
                        programCounter += 2;
                        break;

                    default:
                        //Unrecognized opcode.
                        System.out.printf("OPCODE ERROR: Unrecognized Opcode 0x%X\n", opcode);
                        break;
                }
                break;

            case 0x1000:
                //Jumps to address NNN. 
                nnn = opcode & 0x0FFF;
                programCounter = nnn;
                break;

            case 0x2000:
                //Calls subroutine at NNN. 
                nnn = opcode & 0x0FFF;
                stack[stackPointer] = programCounter;
                programCounter = nnn;
                stackPointer += 1;
                break;

            case 0x3000:
                //Skips the next instruction if VX equals NN. .
                vxi = (opcode & 0x0F00) >> 8;
                nn = opcode & 0x00FF;
                if(V[vxi] == nn){
                    programCounter += 4;
                } 
                else{
                    programCounter +=2;
                }
                break;

            case 0x4000:
                //Skips the next instruction if VX doesn't equal NN.
                vxi = (opcode & 0x0F00) >> 8;
                nn = opcode & 0x00FF;
                if(V[vxi] != nn){
                    programCounter += 4;
                }
                else{
                    programCounter += 2;
                }
                break;

            case 0x5000:
                //Skips the next instruction if VX equals VY. 
                vxi = (opcode & 0x0F00) >> 8;
                vyi = (opcode & 0x00F0) >> 4;
                if(V[vxi] == V[vyi]){
                    programCounter += 4;
                }
                else{
                    programCounter += 2;
                }
                break;

            case 0x6000:
                //Sets VX to NN. 
                nn = opcode & 0x00FF;
                vxi = (opcode & 0x0F00) >> 8;
                V[vxi] = (short) nn;
                programCounter += 2;
                break;

            case 0x7000:
                //Adds NN to VX. (Carry flag is not changed) 
                nn = opcode & 0x00FF;
                vxi = (opcode & 0x0F00) >> 8;
                V[vxi] += nn;
                programCounter += 2;
                break;
            
            case 0x8000:

                switch (opcode & 0x000F){
                    case 0x0000:
                        //Sets VX to the value of VY. 
                        vxi = (opcode & 0x0F00) >> 8;
                        vyi = (opcode & 0x00F0) >> 4;
                        V[vxi] = V[vyi];
                        programCounter += 2;
                        break;

                    case 0x0001:
                        //Sets VX to VX or VY.
                        vxi = (opcode & 0x0F00) >> 8;
                        vyi = (opcode & 0x00F0) >> 4;
                        V[vxi] = (short) (V[vxi] | V[vyi]);
                        programCounter += 2;
                        break;

                    case 0x0002:
                        //Sets VX to VX and VY.
                        vxi = (opcode & 0x0F00) >> 8;
                        vyi = (opcode & 0x00F0) >> 4;
                        V[vxi] = (short) (V[vxi] & V[vyi]);
                        programCounter += 2;
                        break;

                    case 0x0003:
                        //Sets VX to VX xor VY. 
                        vxi = (opcode & 0x0F00) >> 8;
                        vyi = (opcode & 0x00F0) >> 4;
                        V[vxi] = (short) (V[vxi] ^ V[vyi]);
                        programCounter += 2;
                        break;
                    
                    case 0x0004:
                        //Adds VY to VX. VF is set to 1 when there's a carry, and to 0 when there isn't. 
                        vxi = (opcode & 0x0F00) >> 8;
                        vyi = (opcode & 0x00F0) >> 4;
                        sum = V[vxi] + V[vyi];
                        V[vxi] = (short) (sum & 0xFF);
                        if(sum > 255){
                            V[15] = 1;
                        } 
                        else {
                            V[15] = 0;
                        }
                        programCounter += 2;
                        break;

                    case 0x0005:
                        //VY is subtracted from VX. VF is set to 0 when there's a borrow, and 1 when there isn't. 
                        vxi = (opcode & 0x0F00) >> 8;
                        vyi = (opcode & 0x00F0) >> 4;
                        V[vxi] = (short) (V[vxi] - V[vyi]);
                        if(V[vxi] > V[vyi]){
                            V[15] = 1;
                        } 
                        else {
                            V[15] = 0;
                        }
                        programCounter += 2;
                        break;

                    case 0x0006:
                        //Stores the least significant bit of VX in VF and then shifts VX to the right by 1.
                        vxi = (opcode & 0x0F00) >> 8;
                        if((V[vxi] & 1) == 1){
                            V[15] = 1;
                        }
                        else{
                            V[15] = 0;
                        }
                        V[vxi] >>= 1;
                        programCounter += 2;
                        break;

                    case 0x0007:
                        //Sets VX to VY minus VX. VF is set to 0 when there's a borrow, and 1 when there isn't. 
                        vxi = (opcode & 0x0F00) >> 8;
                        vyi = (opcode & 0x00F0) >> 4;
                        V[vxi] = (short) (V[vyi] - V[vxi]);
                        if(V[vyi] > V[vxi]){
                            V[15] = 1;
                        } 
                        else {
                            V[15] = 0;
                        }
                        programCounter += 2;
                        break;

                    case 0x000E:
                        //Stores the most significant bit of VX in VF and then shifts VX to the left by 1.
                        vxi = (opcode & 0x0F00) >> 8;
                        if((V[vxi] & 0x80) == 1){
                            V[15] = 1;
                        }
                        else{
                            V[15] = 0;
                        }
                        V[vxi] <<= 1;
                        programCounter += 2;
                        break;

                    default:
                        //Unrecognized opcode.
                        System.out.printf("OPCODE ERROR: Unrecognized Opcode 0x%X\n", opcode);
                        break;
                }
                break;

            case 0x9000:
                //Skips the next instruction if VX doesn't equal VY. 
                vxi = (opcode & 0x0F00) >> 8;
                vyi = (opcode & 0x00F0) >> 4;
                if(V[vxi] != V[vyi]){
                    programCounter += 4;
                }
                else{
                    programCounter += 2;
                }
                break;

            case 0xA000:
                //Sets I to the address NNN. 
                nnn = opcode & 0x0FFF;
                I = nnn;
                programCounter += 2;
                break;

            case 0xB000:
                //Jumps to the address NNN plus V0. 
                nnn = opcode & 0x0FFF;
                programCounter = nnn + V[0];
                break;

            case 0xC000:
                //Sets VX to the result of a bitwise and operation on a random number (Typically: 0 to 255) and NN. 
                vxi = (opcode & 0x0F00) >> 8;
                nn = (opcode & 0x00FF);
                V[vxi] = (short) (nn & random.nextInt(256));
                break;

            case 0xD000:
                //Draws a sprite at coordinate (VX, VY) that has a width of 8 pixels and a height of N pixels. 
                //Each row of 8 pixels is read as bit-coded starting from memory location I; 
                //I value doesn’t change after the execution of this instruction. As described above, 
                //VF is set to 1 if any screen pixels are flipped from set to unset when the sprite is drawn, and to 0 if that doesn’t happen 
                vxi = (opcode & 0x0F00) >> 8;
                vyi = (opcode & 0x00F0) >> 4;
                n = (short) (opcode & 0x000F);

                panel.drawSprite(V[vxi], V[vyi], n);
                if (panel.pixelFlipped == true){
                    V[0xF] = 1;
                }
                else{
                    V[0xF] = 0;
                }
                programCounter += 2;
                break;

            case 0xE000:

                switch (opcode & 0x00FF){
                    case 0x009E:
                        //Skips the next instruction if the key stored in VX is pressed. (Usually the next instruction is a jump to skip a code block) 
                        //TODO: write this case out
                        break;

                    case 0x00A1:
                        //Skips the next instruction if the key stored in VX isn't pressed. (Usually the next instruction is a jump to skip a code block) 
                        //TODO: write this case out
                        break;
                    
                    default:
                        //Unrecognized opcode.
                        System.out.printf("OPCODE ERROR: Unrecognized Opcode 0x%X\n", opcode);
                        break;
                }
                break;

            case 0xF000:
                
                switch (opcode & 0x00FF){
                    case 0x0007:
                        //Sets VX to the value of the delay timer. 
                        vxi = (opcode & 0x0F00) >> 8;
                        V[vxi] = delayTimer;
                        programCounter += 2;
                        break;

                    case 0x000A:
                        //A key press is awaited, and then stored in VX. (Blocking Operation. All instruction halted until next key event) 
                        //TODO: Write This method
                        break;

                    case 0x0015:
                        //Sets the delay timer to VX. 
                        //TODO: Write this method
                        break;

                    case 0x0018:
                        //Sets the sound timer to VX. 
                        //TODO: Write this method
                        break;

                    case 0x001E:
                        //Adds VX to I. VF is set to 1 when there is a range overflow (I+VX>0xFFF), and to 0 when there isn't.
                        //TODO: Write this method
                        break;

                    case 0x0029:
                        //Sets I to the location of the sprite for the character in VX. Characters 0-F (in hexadecimal) are represented by a 4x5 font. 
                        //TODO: Write this method
                        break;

                    case 0x0033:
                        //Stores the binary-coded decimal representation of VX, with the most significant of three digits at the address in I, 
                        //the middle digit at I plus 1, and the least significant digit at I plus 2. (In other words, 
                        //take the decimal representation of VX, place the hundreds digit in memory at location in I, 
                        //the tens digit at location I+1, and the ones digit at location I+2.) 
                        //TODO: Write this method
                        break;

                    case 0x0055:
                        //Stores V0 to VX (including VX) in memory starting at address I. The offset from I is 
                        //increased by 1 for each value written, but I itself is left unmodified.
                        //TODO: Write this method
                        break;

                    case 0x0065:
                        //Fills V0 to VX (including VX) with values from memory starting at address I. 
                        //The offset from I is increased by 1 for each value written, but I itself is left unmodified.
                        //TODO: Write this method
                        break;
                    
                    default:
                        //Unrecognized opcode.
                        System.out.printf("OPCODE ERROR: Unrecognized Opcode 0x%X\n", opcode);
                        break;
                }
                break;
        }
    }  

    public boolean loadRom(File rom){
        try{
            FileInputStream fileInputStream = new FileInputStream(rom);
            byte[] instructions = new byte[(int) rom.length()];

            //convert file into array of bytes
            fileInputStream.read(instructions);
            fileInputStream.close();
            for (int i = 0; i < instructions.length; i++)
            {
                //TODO: load instructions into CPU memory
                // do that here, or call method or something
                memory[PROGRAM_START_ADDRESS + i] = instructions[i];
                System.out.print((byte) instructions[i]);
            }
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public void step(){
        opcode = (short) memory[programCounter] << 8; 
        opcode += ((short) memory[programCounter + 1]);
        interpretOpcode(opcode);
    }
}