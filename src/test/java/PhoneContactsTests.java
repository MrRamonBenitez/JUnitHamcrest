import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
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
        assertEquals(expected, phoneContacts.getGroupMap());
        assertTrue(expected.containsKey(groupName));
    }

    @ParameterizedTest
    @MethodSource("addGroupDataProvider")
    public void testAddGroupFalse(Map<String, List<Contact>> expected, String[] groupName) {
        //act
        for (String name : groupName) {
            phoneContacts.addGroup(name);
            assertTrue(expected.containsKey(name));
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
        assertEquals(expected, phoneContacts.getGroupMap());

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

}

