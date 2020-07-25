package emulator;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import jdk.internal.jline.internal.TestAccessible;

/**
 * Testing for opcodes!
 */
public class CPUTest {

    CPU cpu = new CPU();


    /**
     * Test: Opcode 0x1NNN
     * 
     * Correct functionality is:
     *  - moves programCounter to NNN
     */
    @Test
    public void testOpcode_0x1NNN(){
        // load opcode 0x14AB into memory and emulate a cycle
        short[] instructions = {0x14, 0xAB};
        cpu.initializeCPU();
        cpu.loadInstructions(instructions);
        cpu.step();

        // check programCounter to make sure it is at address 0x4AB
        assertTrue(cpu.programCounter == 0x4AB);
    }


    /**
     * Test: Opcode 0x2NNN
     * 
     * Correct Functionality is:
     *  - moves programCounter to NNN
     *  - stores current programCounter on stack[] at current stack pointer
     *  - increments stackPointer
     */
    @Test 
    public void testOpcode_0x2NNN(){
        // load opcode 0x24AB into memory and emulate a cycle
        short[] instructions = {0x24, 0xAB};
        cpu.initializeCPU();
        cpu.loadInstructions(instructions);
        cpu.step();

        // check programCounter to make sure it is at address 0x4AB
        assertTrue(cpu.programCounter == 0x4AB);

        // check stack at stackPointer - 1 to see if it is our original address
        assertTrue(cpu.stack[cpu.stackPointer - 1] == cpu.PROGRAM_START_ADDRESS);

        //check stackPointer to make sure it has incremented
        assertTrue(cpu.stackPointer == 1);
    }


    /**
     * Test: Opcode 0x3XNN
     * 
     * Correct Functionality is:
     *  - increments programCounter by 2 if VX != NN
     *  - increments programCounter by 4 if VX == NN
     */
    @Test
    public void testOpcode_0x3XNN(){
        // Increment by 2 case
        // load opcode 0x32AB into memory, emulate a cycle, and test
        short[] instructions = {0x32, 0xAB};
        cpu.initializeCPU();
        cpu.loadInstructions(instructions);
        cpu.V[2] = 0x28;
        cpu.step();
        assertTrue(cpu.programCounter == cpu.PROGRAM_START_ADDRESS + 2);

        //Increment by 4 case
        // load opcode 032AB into memory, emulate a cycle, and test
        cpu.initializeCPU();
        cpu.loadInstructions(instructions);
        cpu.V[2] = 0xAB;
        cpu.step();
        assertTrue(cpu.programCounter == cpu.PROGRAM_START_ADDRESS + 4);
    }


    /**
     * Test: Opcode 0x4XNN
     * 
     * Correct Functionality is:
     *  - increments programCounter by 2 if VX == NN
     *  - increments programCounter by 4 if VX != NN
     */
    @Test
    public void testOpcode_0x4XNN(){
        // Increment by 2 case
        // load opcode 0x42AB into memory, emulate a cycle, and test
        short[] instructions = {0x42, 0xAB};
        cpu.initializeCPU();
        cpu.loadInstructions(instructions);
        cpu.V[2] = 0xAB;
        cpu.step();
        assertTrue(cpu.programCounter == cpu.PROGRAM_START_ADDRESS + 2);

        //Increment by 4 case
        // load opcode 042AB into memory, emulate a cycle, and test
        cpu.initializeCPU();
        cpu.loadInstructions(instructions);
        cpu.V[2] = 0x28;
        cpu.step();
        assertTrue(cpu.programCounter == cpu.PROGRAM_START_ADDRESS + 4);
    }
    

    /**
     * Test: Opcode 0x5XY0
     * 
     * Correct Functionality is:
     *  - increments program counter by 2 if VX != VY
     *  - increments program counter by 4 if VX == VY
     */
    @Test
    public void testOpcode_0x5XY0(){
        // Increment by 2 case
        // load the opcode 0x51F0 into memory, emulate a cycle, and test
        short[] instructions = {0x51, 0xF0};
        cpu.initializeCPU();
        cpu.loadInstructions(instructions);
        cpu.V[0x1] = 51; cpu.V[0xF] = 123;
        cpu.step();
        assertTrue(cpu.programCounter == cpu.PROGRAM_START_ADDRESS + 2);

        // Increment by 4 case
        // load the opcode 0x51F0 into memory, emulate a cycle, and test
        cpu.initializeCPU();
        cpu.loadInstructions(instructions);
        cpu.V[0x1] = 51; cpu.V[0xF] = 51;
        cpu.step();
        assertTrue(cpu.programCounter == cpu.PROGRAM_START_ADDRESS + 4);
    }


