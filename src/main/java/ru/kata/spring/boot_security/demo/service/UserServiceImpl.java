package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.RoleDaoImpl;
import ru.kata.spring.boot_security.demo.dao.UserDaoImpl;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final RoleDaoImpl roleDao;
    private final UserDaoImpl userDao;

    public PasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder(20);
    }

    @Autowired
    public UserServiceImpl(RoleDaoImpl roleDao, UserDaoImpl userDao) {
        this.roleDao = roleDao;
        this.userDao = userDao;
    }

    public boolean addRole(Role role) {
        Role userBas = roleDao.findByName(role.getRole());
        if(userBas != null) {return false;}
        roleDao.add(role);
        return true;
    }

    public Role findByNameRole(String name) { return roleDao.findByName(name); }

    public List<Role> listRoles() { return roleDao.listRoles(); }

    public Role findByIdRole(Long id) {
        return roleDao.findByIdRole(id);
    }

    public List<Role> listByRole(List<String> name) {
        return roleDao.listByName(name);
    }

    public boolean add(User user) {
        User userBas = userDao.findByName(user.getUsername());
        if(userBas != null) {return false;}
        user.setPassword(bCryptPasswordEncoder().encode(user.getPassword()));//у юзера в поле пассворд хранится зашифр. пароль
        userDao.add(user);
        return true;
    }

    public List<User> listUsers() {
        return userDao.listUsers();
    }

    public void delete(Long id) {
        userDao.delete(id);
    }

    public void update(User us) {
        User userBas = findById(us.getId());
        System.out.println(userBas);
        System.out.println(us);
        if(!userBas.getPassword().equals(us.getPassword())) {
            us.setPassword(bCryptPasswordEncoder().encode(us.getPassword()));
        }
        userDao.update(us);
    }

    public User findById(Long id) {
        return userDao.findById(id);
    }

    public User findByUsername(String userName) { return userDao.findByName(userName);
    }

    public User findByEmail(String email) {
        return userDao.findByEmail(email);
    }

    @Override//кто то нам дал email пользователя и наша задача вернуть пользователя по имени
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User userBas = findByEmail(email);//ищем..
        if (userBas == null) { //тогда мы бросим эксепшн если не нашли
            throw new UsernameNotFoundException(email + " not found");
        }
        UserDetails user = new org.springframework.security.core.userdetails.User(userBas.getEmail(), userBas.getPassword(), aug(userBas.getRoles()));
        return userBas; //нужно для авторизации
    }

    private Collection<? extends GrantedAuthority> aug(Collection<Role> roles) {
        return roles.stream().map(r -> new SimpleGrantedAuthority(r.getRole()))
                .collect(Collectors.toList());
        //берет любую пачку ролей и из этой пачки ролей делает пачку авторитиз с такими же строками
    }
}
