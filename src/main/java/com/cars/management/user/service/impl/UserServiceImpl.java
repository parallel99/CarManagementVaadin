package com.cars.management.user.service.impl;

import com.cars.management.core.service.impl.CoreCRUDServiceImpl;
import com.cars.management.user.entity.UserEntity;
import com.cars.management.user.service.UserService;
import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.persistence.TypedQuery;

import java.util.List;

import static org.passay.DictionarySubstringRule.ERROR_CODE;

@Service
public class UserServiceImpl extends CoreCRUDServiceImpl<UserEntity> implements UserService {

    @Override
    protected void updateCore(UserEntity persistedEntity, UserEntity entity) {
        persistedEntity.setAuthorities(entity.getAuthorities());
        persistedEntity.setUsername(entity.getUsername());
        persistedEntity.setFirstName(entity.getFirstName());
        persistedEntity.setLastName(entity.getLastName());
    }

    @Override
    protected Class<UserEntity> getManagedClass() {
        return UserEntity.class;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        TypedQuery<UserEntity> query = entityManager.createNamedQuery(UserEntity.FIND_USER_BY_USERNAME, UserEntity.class);
        query.setParameter("username", username);
        return query.getSingleResult();
    }

    public String generatePassayPassword() {
        PasswordGenerator gen = new PasswordGenerator();
        CharacterData lowerCaseChars = EnglishCharacterData.LowerCase;
        CharacterRule lowerCaseRule = new CharacterRule(lowerCaseChars);
        lowerCaseRule.setNumberOfCharacters(2);

        CharacterData upperCaseChars = EnglishCharacterData.UpperCase;
        CharacterRule upperCaseRule = new CharacterRule(upperCaseChars);
        upperCaseRule.setNumberOfCharacters(2);

        CharacterData digitChars = EnglishCharacterData.Digit;
        CharacterRule digitRule = new CharacterRule(digitChars);
        digitRule.setNumberOfCharacters(2);

        CharacterData specialChars = new CharacterData() {
            public String getErrorCode() {
                return ERROR_CODE;
            }

            public String getCharacters() {
                return "!@#$%^&*()_+";
            }
        };
        CharacterRule splCharRule = new CharacterRule(specialChars);
        splCharRule.setNumberOfCharacters(2);

        String password = gen.generatePassword(10, splCharRule, lowerCaseRule,
                upperCaseRule, digitRule);
        return password;
    }

    @Override
    public List<UserEntity> filteredSearch(String username, String firstName, String lastName) {
        return entityManager.createQuery("SELECT n FROM UserEntity n WHERE n.username LIKE :username AND n.firstName LIKE :first_name AND n.lastName LIKE :last_name", UserEntity.class)
                .setParameter("username", "%"+username+"%")
                .setParameter("first_name", "%"+firstName+"%")
                .setParameter("last_name", "%"+lastName+"%")
                .getResultList();
    }
}
