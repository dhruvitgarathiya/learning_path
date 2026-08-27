public class Developer extends Employee {

    public int githubId;
    

    protected Developer(String name, int emoloyeeId, int baseSalary,int gitubID) {
        super(name, emoloyeeId, baseSalary);
        this.githubId = gitubID;
    }

    @Override
    public double calculateSalary() {
        return salary = baseSalary + (0.2 * (baseSalary));
    }
}
