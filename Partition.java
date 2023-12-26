public class Partition {

    private int initialAddress;
    private int finalAddress;
    private String processId;
    private int fragmentSize;
    private String status;
    private int partition;
    private int processSize;


    public Partition(int startAddress, int endAddress, int partition) {
        this.initialAddress = startAddress;
        this.finalAddress = endAddress;
        this.partition = partition;
        this.fragmentSize = -1;
        this.status = "Free";
        this.processId = "Null";
    }

    public int getStartAddress() {
        return initialAddress;
    }

    public int getEndAddress() {
        return finalAddress;
    }

    public String getProcessId() {
        return processId;
    }

    public int getFragmentSize() {
        return fragmentSize;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public void setFragmentSize(int fragmentationSize) {
        this.fragmentSize = fragmentationSize;
    }

    public void setPartitionStatus(String partitionStatus) {
        this.status = partitionStatus;
    }

    public String getPartitionStatus() {
        return status;
    }


    public int getProcessSize() {
        return processSize;
    }

    public void setProcessSize(int processSize) {
        this.processSize = processSize;
    }

    public int getPartition() {
        return partition;
    }

    public void printStatus() {
        System.out.printf("Size of Partition: %-5d |  " +
                        "Partition Status:%-5s |  " +
                        "Process Number: %-5s |  " +
                        "Fragmentation Size: %-5s |  " +
                        "Initial Address:%-9d Bytes |  " +
                        "Final Address:%-8d Bytes  \n",
                partition, status, processId, fragmentSize,
                initialAddress, finalAddress);
    }


    public void calculateFragment() {
        if (status.equals("Allocated") && !processId.equals("Null")) {
            setFragmentSize(getPartition() - getProcessSize());
        }
    }

}