    /**
     * Test: Opcode 0x6XNN
     * 
     * Correct Functionality is:
     *  - sets register V[x] = NN
     *  - increments programCounter by 2
     */
    @Test
    public void testOpcode_0x6XNN(){
        //load the opcode 0x63AB into memory, emulate a cycle, and test
        short[] instructions = {0x63, 0xAB};
        cpu.initializeCPU();
        cpu.loadInstructions(instructions);
        cpu.V[0x3] = 12;
        cpu.step();

        // test that value of register was updated
        assertTrue(cpu.V[0x3] == 0xAB);
        assertFalse(cpu.V[0x3] == 12);

        // test that programCounter was incremented
        assertTrue(cpu.programCounter == cpu.PROGRAM_START_ADDRESS + 2);
    }


    /**
     * Test: Opcode 0x7XNN
     * 
     * Correct Functionality is:
     *  - sets V[x] = V[x] + NN (modulo 256)
     *  - carry flag is unchanged
     *  - program counter is incremented by 2
     */
    @Test
    public void testOpcode_0x7XNN(){
        // opcode 72AB, test for addition under 256
        short[] instructions = {0x72, 0xAB};
        cpu.initializeCPU();
        cpu.loadInstructions(instructions);
        cpu.V[0x2] = 15;
        cpu.step();
        assertTrue(cpu.V[0x2] == (15 + 0xAB));

        // test for addition over 256 & program counter & carry flag
        cpu.initializeCPU();
        cpu.loadInstructions(instructions);
        cpu.V[0x2] = 0xFF;
        cpu.step();
        assertTrue(cpu.V[0x2] == (short) ((0xFF + 0xAB) % 256));
        assertTrue(cpu.programCounter == cpu.PROGRAM_START_ADDRESS + 2);
        assertTrue(cpu.carry == false);
    }


    /**
     * Test: Opcode 0x8XY0
     * 
     * Correct Functionality is:
     *  - Sets V[y] = V[x]
     *  - increments program counter by 2
     */
    @Test
    public void testOpcode_08XY0(){
        // Opcode 8AB0, test for v[y] = v[x] and pc +=2
        short[] instructions = {0x8A, 0xB0};
        cpu.initializeCPU();
        cpu.loadInstructions(instructions);
        cpu.V[0xA] = 42;
        cpu.V[0xB] = 82;
        cpu.step();
        assertTrue(cpu.V[0xA] == cpu.V[0xB]);
        assertTrue(cpu.V[0xB] == 82);
        assertTrue(cpu.programCounter == cpu.PROGRAM_START_ADDRESS + 2);
    }


    /**
     * Test: Opcode 0x8XY1
     * 
     * Correct Functionality:
     *  - Sets V[x] = V[x] or V[y] (bitwise or)
     *  - increments program counter by 2
     */
    @Test
    public void testOpcode_08XY1(){
        // Opcode 0x8AB1, test bitwise or, and pc +=2
        short[] instructions = {0x8A, 0xB1};
        cpu.initializeCPU();
        cpu.loadInstructions(instructions);
        cpu.V[0xA] = 0b10000000;
        cpu.V[0xB] = 0b00000001;
        cpu.step();
        assertTrue(cpu.V[0xA] == 0b10000001);
        assertTrue(cpu.V[0xB] == 0b00000001);
        assertTrue(cpu.programCounter == cpu.PROGRAM_START_ADDRESS + 2);
    }


    /**
     * Test: Opcode 0x8XY2
     * 
     * Correct Functionality:
     *  - Sets v[x] = v[x] and v[y] (bitwise and)
     *  - increments program counter by 2
     */
    @Test
    public void testOpcode_0x8XY2(){
        //opcode 0x8AB2, test bitwise and, and pc += 2
        short[] instructions = {0x8A, 0xB2};
        cpu.initializeCPU();
        cpu.loadInstructions(instructions);
        cpu.V[0xA] = 0b00000001;
        cpu.V[0xB] = 0b10000000;
        cpu.step();
        assertTrue(cpu.V[0xA] == 0b00000000);
        assertTrue(cpu.V[0xB] == 0b10000000);
        assertTrue(cpu.programCounter == cpu.PROGRAM_START_ADDRESS + 2);
    }


