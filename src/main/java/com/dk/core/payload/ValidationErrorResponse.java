package com.dk.core.payload;

import com.dk.core.domain.Violation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ValidationErrorResponse {

    private List<Violation> violations = new ArrayList<>();
}

