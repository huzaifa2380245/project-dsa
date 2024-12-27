import java.util.*;

public class BankQueueManagement {

    static class Node {
        int serialNumber;
        int priority;

        Node(int serialNumber, int priority) {
            this.serialNumber = serialNumber;
            this.priority = priority;
        }
    }

    static class CustomPriorityQueue {
        private PriorityQueue<Node> heap;

        public CustomPriorityQueue() {
            heap = new PriorityQueue<>(Comparator.comparingInt((Node node) -> node.priority)
                    .thenComparingInt(node -> node.serialNumber));
        }

        public void add(int serialNumber, int priority) {
            Node newNode = new Node(serialNumber, priority);
            heap.add(newNode);
            System.out.println("Customer with serial number " + serialNumber + " added to the queue with priority " + priority + ".");
        }

        public int poll() {
            if (heap.isEmpty()) {
                return -1;
            } else {
                Node servedCustomer = heap.poll();
                return servedCustomer.serialNumber;
            }
        }

        public void displayQueue() {
            if (heap.isEmpty()) {
                System.out.println("Queue is empty!");
            } else {
                System.out.println("Current Queue (Priority): ");
                for (Node node : heap) {
                    System.out.println("Serial Number: " + node.serialNumber + "(Priority: " + node.priority + ")");
                }
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CustomPriorityQueue bankQueue = new CustomPriorityQueue();
        int serialNumberCounter = 1;

        System.out.println("--- Welcome to Karachi Bank ---");
        String userType = getValidUserType(scanner);

        if (userType.equals("customer")) {
            customerMode(scanner, bankQueue, serialNumberCounter);
        } else if (userType.equals("staff")) {
            staffMode(scanner, bankQueue);
        }
        scanner.close();
    }

    private static String getValidUserType(Scanner scanner) {
        String userType = "";
        while (!(userType.equals("customer") || userType.equals("staff"))) {
            System.out.print("Are you a Customer or Bank Staff? (Enter 'customer' or 'staff'): ");
            userType = scanner.nextLine().trim().toLowerCase();
            if (!(userType.equals("customer") || userType.equals("staff"))) {
                System.out.println("Invalid input! Please enter 'customer' or 'staff'.");
            }
        }
        return userType;
    }

    private static void customerMode(Scanner scanner, CustomPriorityQueue bankQueue, int serialNumberCounter) {
        while (true) {
            System.out.println("\n--- Customer Mode ---");
            System.out.println("1. Add customer to queue");
            System.out.println("2. Switch to Bank Staff Mode");
            System.out.println("3. Exit");
            int choice = getValidMenuChoice(scanner);

            switch (choice) {
                case 1:
                    System.out.print("Are you a bank customer? (yes/no): ");
                    String isCustomer = scanner.nextLine().trim().toLowerCase();

                    if (isCustomer.equals("yes")) {
                        handleCustomer(scanner, bankQueue, serialNumberCounter);
                    } else {
                        handleNonCustomer(scanner, bankQueue, serialNumberCounter);
                    }
                    serialNumberCounter++;
                    break;

                case 2:
                    System.out.println("Switching to Bank Staff Mode...");
                    staffMode(scanner, bankQueue);
                    return;

                case 3:
                    System.out.println("Exiting Customer Mode. Goodbye!");
                    return;

                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }

    private static void staffMode(Scanner scanner, CustomPriorityQueue bankQueue) {
        while (true) {
            System.out.println("\n--- Bank Staff Mode ---");
            System.out.println("1. Serve customer");
            System.out.println("2. Display queue");
            System.out.println("3. Switch to Customer Mode");
            System.out.println("4. Exit");
            int choice = getValidMenuChoice(scanner);

            switch (choice) {
                case 1:
                    int servedCustomer = bankQueue.poll();
                    if (servedCustomer != -1) {
                        System.out.println("Customer with serial number " + servedCustomer + " has been served.");
                    } else {
                        System.out.println("No customer to serve! The queue is empty.");
                    }
                    break;

                case 2:
                    bankQueue.displayQueue();
                    break;

                case 3:
                    System.out.println("Switching to Customer Mode...");
                    customerMode(scanner, bankQueue, 1);
                    return;

                case 4:
                    System.out.println("Exiting Bank Staff Mode. Goodbye!");
                    return;

                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }

    private static int getValidMenuChoice(Scanner scanner) {
        int choice = -1;
        while (choice < 1 || choice > 4) {
            try {
                System.out.print("Enter your choice: ");
                choice = scanner.nextInt();
                scanner.nextLine();
                if (choice < 1 || choice > 4) {
                    System.out.println("Invalid choice! Please enter a number between 1 and 4.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a number.");
                scanner.nextLine();
            }
        }
        return choice;
    }

    private static int getValidCardBin(Scanner scanner) {
        int binNumber = -1;
        while (binNumber < 100000 || binNumber > 999999) {
            try {
                System.out.print("Please enter the first 6 digits of your card number (BIN): ");
                binNumber = scanner.nextInt();
                scanner.nextLine();
                if (binNumber < 100000 || binNumber > 999999) {
                    System.out.println("Invalid BIN! Please enter a valid 6-digit BIN number.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a valid 6-digit BIN number.");
                scanner.nextLine();
            }
        }
        return binNumber;
    }

    private static int getPriorityByBin(int binNumber) {
        if (binNumber >= 500000 && binNumber <= 599999) {
            System.out.println("You have a Platinum Card!");
            return 1;
        } else if (binNumber >= 400000 && binNumber <= 499999) {
            System.out.println("You have a Golden Card!");
            return 2;
        } else if (binNumber >= 300000 && binNumber <= 399999) {
            System.out.println("You have a Silver Card!");
            return 3;
        } else {
            System.out.println("You are a Regular customer.");
            return 4;
        }
    }

    private static void handleCustomer(Scanner scanner, CustomPriorityQueue bankQueue, int serialNumberCounter) {
        int binNumber = getValidCardBin(scanner);
        int priority = getPriorityByBin(binNumber);
        bankQueue.add(serialNumberCounter, priority);
    }

    private static void handleNonCustomer(Scanner scanner, CustomPriorityQueue bankQueue, int serialNumberCounter) {
        System.out.print("Are you aged or disabled? (yes/no): ");
        String isAgedOrDisabled = scanner.nextLine().trim().toLowerCase();

        int priority;
        if (isAgedOrDisabled.equals("yes")) {
            System.out.println("You will be prioritized.");
            priority = 1;
        } else {
            priority = 5;
        }

        bankQueue.add(serialNumberCounter, priority);
    }
}
