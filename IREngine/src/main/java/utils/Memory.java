package utils;

public class Memory {
    private Runtime runtime;
    public Memory()
    {
        runtime = Runtime.getRuntime();
    }
    public long getCurrentMemory()
    {
        return (runtime.totalMemory() - runtime.freeMemory()) / 1000000;
    }
    public void printMemory(){
        System.out.println((runtime.totalMemory() - runtime.freeMemory()) / 1000000);
    }
    //k
}