    /**
     * Test: Opcode 0x8XY3
     * 
     * Correct Functionality:
     *  - sets v[x] = v[x] xor v[y] (bitwise xor)
     *  - increments program counter by 2
     */
    @Test
    public void testOpcode_0x8XY3(){
        //opcode 0x8AB3, test bitwise xor, and pc += 2
        short[] instructions = {0x8A, 0xB3};
        cpu.initializeCPU();
        cpu.loadInstructions(instructions);
        cpu.V[0xA] = 0b11111000;
        cpu.V[0xB] = 0b00011111;
        cpu.step();
        assertTrue(cpu.V[0xA] == 0b11100111);
        assertTrue(cpu.V[0xB] == 0b00011111);
        assertTrue(cpu.programCounter == cpu.PROGRAM_START_ADDRESS + 2);
    }


    /**
     * Test: Opcode 0x8XY4
     * 
     * Correct Functionality:
     *  - set v[x] = v[x] + v[y]
     *  - Set VF to 01 if a carry occurs
     *  - Set VF to 00 if a carry does not occur
     *  - increment program counter by 2
     */
    @Test
    public void testOpcode_0x8XY4(){
        //opcode 0x8AB4, carry occurs
        short[] instructions = {0x8A, 0xB4};
        cpu.initializeCPU();
        cpu.loadInstructions(instructions);
        cpu.V[0xA] = 0xFF;
        cpu.V[0xB] = 0x0F;
        cpu.step();
        assertTrue(cpu.V[0xA] == 0x0E);
        assertTrue(cpu.V[0xB] == 0x0F);
        assertTrue(cpu.V[0xF] == 1);
        assertTrue(cpu.programCounter == cpu.PROGRAM_START_ADDRESS + 2);

        //same opcode, carry does not occur
        cpu.initializeCPU();
        cpu.loadInstructions(instructions);
        cpu.V[0xA] = 0x0F;
        cpu.V[0xB] = 0x01;
        cpu.step();
        assertTrue(cpu.V[0xA] == 0x10);
        assertTrue(cpu.V[0xB] == 0x01);
        assertTrue(cpu.V[0xF] == 0);
        assertTrue(cpu.programCounter == cpu.PROGRAM_START_ADDRESS + 2);
    }


    /**
     * Test: Opcode 0x8XY5
     * 
     * Correct Functionality:
     *  - set V[x] = V[x] - V[y] (mod 256)
     *  - Set VF to 00 if a borrow occurs
     *  - Set VF to 01 if a borrow does not occur
     *  - increment program counter by 2
     */
    @Test
    public void testOpcode_0x8XY5(){
        //opcode 0x8AB5, borrow occurs
        short[] instructions = {0x8A, 0xB5};
        cpu.initializeCPU();
        cpu.loadInstructions(instructions);
        cpu.V[0xA] = 0x0F;
        cpu.V[0xB] = 0xFF;
        cpu.step();
        assertTrue(cpu.V[0xA] == 0x10);
        assertTrue(cpu.V[0xB] == 0xFF);
        assertTrue(cpu.V[0xF] == 0);
        assertTrue(cpu.programCounter == cpu.PROGRAM_START_ADDRESS + 2);

        //same opcode, borrow does not occur
        cpu.initializeCPU();
        cpu.loadInstructions(instructions);
        cpu.V[0xA] = 0xFF;
        cpu.V[0xB] = 0x0F;
        cpu.step();
        assertTrue(cpu.V[0xA] == 0xF0);
        assertTrue(cpu.V[0xB] == 0x0F);
        assertTrue(cpu.V[0xF] == 1);
        assertTrue(cpu.programCounter == cpu.PROGRAM_START_ADDRESS + 2);
    }


    /**
     * Test: Opcode 0x8XY6
     * 
     * Correct Functionality:
     *  - set V[x] = v[y] >> 1 (bitwise right shift)
     *  - Set register VF to the least significant bit prior to the shift
     *  - increment program counter by 2
     */
    @Test
    public void testOpcode_0x8XY6(){
        //TODO: fix test for 0x8XX6 implementation

        // //opcode 0x8AB6, LSB is 0
        // short[] instructions = {0x8A, 0xB6};
        // cpu.initializeCPU();
        // cpu.loadInstructions(instructions);
        // cpu.V[0xB] = 0b10000100;
        // cpu.step();
        // assertTrue(cpu.V[0xA] == 0b01000010);
        // assertTrue(cpu.V[0xB] == 0b10000100);
        // assertTrue(cpu.V[0xF] == 0);
        // assertTrue(cpu.programCounter == cpu.PROGRAM_START_ADDRESS + 2);

        // //same opcode, LSB is 1
        // cpu.initializeCPU();
        // cpu.loadInstructions(instructions);
        // cpu.V[0xB] = 0b10001011;
        // cpu.step();
        // assertTrue(cpu.V[0xA] == 0b01000101);
        // assertTrue(cpu.V[0xB] == 0b10001011);
        // assertTrue(cpu.V[0xF] == 1);
        // assertTrue(cpu.programCounter == cpu.PROGRAM_START_ADDRESS + 2);
    }


