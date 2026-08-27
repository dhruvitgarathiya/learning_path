public class Intern extends Employee{

    

    protected Intern(String name, int emoloyeeId, int baseSalary) {
        super(name, emoloyeeId, baseSalary);
       
    }

    @Override
    public double calculateSalary() {
        return salary = baseSalary;
    }
}
