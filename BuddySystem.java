/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package buddyallocater;
import java.util.Scanner;

/**
 *
 * @author Lenovo
 */
public class BuddySystem {
    // Memory pool size is in KB
    static final int MEMORY_SIZE = 1024; 

    // Head pointer for the linked list of memory blocks
    private Block head; 

    //Constructor to initialize the memory
    public BuddySystem() {
        initializeMemory();
    }

    // Initialize memory by creating a single free block
    private void initializeMemory() {
        head = new Block(MEMORY_SIZE, 0);
    }

    // Get nearest power of 2 greater than or equal to the size
    private int getPowerOf2(int size) {
        return (int) Math.pow(2, Math.ceil(Math.log(size) / Math.log(2)));
    }

    // Split memory blocks into smaller blocks until it matches the required size
    private Block splitBlock(Block block, int size) {
        while (block.size > size) {
            // Create a new buddy block of half the size
            Block buddy = new Block(block.size / 2, block.address + block.size / 2);
            buddy.next = block.next;

            // Update the original block size and link it to the buddy block
            block.size /= 2;
            block.next = buddy;
        }
        // Update the block as allocated
        block.isFree = false; 
        return block;
    }

    // Allocate memory of the requested size
    public void allocateMemory(int size) {
         // Round size to nearest power of 2
        int requiredSize = getPowerOf2(size);
        Block current = head;

        // Traverse the memory blocks to find a suitable free block
        while (current != null) {
            if (current.isFree && current.size >= requiredSize) {
                // Split the block if necessary and allocate memory
                Block allocatedBlock = splitBlock(current, requiredSize);
                System.out.println("Memory of size " + size + " KB allocated in block of size " +
                        allocatedBlock.size + " KB at address " + allocatedBlock.address );
                return;
            }
            // Move to the next block
            current = current.next;
        }
        // If no suitable block is found
        System.out.println("Memory is not enough to allocate " + size + " KB!");
    }

    // Merge a free block with its buddy if they are both free and of the same size
    private void mergeBuddies(Block block) {
        if (block.next != null && block.next.isFree && block.size == block.next.size) {
            System.out.println("Merging buddies of size " + block.size + " KB at addresses " +
                    block.address + " and " + block.next.address + " into a single block of size " +
                    (block.size * 2) + " KB");
            block.size *= 2; // Merge blocks by doubling the size
            block.next = block.next.next; // Remove buddy block from list
        }
    }

    // Free a previously allocated memory block
    public void freeMemory(int address) {
        Block current = head;
        // Traverse the memory blocks to find the allocated block with the given address
        while (current != null) {
            if (!current.isFree && current.address == address) {
                // Update the block as free
                current.isFree = true;
                System.out.println("Freed memory block of size " + current.size + " KB at address " + address );
                // Attempt to merge with the buddy
                mergeBuddies(current);
                return;
            }
            // Move to the next block
            current = current.next;
        }
        // If no block is found with the given address
        System.out.println("No block has been allocated at the specified address " + address + " to free!");
    }

    // Display the current state of the memory
    public void displayMemory() {
        Block current = head;
        System.out.println("Memory State:");
        // Traverse and print the details of each block
        while (current != null) {
            System.out.println("Block size: " + current.size + " KB, Address: " + current.address +
                    ", " + (current.isFree ? "Free" : "Allocated"));
            current = current.next;
        }
        System.out.println();
    }

    //  Main menu for interacting with the Buddy System
    public void mainMenu() {
        Scanner scanner = new Scanner(System.in);
        int choice;

        //  Display a welcome message
        System.out.println("*****************************************************");
        System.out.println("~*~ Welcome to the Buddy System Memory Allocator! ~*~");
        System.out.println("*****************************************************");
        System.out.println("\nThis program allows you to allocate, free, and display memory state dynamically");
        System.out.println("You have 1024 KB of memory available for use");
        System.out.println();

        while (true) {
            // Display menu options
            System.out.println("\nMenu:");
            System.out.println("1. Allocate Memory");
            System.out.println("2. Free Memory");
            System.out.println("3. Display Memory State");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            // Validate user input
            if (!scanner.hasNextInt()) {
                System.out.println("Invalid input! Please enter a number between 1 and 4");
                // Clear invalid input
                scanner.next(); 
                continue;
            }

            // Get user choice
            choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    // Allocate memory
                    int size = 0;
                    while (true) {
                        System.out.print("Enter the size of memory to allocate (in KB): ");
                        if (scanner.hasNextInt()) {
                            size = scanner.nextInt();
                            if (size > 0 && size <= MEMORY_SIZE) {
                                allocateMemory(size);
                                break;
                            } else if (size > MEMORY_SIZE) {
                                System.out.println("Memory is not enough to allocate " + size + " KB!");
                            } else {
                                System.out.println("Invalid size! Enter a positive number up to " + MEMORY_SIZE + " KB");
                            }
                        } else {
                            System.out.println("Invalid input! Please enter a valid number");
                            // Clear invalid input
                            scanner.next(); 
                        }
                    }
                    break;

                case 2:
                    // Free memory
                    int address = 0;
                    while (true) {
                        System.out.print("Enter the address of the memory block to free: ");
                        if (scanner.hasNextInt()) {
                            address = scanner.nextInt();
                            if (address >= 0) {
                                freeMemory(address);
                                break;
                            } else {
                                System.out.println("Invalid address! Please enter a non-negative number");
                            }
                        } else {
                            System.out.println("Invalid input! Please enter a valid number");
                            // Clear invalid input
                            scanner.next(); 
                        }
                    }
                    break;

                case 3:
                     // Display memory state
                    displayMemory();
                    break;

                case 4:
                    // Exit the program
                    System.out.println("Exiting the program.... Welcome Back!!!");
                    return;

                default:
                    // Handle invalid menu choices
                    System.out.println("Invalid choice! Please enter a number between 1 and 4");
            }
        }
    }

    // Main method
    public static void main(String[] args) {
        BuddySystem buddySystem = new BuddySystem();
        buddySystem.mainMenu();
    }
}