    /**
     * Test: Opcode 0x8XY7
     * 
     * Correct Functionality:
     *  - Set v[x] = v[y] - v[x] (mod 256)
     *  - Set VF to 00 if a borrow occurs
     *  - Set VF to 01 if a borrow does not occur
     *  - increment program counter by 2
     */
    @Test
    public void testOpcode_08XY7(){
        //opcode 0x8AB7, borrow occurs
        short[] instructions = {0x8A, 0xB7};
        cpu.initializeCPU();
        cpu.loadInstructions(instructions);
        cpu.V[0xA] = 0xFF;
        cpu.V[0xB] = 0x0F;
        cpu.step();
        assertTrue(cpu.V[0xA] == 0x10);
        assertTrue(cpu.V[0xB] == 0x0F);
        assertTrue(cpu.V[0xF] == 0);
        assertTrue(cpu.programCounter == cpu.PROGRAM_START_ADDRESS + 2);

        //same opcode, borrow does not occur
        cpu.initializeCPU();
        cpu.loadInstructions(instructions);
        cpu.V[0xA] = 0x0F;
        cpu.V[0xB] = 0xFF;
        cpu.step();
        assertTrue(cpu.V[0xA] == 0xF0);
        assertTrue(cpu.V[0xB] == 0xFF);
        assertTrue(cpu.V[0xF] == 1);
        assertTrue(cpu.programCounter == cpu.PROGRAM_START_ADDRESS + 2);
    }


    /**
     * Test: Opcode 0x8XYE
     * 
     * Correct Functionality:
     *  - set v[x] = v[y] << 1 (bitwise shift left)
     *  - Set register VF to the most significant bit prior to the shift
     *  - increment program counter by 2
     */
    @Test
    public void testOpcode_0x8XYE(){
        //TODO: fix test for 0x8XXE implementation  

        // //opcode 0x8ABE, MSB is 0
        // short[] instructions = {0x8A, 0xBE};
        // cpu.initializeCPU();
        // cpu.loadInstructions(instructions);
        // cpu.V[0xB] = 0b01110011;
        // cpu.step();
        // assertTrue(cpu.V[0xA] == 0b11100110);
        // assertTrue(cpu.V[0xB] == 0b01110011);
        // assertTrue(cpu.V[0xF] == 0);
        // assertTrue(cpu.programCounter == cpu.PROGRAM_START_ADDRESS + 2);

        // // same opcode, MSB is 1
        // cpu.initializeCPU();
        // cpu.loadInstructions(instructions);
        // cpu.V[0xB] = 0b10000000;
        // cpu.step();
        // assertTrue(cpu.V[0xA] == 0b00000000);
        // assertTrue(cpu.V[0xB] == 0b10000000);
        // assertTrue(cpu.V[0xF] == 1);
        // assertTrue(cpu.programCounter == cpu.PROGRAM_START_ADDRESS + 2);
    }


    /**
     * Test: Opcode 0x9XY0
     * 
     * Correct Functionality:
     *  - increment program counter by 2 if v[x] == v[y]
     *  - increment program counter by 4 if v[x] != v[y]
     */
    @Test
    public void testOpcode_0x9XY0(){
        //opcode 9AB0, increment by 2
        short[] instructions = {0x9A, 0xB0};
        cpu.initializeCPU();
        cpu.loadInstructions(instructions);
        cpu.V[0xA] = 0xFB;
        cpu.V[0xB] = 0xFB;
        cpu.step();
        assertTrue(cpu.programCounter == cpu.PROGRAM_START_ADDRESS + 2);

        //same opcode, increment by 4
        cpu.initializeCPU();
        cpu.loadInstructions(instructions);
        cpu.V[0xA] = 0xFF;
        cpu.V[0xB] = 0xAB;
        cpu.step();
        assertTrue(cpu.programCounter == cpu.PROGRAM_START_ADDRESS + 4);
    }


