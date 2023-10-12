package com.joboffers.domain.loginandregister;

import com.joboffers.domain.loginandregister.dto.RegisterUserDto;
import com.joboffers.domain.loginandregister.dto.RegistrationResultDto;
import com.joboffers.domain.loginandregister.dto.UserDto;
import lombok.AllArgsConstructor;

import java.util.UUID;

/**
 * Metody:
 * - register
 * jej zadaniem jest utworzenie nowego użytkownika w systemie
 * - login
 * jej zadaniem jest zalogowanie użytkownika do systemu
 *
 *WYMAGANIA
 * # jako klient aplikacji chcę widzieć oferty pracy dla Junior Java Developera
 * korzystamy ze zdalnego serwera HTTP (skrypt który pobiera oferty ze stron WWW)
 * klient musi używać tokena, żeby zobaczyć oferty
 * klient może się zarejestrować
 * aktualizacja ofert w bazie danych jest co 3 godziny (wtedy odpytujemy zdalny serwer z pkt. 1)
 * oferty w bazie nie mogą się powtarzać (decyduje url oferty)
 * klient może pobrać jedną ofertę pracy poprzez unikalne Id
 * klient może pobrać wszystkie dostępne oferty kiedy jest zautoryzowany
 * jeśli klient w ciągu 60 minut robi więcej niż jedno zapytanie, to dane powinny pobierać się z cache (ponieważ pobieranie z bazy danych kosztuję pieniądze naszego klienta)
 * klient może ręcznie dodać ofertę pracy
 * każda oferta pracy ma (link do oferty, nazwę stanowiska, nazwę firmy, zarobki (mogą być widełki))
 */

@AllArgsConstructor
public class LoginAndRegisterFacade {

    private static final String USER_NOT_FOUND = "User not found";
    private final LoginRepository loginRepository; //bd?

    public UserDto findByUsername(String username) {
        return loginRepository.findByUsername(username)
                .map(user -> new UserDto(user.id(), user.username(), user.password()))
                .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND));
    }

    public RegistrationResultDto register(RegisterUserDto registerUserDto) {
        final User user = RegisterUserDtoMapper.mapFromRegisterUserDto(
                UUID.randomUUID().toString(), registerUserDto);
        User savedUser = loginRepository.save(user);
        return new RegistrationResultDto(savedUser.id(), true, savedUser.username());

    }
}
