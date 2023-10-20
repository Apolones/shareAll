package ru.fisenko.shareAll.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fisenko.shareAll.models.Person;
import ru.fisenko.shareAll.repositories.PeopleRepository;
import ru.fisenko.shareAll.security.PersonDetails;

import java.util.HashSet;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PersonDetailsService implements UserDetailsService {

    private PeopleRepository peopleRepository;

    @Autowired
    public PersonDetailsService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional <Person> person = peopleRepository.findPersonByLogin(username);
        if(person.isEmpty())throw new UsernameNotFoundException("User not found");
        return new PersonDetails(person.get());
    }

    public boolean isUsernameUnique(Person person){
       Optional <Person> persondb = peopleRepository.findPersonByLogin(person.getLogin());
       return persondb.isEmpty();
    }

    @Transactional
    public void saveUser(Person person) {
        person.setRole("USER");
        person.setEnabled(true);
        peopleRepository.save(person);
    }
}
