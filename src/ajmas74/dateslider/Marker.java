/*
 * Please keep attribution, but otherwise do whatever you want with this class.
 * 
 * By Andre-John Mas - 2007
 */
package ajmas74.dateslider;

public class Marker {
    
    public static final int NEW_YEAR = 0;
    public static final int NEW_MONTH = 1;
    public static final int CURRENT_DAY = 2;
    
    int offset;
    int type;
    int year;
    int month;
    String label;
    
    public Marker(int offset, int type, int year, int month, String label) {
        this.offset = offset;
        this.type = type;
        this.year = year;
        this.month = month;
        this.label = label;
    }

    public int getOffset() {
        return offset;
    }

    public int getType() {
        return type;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public String getMonthName() {
        return label;
    }
    
    public String toString () {
    	return "[" + type + "," + label + "]";
    }
}
