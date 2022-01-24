import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.*;
import java.util.stream.Stream;

import static java.lang.String.valueOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class PhoneContactsTests {

    PhoneContacts phoneContacts;

    @BeforeAll
    public static void started() { System.out.println("Tests started"); }

    @AfterEach
    public void finished() { System.out.println("Test completed"); }

    @AfterAll
    public static void finishedAll() { System.out.println("Tests completed"); }

    @BeforeEach
    void setUp() {
        phoneContacts = new PhoneContacts();
    }

    @Test
    public void testAddGroup() {
        //arrange
        String groupName = "Family";
        Map<String, List<Contact>> expected = new HashMap<>();
        expected.put("Family", new ArrayList<>());

        //act
        phoneContacts.addGroup(groupName);

        //assert
        assertThat(expected, is (equalTo(phoneContacts.getGroupMap())));
        assertThat(valueOf(true), expected.containsKey(groupName));
    }

    @ParameterizedTest
    @MethodSource("addGroupDataProvider")
    public void testAddGroupTrue(Map<String, List<Contact>> expected, String[] groupName) {
        //act
        for (String name : groupName) {
            phoneContacts.addGroup(name);
            assertThat(valueOf(true), expected.containsKey(name));
        }
    }

    static Stream<Arguments> addGroupDataProvider() {
        String[] groupName = new String[]{"Family", "Work", "Friends"};
        Map<String, List<Contact>> expected = new HashMap<>();
        for (String name : groupName) expected.put(name, new ArrayList<>());
        return Stream.of(
                arguments(expected, groupName)
        );
    }

    @ParameterizedTest
    @MethodSource("addContactDataProvider")
    public void testAddContactToMap(Map<String, List<Contact>> expected, PhoneContacts phoneContacts,
                                    Contact[] contacts, String[] groupName) {

        for (Contact contact : contacts) phoneContacts.addContactToMap(contact, groupName);
        assertThat(expected, is (equalTo(phoneContacts.getGroupMap())));

    }

    static Stream<Arguments> addContactDataProvider() {
        PhoneContacts phoneContacts = new PhoneContacts();

        // создание набора тестовых контактов
        Contact[] contacts = {new Contact("Ivan Petrov", "+79354445566"),
                new Contact("Petr Ivanov", "+79374497788"),
                new Contact("Nikolai Semenov", "+79374494353"),
                new Contact("Michaela Nekrasova", "+79384498900"),
                new Contact("Svetlana Semenova", "+793744947777")};

        // создание тестового списка контактов
        List<Contact> testList = new ArrayList<>();
        for (Contact contact : contacts) {
            Collections.sort(testList);
            int position = Collections.binarySearch(testList, contact);
            if (position == -1) position = Math.abs(position) - 1;
            testList.add(position, contact);
        }

        // подготовка ожидаемых наборов по группам телефонных контактов
        Map<String, List<Contact>> expected = new HashMap<>();
        expected.put("Family", testList);
        expected.put("Work", testList);
        expected.put("Friends", testList);

        // набор наименований групп
        String[] groupName = new String[]{"Family", "Work", "Friends"};

        // создание набора пустых списков контактов по группам
        for (String name : groupName) phoneContacts.addGroup(name);

        return Stream.of(arguments(expected, phoneContacts, contacts, groupName));
    }

    @Test
    public void testToString() {
        // arrange
        Contact contact1 = new Contact("Tatyana Petrova", "89767657766");
        Contact contact2 = new Contact("Michael Popov", "89777439922");
        Contact contact3 = new Contact("Nikolay Nekrasov", "89876268652");

        String[] groupName1 = new String[]{"Family"};
        String[] groupName2 = new String[]{"Work", "Friends"};
        String[] groupName3 = new String[]{"Friends"};

        phoneContacts.addGroup("Family");
        phoneContacts.addGroup("Work");
        phoneContacts.addGroup("Friends");

        phoneContacts.addContactToMap(contact1, groupName1);
        phoneContacts.addContactToMap(contact2, groupName2);
        phoneContacts.addContactToMap(contact3, groupName3);

        // act
        System.out.println(phoneContacts);

        // assert
        assertThat(phoneContacts, hasToString("- Friends:\n" +
                                                            "\t" + contact3 +
                                                            "\t" + contact2 +
                                                            "- Work:\n" +
                                                            "\t" + contact2 +
                                                            "- Family:\n" +
                                                            "\t" + contact1));
    }

    @Test
    public void testAddGroupCheckThenContains() {
        // arrange
        String[] groupName = new String[]{"Family", "Work", "Friends"};

        //act
        for (String name : groupName) {
            phoneContacts.addGroup(name);
        }

        // assert
        assertThat(phoneContacts.getGroupMap(), hasKey("Family"));
        assertThat(phoneContacts.getGroupMap(), hasKey("Work"));
        assertThat(phoneContacts.getGroupMap(), hasKey("Friends"));

    }

}

