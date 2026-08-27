public class Manager  extends Employee{

    

    protected Manager(String name, int emoloyeeId, int baseSalary) {
        super(name, emoloyeeId, baseSalary);
        
    }

    @Override
    public double calculateSalary() {
        return salary  = baseSalary + (0.4*baseSalary);
    }
    
}
