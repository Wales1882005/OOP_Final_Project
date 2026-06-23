class lecturer extends person {

    private String subject;

    public lecturer(String name, int id, String subject) {
        super(name, id);
        this.subject = subject;
    }

    public String getSubject() {
        return subject;
    }

    @Override
    public void introduce() {
        System.out.println("I am a lecturer teaching " + subject + ".");
    }

}
