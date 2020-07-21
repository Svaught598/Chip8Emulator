package emulator;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

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


}