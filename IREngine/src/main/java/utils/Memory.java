package utils;

public class Memory {
    public final static Runtime runtime = Runtime.getRuntime();
    private Memory()
    {}
    public static long getCurrentMemory()
    {
        return (runtime.totalMemory() - runtime.freeMemory()) / 1000000;
    }
    public static void printMemory(){
        System.out.println((runtime.totalMemory() - runtime.freeMemory()) / 1000000);
    }
    //k
}