package entities;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Enumerated;


public enum Role {
    ROLE_CLIENT,
    ROLE_MANAGER,
    ROLE_ADMIN
}
