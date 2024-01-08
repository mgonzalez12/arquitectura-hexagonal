package com.gft.prueba.arquitecture.infraestructure.adapter;

import com.gft.prueba.arquitecture.domain.model.User;
import com.gft.prueba.arquitecture.domain.model.constant.UserConstant;
import com.gft.prueba.arquitecture.domain.port.UserPersistencePort;
import com.gft.prueba.arquitecture.infraestructure.adapter.exception.UserException;
import com.gft.prueba.arquitecture.infraestructure.adapter.mapper.UserDboMapper;
import com.gft.prueba.arquitecture.infraestructure.adapter.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserSpringJpaAdapter implements UserPersistencePort {

    private final UserRepository userRepository;
    private final UserDboMapper userDboMapper;

    public UserSpringJpaAdapter(UserRepository userRepository, UserDboMapper userDboMapper) {
        this.userRepository = userRepository;
        this.userDboMapper = userDboMapper;
    }

    @Override
    public User create(User user) {
        return userDboMapper.toDomain(userRepository.save(userDboMapper.toDbo(user)));
    }

    @Override
    public User getById(Long id) {
        var optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()){
            throw new UserException(HttpStatus.NOT_FOUND,
                    String.format(UserConstant.TASK_NOT_FOUND_MESSAGE_ERROR, id));
        }
        return userDboMapper.toDomain(optionalUser.get());
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll()
                .stream()
                .map(userDboMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User update(User user) {

        var userToUpdate = userDboMapper.toDbo(user);
        var userUpdated = userRepository.save(userToUpdate);

        return userDboMapper.toDomain(userUpdated);
    }
}
