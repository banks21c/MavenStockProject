package html.parsing.stock;

import java.lang.reflect.Field;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class MajorStockHolderVO {

    private String majorStockHolderName;
    private String retainVolume;
    private String retainAmount;
    private String retainAmountByMillion;
    private String retainRatio;
    private long lRetainVolume;
    private long lRetainAmount;
    private float fRetainRatio;
    private long lRetainAmountByMillion = 0;

    public long getlRetainAmountByMillion() {
        return lRetainAmountByMillion;
    }

    public void setlRetainAmountByMillion(long lRetainAmountByMillion) {
        this.lRetainAmountByMillion = lRetainAmountByMillion;
    }

    public String getMajorStockHolderName() {
        return majorStockHolderName;
    }

    public void setMajorStockHolderName(String majorStockHolderName) {
        this.majorStockHolderName = majorStockHolderName;
    }

    public String getRetainVolume() {
        return retainVolume;
    }

    public void setRetainVolume(String retainVolume) {
        this.retainVolume = retainVolume;
    }

    public String getRetainRatio() {
        return retainRatio;
    }

    public void setRetainRatio(String retainRatio) {
        this.retainRatio = retainRatio;
    }

    public String getRetainAmount() {
        return retainAmount;
    }

    public void setRetainAmount(String retainAmount) {
        this.retainAmount = retainAmount;
    }

    public String getRetainAmountByMillion() {
        return retainAmountByMillion;
    }

    public void setRetainAmountByMillion(String retainAmountByMillion) {
        this.retainAmountByMillion = retainAmountByMillion;
    }

    public long getlRetainVolume() {
        return lRetainVolume;
    }

    public void setlRetainVolume(long lRetainVolume) {
        this.lRetainVolume = lRetainVolume;
    }

    public long getlRetainAmount() {
        return lRetainAmount;
    }

    public void setlRetainAmount(long lRetainAmount) {
        this.lRetainAmount = lRetainAmount;
    }

    public float getfRetainRatio() {
        return fRetainRatio;
    }

    public void setfRetainRatio(float fRetainRatio) {
        this.fRetainRatio = fRetainRatio;
    }

    public String toString1() {
        StringBuilder result = new StringBuilder();
        String newLine = System.getProperty("line.separator");

        result.append(this.getClass().getName());
        result.append(" Object {");
        result.append(newLine);

        // determine fields declared in this class only (no fields of
        // superclass)
        Field[] fields = this.getClass().getDeclaredFields();

        // print field names paired with their values
        for (Field field : fields) {
            result.append("  ");
            try {
                result.append(field.getName());
                result.append(": ");
                // requires access to private field:
                result.append(field.get(this));
            } catch (IllegalAccessException ex) {
                System.out.println(ex);
            }
            result.append(newLine);
        }
        result.append("}");

        return result.toString();
    }

    public String toString() {
        ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
        return ReflectionToStringBuilder.toString(this);
    }

    public static void main(String args[]) {
        System.out.println(new MajorStockHolderVO().toString());
    }
}
