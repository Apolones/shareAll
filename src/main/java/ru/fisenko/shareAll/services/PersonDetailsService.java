package ru.fisenko.shareAll.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fisenko.shareAll.models.Person;
import ru.fisenko.shareAll.repositories.PeopleRepository;
import ru.fisenko.shareAll.security.PersonDetails;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PersonDetailsService implements UserDetailsService {

    private PeopleRepository peopleRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public PersonDetailsService(PeopleRepository peopleRepository, PasswordEncoder passwordEncoder) {
        this.peopleRepository = peopleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public PersonDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Person person = peopleRepository.findPersonByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new PersonDetails(person);

    }

    public boolean isUsernameTaken(Person person){
       return peopleRepository.findPersonByLogin(person.getLogin()).isPresent();
    }

    @Transactional
    public void saveUser(Person person) {
        if(isUsernameTaken(person)) throw new DuplicateKeyException("Username already taken");
        person.setRole("USER");
        person.setEnabled(true);
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        peopleRepository.save(person);
    }

    public Person getPersonByUsername (String username){
        Optional <Person> person = peopleRepository.findPersonByLogin(username);
        return person.orElseGet(() -> peopleRepository.findPersonByLogin("ADMIN").get());
    }
}
