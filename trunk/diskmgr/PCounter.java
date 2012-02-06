package diskmgr;
public class PCounter {
public static int rcounter;
public static int wcounter;
/**
 * Initializes the rcounter and wcounter to keep the record of number of disk pages read.
 */
public static void initialize() {
rcounter =0;
wcounter =0;
}
/**
 * Increment the read counter
 */
public static void readIncrement() {
rcounter++;
}
/**
 * Increment the write counter
 */
public static void writeIncrement() {
wcounter++;
}
}