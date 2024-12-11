/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package buddyallocater;

/**
 *
 * @author Lenovo
 */

// Structure for a memory block
class Block {
        int size;          // Size of the block
        boolean isFree;    // True if free, false if allocated
        int address;       // Address of the block
        Block next;        // Pointer to next block

        Block(int size, int address) {
            this.size = size;
            this.isFree = true;
            this.address = address;
            this.next = null;
        }
    }