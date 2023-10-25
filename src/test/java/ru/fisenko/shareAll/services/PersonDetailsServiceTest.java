package ru.fisenko.shareAll.services;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.fisenko.shareAll.models.Person;
import ru.fisenko.shareAll.repositories.PeopleRepository;
import ru.fisenko.shareAll.security.PersonDetails;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PersonDetailsServiceTest {

    @Mock
    private PeopleRepository peopleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private PersonDetailsService personDetailsService;

    @BeforeEach
    public void setUp() {
        personDetailsService = new PersonDetailsService(peopleRepository, passwordEncoder);
    }

    @Test
    public void loadUserByUsername_UserExists_ReturnsPersonDetails() {
        Person person = new Person();
        person.setLogin("testUser");
        when(peopleRepository.findPersonByLogin("testUser")).thenReturn(Optional.of(person));

        PersonDetails personDetails = personDetailsService.loadUserByUsername("testUser");

        assertNotNull(personDetails);
        assertEquals("testUser", personDetails.getUsername());
    }

    @Test
    public void loadUserByUsername_UserDoesNotExist_ThrowsUsernameNotFoundException() {
        when(peopleRepository.findPersonByLogin("nonExistentUser")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> personDetailsService.loadUserByUsername("nonExistentUser"));
    }

    @Test
    public void isUsernameTaken_UsernameTaken_ReturnsTrue() {
        Person person = new Person();
        person.setLogin("takenUsername");
        when(peopleRepository.findPersonByLogin("takenUsername")).thenReturn(Optional.of(person));

        boolean isTaken = personDetailsService.isUsernameTaken(person);

        assertTrue(isTaken);
    }

    @Test
    public void isUsernameTaken_UsernameNotTaken_ReturnsFalse() {
        Person person = new Person();
        person.setLogin("notTakenUsername");
        when(peopleRepository.findPersonByLogin("notTakenUsername")).thenReturn(Optional.empty());

        boolean isTaken = personDetailsService.isUsernameTaken(person);

        assertFalse(isTaken);
    }

    @Test
    public void saveUser_UsernameTaken_ThrowsDuplicateKeyException() {
        Person person = new Person();
        person.setLogin("takenUsername");
        when(peopleRepository.findPersonByLogin("takenUsername")).thenReturn(Optional.of(person));

        assertThrows(DuplicateKeyException.class, () -> personDetailsService.saveUser(person));
    }

    @Test
    public void saveUser_UsernameNotTaken_UserSaved() {
        Person person = new Person();
        person.setLogin("notTakenUsername");
        person.setPassword("testPassword");
        when(peopleRepository.findPersonByLogin("notTakenUsername")).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        personDetailsService.saveUser(person);

        verify(peopleRepository, times(1)).save(person);
        assertEquals("encodedPassword", person.getPassword());
        assertEquals("USER", person.getRole());
    }

    @Test
    public void getPersonByUsername_UserExists_ReturnsPerson() {
        Person person = new Person();
        person.setLogin("testUser");
        when(peopleRepository.findPersonByLogin("testUser")).thenReturn(Optional.of(person));

        Person result = personDetailsService.getPersonByUsername("testUser");

        assertNotNull(result);
        assertEquals("testUser", result.getLogin());
    }

    @Test
    public void getPersonByUsername_UserDoesNotExist_ReturnsAdmin() {
        when(peopleRepository.findPersonByLogin("nonExistentUser")).thenReturn(Optional.empty());

        Person admin = new Person();
        admin.setLogin("ADMIN");
        when(peopleRepository.findPersonByLogin("ADMIN")).thenReturn(Optional.of(admin));

        Person result = personDetailsService.getPersonByUsername("nonExistentUser");

        assertNotNull(result);
        assertEquals("ADMIN", result.getLogin());
    }
}