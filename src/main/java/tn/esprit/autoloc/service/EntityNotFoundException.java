package tn.esprit.autoloc.service;

import tn.esprit.autoloc.exception.ResourceNotFoundException;

public class EntityNotFoundException extends ResourceNotFoundException {

    private static final long serialVersionUID = 1L;

    public EntityNotFoundException(String entity, Long id) {
        super(entity, id);
    }
}
