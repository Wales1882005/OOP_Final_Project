class Student extends person {

    public Student(String name, int id) {
        super(name, id);
    }

    @Override
    public void introduce() {
        System.out.println("I am a student.");
    }
}
