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
        //opcode 0x8AB6, LSB is 0
        short[] instructions = {0x8A, 0xB6};
        cpu.initializeCPU();
        cpu.loadInstructions(instructions);
        cpu.V[0xB] = 0b10000100;
        cpu.step();
        assertTrue(cpu.V[0xA] == 0b01000010);
        assertTrue(cpu.V[0xB] == 0b10000100);
        assertTrue(cpu.V[0xF] == 0);
        assertTrue(cpu.programCounter == cpu.PROGRAM_START_ADDRESS + 2);

        //same opcode, LSB is 1
        cpu.initializeCPU();
        cpu.loadInstructions(instructions);
        cpu.V[0xB] = 0b10001011;
        cpu.step();
        assertTrue(cpu.V[0xA] == 0b01000101);
        assertTrue(cpu.V[0xB] == 0b10001011);
        assertTrue(cpu.V[0xF] == 1);
        assertTrue(cpu.programCounter == cpu.PROGRAM_START_ADDRESS + 2);
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
        //opcode 0x8ABE, MSB is 0
        short[] instructions = {0x8A, 0xBE};
        cpu.initializeCPU();
        cpu.loadInstructions(instructions);
        cpu.V[0xB] = 0b01110011;
        cpu.step();
        assertTrue(cpu.V[0xA] == 0b11100110);
        assertTrue(cpu.V[0xB] == 0b01110011);
        assertTrue(cpu.V[0xF] == 0);
        assertTrue(cpu.programCounter == cpu.PROGRAM_START_ADDRESS + 2);

        // same opcode, MSB is 1
        cpu.initializeCPU();
        cpu.loadInstructions(instructions);
        cpu.V[0xB] = 0b10000000;
        cpu.step();
        assertTrue(cpu.V[0xA] == 0b00000000);
        assertTrue(cpu.V[0xB] == 0b10000000);
        System.out.println(cpu.V[0xF]);
        assertTrue(cpu.V[0xF] == 1);
        assertTrue(cpu.programCounter == cpu.PROGRAM_START_ADDRESS + 2);
    }
}