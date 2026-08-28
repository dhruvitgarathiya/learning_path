package Daily_Practise_Coding.27_8_hashcode_equals;

public import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class LabeledMatrix extends Matrix {
    private String Category;

    public LabeledMatrix(double[][] data, String label, List<String> tages, String category) {
        super(data, label, tages);
        this.Category = category;
    }

    public String getCategory() {
        return this.Category;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != this) {
            return false;
        }

        if (obj == null || obj.getClass() != this.getClass() ){
            return  false;
        }

        LabeledMatrix lm = (LabeledMatrix) obj;

        return super.equals(lm) && lm.getCategory().equals(this.getCategory());

    }

    @Override
    public int hashCode() {
        int p = Objects.hash(this.getCategory());
        int result  = super.hashCode() + p;
        return result;
    }
} {
    
}
