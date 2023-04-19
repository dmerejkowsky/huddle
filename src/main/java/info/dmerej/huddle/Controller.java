package info.dmerej.huddle;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController()
public class Controller {
    private final SqlStorage storage;

    public Controller(SqlStorage storage) {
        this.storage = storage;
    }

    @GetMapping(path = "/account/{username}")
    public Account get(@PathVariable String username) {
        Account result;
        try {
            result = storage.getAccountByUserName(username);
        } catch (NoSuchAccount e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return result;
    }

    @PostMapping(path = "/account")
    public void createAccount(@RequestBody AccountCreationRequest account) {
        storage.createAccount(account);
    }

    /**
     * Reset the DB - just for the tests :)
     */
    @PostMapping(path = "/reset-db")
    public void resetDb() {
        storage.reset();
    }
}