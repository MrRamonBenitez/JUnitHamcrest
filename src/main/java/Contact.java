public class Contact implements Comparable<Contact> {

    private String name;
    private String mobileNumber;

    public Contact(String name, String mobileNumber) {
        this.name = name;
        this.mobileNumber = mobileNumber;
    }

    @Override
    public String toString() {
        return name + "\t" + mobileNumber + "\n";
    }

    @Override
    public int compareTo(Contact o) {
        return 0;
    }
}

