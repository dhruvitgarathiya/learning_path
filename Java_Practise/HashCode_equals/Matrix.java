import javax.swing.border.LineBorder;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Matrix {

    private final double[][] data;
    private final String label;
    private List<String> tages;

    public Matrix(double[][] data, String label, List<String> tages){
        this.data  = data;
        this.label = label;
        this.tages = tages;
    }



    public double[][] getData(){
        return this.data;
    }

    public String getLabel(){
        return  this.label;
    }

    public List<String> getTages(){
        return this.tages;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj != this){
            return false;
        }

        if(obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        Matrix m = (Matrix) obj;

        return Arrays.deepEquals(m.getData(), this.getData()) &&  m.getTages().equals(this.getTages()) ;
    }

    @Override
    public int hashCode() {
        int p = Arrays.deepHashCode(this.getData());
        int result = p*31 + Objects.hash(this.getTages());
        return result;
    }
}
