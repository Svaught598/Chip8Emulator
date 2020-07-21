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
        // test for addition under 256
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
}