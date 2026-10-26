package tn.esprit.autoloc.service;

public class EntityNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public EntityNotFoundException(String entity, Long id) {
        super(entity + " introuvable : " + id);
    }
}