    /**
     * Test: Opcode 0xANNN
     * 
     * Correct Functionality:
     *  - set I = NNN
     *  - increment program counter by 2
     */
    @Test
    public void testOpcode_0xANNN(){
        //opcode 0xA4BC
        short[] instructions = {0xA4, 0xBC};
        cpu.initializeCPU();
        cpu.loadInstructions(instructions);
        cpu.step();
        assertTrue(cpu.I == 0x4BC);
        assertTrue(cpu.programCounter == cpu.PROGRAM_START_ADDRESS + 2);
    }


    /**
     * Test: Opcode 0xBNNN
     * 
     * Correct Functionality:
     *  - set program counter = NNN + v[0]
     */
    @Test
    public void testOpcode_0xBNNN(){
        //opcode 0xB234, jump within memory
        short[] instructions = {0xB2, 0x34};
        cpu.initializeCPU();
        cpu.loadInstructions(instructions);
        cpu.V[0] = 0xFF;
        cpu.step();
        assertTrue(cpu.programCounter == 0x0234 + 0x00FF);

        //opcode 0xBFFF, jump out of memory, should stay at current memory address
        instructions[0] = 0xBF;
        instructions[1] = 0xFF;
        cpu.initializeCPU();
        cpu.loadInstructions(instructions);
        cpu.V[0] = 0xFF;
        cpu.step();
        assertTrue(cpu.programCounter == cpu.PROGRAM_START_ADDRESS);
    }


    /**
     * Test: Opcode 0xCXNN
     * 
     * Correct Functionality:
     *  - Set VX to a random number with a mask of NN
     *  - increment program counter by 2
     */
    @Test
    public void testOpcode_0xCXNN(){
        //opcode 0xCA55, check for less than NN, and pc += 2
        short[] instructions = {0xCA, 0x55};
        cpu.initializeCPU();
        cpu.loadInstructions(instructions);
        cpu.step();
        assertTrue(cpu.V[0xA] <= 0x55);
        assertTrue(cpu.programCounter == cpu.PROGRAM_START_ADDRESS + 2);
    }


    /**
     * Test: Opcode 0xFX07
     * 
     * Correct Functionality:
     *  - set v[x] = delayTimer
     *  - increment program counter by 2
     */
    @Test
    public void testOpcode_0xFX07(){
        //opcode 0xFA07, test delayTimer to register and pc += 2
        short[] instructions = {0xFA, 0x07};
        cpu.initializeCPU();
        cpu.loadInstructions(instructions);
        cpu.delayTimer = 200;
        cpu.step();
        assertTrue(cpu.V[0xA] == 200);
        assertTrue(cpu.programCounter == cpu.PROGRAM_START_ADDRESS + 2);
    }


    /**
     * Test: Opcode 0xFX15
     * 
     * Correct Functionality:
     *  - set delayTimer = v[X]
     *  - increment program counter by 2
     */
    @Test
    public void testOpcode_0xFX15(){
        //opcode 0xFA15, test register to delayTimer and pc += 2
        short[] instructions = {0xFA, 0x15};
        cpu.initializeCPU();
        cpu.loadInstructions(instructions);
        cpu.V[0xA] = 200;
        cpu.step();
        assertTrue(cpu.delayTimer == 200);
        assertTrue(cpu.programCounter == cpu.PROGRAM_START_ADDRESS + 2);
    }


    /**
     * Test: Opcode 0xFX18
     * 
     * Correct Functionality:
     *  - set soundTimer = V[x]
     *  - increment program counter by 2
     */
    @Test
    public void testOpcode_0xFX18(){
        //opcode 0xFA18, test register to soundTimer and pc += 2
        short[] instructions = {0xFA, 0x18};
        cpu.initializeCPU();
        cpu.loadInstructions(instructions);
        cpu.V[0xA] = 200;
        cpu.step();
        assertTrue(cpu.soundTimer == 200);
        assertTrue(cpu.programCounter == cpu.PROGRAM_START_ADDRESS + 2);
    }


    /**
     * Test: Opcode 0xFX1E
     * 
     * Correct Functionality:
     *  - set I = V[x]
     *  - increment program counter by 2
     */
    @Test
    public void testOpcode_0xFX1E(){
        //opcode 0xFA1E, test register to I, and pc += 2
        short[] instructions = {0xFA, 0x1E};
        cpu.initializeCPU();
        cpu.loadInstructions(instructions);
        cpu.V[0xA] = 200;
        cpu.step();
        assertTrue(cpu.I == 200);
        assertTrue(cpu.programCounter == cpu.PROGRAM_START_ADDRESS + 2);
    }


