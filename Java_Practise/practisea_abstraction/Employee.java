public abstract class Employee {
    public String name;
    public int employeeId;
    public int baseSalary;
    protected double salary;
    protected Employee(String name, int emoloyeeId, int baseSalary){
        this.name  = name;
        this.employeeId = emoloyeeId;
        this.baseSalary = baseSalary;
    }

    public abstract double calculateSalary();
    public void displayDetails(){
       salary = calculateSalary();
       System.out.println(salary);
    }

}
