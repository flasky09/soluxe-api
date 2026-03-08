import lombok.Data;

@Data
public class TestLombok {
    private String name;

    public static void main(String[] args) {
        TestLombok t = new TestLombok();
        t.setName("Lombok Works");
        System.out.println(t.getName());
    }
}
