package com.dk.security.login.services;


import com.dk.security.login.domain.Code;
import com.dk.security.login.repository.CodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CodeService {

    private CodeRepository codeRepository;

    @Autowired
    public CodeService(CodeRepository codeRepository){
        this.codeRepository = codeRepository;
    }

    public void save(Code code){
        codeRepository.save(code);
    }

    public Optional<Code> findCode(String code){
       return codeRepository.findByCode(code);
    }
}