    /**
     * Test: Opcode 0xFX29
     * 
     * Correct Functionality:
     *  - Set I = address of sprite stored in V[x] 
     *  - INcrement program counter by 2
     */
    @Test
    public void testOpcode_0xFX29(){
        //opcode 0xFA29, test I = correct address
        short[] instructions = {0xFA, 0x29};
        cpu.initializeCPU();
        cpu.loadInstructions(instructions);
        cpu.V[0xA] = 5;
        cpu.step();
        assertTrue(cpu.I == 25);
        assertTrue(cpu.programCounter == cpu.PROGRAM_START_ADDRESS + 2);

        //same opcode, make sure we only get 4 bits
        cpu.initializeCPU();
        cpu.loadInstructions(instructions);
        cpu.V[0xA] = 0xF5;
        cpu.step();
        assertTrue(cpu.I == 25);
        assertTrue(cpu.programCounter == cpu.PROGRAM_START_ADDRESS + 2);
    }


    /**
     * Test: Opcode 0xFX33
     * 
     * Correct Functionality:
     *  - set memory[I] = hundreds digit of v[x]
     *  - set memory[I+1] = tens digit of v[x]
     *  - set memory[I+2] = ones digit of v[x]
     *  - increment program counter by 2
     */
    @Test
    public void testOpcode_0xFX33(){
        //opcode 0xFA33
        short[] instructions = {0xFA, 0x33};
        cpu.initializeCPU();
        cpu.loadInstructions(instructions);
        cpu.I = 1000;
        cpu.V[0xA] = 123;
        cpu.step();
        assertTrue(cpu.memory[1000] == 1);
        assertTrue(cpu.memory[1001] == 2);
        assertTrue(cpu.memory[1002] == 3);
        assertTrue(cpu.programCounter == cpu.PROGRAM_START_ADDRESS + 2);
    }

    /**
     * Test: Opcode 0xFX55
     * 
     * Correct Functionality:
     *  - sets memory[I+i] = V[i] up to V[x]
     *  - sets I = I+x+1 aftwerwards
     *  - increments program coutner by 2
     */
    @Test
    public void testOpcode_0xFX55(){
        //opcode 0xF355, test memory is set correcctly, and I is set correctly, pc += 2
        short[] instructions = {0xF3, 0x55};
        cpu.initializeCPU();
        cpu.loadInstructions(instructions);
        cpu.I = 600;
        cpu.V[0] = 0x0A; 
        cpu.V[1] = 0x0B;
        cpu.V[2] = 0x0C;
        cpu.V[3] = 0x0D;
        cpu.step();
        assertTrue(cpu.memory[600] == 0x0A);
        assertTrue(cpu.memory[601] == 0x0B);
        assertTrue(cpu.memory[602] == 0x0C);
        assertTrue(cpu.memory[603] == 0x0D);
        assertTrue(cpu.I == 604);
        assertTrue(cpu.programCounter == cpu.PROGRAM_START_ADDRESS + 2);
    }


    /**
     * Test: Opcode 0xFX65
     * 
     * Correct Functionality:
     *  - sets v[i] = memory[I+i] up to V[x]
     *  - sets I = I+x+1
     *  - increments program counter by 2
     */
    @Test
    public void testOpcode_0xFX65(){
        //opcode 0xF365, test memory is set correcctly, and I is set correctly, pc += 2
        short[] instructions = {0xF3, 0x65};
        cpu.initializeCPU();
        cpu.loadInstructions(instructions);
        cpu.I = 600;
        cpu.memory[cpu.I+0] = 0x0A; 
        cpu.memory[cpu.I+1] = 0x0B;
        cpu.memory[cpu.I+2] = 0x0C;
        cpu.memory[cpu.I+3] = 0x0D;
        cpu.step();
        assertTrue(cpu.V[0] == 0x0A);
        assertTrue(cpu.V[1] == 0x0B);
        assertTrue(cpu.V[2] == 0x0C);
        assertTrue(cpu.V[3] == 0x0D);
        assertTrue(cpu.I == 604);
        assertTrue(cpu.programCounter == cpu.PROGRAM_START_ADDRESS + 2);
    }
}