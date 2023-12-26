import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {
    static Scanner read = new Scanner(System.in);
    static int numOfPartitions = 0;
    static int sizeOfPartition = 0;
    static Partition[] partition;
    static String strategy;

    public static void main(String[] args) throws IOException {
        boolean f = false;
        while (!f) {
            try {
                System.out.println("Please enter the number of partitions (M): ");
                numOfPartitions = read.nextInt();
                if (numOfPartitions < 0)
                    System.out.println("Please enter a positive number.");
                else
                    f = true;
            } catch (InputMismatchException e) {
                System.out.println("Please enter an integer.");
                read.nextLine();}}

        partition = new Partition[numOfPartitions];
        int sAddress = 0;
        int eAddress;

        for (int i = 0; i < numOfPartitions; i++) {
            f = false;
            while (!f) {
                try {
                    System.out.println("Please enter the size of the partition " + (i + 1) + " in KB: ");
                    sizeOfPartition = read.nextInt();
                    if (sizeOfPartition < 0)
                        System.out.println("Please enter a positive number.");
                    else
                        f = true;
                } catch (InputMismatchException e) {
                    System.out.println("Please enter an integer.");
                    read.nextLine();}}

            eAddress = sAddress + (sizeOfPartition * 1024) - 1;
            partition[i] = new Partition(sAddress, eAddress, sizeOfPartition);
            sAddress = partition[i].getEndAddress() + 1;}

        System.out.println("Please enter the allocation strategy [First-fit (F), Best-fit (B), or Worst-fit (W)].");
        strategy = read.next();
        int choice;

        do {
            System.out.println("Please choose an action:");
            System.out.println("1. Allocate a block of memory.");
            System.out.println("2. De-allocate a block of memory.");
            System.out.println("3. Report detailed information about regions of free and allocated memory blocks");
            System.out.println("4. Exit the program.");
            choice = read.nextInt();

            switch (choice) {
                case 1 -> {
                    try {
                        System.out.println("Please enter the process name and size in the following format PN SIZE:");
                        String processName = read.next();
                        int processSize = read.nextInt();
                        //System.out.println("Please the process size:");

                        switch (strategy.toUpperCase().charAt(0)) {
                            case 'F' -> firstFit(processName, processSize);
                            case 'B' -> bestFit(processName, processSize);
                            case 'W' -> worstFit(processName, processSize);
                            default -> System.out.println("Please enter only F, B, W.");}
                    } catch (InputMismatchException e) {
                        System.out.println("Please enter an integer.");
                        read.nextLine();}}
                case 2 -> {
                    System.out.println("Enter the process name:");
                    String pName = read.next();
                    release(pName);}
                case 3 -> {
                    for (int i = 0; i < numOfPartitions; i++) {
                        System.out.printf("%d) ", i + 1);
                        partition[i].printStatus();}
                    System.out.println("Memory State: ");
                    System.out.print("[");
                    for (int i = 0; i < numOfPartitions; i++) {
                        if (partition[i].getProcessId().equals("Null")) {
                            System.out.print("H");
                        } else {
                            System.out.print(partition[i].getProcessId());
                        }
                        if (i < numOfPartitions - 1)
                            System.out.print(" | ");}
                    System.out.println("]");
                    Report();}
                case 4 -> System.out.println("Exiting the program.");
                default -> System.out.println("Please select a valid number.");}} while (choice != 4);}

    static void firstFit(String name, int size) {

        boolean allocated = false;
        for (Partition value : partition) {
            int space = value.getPartition();
            if ((!value.getPartitionStatus().equals("Allocated")) && (size <= space)) {
                value.setPartitionStatus("Allocated");
                value.setProcessId(name);
                value.setProcessSize(size);
                value.calculateFragment();
                allocated = true;
                break;
            }
        }

        if (!allocated)
            System.out.println("Sorry, there is no free space");
    }

    static void bestFit(String name, int size) {
        List<Partition> freePartitions = new ArrayList<>();

        for (Partition p : partition) {
            if (p.getPartitionStatus().equals("Free") && p.getPartition() >= size) {
                freePartitions.add(p);
            }
        }

        if (!freePartitions.isEmpty()) {
            Partition bestFitPartition = Collections.min(freePartitions, Comparator.comparingInt(Partition::getPartition));
            bestFitPartition.setPartitionStatus("Allocated");
            bestFitPartition.setProcessId(name);
            bestFitPartition.setProcessSize(size);
            bestFitPartition.calculateFragment();
        } else {
            System.out.println("Sorry, there is no free space");
        }
    }

    static void worstFit(String name, int size) {


        int worstFit = -1;

        for (int i = 0; i < partition.length; i++) {
            if (partition[i].getPartitionStatus().equals("Free") && partition[i].getPartition() >= size) {
                if (worstFit == -1)
                    worstFit = i;
                    /*
                     * for any other pass check if the current partition is grater than the
                     * previously allocated
                     */
                else if (partition[i].getPartition() > partition[worstFit].getPartition())
                    worstFit = i;
            }
        }

        if (worstFit != -1) {
            partition[worstFit].setPartitionStatus("Allocated");
            partition[worstFit].setProcessId(name);
            partition[worstFit].setProcessSize(size);
            partition[worstFit].calculateFragment();
        }

        else
            System.out.println("Sorry, there is no free space");

    }

    static void release(String name) {
        boolean found = false;
        for (Partition value : partition) {
            if (value.getProcessId().equals(name)) {
                value.setPartitionStatus("Free");
                value.setProcessId("Null");
                found = true;
                value.setFragmentSize(-1);
            }
        }

        if (!found)
            System.out.println("the process not found");
    }

    public static void Report() throws IOException {

        BufferedWriter outputWriter;
        outputWriter = new BufferedWriter(new FileWriter("Report.txt"));
        for (Partition value : partition) {
            outputWriter.write(" | The Size of Partition: " + value.getPartition());
            // outputWriter.newLine();
            outputWriter.write(" | The Partition Status: " + value.getPartitionStatus());
            // outputWriter.newLine();
            outputWriter.write(" | The Process Number: " + value.getProcessId());
            // outputWriter.newLine();
            outputWriter.write(" | The Fragmentation Size:" + value.getFragmentSize());
            // outputWriter.newLine();
            outputWriter.write(" | Starting Address: " + value.getStartAddress() + "(bytes)");
            // outputWriter.newLine();
            outputWriter.write(" | Ending Address:(bytes) " + value.getEndAddress() + "(bytes)");
            outputWriter.newLine();
        }
        outputWriter.flush();
        outputWriter.close();
    }}